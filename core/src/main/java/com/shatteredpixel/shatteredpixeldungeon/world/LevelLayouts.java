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

import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;

/**
 * Pre-designed level layouts for different location types.
 * Each layout is designed to match the theme and gameplay of its location type.
 */
public class LevelLayouts {

    // Village Layout - Peaceful, open areas with buildings
    public static void createVillageLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Main square (center)
        data.createSimpleRoom(12, 12, 8, 8);
        
        // Buildings around the square
        data.createSimpleRoom(4, 4, 6, 6);    // House 1
        data.createSimpleRoom(22, 4, 6, 6);   // House 2
        data.createSimpleRoom(4, 22, 6, 6);   // House 3
        data.createSimpleRoom(22, 22, 6, 6);  // House 4
        
        // Shop building (larger)
        data.createSimpleRoom(14, 4, 4, 6);
        
        // Paths connecting buildings to square
        data.createCorridor(16, 16, 16, 10);  // To shop
        data.createCorridor(16, 16, 10, 10);  // To house 1
        data.createCorridor(16, 16, 22, 10);  // To house 2
        data.createCorridor(16, 16, 10, 22);  // To house 3
        data.createCorridor(16, 16, 22, 22);  // To house 4
        
        // Add some decorative elements
        for (int x = 15; x < 17; x++) {
            for (int y = 15; y < 17; y++) {
                data.setTile(x, y, Terrain.WALL_DECO);
            }
        }
        
        // Set properties
        data.setProperty("hasShop", true);
        data.setProperty("hasInn", true);
        data.setProperty("isSafe", true);
        data.setProperty("mobCount", 0); // No enemies in village
    }

    // Forest Layout - Natural, winding paths with vegetation
    public static void createForestLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Main winding path
        for (int x = 0; x < data.width; x++) {
            if (x % 4 == 0) {
                data.setTile(x, data.height/2, Terrain.EMPTY);
            }
        }
        
        // Cross path
        for (int y = 0; y < data.height; y++) {
            if (y % 4 == 0) {
                data.setTile(data.width/2, y, Terrain.EMPTY);
            }
        }
        
        // Add forest areas (high grass)
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                if (data.getTile(x, y) == Terrain.WALL) {
                    if (Math.random() < 0.3) {
                        data.setTile(x, y, Terrain.HIGH_GRASS);
                    }
                }
            }
        }
        
        // Add some clearings
        data.createSimpleRoom(8, 8, 6, 6);
        data.createSimpleRoom(18, 18, 6, 6);
        
        // Set properties
        data.setProperty("hasWildlife", true);
        data.setProperty("isOutdoor", true);
        data.setProperty("mobCount", 4);
    }

    // Cave Layout - Underground, tight corridors with cave features
    public static void createCaveLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Cave entrance (center)
        data.createSimpleRoom(data.width/2 - 3, data.height/2 - 2, 6, 4);
        
        // Cave passages (irregular)
        for (int x = 0; x < data.width; x++) {
            if (x % 3 == 0) {
                data.setTile(x, data.height/2, Terrain.EMPTY);
            }
        }
        
        // Add some cave rooms
        data.createSimpleRoom(6, 6, 4, 4);
        data.createSimpleRoom(22, 6, 4, 4);
        data.createSimpleRoom(6, 22, 4, 4);
        data.createSimpleRoom(22, 22, 4, 4);
        
        // Add cave features (wall decorations)
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                if (data.getTile(x, y) == Terrain.WALL && Math.random() < 0.1) {
                    data.setTile(x, y, Terrain.WALL_DECO);
                }
            }
        }
        
        // Set properties
        data.setProperty("isUnderground", true);
        data.setProperty("hasCrystals", false);
        data.setProperty("mobCount", 5);
    }

    // City Layout - Urban, structured with buildings and streets
    public static void createCityLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Main street (horizontal)
        for (int x = 0; x < data.width; x++) {
            data.setTile(x, data.height/2, Terrain.EMPTY);
        }
        
        // Cross street (vertical)
        for (int y = 0; y < data.height; y++) {
            data.setTile(data.width/2, y, Terrain.EMPTY);
        }
        
        // Buildings in grid pattern
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                int x = 2 + col * 7;
                int y = 2 + row * 7;
                data.createSimpleRoom(x, y, 5, 5);
            }
        }
        
        // Central plaza
        data.createSimpleRoom(data.width/2 - 2, data.height/2 - 2, 4, 4);
        
        // Set properties
        data.setProperty("hasShop", true);
        data.setProperty("isUrban", true);
        data.setProperty("mobCount", 3);
    }

    // Ruins Layout - Ancient, partially destroyed structures
    public static void createRuinsLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Main ruins structure
        data.createSimpleRoom(8, 8, 16, 16);
        
        // Add some internal walls (ruins)
        for (int x = 12; x < 20; x++) {
            data.setTile(x, 12, Terrain.WALL);
            data.setTile(x, 20, Terrain.WALL);
        }
        for (int y = 12; y < 20; y++) {
            data.setTile(12, y, Terrain.WALL);
            data.setTile(20, y, Terrain.WALL);
        }
        
        // Add some destroyed areas (empty spaces in walls)
        data.setTile(16, 12, Terrain.EMPTY);
        data.setTile(16, 20, Terrain.EMPTY);
        data.setTile(12, 16, Terrain.EMPTY);
        data.setTile(20, 16, Terrain.EMPTY);
        
        // Add decorative elements
        for (int x = 10; x < 22; x += 4) {
            for (int y = 10; y < 22; y += 4) {
                if (Math.random() < 0.5) {
                    data.setTile(x, y, Terrain.WALL_DECO);
                }
            }
        }
        
        // Set properties
        data.setProperty("isAncient", true);
        data.setProperty("hasTraps", true);
        data.setProperty("mobCount", 6);
    }

    // Boss Arena Layout - Large open area for boss fights
    public static void createBossArenaLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Large central arena
        data.createSimpleRoom(6, 6, 20, 20);
        
        // Arena entrance
        data.createCorridor(data.width/2, 0, data.width/2, 6);
        
        // Add some pillars for cover
        data.setTile(10, 10, Terrain.WALL);
        data.setTile(22, 10, Terrain.WALL);
        data.setTile(10, 22, Terrain.WALL);
        data.setTile(22, 22, Terrain.WALL);
        
        // Add decorative elements around arena
        for (int x = 4; x < 28; x += 4) {
            data.setTile(x, 4, Terrain.WALL_DECO);
            data.setTile(x, 28, Terrain.WALL_DECO);
        }
        for (int y = 4; y < 28; y += 4) {
            data.setTile(4, y, Terrain.WALL_DECO);
            data.setTile(28, y, Terrain.WALL_DECO);
        }
        
        // Set properties
        data.setProperty("isBossArena", true);
        data.setProperty("hasBoss", true);
        data.setProperty("mobCount", 1); // Just the boss
    }

    // Shop Layout - Commercial area with vendor
    public static void createShopLayout(LevelData data) {
        data.width = 24;
        data.height = 24;
        data.initializeDefaultLayout();
        
        // Main shop area
        data.createSimpleRoom(4, 4, 16, 16);
        
        // Shop counter
        for (int x = 6; x < 18; x++) {
            data.setTile(x, 12, Terrain.WALL);
        }
        
        // Customer area (front)
        data.createSimpleRoom(6, 6, 12, 6);
        
        // Storage area (back)
        data.createSimpleRoom(6, 16, 12, 4);
        
        // Entrance
        data.createCorridor(12, 0, 12, 4);
        
        // Set properties
        data.setProperty("hasShopkeeper", true);
        data.setProperty("isSafe", true);
        data.setProperty("mobCount", 0);
    }

    // Quest Layout - Special area for quest objectives
    public static void createQuestLayout(LevelData data) {
        data.width = 28;
        data.height = 28;
        data.initializeDefaultLayout();
        
        // Central quest area
        data.createSimpleRoom(8, 8, 12, 12);
        
        // Quest objective in center
        data.setTile(14, 14, Terrain.WALL_DECO);
        
        // Paths to objective
        data.createCorridor(14, 8, 14, 14);
        data.createCorridor(8, 14, 14, 14);
        
        // Guard posts
        data.createSimpleRoom(4, 4, 4, 4);
        data.createSimpleRoom(20, 4, 4, 4);
        data.createSimpleRoom(4, 20, 4, 4);
        data.createSimpleRoom(20, 20, 4, 4);
        
        // Set properties
        data.setProperty("hasQuest", true);
        data.setProperty("isSpecial", true);
        data.setProperty("mobCount", 2);
    }

    /**
     * Creates a layout based on the location type.
     * @param data The LevelData to populate
     * @param levelType The type of location
     */
    public static void createLayoutForType(LevelData data, WorldLocation.LevelType levelType) {
        switch (levelType) {
            case VILLAGE:
                createVillageLayout(data);
                break;
            case FOREST:
                createForestLayout(data);
                break;
            case CAVES:
                createCaveLayout(data);
                break;
            case CITY:
                createCityLayout(data);
                break;
            case RUINS:
                createRuinsLayout(data);
                break;
            case BOSS:
                createBossArenaLayout(data);
                break;
            case SHOP:
                createShopLayout(data);
                break;
            case QUEST:
                createQuestLayout(data);
                break;
            case SPECIAL:
                // For special locations, use a random layout
                createRandomLayout(data);
                break;
            default:
                createForestLayout(data); // Default fallback
                break;
        }
    }

    /**
     * Creates a random layout for special locations.
     */
    private static void createRandomLayout(LevelData data) {
        data.width = 28;
        data.height = 28;
        data.initializeDefaultLayout();
        
        // Random room placement
        for (int i = 0; i < 5; i++) {
            int x = (int)(Math.random() * (data.width - 6)) + 2;
            int y = (int)(Math.random() * (data.height - 6)) + 2;
            int w = (int)(Math.random() * 4) + 3;
            int h = (int)(Math.random() * 4) + 3;
            data.createSimpleRoom(x, y, w, h);
        }
        
        // Connect rooms with corridors
        for (int i = 0; i < 3; i++) {
            int x1 = (int)(Math.random() * data.width);
            int y1 = (int)(Math.random() * data.height);
            int x2 = (int)(Math.random() * data.width);
            int y2 = (int)(Math.random() * data.height);
            data.createCorridor(x1, y1, x2, y2);
        }
        
        data.setProperty("isRandom", true);
        data.setProperty("mobCount", 4);
    }
} 