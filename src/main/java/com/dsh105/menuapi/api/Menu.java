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

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryHolder;

import java.util.Map;

/**
 * Represents an Inventory based Menu that can be managed and shown to various players.
 * </p>
 * Click events and icon slots are handled internally by the Menu
 * </p>
 * An {@link com.dsh105.menuapi.api.Icon} represents an action to be performed when a certain item in the inventory is clicked
 */
public interface Menu extends InventoryHolder, Listener {

    /**
     * Gets the registered ID of a Menu
     *
     * @return Registered ID of a menu
     */
    public long getId();

    /**
     * Gets the inventory size of a Menu
     *
     * @return Size of a menu
     */
    public int getSize();

    /**
     * Gets the inventory title of a Menu
     *
     * @return Title of a menu
     */
    public String getTitle();

    /**
     * Gets a map of {@link com.dsh105.menuapi.api.Icon} slots registered with a Menu
     *
     * @return Map of slot numbers to Icons representing the slots registered with a menu
     * @see com.dsh105.menuapi.api.Icon for more information on adding Icons to a Menu
     */
    public Map<Integer, Icon> getSlots();

    /**
     * Registers an Icon to a certain slot in a menu
     *
     * @param slot Slot number to apply the Icon to
     * @param icon Icon to apply to the slot
     * @throws java.lang.IllegalArgumentException if the slot number does not exist
     */
    public void setSlot(int slot, Icon icon);

    /**
     * Gets the Icon registered in a slot of a Menu
     *
     * @param slot Slot number to look for
     * @return An Icon if the slot is registered, null if not
     */
    public Icon getSlot(int slot);

    /**
     * Shows a Menu to a player
     *
     * @param viewer Player to show the Menu to
     */
    public void show(Player viewer);

    /**
     * Shows a Menu to multiple players
     *
     * @param viewers Players to show the Menu to
     */
    public void show(Player... viewers);

}