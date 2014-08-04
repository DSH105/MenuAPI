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

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Represents a slot holder that can be used to store icons for use in a {@link com.dsh105.menuapi.api.Menu}
 */
public abstract class SlotHolder {

    protected HashMap<Integer, Icon> slots = new HashMap<>();
    protected int size;
    protected String title;
    protected ItemStack clickItem;

    protected SlotHolder() {

    }

    protected SlotHolder(int size, String title) {
        this(size, title, new HashMap<Integer, Icon>());
    }

    protected SlotHolder(int size, String title, HashMap<Integer, Icon> slots) {
        this(size, title, null, slots);
    }

    protected SlotHolder(int size, String title, ItemStack clickItem, HashMap<Integer, Icon> slots) {
        setSize(size);
        this.title = title;
        this.slots = slots;
        this.clickItem = clickItem;
    }

    protected void setSize(int size) {
        if (size < 0) {
            size = 9;
        } else if (size % 9 != 0) {
            size += 9 - (size % 9);
        }
        this.size = size;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the inventory size of a Menu
     *
     * @return Size of a menu
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the inventory title of a Menu
     *
     * @return Title of a menu
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the registered click item that opens a Menu when clicked by a player
     *
     * @return Click item registered to a Menu
     */
    public ItemStack getClickItem() {
        return clickItem;
    }

    /**
     * Sets the click item that opens a Menu when clicked by a player
     * <p>
     *
     * @param clickItem Click item to register
     * @see {@link org.bukkit.inventory.ItemStack#isSimilar(org.bukkit.inventory.ItemStack)} for method used to compare
     * itemstack similarity for menu triggers
     */
    public void setClickItem(ItemStack clickItem) {
        this.clickItem = clickItem;
    }

    /**
     * Gets a map of {@link com.dsh105.menuapi.api.Icon} slots registered with a SlotHolder
     *
     * @return Map of slot numbers to Icons representing the slots registered with a SlotHolder
     * @see com.dsh105.menuapi.api.Icon for more information on adding Icons to a SlotHolder
     */
    public HashMap<Integer, Icon> getSlots() {
        return new HashMap<>(slots);
    }

    /**
     * Registers an Icon to a certain slot in a SlotHolder
     *
     * @param slot Slot number to apply the Icon to
     * @param icon Icon to apply to the slot
     * @throws java.lang.IllegalArgumentException if the slot number does not exist
     */
    public void setSlot(int slot, Icon icon) {
        if (slot >= this.size) {
            throw new IllegalArgumentException("Slot " + slot + " does not exist. Failed to apply Icon to slot.");
        }
        this.slots.put(slot, icon);
    }

    /**
     * Gets the Icon registered in a slot of a SlotHolder
     *
     * @param slot Slot number to look for
     * @return An Icon if the slot is registered, null if not
     */
    public Icon getSlot(int slot) {
        return this.slots.get(slot);
    }
}