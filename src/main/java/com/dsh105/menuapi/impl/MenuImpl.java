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

package com.dsh105.menuapi.impl;

import com.dsh105.menuapi.api.Icon;
import com.dsh105.menuapi.api.Menu;
import com.dsh105.menuapi.persistence.Layout;
import com.dsh105.menuapi.util.MenuId;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a {@link com.dsh105.menuapi.api.Menu}
 */
public class MenuImpl implements Menu {

    private long id;
    private int size;
    private String title;

    private HashMap<Integer, Icon> slots = new HashMap<>();
    private ItemStack clickItem;

    protected MenuImpl(JavaPlugin plugin) {
        this.id = MenuId.next();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Construct a Menu with the given title and size
     * </p>
     * Menu sizes will be automatically adjusted to accommodate a multiple of nine
     *
     * @param plugin Plugin instance
     * @param title  Title of the inventory
     * @param size   Size of the inventory
     */
    public MenuImpl(JavaPlugin plugin, String title, int size) {
        this(plugin);
        if (size < 0) {
            size = 9;
        } else if (size % 9 != 0) {
            size += 9 - (size % 9);
        }
        this.title = title;
        this.size = size;
    }

    public MenuImpl(JavaPlugin plugin, Layout layout) {
        this(plugin, layout.getTitle(), layout.getSize());
        for (Map.Entry<Integer, Icon> entry : layout.getSlots().entrySet()) {
            this.setSlot(entry.getKey(), entry.getValue());
        }
        this.clickItem = layout.getClickItem();
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, this.getSize(), this.getTitle());
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Map<Integer, Icon> getSlots() {
        return new HashMap<>(slots);
    }

    @Override
    public void setSlot(int slot, Icon icon) {
        if (slot >= this.size) {
            throw new IllegalArgumentException("Slot " + slot + " does not exist. Failed to apply Icon to slot.");
        }
        this.slots.put(slot, icon);
    }

    @Override
    public Icon getSlot(int slot) {
        return this.slots.get(slot);
    }

    @Override
    public ItemStack getClickItem() {
        return clickItem;
    }

    @Override
    public void setClickItem(ItemStack clickItem) {
        this.clickItem = clickItem;
    }

    @Override
    public void show(Player viewer) {
        Event openEvent = this.getEventToCall();
        if (openEvent != null) {
            Bukkit.getServer().getPluginManager().callEvent(openEvent);
            if (openEvent instanceof Cancellable && ((Cancellable) openEvent).isCancelled()) {
                return;
            }
        }

        Inventory inv = this.getInventory();
        for (Map.Entry<Integer, Icon> entry : this.getSlots().entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getIcon(viewer));
        }
        viewer.openInventory(inv);
    }

    @Override
    public void show(Player... viewers) {
        for (Player viewer : viewers) {
            this.show(viewer);
        }
    }

    @Override
    public Event getEventToCall() {
        return null;
    }

    @Override
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
                    Icon icon = slots.get(event.getSlot());
                    if (icon != null) {
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