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
    private int amount;
    private String name;
    private String[] lore;

    private IconCallback callback;

    private boolean close = true;

    protected Icon() {

    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param material Type of item
     * @param name     Name of item
     * @param lore     Item description
     */
    public Icon(Material material, String name, String... lore) {
        this(material, 1, (short) 0, name, lore);
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param material Type of item
     * @param amount   Amount of the item
     * @param name     Name of item
     * @param lore     Item description
     */
    public Icon(Material material, int amount, String name, String... lore) {
        this(material, amount, (short) 0, name, lore);
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param material     Type of item
     * @param amount       Amount of the item
     * @param materialData Item data
     * @param name         Name of item
     * @param lore         Item description
     */
    public Icon(Material material, int amount, short materialData, String name, String... lore) {
        this.material = material;
        this.amount = amount;
        this.materialData = materialData;
        this.name = name;
        this.lore = lore;
    }

    /**
     * Constructs an Icon for a Menu
     *
     * @param itemStack ItemStack to represent this Icon
     */
    public Icon(ItemStack itemStack) {
        this(itemStack.getType(), itemStack.getAmount(), itemStack.getDurability(), itemStack.getItemMeta().getDisplayName());
        List<String> lore = itemStack.getItemMeta().getLore();
        this.lore = lore.toArray(new String[lore.size()]);
    }

    /**
     * Builds an ItemStack from the given information
     *
     * @param material     Type of item
     * @param amount       Amount of the item
     * @param materialData Item data
     * @param name         Name of item
     * @param lore         Item description
     * @return Constructed ItemStack
     */
    public static ItemStack buildItemStack(Material material, int amount, short materialData, String name, String... lore) {
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
     * Sets the type of an Icon
     *
     * @param material New type
     */
    public void setMaterial(Material material) {
        this.material = material;
        this.itemStack = null;
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
     * Sets the item data of an Icon
     *
     * @param materialData New item data
     */
    public void setMaterialData(short materialData) {
        this.materialData = materialData;
        this.itemStack = null;
    }

    /**
     * Gets the amount of an item shown in a Menu to represent an Icon
     *
     * @return Amount of an item shown to represent an Icon
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of an item shown in a Menu to represent an Icon
     *
     * @param amount New item amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
        this.itemStack = null;
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
     * Sets the item name of an Icon
     *
     * @param name New item name
     */
    public void setName(String name) {
        this.name = name;
        this.itemStack = null;
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
     * Sets the item description of an Icon
     *
     * @param lore New item description
     */
    public void setLore(String... lore) {
        this.lore = lore;
        this.itemStack = null;
    }

    /**
     * Gets the {@link org.bukkit.inventory.ItemStack} that represents an Icon
     *
     * @return Type of an Icon
     */
    public ItemStack getIcon() {
        if (itemStack == null) {
            itemStack = buildItemStack(this.material, this.amount, this.materialData, this.name, this.lore);
        }
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
     *
     * @return True if the Menu is to close, false if not
     */
    public boolean willClose() {
        return close;
    }

    /**
     * Sets whether the {@link com.dsh105.menuapi.api.Menu} an Icon is added to will close when an Icon is clicked
     *
     * @param close True if the Menu is to close, false if not
     */
    public void setClose(boolean close) {
        this.close = close;
    }

    /**
     * Sets the callback for an Icon
     * <p/>
     * Callbacks are executed when the icon is clicked by a player
     *
     * @param callback Callback
     */
    public void setCallback(IconCallback callback) {
        this.callback = callback;
    }

    /**
     * Called when an Icon is clicked by a player viewing a Menu
     *
     * @param viewer Player that opened the Menu
     */
    public void onClick(Player viewer) {
        if (this.callback != null) {
            this.callback.run(viewer);
        }
    }
}