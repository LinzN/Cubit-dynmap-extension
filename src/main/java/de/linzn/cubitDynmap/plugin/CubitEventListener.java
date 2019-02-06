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


import de.linzn.cubit.api.events.CubitLandBuyEvent;
import de.linzn.cubit.api.events.CubitLandSellEvent;
import de.linzn.cubit.api.events.CubitLandUpdateEvent;
import de.linzn.cubit.internal.cubitRegion.region.CubitLand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CubitEventListener implements Listener {

    @EventHandler
    public void onCubitLandBuyEvent(final CubitLandBuyEvent cubitLandBuyEvent) {
        Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.setNewStyle(cubitLandBuyEvent.getCubitLand()), 40L);
    }

    @EventHandler
    public void onCubitLandSellEvent(final CubitLandSellEvent cubitLandSellEvent) {
        Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.removeExistStyle(cubitLandSellEvent.getRegionID()), 40L);
    }

    @EventHandler
    public void onCubitLandUpdateEvent(final CubitLandUpdateEvent cubitLandUpdateEvent) {
        CubitLand cubitLand = cubitLandUpdateEvent.getRegionData();
        //todo only tempfix
        Bukkit.getScheduler().runTaskLaterAsynchronously(CubitDynmapPlugin.inst(), () -> {
            cubitLand.setWGRegion(cubitLand.getWGRegion(), true);
            Bukkit.getScheduler().runTask(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.setNewStyle(cubitLand));
        }, 20);
    }
}
