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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a command based Icon
 */
public class CommandIcon extends Icon {

    private String permission;
    private String command;

    private boolean changeNameColours = true;
    private boolean performAsConsole;

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param permission Permission required by the player to run the command
     * @param command    Command to run
     * @param material   Type of item
     * @param name       Name of item
     * @param lore       Item description
     */
    public CommandIcon(String permission, String command, Material material, String name, String... lore) {
        super(material, name, lore);
        prepare(permission, command);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param permission Permission required by the player to run the command
     * @param command    Command to run
     * @param material   Type of item
     * @param amount     Amount of the item
     * @param name       Name of item
     * @param lore       Item description
     */
    public CommandIcon(String permission, String command, Material material, int amount, String name, String... lore) {
        super(material, amount, name, lore);
        prepare(permission, command);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param permission   Permission required by the player to run the command
     * @param command      Command to run
     * @param material     Type of item
     * @param amount       Amount of the item
     * @param materialData Item data
     * @param name         Name of item
     * @param lore         Item description
     */
    public CommandIcon(String permission, String command, Material material, int amount, short materialData, String name, String... lore) {
        super(material, amount, materialData, name, lore);
        prepare(permission, command);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param permission Permission required by the player to run the command
     * @param command    Command to run
     * @param itemStack  ItemStack to represent this Icon
     */
    public CommandIcon(String permission, String command, ItemStack itemStack) {
        super(itemStack);
        prepare(permission, command);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param command  command to run
     * @param material material of icon
     * @param name     display name of icon
     * @param lore     lore of icon
     */
    public CommandIcon(String command, Material material, String name, String... lore) {
        this(null, command, material, name, lore);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param command  Command to run
     * @param material Type of item
     * @param amount   Amount of the item
     * @param name     Name of item
     * @param lore     Item description
     */
    public CommandIcon(String command, Material material, int amount, String name, String... lore) {
        this(null, command, material, amount, name, lore);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param command      Command to run
     * @param material     Type of item
     * @param amount       Amount of the item
     * @param materialData Item data
     * @param name         Name of item
     * @param lore         Item description
     */
    public CommandIcon(String command, Material material, int amount, short materialData, String name, String... lore) {
        this(null, command, material, amount, materialData, name, lore);
    }

    /**
     * Constructs a command based Icon for a Menu
     *
     * @param command   Command to run
     * @param itemStack ItemStack to represent this Icon
     */
    public CommandIcon(String command, ItemStack itemStack) {
        this(null, command, itemStack);
    }

    private void prepare(String permission, String command) {
        this.permission = permission;
        this.command = command;
    }

    /**
     * Get the permission node that is required
     *
     * @return Permission node required to run the command
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Get the command that will be executed
     *
     * @return Command to be executed
     */
    public String getCommand() {
        return command;
    }

    /**
     * Gets whether the icon can modify the name to match whether the player viewing it can execute the command or not
     * <p/>
     * Modifies item name to either green or red, based on the above condition
     *
     * @return True if the item name can be changed
     */
    public boolean willChangeNameColours() {
        return changeNameColours;
    }

    /**
     * Sets whether the icon can modify the name to match whether the player viewing it can execute the command or not
     * <p/>
     * Modifies item name to either green or red, based on the above condition
     *
     * @param changeNameColours True if the item name can be changed
     */
    public void setChangeNameColours(boolean changeNameColours) {
        this.changeNameColours = changeNameColours;
    }

    /**
     * Gets if the command is to be executed as console
     *
     * @return True if command will be executed as console
     */
    public boolean willPerformAsConsole() {
        return performAsConsole;
    }

    /**
     * Sets if the command is to be executed as console
     *
     * @param performAsConsole True if command will be executed as console
     */
    public void setPerformAsConsole(boolean performAsConsole) {
        this.performAsConsole = performAsConsole;
    }

    @Override
    public ItemStack getIcon(Player viewer) {
        String finalName = this.getName();
        if (willChangeNameColours()) {
            finalName = ((this.permission == null ? true : viewer.hasPermission(this.permission)) ? ChatColor.GREEN : ChatColor.RED) + ChatColor.stripColor(this.getName());
        }
        return buildItemStack(this.getMaterial(), this.getAmount(), this.getMaterialData(), finalName, this.getLore());
    }

    @Override
    public void onClick(Player viewer) {
        Bukkit.dispatchCommand(this.willPerformAsConsole() ? Bukkit.getConsoleSender() : viewer, this.getCommand());
    }
}