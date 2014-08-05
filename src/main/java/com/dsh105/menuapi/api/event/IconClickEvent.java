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

package com.dsh105.menuapi.api.event;

import com.dsh105.menuapi.api.Icon;
import com.dsh105.menuapi.api.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when an {@link com.dsh105.menuapi.api.Icon} is clicked in a {@link com.dsh105.menuapi.api.Menu} by a {@link
 * org.bukkit.entity.Player}
 */
public class IconClickEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;

    private Menu menu;
    private Icon clicked;
    private Player viewer;

    public IconClickEvent(Menu menu, Icon clicked, Player viewer) {
        this.menu = menu;
        this.clicked = clicked;
        this.viewer = viewer;
    }

    /**
     * Gets the Menu in which the Icon was clicked
     *
     * @return Menu hosting the Icon clicked
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * Gets the Icon clicked
     *
     * @return Icon clicked
     */
    public Icon getClicked() {
        return clicked;
    }

    /**
     * Gets the Player viewing the Menu
     *
     * @return Player viewing the Menu
     */
    public Player getViewer() {
        return viewer;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}