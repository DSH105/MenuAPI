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
 * TODO: JavaDocs
 */
public class CommandIcon extends Icon {

    private String permission;
    private String command;

    private boolean changeNameColours = true;
    private boolean performAsConsole;

    public CommandIcon(String permission, String command, Material material, String name, String... lore) {
        super(material, name, lore);
        prepare(permission, command);
    }

    public CommandIcon(String permission, String command, Material material, int amount, String name, String... lore) {
        super(material, amount, name, lore);
        prepare(permission, command);
    }

    public CommandIcon(String permission, String command, Material material, int amount, short materialData, String name, String... lore) {
        super(material, amount, materialData, name, lore);
        prepare(permission, command);
    }

    public CommandIcon(String permission, String command, ItemStack itemStack) {
        super(itemStack);
        prepare(permission, command);
    }

    public CommandIcon(String command, Material material, String name, String... lore) {
        this(null, command, material, name, lore);
    }

    public CommandIcon(String command, Material material, int amount, String name, String... lore) {
        this(null, command, material, amount, name, lore);
    }

    public CommandIcon(String command, Material material, int amount, short materialData, String name, String... lore) {
        this(null, command, material, amount, materialData, name, lore);
    }

    public CommandIcon(String command, ItemStack itemStack) {
        this(null, command, itemStack);
    }

    private void prepare(String permission, String command) {
        this.permission = permission;
        this.command = command;
    }

    public String getPermission() {
        return permission;
    }

    public String getCommand() {
        return command;
    }

    public boolean willChangeNameColours() {
        return changeNameColours;
    }

    public void setChangeNameColours(boolean changeNameColours) {
        this.changeNameColours = changeNameColours;
    }

    public boolean willPerformAsConsole() {
        return performAsConsole;
    }

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