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

package com.dsh105.menuapi.persistence;

import com.dsh105.menuapi.api.Icon;
import com.dsh105.menuapi.api.Menu;
import com.dsh105.menuapi.impl.MenuImpl;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class Layout {

    private static final String LOAD_FAIL_MESSAGE = "Invalid Menu configuration. %s";

    private HashMap<Integer, Icon> slots = new HashMap<>();
    private int size;
    private String title;
    private ItemStack clickItem;

    public Layout(Map<Integer, Icon> slots, int size) {
        this(slots, size, "Custom Menu");
    }

    public Layout(Map<Integer, Icon> slots, int size, String title) {
        this(slots, size, title, null);
    }

    public Layout(Map<Integer, Icon> slots, int size, String title, ItemStack clickItem) {
        this.slots = new HashMap<>(slots);
        this.size = size;
        this.title = title;
        this.clickItem = clickItem;
    }

    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public ItemStack getClickItem() {
        return clickItem;
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

    public Menu createMenu(JavaPlugin plugin) {
        return new MenuImpl(plugin, this);
    }

    public Layout loadFromFile(FileConfiguration config) {
        return this.loadFromFile(config, "");
    }

    public Layout loadFromFile(FileConfiguration config, String sectionName) {
        ConfigurationSection section = (sectionName == null || sectionName.isEmpty()) ? config : config.getConfigurationSection(sectionName);

        Validate.notNull(section.get("size"), String.format(LOAD_FAIL_MESSAGE, section.getName(), "Inventory size not found!"));
        Validate.notNull(section.get("title"), String.format(LOAD_FAIL_MESSAGE, section.getName(), "Menu name not found!"));

        title = section.getString("title", "Menu");
        size = section.getInt("slots", 45);

        ConfigurationSection clickItemSection = config.getConfigurationSection("item");
        if (clickItemSection != null) {
            clickItem = this.loadItem(clickItemSection, "");
        }

        ConfigurationSection slotsSection = section.getConfigurationSection("slots");
        for (int i = 1; i <= size; i++) { // Account for people who don't know about '0' being the first. Use '1' instead
            this.setSlot(i - 1, new Icon(this.loadItem(slotsSection, "slot-" + i + ".")));
        }

        return this;
    }

    private ItemStack loadItem(ConfigurationSection configSection, String searchPrefix) {
        String name = configSection.getString(searchPrefix + "name");
        Material material = Material.getMaterial(configSection.getString(searchPrefix + "material"));
        short materialData = (short) configSection.getInt(searchPrefix + "materialData", 0);
        int amount = configSection.getInt(searchPrefix + "amount", 1);
        String rawLore = configSection.getString(searchPrefix + "lore");

        Validate.notNull(name, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item name was not found!"));
        Validate.notNull(material, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item material was not found!"));
        Validate.notNull(rawLore, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item lore was not found!"));

        String[] lore = new String[]{rawLore};
        if (rawLore.contains("\\n")) {
            lore = rawLore.split("\\n");
        }

        String[] loreCopy = new String[lore.length];
        for (int i = 0; i < loreCopy.length; i++) {
            loreCopy[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
        }

        return Icon.buildItemStack(material, amount, materialData, ChatColor.translateAlternateColorCodes('&', name), loreCopy);
    }
}