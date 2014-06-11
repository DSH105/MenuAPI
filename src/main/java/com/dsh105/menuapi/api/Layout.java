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

import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Represents a Layout that can be used to create persistent Menus
 * </p>
 * Saving and loading from files is done using the Bukkit Config API
 */
public class Layout extends SlotHolder {

    private static final String LOAD_FAIL_MESSAGE = "Invalid Menu configuration. %s";

    public Layout() {
    }

    /**
     * Construct a new Layout from an existing Menu
     *
     * @param menu Menu to construct the Layout from
     */
    public Layout(Menu menu) {
        this(menu.getSlots(), menu.getSize(), menu.getTitle(), menu.getClickItem());
    }

    /**
     * Construct a new Layout
     *
     * @param size Size of the Layout
     */
    public Layout(int size, String title) {
        this(new HashMap<Integer, Icon>(), size, title);
    }

    /**
     * Construct a new Layout
     *
     * @param size Size of the Layout
     */
    public Layout(int size, String title, ItemStack clickItem) {
        this(new HashMap<Integer, Icon>(), size, title, clickItem);
    }

    /**
     * Construct a new Layout
     *
     * @param slots Slots to initialise the Layout with
     * @param size  Size of the Layout
     */
    public Layout(HashMap<Integer, Icon> slots, int size) {
        this(slots, size, "Custom Menu");
    }

    /**
     * Construct a new Layout
     *
     * @param slots Slots to initialise the Layout with
     * @param size  Size of the Layout
     * @param title Title of the Layout
     */
    public Layout(HashMap<Integer, Icon> slots, int size, String title) {
        this(slots, size, title, null);
    }

    /**
     * Construct a new Layout
     *
     * @param slots     Slots to initialise the Layout with
     * @param size      Size of the Layout
     * @param title     Title of the Layout
     * @param clickItem Click item registered to a Menu
     */
    public Layout(HashMap<Integer, Icon> slots, int size, String title, ItemStack clickItem) {
        super(size, title, clickItem, slots);
    }

    /**
     * Converts a Layout to a new {@link com.dsh105.menuapi.api.Menu}
     *
     * @param plugin Plugin to create the Menu for
     * @return Constructed Menu
     */
    public Menu toMenu(JavaPlugin plugin) {
        return new Menu(plugin, this);
    }

    /**
     * Save a Layout to a file to allow for persistence
     *
     * @param config Config file to save to
     * @return This object
     */
    public Layout saveToFile(FileConfiguration config) {
        return this.saveToFile(config, "");
    }

    /**
     * Save a Layout to a file to allow for persistence
     *
     * @param config      Config file to save to
     * @param sectionName Section to save the data under
     * @return This object
     */
    public Layout saveToFile(FileConfiguration config, String sectionName) {
        ConfigurationSection section = (sectionName == null || sectionName.isEmpty()) ? config : config.getConfigurationSection(sectionName);

        section.set("size", getSize());
        section.set("title", getTitle());

        if (this.getClickItem() != null) {
            this.saveItem(getClickItem(), section, "item.");
        }

        ConfigurationSection slotsSection = section.getConfigurationSection("slots");
        for (int i = 1; i <= getSize(); i++) { // Account for people who don't know about '0' being the first. Use '1' instead
            Icon icon = getSlot(i - 1);
            this.saveItem(icon.getIcon(), slotsSection, "slot-" + i + ".");
            if (icon instanceof CommandIcon) {
                config.set("slot-" + i + ".command", ((CommandIcon) icon).getCommand());
                config.set("slot-" + i + ".permission", ((CommandIcon) icon).getPermission());
                config.set("slot-" + i + ".changeNameColours", ((CommandIcon) icon).willChangeNameColours());
                config.set("slot-" + i + ".performAsConsole", ((CommandIcon) icon).willPerformAsConsole());
            }
        }

        return this;
    }

    /**
     * Load a saved Layout from a configuration file
     *
     * @param config Config file to save to
     * @return This object
     */
    public Layout loadFromFile(FileConfiguration config) {
        return this.loadFromFile(config, "");
    }

    /**
     * Load a saved Layout from a configuration file
     *
     * @param config      Config file to save to
     * @param sectionName Section to save the data under
     * @return This object
     */
    public Layout loadFromFile(FileConfiguration config, String sectionName) {
        ConfigurationSection section = (sectionName == null || sectionName.isEmpty()) ? config : config.getConfigurationSection(sectionName);

        Validate.notNull(section.get("slots"), String.format(LOAD_FAIL_MESSAGE, "Inventory size not found!"));
        Validate.notNull(section.get("title"), String.format(LOAD_FAIL_MESSAGE, "Menu name not found!"));

        this.title = section.getString("title", "Menu");
        this.size = section.getInt("slots", 45);

        ConfigurationSection clickItemSection = config.getConfigurationSection("item");
        if (clickItemSection != null) {
            this.setClickItem(this.loadItem(clickItemSection, ""));
        }

        ConfigurationSection slotsSection = section.getConfigurationSection("slots");
        for (int i = 1; i <= getSize(); i++) { // Account for people who don't know about '0' being the first. Use '1' instead
            Icon icon;
            ItemStack iconStack = this.loadItem(slotsSection, "slot-" + i + ".");
            if (config.get("slot-" + i + ".command") != null) {
                icon = new CommandIcon(config.getString("slot-" + i + ".command"), config.getString("slot-" + i + ".permission"), iconStack);
                ((CommandIcon) icon).setChangeNameColours(config.getBoolean("slot-" + i + ".changeNameColours", true));
                ((CommandIcon) icon).setPerformAsConsole(config.getBoolean("slot-" + i + ".performAsConsole", false));
            } else {
                icon = new Icon(this.loadItem(slotsSection, "slot-" + i + "."));
            }
            this.setSlot(i - 1, icon);
        }

        return this;
    }

    /**
     * Move a saved Layout from one configuration file to another
     * </p>
     * This does NOT delete the old data
     *
     * @param from Config file to retrieve saved data from
     * @param to   Config file to save data to
     * @return This object
     */
    public Layout moveFileDataTo(FileConfiguration from, FileConfiguration to) {
        return this.moveFileDataTo(from, "", to, "");
    }

    /**
     * @param from            Config file to retrieve saved data from
     * @param fromSectionName Section to retrieve data from
     * @param to              Config file to save data to
     * @param toSectionName   Section to save data to
     * @return This object
     */
    public Layout moveFileDataTo(FileConfiguration from, String fromSectionName, FileConfiguration to, String toSectionName) {
        ConfigurationSection fromSection = (fromSectionName == null || fromSectionName.isEmpty()) ? from : from.getConfigurationSection(fromSectionName);

        Validate.notNull(fromSection.get("size"), String.format(LOAD_FAIL_MESSAGE, "Inventory size not found!"));
        Validate.notNull(fromSection.get("title"), String.format(LOAD_FAIL_MESSAGE, "Menu name not found!"));

        this.loadFromFile(from, fromSectionName);
        this.saveToFile(to, toSectionName);

        return this;
    }

    public ItemStack loadItem(ConfigurationSection configSection, String searchPrefix) {
        String name = configSection.getString(searchPrefix + "name");
        Material material = Material.getMaterial(configSection.getString(searchPrefix + "material"));
        short materialData = (short) configSection.getInt(searchPrefix + "materialData", 0);
        int amount = configSection.getInt(searchPrefix + "amount", 1);
        String rawLore = configSection.getString(searchPrefix + "lore");

        Validate.notNull(name, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item name was not found!"));
        Validate.notNull(material, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item material was not found!"));
        Validate.notNull(rawLore, String.format(LOAD_FAIL_MESSAGE, "Click item config section located, but item lore was not found!"));

        String[] lore = new String[]{rawLore};
        if (rawLore.contains("_l")) {
            lore = rawLore.split("_l");
        }

        String[] loreCopy = new String[lore.length];
        for (int i = 0; i < loreCopy.length; i++) {
            loreCopy[i] = ChatColor.translateAlternateColorCodes('&', lore[i]);
        }

        return Icon.buildItemStack(material, amount, materialData, ChatColor.translateAlternateColorCodes('&', name), loreCopy);
    }

    public void saveItem(ItemStack toSave, ConfigurationSection configSection, String searchPrefix) {
        configSection.set(searchPrefix + "name", toSave.getItemMeta().getDisplayName());
        configSection.set(searchPrefix + "material", toSave.getType());
        configSection.set(searchPrefix + "materialData", toSave.getDurability());
        configSection.set(searchPrefix + "amount", toSave.getAmount());

        StringBuilder builder = new StringBuilder();
        for (String l : toSave.getItemMeta().getLore()) {
            builder.append(l + (builder.length() == 0 ? "" : "_l"));
        }
        configSection.set(searchPrefix + "lore", builder.toString());
    }
}