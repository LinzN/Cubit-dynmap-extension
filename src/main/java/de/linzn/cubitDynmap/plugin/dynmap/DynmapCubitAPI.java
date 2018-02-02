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

package de.linzn.cubitDynmap.plugin.dynmap;

import com.sk89q.worldedit.BlockVector;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
import de.linzn.cubit.internal.cubitRegion.CubitType;
import de.linzn.cubit.internal.cubitRegion.region.CubitLand;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import static org.bukkit.Bukkit.getServer;


public class DynmapCubitAPI {
    private MarkerSet cubitMarkers;
    private DynmapAPI dynmapAPI;

    public DynmapCubitAPI() {
        this.dynmapAPI = (DynmapAPI) getServer().getPluginManager().getPlugin("dynmap");
        this.cubitMarkers = this.dynmapAPI.getMarkerAPI().createMarkerSet("Cubit", "Cubit", null, true);
        /* If already exist */
        if (this.cubitMarkers == null) {
            this.cubitMarkers = this.dynmapAPI.getMarkerAPI().getMarkerSet("Cubit");
        }
    }

    public void addNewStyle(CubitLand regionData) {
        double[] x;
        double[] z;
        AreaMarker areaMarker;
        ProtectedRegion wgRegion = regionData.getWGRegion();
        RegionType tn = wgRegion.getType();
        BlockVector l0 = wgRegion.getMinimumPoint();
        BlockVector l1 = wgRegion.getMaximumPoint();

        if (tn != RegionType.CUBOID) {
            /* No valid region. But this should never happen */
            return;
        }
        x = new double[4];
        z = new double[4];
        x[0] = l0.getX();
        z[0] = l0.getZ();
        x[1] = l0.getX();
        z[1] = l1.getZ() + 1.0;
        x[2] = l1.getX() + 1.0;
        z[2] = l1.getZ() + 1.0;
        x[3] = l1.getX() + 1.0;
        z[3] = l0.getZ();

        String descriptionString = "Test";

        areaMarker = cubitMarkers.createAreaMarker(regionData.getLandName(), descriptionString, false, regionData.getWorld().getName(), x, z, true);
        int colorCode = getColorCode(regionData.getCubitType());
        if (areaMarker != null) {
            areaMarker.setFillStyle(0.4, colorCode);
            areaMarker.setLineStyle(0, 0, 0);
        }
    }

    public void removeExistStyle(final String regionID) {
        AreaMarker cubitMarker = this.cubitMarkers.findAreaMarker(regionID);
        if (cubitMarker != null){
            cubitMarker.deleteMarker();
        }
    }

    public void clearData(){
        this.cubitMarkers.deleteMarkerSet();
    }

    private int getColorCode(CubitType landTypes) {
        int colorCode;
        switch (landTypes) {
            case SHOP:
                colorCode = Integer.parseInt("275db0", 16);
                break;
            case SERVER:
                colorCode = Integer.parseInt("9c27b0", 16);
                break;
            default:
                colorCode = Integer.parseInt("a70000", 16);
                break;
        }
        return colorCode;
    }
}
