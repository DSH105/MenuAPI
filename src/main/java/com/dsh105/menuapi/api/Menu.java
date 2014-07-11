/*
 * This file is part of MenuAPI.
 *
 * MenuAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MenuAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MenuAPI.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.dsh105.menuapi.api;

import com.dsh105.menuapi.api.event.IconClickEvent;
import com.dsh105.menuapi.api.event.MenuOpenEvent;
import com.dsh105.menuapi.util.MenuId;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an Inventory based Menu that can be managed and shown to various players.
 * <p/>
 * Click events and icon slots are handled internally by the Menu
 * <p/>
 * An {@link com.dsh105.menuapi.api.Icon} represents an action to be performed when a certain item in the inventory is
 * clicked
 */
public class Menu extends SlotHolder implements InventoryHolder, Listener {

    private long id;
    private boolean closeOnOutsideClick = true;

    /**
     * Construct a Menu with the given title and size
     * <p/>
     * Menu sizes will be automatically adjusted to accommodate a multiple of nine
     *
     * @param plugin Plugin instance
     * @param title  Title of the inventory
     * @param size   Size of the inventory
     */
    public Menu(Plugin plugin, String title, int size) {
        this(plugin, title, size, new HashMap<Integer, Icon>());
    }

    /**
     * Construct a Menu from a given {@link Layout}
     *
     * @param plugin Plugin instance
     * @param layout Layout to construct the Menu from
     */
    public Menu(Plugin plugin, Layout layout) {
        this(plugin, layout.getTitle(), layout.getSize(), layout.getClickItem(), layout.getSlots());
    }

    /**
     * Construct a new Menu
     *
     * @param plugin Plugin instance
     * @param title  Title of the inventory
     * @param size   Size of the inventory
     * @param slots  Slots to initialise the Menu with
     */
    public Menu(Plugin plugin, String title, int size, HashMap<Integer, Icon> slots) {
        this(plugin, title, size, null, slots);
    }

    /**
     * Construct a new Menu
     *
     * @param plugin    Plugin instance
     * @param title     Title of the inventory
     * @param size      Size of the inventory
     * @param clickItem Click item registered to a Menu
     * @param slots     Slots to initialise the Menu with
     */
    public Menu(Plugin plugin, String title, int size, ItemStack clickItem, HashMap<Integer, Icon> slots) {
        super(size, title, clickItem, slots);
        this.id = MenuId.next();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, this.getSize(), this.getTitle());
    }

    /**
     * Gets the registered ID of a Menu
     *
     * @return Registered ID of a menu
     */
    public long getId() {
        return id;
    }

    public boolean willCloseOnOutsideClick() {
        return closeOnOutsideClick;
    }

    public void setCloseOnOutsideClick(boolean closeOnOutsideClick) {
        this.closeOnOutsideClick = closeOnOutsideClick;
    }

    /**
     * Shows a Menu to a player
     *
     * @param viewer Player to show the Menu to
     */
    public void show(Player viewer) {
        MenuOpenEvent openEvent = new MenuOpenEvent(this, viewer);
        Bukkit.getServer().getPluginManager().callEvent(openEvent);
        if (openEvent.isCancelled()) {
            return;
        }

        Inventory inv = this.getInventory();
        for (Map.Entry<Integer, Icon> entry : this.getSlots().entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getIcon(viewer));
        }
        viewer.openInventory(inv);
    }

    /**
     * Shows a Menu to multiple players
     *
     * @param viewers Players to show the Menu to
     */
    public void show(Player... viewers) {
        for (Player viewer : viewers) {
            this.show(viewer);
        }
    }

    /**
     * Converts a Menu to a {@link Layout} for saving and creating new Menus
     *
     * @return Layout representing a Menu
     */
    public Layout toLayout() {
        return new Layout(this.getSlots(), this.getSize(), this.getTitle(), this.getClickItem());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        HumanEntity human = event.getWhoClicked();
        if (human instanceof Player) {
            Player player = (Player) human;
            Inventory inv = player.getOpenInventory().getTopInventory();
            if (inv.getHolder() != null && inv.getHolder() instanceof Menu && event.getRawSlot() >= 0 && event.getRawSlot() < this.getSize()) {
                Menu menu = (Menu) inv.getHolder();
                if (menu.getId() == this.getId()) {
                    event.setCancelled(true);

                    if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE) && willCloseOnOutsideClick()) {
                        player.closeInventory();
                        return;
                    }

                    Icon icon = getSlots().get(event.getSlot());
                    if (icon != null) {
                        IconClickEvent openEvent = new IconClickEvent(this, icon, player);
                        Bukkit.getServer().getPluginManager().callEvent(openEvent);
                        if (openEvent.isCancelled()) {
                            return;
                        }

                        if (icon.willClose()) {
                            player.closeInventory();
                        }
                        icon.onClick(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack clickIcon = this.getClickItem();
        if (clickIcon != null) {
            ItemStack itemStack = event.getItem();
            if (itemStack != null && itemStack.isSimilar(clickIcon)) {
                this.show(event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

}