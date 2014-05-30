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

package com.dsh105.menuapi.util;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Original source: https://gist.github.com/DarkBlade12/9002495
 */

public class InventoryFactory {

    public static String toString(Inventory i) {
        YamlConfiguration configuration = new YamlConfiguration();
        configuration.set("Title", i.getTitle());
        configuration.set("Size", i.getSize());
        for (int a = 0; a < i.getSize(); a++) {
            ItemStack s = i.getItem(a);
            if (s != null) {
                configuration.set("Contents." + a, s);
            }
        }
        return Base64Coder.encodeString(configuration.saveToString());
    }

    public static Inventory fromString(String s) {
        YamlConfiguration configuration = new YamlConfiguration();
        try {
            configuration.loadFromString(Base64Coder.decodeString(s));
            Inventory i = Bukkit.createInventory(null, configuration.getInt("Size"), configuration.getString("Title"));
            ConfigurationSection contents = configuration.getConfigurationSection("Contents");
            for (String index : contents.getKeys(false)) {
                i.setItem(Integer.parseInt(index), contents.getItemStack(index));
            }
            return i;
        } catch (InvalidConfigurationException e) {
            return null;
        }
    }
}