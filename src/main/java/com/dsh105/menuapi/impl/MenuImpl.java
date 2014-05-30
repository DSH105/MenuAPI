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
import com.dsh105.menuapi.util.MenuId;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
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

    /**
     * Construct a Menu with the given title and size
     * </p>
     * Menu sizes will be automatically adjusted to accommodate a multiple of nine
     *
     * @param plugin Plugin instance
     * @param title Title of the inventory
     * @param size Size of the inventory
     */
    public MenuImpl(JavaPlugin plugin, String title, int size) {
        this.id = MenuId.next();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        if (size < 0) {
            size = 9;
        } else if (size % 9 != 0) {
            size += 9 - (size % 9);
        }
        this.title = title;
        this.size = size;
    }

    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(this, this.getSize(), this.getTitle());
    }

    public long getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public Map<Integer, Icon> getSlots() {
        return new HashMap<>(slots);
    }

    public void setSlot(int slot, Icon icon) {
        if (slot >= this.size) {
            throw new IllegalArgumentException("Slot " + slot + " does not exist. Failed to apply Icon to slot.");
        }
        this.slots.put(slot, icon);
    }

    public Icon getSlot(int slot) {
        return this.slots.get(slot);
    }

    public void show(Player viewer) {
        Inventory inv = this.getInventory();
        for (Map.Entry<Integer, Icon> entry : this.getSlots().entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().getIcon(viewer));
        }
        viewer.openInventory(inv);
    }

    public void show(Player... viewers) {
        for (Player viewer : viewers) {
            this.show(viewer);
        }
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
}