/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.cubitDynmap.plugin;


import de.linzn.cubitDynmap.plugin.dynmap.DynmapCubitAPI;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;


public class CubitDynmapPlugin extends JavaPlugin {

    private static CubitDynmapPlugin inst;
    public DynmapCubitAPI dynmapCubitAPI;

    public static CubitDynmapPlugin inst() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        if (!getPluginDepends()) {
            this.setEnabled(false);
            return;
        }
        this.dynmapCubitAPI = new DynmapCubitAPI();
        this.getServer().getPluginManager().registerEvents(new CubitEventListener(), this);
        new Metrics(this);
        getLogger().info("Cubit dynmap hook enabled");

    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(CubitDynmapPlugin.inst());
    }

    private boolean getPluginDepends() {
        if (this.getServer().getPluginManager().getPlugin("Cubit") == null) {
            this.getLogger().severe("Error: " + "Cubit not found!");
            return false;
        }

        if (this.getServer().getPluginManager().getPlugin("dynmap") == null) {
            this.getLogger().severe("Error: " + "Dynmap not found!");
            return false;
        }

        return true;
    }


}
