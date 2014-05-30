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

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Represents an Icon that appears in a {@link com.dsh105.menuapi.api.Menu}
 * </p>
 * Icons handle the events that occur when a certain button in an inventory is clicked
 */
public class Icon {

    private ItemStack itemStack;
    private Material material;
    private short materialData;
    private String name;
    private String[] lore;

    /**
     * Constructs an Icon for a Menu
     *
     * @param material Type of item
     * @param name Name of item
     * @param lore Item description
     */
    public Icon(Material material, String name, String... lore) {
        this(material, 1, (short) 0, name, lore);
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param material Type of item
     * @param amount Amount of the item
     * @param name Name of item
     * @param lore Item description
     */
    public Icon(Material material, int amount, String name, String... lore) {
        this(material, amount, (short) 0, name, lore);
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param material Type of item
     * @param amount Amount of the item
     * @param materialData Item data
     * @param name Name of item
     * @param lore Item description
     */
    public Icon(Material material, int amount, short materialData, String name, String... lore) {
        this(buildItemStack(material, amount, materialData, name, lore));
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param itemStack ItemStack to represent this Icon
     */
    public Icon(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.material = itemStack.getType();
        this.materialData = itemStack.getDurability();
        this.name = itemStack.getItemMeta().getDisplayName();
        List<String> lore = itemStack.getItemMeta().getLore();
        this.lore = lore.toArray(new String[lore.size()]);
    }

    private static ItemStack buildItemStack(Material material, int amount, short materialData, String name, String... lore) {
        ItemStack i = new ItemStack(material, amount, materialData);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(name);
        if (lore != null && lore.length > 0) {
            meta.setLore(Arrays.asList(lore));
        }
        i.setItemMeta(meta);
        return i;
    }

    /**
     * Gets the type of an Icon
     *
     * @return Type of an Icon
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Gets the item data of an Icon
     *
     * @return Item data
     */
    public short getMaterialData() {
        return materialData;
    }

    /**
     * Gets the item name of an Icon
     *
     * @return Name of an Icon
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item description of an Icon
     *
     * @return Item description of an Icon
     */
    public String[] getLore() {
        return lore;
    }

    /**
     * Gets the {@link org.bukkit.inventory.ItemStack} that represents an Icon
     *
     * @return Type of an Icon
     */
    public ItemStack getIcon() {
        return itemStack;
    }

    /**
     * Gets the {@link org.bukkit.inventory.ItemStack} that represents an Icon
     * </p>
     * Override this method to implement custom named items for certain players
     *
     * @return Type of an Icon
     */
    public ItemStack getIcon(Player viewer) {
        return getIcon();
    }

    /**
     * Gets whether the {@link com.dsh105.menuapi.api.Menu} an Icon is added to will close when an Icon is clicked
     * @return
     */
    public boolean willClose() {
        return true;
    }

    /**
     * Called when an Icon is clicked by a player viewing a Menu
     *
     * @param viewer Player that opened the Menu
     */
    public void onClick(Player viewer) {

    }
}