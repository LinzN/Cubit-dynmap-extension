/*
 *  Copyright (C) 2019. MineGaming - All Rights Reserved
 *  You may use, distribute and modify this code under the
 *  terms of the LGPLv3 license, which unfortunately won't be
 *  written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.cubitDynmap.plugin;


import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.linzn.cubit.api.CubitAPI;
import de.linzn.cubit.internal.cubitRegion.region.CubitLand;
import de.linzn.cubitDynmap.plugin.dynmap.DynmapCubitAPI;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collection;


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
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, this::loadRegionsToDynmap, 60L);
    }

    @Override
    public void onDisable() {
        this.dynmapCubitAPI.clearData();
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

    private void loadRegionsToDynmap() {
        this.getLogger().info("Loading all cubitLands to dynmap...");
        for (World world : Bukkit.getWorlds()) {
            ArrayList<CubitLand> worldLands = new ArrayList<>();

            Collection<ProtectedRegion> regions = WorldGuard.getInstance().getPlatform().getRegionContainer().get(new BukkitWorld(world)).getRegions().values();
            for (ProtectedRegion region : regions) {
                if (region.getId().split("_").length == 3) {
                    try {
                        int chunkX = Integer.parseInt(region.getId().split("_")[1]);
                        int chunkY = Integer.parseInt(region.getId().split("_")[2]);
                        CubitLand land = CubitAPI.getCubitLand(world, chunkX, chunkY);
                        worldLands.add(land);
                    } catch (NumberFormatException ignored) {
                        /* If __global__ ore somthing else*/
                        this.getLogger().info("Not a cubit land. Ignoring " + region.getId());
                    }
                }
            }

            Bukkit.getScheduler().runTask(CubitDynmapPlugin.inst(), () -> {
                for (CubitLand land : worldLands) {
                    this.dynmapCubitAPI.setNewStyle(land);
                }
                this.getLogger().info("Finish for world " + world.getName());
            });

        }
    }


}
