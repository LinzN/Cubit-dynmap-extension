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

import de.linzn.cubit.internal.cubitEvents.CubitLandBuyEvent;
import de.linzn.cubit.internal.cubitEvents.CubitLandSellEvent;
import de.linzn.cubit.internal.cubitEvents.CubitLandUpdateEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CubitEventListener implements Listener {
    @EventHandler
    public void onCubitLandBuyEvent(CubitLandBuyEvent cubitLandBuyEvent) {

        Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.addNewStyle(cubitLandBuyEvent.getRegionData()), 40L);
    }

    @EventHandler
    public void onCubitLandSellEvent(CubitLandSellEvent cubitLandSellEvent) {
        Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.removeExistStyle(cubitLandSellEvent.getRegionID()), 40L);
    }

    @EventHandler
    public void onCubitLandUpdateEvent(CubitLandUpdateEvent cubitLandUpdateEvent) {
            Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.removeExistStyle(cubitLandUpdateEvent.getRegionID()), 20L);
            Bukkit.getScheduler().runTaskLater(CubitDynmapPlugin.inst(), () -> CubitDynmapPlugin.inst().dynmapCubitAPI.addNewStyle(cubitLandUpdateEvent.getRegionData()), 40L);
    }
}
