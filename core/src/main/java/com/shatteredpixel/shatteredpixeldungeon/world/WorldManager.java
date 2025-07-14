/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2025 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.shatteredpixel.shatteredpixeldungeon.world;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;

import java.util.HashMap;

public class WorldManager {

    private static HashMap<Integer, LevelData> levelDataCache;
    private static WorldMap worldMap;

    public static void initialize() {
        levelDataCache = new HashMap<>();
        worldMap = new WorldMap();
        TestWorldData.createTestWorld(worldMap);
    }

    public static WorldMap getWorldMap() {
        return worldMap;
    }

    public static void setWorldMap(WorldMap map) {
        worldMap = map;
    }

    public static Level createLevelForLocation(int locationId) {
        WorldLocation location = worldMap.getLocation(locationId);
        if (location == null) {
            return null;
        }

        LevelData levelData = getOrCreateLevelData(location);
        return new WorldLevel(levelData, location);
    }

    public static LevelData getOrCreateLevelData(WorldLocation location) {
        if (levelDataCache.containsKey(location.locationId)) {
            return levelDataCache.get(location.locationId);
        }

        LevelData levelData = createDefaultLevelData(location);
        levelDataCache.put(location.locationId, levelData);
        return levelData;
    }

    public static void cacheLevelData(int locationId, LevelData levelData) {
        levelDataCache.put(locationId, levelData);
    }

    private static LevelData createDefaultLevelData(WorldLocation location) {
        LevelData data = new LevelData(location.locationId, 32, 32); // Default 32x32 level

        // Create layout based on location type
        data.createLayoutForType(location.levelType);
        
        // Set default properties based on location type
        switch (location.levelType) {
            case VILLAGE:
                data.tilesTexture = Assets.Environment.TILES_CITY;
                data.waterTexture = Assets.Environment.WATER_CITY;
                data.color1 = 0x4b6636;
                data.color2 = 0xf2f2f2;
                break;
            case FOREST:
                data.tilesTexture = Assets.Environment.TILES_CAVES; // Using caves tiles for forest
                data.waterTexture = Assets.Environment.WATER_CAVES;
                data.color1 = 0x534f3e;
                data.color2 = 0xb9d661;
                break;
            case CAVES:
                data.tilesTexture = Assets.Environment.TILES_CAVES;
                data.waterTexture = Assets.Environment.WATER_CAVES;
                data.color1 = 0x534f3e;
                data.color2 = 0xb9d661;
                break;
            case CITY:
                data.tilesTexture = Assets.Environment.TILES_CITY;
                data.waterTexture = Assets.Environment.WATER_CITY;
                data.color1 = 0x4b6636;
                data.color2 = 0xf2f2f2;
                break;
            case RUINS:
                data.tilesTexture = Assets.Environment.TILES_HALLS;
                data.waterTexture = Assets.Environment.WATER_HALLS;
                data.color1 = 0x801500;
                data.color2 = 0xa68521;
                break;
            case BOSS:
                data.tilesTexture = Assets.Environment.TILES_HALLS;
                data.waterTexture = Assets.Environment.WATER_HALLS;
                data.color1 = 0x801500;
                data.color2 = 0xa68521;
                break;
            case SPECIAL:
                data.tilesTexture = Assets.Environment.TILES_CAVES;
                data.waterTexture = Assets.Environment.WATER_CAVES;
                data.color1 = 0x534f3e;
                data.color2 = 0xb9d661;
                break;
            default:
                data.tilesTexture = Assets.Environment.TILES_CAVES;
                data.waterTexture = Assets.Environment.WATER_CAVES;
                data.color1 = 0x534f3e;
                data.color2 = 0xb9d661;
                break;
        }

        // Copy exits from world location to level data
        for (ExitPoint exit : location.exits) {
            data.exits.add(exit);
        }

        // Copy properties from world location to level data
        for (String key : location.properties.keySet()) {
            data.setProperty(key, location.getProperty(key));
        }

        return data;
    }

    public static boolean isWorldExplorationEnabled() {
        return worldMap != null;
    }

    public static int getCurrentLocationId() {
        if (worldMap != null) {
            return worldMap.getCurrentLocationId();
        }
        return 1; // Default fallback
    }

    public static WorldLocation getCurrentLocation() {
        if (worldMap != null) {
            return worldMap.getCurrentLocation();
        }
        return null;
    }

    public static boolean moveToLocation(int targetLocationId) {
        if (worldMap != null) {
            return worldMap.moveToLocation(targetLocationId);
        }
        return false;
    }

    public static boolean moveToLocation(int targetLocationId, int sourceCell) {
        if (worldMap != null) {
            return worldMap.moveToLocation(targetLocationId, sourceCell);
        }
        return false;
    }

    public static void markLocationExplored(int locationId) {
        if (worldMap != null) {
            worldMap.markLocationExplored(locationId);
        }
    }

    public static void markLocationDiscovered(int locationId) {
        if (worldMap != null) {
            worldMap.markLocationDiscovered(locationId);
        }
    }

    public static boolean isLocationExplored(int locationId) {
        if (worldMap != null) {
            return worldMap.isLocationExplored(locationId);
        }
        return false;
    }

    public static boolean isLocationDiscovered(int locationId) {
        if (worldMap != null) {
            return worldMap.isLocationDiscovered(locationId);
        }
        return false;
    }

    public static float getExplorationPercentage() {
        if (worldMap != null) {
            return worldMap.getExplorationPercentage();
        }
        return 0f;
    }

    public static int getLocationCount() {
        if (worldMap != null) {
            return worldMap.getLocationCount();
        }
        return 0;
    }

    public static int getDiscoveredLocationCount() {
        if (worldMap != null) {
            return worldMap.getDiscoveredLocationCount();
        }
        return 0;
    }

    public static int getExploredLocationCount() {
        if (worldMap != null) {
            return worldMap.getExploredLocationCount();
        }
        return 0;
    }

    public static void clearCache() {
        if (levelDataCache != null) {
            levelDataCache.clear();
        }
    }

    public static void reset() {
        clearCache();
        initialize();
    }
} 