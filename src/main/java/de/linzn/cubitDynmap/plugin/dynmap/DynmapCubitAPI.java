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


import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionType;
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
        this.cubitMarkers = this.dynmapAPI.getMarkerAPI().getMarkerSet("cubit.markerset");
        /* If already exist */
        if (this.cubitMarkers == null) {
            this.cubitMarkers = this.dynmapAPI.getMarkerAPI().createMarkerSet("cubit.markerset", "Cubit", null, true);
        }
    }

    public void addNewStyle(CubitLand cubitLand) {
        double[] x;
        double[] z;
        AreaMarker areaMarker;
        ProtectedRegion wgRegion = cubitLand.getWGRegion();
        RegionType tn = wgRegion.getType();
        BlockVector3 l0 = wgRegion.getMinimumPoint();
        BlockVector3 l1 = wgRegion.getMaximumPoint();

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

        String descriptionString = this.getDescription(cubitLand);

        areaMarker = cubitMarkers.createAreaMarker(cubitLand.getLandName(), cubitLand.getLandName(), false, cubitLand.getWorld().getName(), x, z, true);
        int colorCode = getColorCode(cubitLand);
        if (areaMarker != null) {
            areaMarker.setDescription(descriptionString);
            areaMarker.setFillStyle(0.4, colorCode);
            areaMarker.setLineStyle(0, 0, 0);
        }
    }

    public void removeExistStyle(final String regionID) {
        AreaMarker cubitMarker = this.cubitMarkers.findAreaMarker(regionID);
        if (cubitMarker != null) {
            cubitMarker.deleteMarker();
        }
    }

    public void clearData() {
        for(AreaMarker cubitMarker : this.cubitMarkers.getAreaMarkers()){
            cubitMarker.deleteMarker();
        }
        this.cubitMarkers.deleteMarkerSet();
    }

    private String getDescription(CubitLand cubitLand) {
        String cubitStringType = "<div class=\"cubitInfo\">" + "{typeString}" + "</div>";
        /* Set type */
        switch (cubitLand.getCubitType()) {
            case SERVER:
                cubitStringType = cubitStringType.replace("{typeString}", "<div class=\"infowindow\"><span style=\"font-size:120%;\"><center><b>Cubit Info</b></center> <br>{type} <br>{cubitLand}</span></div>");
                break;
            case SHOP:
                cubitStringType = cubitStringType.replace("{typeString}", "<div class=\"infowindow\"><span style=\"font-size:120%;\"><center><b>Cubit Info</b></center> <br>{type} <br>{cubitLand} <br>{owner}</span></div>");
                if (cubitLand.getOwnersUUID().length == 0) {
                    cubitStringType = cubitStringType.replace("{owner}", "Zum Verkauf");
                } else {
                    cubitStringType = cubitStringType.replace("{owner}", "<b>Inhaber: </b>" + cubitLand.getOwnerNames()[0]);
                }
                break;
            case WORLD:
                cubitStringType = cubitStringType.replace("{typeString}", "<div class=\"infowindow\"><span style=\"font-size:120%;\"><center><b>Cubit Info</b></center> <br>{type} <br>{cubitLand} <br>{owner}</span></div>");
                if (cubitLand.getOwnersUUID().length == 0) {
                    cubitStringType = cubitStringType.replace("{owner}", "Unbewohnt");
                } else {
                    cubitStringType = cubitStringType.replace("{owner}", "<b>Besitzer: </b>" + cubitLand.getOwnerNames()[0]);
                }
                break;
            default:
                cubitStringType = cubitStringType.replace("{typeString}", "<div class=\"infowindow\"><span style=\"font-size:120%;\"><center><b>Cubit Info</b></center> <br>{type} <br>{cubitLand}</span></div>");
                break;
        }
        cubitStringType = cubitStringType.replace("{cubitLand}", "<b>Id: </b>" + cubitLand.getLandName());
        cubitStringType = cubitStringType.replace("{type}", "<b>Typ: </b>" + cubitLand.getCubitType().name());
        return cubitStringType;
    }

    private int getColorCode(CubitLand cubitLand) {
        int colorCode;
        switch (cubitLand.getCubitType()) {
            case SHOP:
                if (cubitLand.getOwnersUUID().length > 0) {
                    colorCode = Integer.parseInt("275db0", 16);
                } else {
                    colorCode = Integer.parseInt("0db914", 16);
                }
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

    public void updateStyle(CubitLand cubitLand){
        removeExistStyle(cubitLand.getLandName());
        addNewStyle(cubitLand);
    }
}
