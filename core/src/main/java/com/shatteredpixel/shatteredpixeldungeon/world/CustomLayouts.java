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
 * Custom layouts for specific named locations.
 * These layouts are designed for particular places in the world,
 * rather than generic location types.
 */
public class CustomLayouts {

    /**
     * Creates a layout for the "Village Square" location.
     * This is a specific village with a unique layout.
     */
    public static void createVillageSquareLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Central fountain
        data.createSimpleRoom(14, 14, 4, 4);
        data.setTile(15, 15, Terrain.WALL_DECO);
        data.setTile(16, 15, Terrain.WALL_DECO);
        data.setTile(15, 16, Terrain.WALL_DECO);
        data.setTile(16, 16, Terrain.WALL_DECO);
        
        // Town hall (larger building)
        data.createSimpleRoom(6, 6, 8, 6);
        
        // Market stalls (small buildings)
        data.createSimpleRoom(20, 6, 4, 4);
        data.createSimpleRoom(20, 12, 4, 4);
        data.createSimpleRoom(20, 18, 4, 4);
        
        // Residential area (houses)
        data.createSimpleRoom(6, 18, 4, 4);
        data.createSimpleRoom(12, 18, 4, 4);
        data.createSimpleRoom(18, 18, 4, 4);
        
        // Paths connecting everything
        data.createCorridor(16, 16, 16, 12); // To town hall
        data.createCorridor(16, 16, 22, 8);  // To market
        data.createCorridor(16, 16, 22, 14); // To market
        data.createCorridor(16, 16, 22, 20); // To market
        data.createCorridor(16, 16, 8, 20);  // To houses
        data.createCorridor(16, 16, 14, 20); // To houses
        data.createCorridor(16, 16, 20, 20); // To houses
        
        // Add decorative elements
        for (int x = 14; x < 18; x++) {
            data.setTile(x, 12, Terrain.WALL_DECO);
            data.setTile(x, 20, Terrain.WALL_DECO);
        }
        
        // Set properties
        data.setProperty("hasFountain", true);
        data.setProperty("hasTownHall", true);
        data.setProperty("hasMarket", true);
        data.setProperty("isSafe", true);
        data.setProperty("mobCount", 0);
    }

    /**
     * Creates a layout for the "Dark Forest Clearing" location.
     * A specific forest area with a mysterious clearing.
     */
    public static void createDarkForestClearingLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Central clearing (circular)
        for (int y = 12; y < 20; y++) {
            for (int x = 12; x < 20; x++) {
                if ((x - 16) * (x - 16) + (y - 16) * (y - 16) <= 16) {
                    data.setTile(x, y, Terrain.EMPTY);
                }
            }
        }
        
        // Ancient tree in center
        data.setTile(16, 16, Terrain.WALL_DECO);
        
        // Forest paths (spiral)
        for (int i = 0; i < 8; i++) {
            int x = 16 + (int)(4 * Math.cos(i * Math.PI / 4));
            int y = 16 + (int)(4 * Math.sin(i * Math.PI / 4));
            data.createCorridor(16, 16, x, y);
        }
        
        // Add forest vegetation
        for (int y = 0; y < data.height; y++) {
            for (int x = 0; x < data.width; x++) {
                if (data.getTile(x, y) == Terrain.WALL && Math.random() < 0.4) {
                    data.setTile(x, y, Terrain.HIGH_GRASS);
                }
            }
        }
        
        // Add some mysterious structures
        data.createSimpleRoom(4, 4, 3, 3);
        data.createSimpleRoom(25, 4, 3, 3);
        data.createSimpleRoom(4, 25, 3, 3);
        data.createSimpleRoom(25, 25, 3, 3);
        
        // Set properties
        data.setProperty("hasAncientTree", true);
        data.setProperty("isMysterious", true);
        data.setProperty("hasWildlife", true);
        data.setProperty("mobCount", 3);
    }

    /**
     * Creates a layout for the "Crystal Cavern" location.
     * A specific cave with crystal formations.
     */
    public static void createCrystalCavernLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Main cavern
        data.createSimpleRoom(8, 8, 16, 16);
        
        // Crystal formations (wall decorations)
        for (int y = 10; y < 22; y++) {
            for (int x = 10; x < 22; x++) {
                if (data.getTile(x, y) == Terrain.WALL && Math.random() < 0.2) {
                    data.setTile(x, y, Terrain.WALL_DECO);
                }
            }
        }
        
        // Central crystal cluster
        data.setTile(16, 16, Terrain.WALL_DECO);
        data.setTile(15, 16, Terrain.WALL_DECO);
        data.setTile(17, 16, Terrain.WALL_DECO);
        data.setTile(16, 15, Terrain.WALL_DECO);
        data.setTile(16, 17, Terrain.WALL_DECO);
        
        // Cave passages
        data.createCorridor(16, 8, 16, 16);  // North passage
        data.createCorridor(24, 16, 16, 16); // East passage
        data.createCorridor(16, 24, 16, 16); // South passage
        data.createCorridor(8, 16, 16, 16);  // West passage
        
        // Small cave rooms
        data.createSimpleRoom(4, 4, 4, 4);
        data.createSimpleRoom(24, 4, 4, 4);
        data.createSimpleRoom(4, 24, 4, 4);
        data.createSimpleRoom(24, 24, 4, 4);
        
        // Set properties
        data.setProperty("hasCrystals", true);
        data.setProperty("isUnderground", true);
        data.setProperty("isMagical", true);
        data.setProperty("mobCount", 4);
    }

    /**
     * Creates a layout for the "Abandoned Castle" location.
     * A specific castle with unique architecture.
     */
    public static void createAbandonedCastleLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Outer walls
        data.createSimpleRoom(4, 4, 24, 24);
        
        // Inner courtyard
        data.createSimpleRoom(12, 12, 8, 8);
        
        // Keep (central tower)
        data.createSimpleRoom(14, 14, 4, 4);
        
        // Guard towers (corners)
        data.createSimpleRoom(6, 6, 4, 4);
        data.createSimpleRoom(22, 6, 4, 4);
        data.createSimpleRoom(6, 22, 4, 4);
        data.createSimpleRoom(22, 22, 4, 4);
        
        // Barracks
        data.createSimpleRoom(8, 8, 8, 4);
        
        // Great hall
        data.createSimpleRoom(16, 8, 8, 4);
        
        // Add some damage (holes in walls)
        data.setTile(16, 8, Terrain.EMPTY);
        data.setTile(16, 28, Terrain.EMPTY);
        data.setTile(8, 16, Terrain.EMPTY);
        data.setTile(28, 16, Terrain.EMPTY);
        
        // Add decorative elements
        for (int x = 12; x < 20; x += 2) {
            data.setTile(x, 12, Terrain.WALL_DECO);
            data.setTile(x, 20, Terrain.WALL_DECO);
        }
        
        // Set properties
        data.setProperty("hasKeep", true);
        data.setProperty("hasBarracks", true);
        data.setProperty("hasGreatHall", true);
        data.setProperty("isAbandoned", true);
        data.setProperty("mobCount", 6);
    }

    /**
     * Creates a layout for the "Mystic Shop" location.
     * A specific shop with magical properties.
     */
    public static void createMysticShopLayout(LevelData data) {
        data.width = 24;
        data.height = 24;
        data.initializeDefaultLayout();
        
        // Main shop area
        data.createSimpleRoom(4, 4, 16, 16);
        
        // Shop counter (magical barrier)
        for (int x = 6; x < 18; x++) {
            data.setTile(x, 12, Terrain.WALL_DECO);
        }
        
        // Customer area (front)
        data.createSimpleRoom(6, 6, 12, 6);
        
        // Storage area (back) with magical items
        data.createSimpleRoom(6, 16, 12, 4);
        
        // Magical circle in storage
        data.setTile(12, 18, Terrain.WALL_DECO);
        data.setTile(11, 18, Terrain.WALL_DECO);
        data.setTile(13, 18, Terrain.WALL_DECO);
        data.setTile(12, 17, Terrain.WALL_DECO);
        data.setTile(12, 19, Terrain.WALL_DECO);
        
        // Entrance with magical door
        data.createCorridor(12, 0, 12, 4);
        data.setTile(12, 4, Terrain.WALL_DECO);
        
        // Add magical decorations
        for (int x = 8; x < 16; x += 2) {
            data.setTile(x, 8, Terrain.WALL_DECO);
            data.setTile(x, 16, Terrain.WALL_DECO);
        }
        
        // Set properties
        data.setProperty("hasShopkeeper", true);
        data.setProperty("isMagical", true);
        data.setProperty("isSafe", true);
        data.setProperty("hasMagicalItems", true);
        data.setProperty("mobCount", 0);
    }

    /**
     * Creates a layout for the "Boss Arena" location.
     * A specific arena designed for boss battles.
     */
    public static void createBossArenaLayout(LevelData data) {
        data.width = 32;
        data.height = 32;
        data.initializeDefaultLayout();
        
        // Large central arena
        data.createSimpleRoom(8, 8, 16, 16);
        
        // Arena entrance
        data.createCorridor(16, 0, 16, 8);
        
        // Arena exit
        data.createCorridor(16, 24, 16, 32);
        
        // Pillars for cover
        data.setTile(12, 12, Terrain.WALL);
        data.setTile(20, 12, Terrain.WALL);
        data.setTile(12, 20, Terrain.WALL);
        data.setTile(20, 20, Terrain.WALL);
        
        // Boss platform (center)
        data.setTile(16, 16, Terrain.WALL_DECO);
        
        // Spectator areas
        data.createSimpleRoom(4, 4, 4, 4);
        data.createSimpleRoom(24, 4, 4, 4);
        data.createSimpleRoom(4, 24, 4, 4);
        data.createSimpleRoom(24, 24, 4, 4);
        
        // Add arena decorations
        for (int x = 8; x < 24; x += 4) {
            data.setTile(x, 8, Terrain.WALL_DECO);
            data.setTile(x, 24, Terrain.WALL_DECO);
        }
        for (int y = 8; y < 24; y += 4) {
            data.setTile(8, y, Terrain.WALL_DECO);
            data.setTile(24, y, Terrain.WALL_DECO);
        }
        
        // Set properties
        data.setProperty("isBossArena", true);
        data.setProperty("hasBoss", true);
        data.setProperty("hasSpectators", true);
        data.setProperty("mobCount", 1); // Just the boss
    }

    /**
     * Creates a layout for a specific location by name.
     * @param data The LevelData to populate
     * @param locationName The specific name of the location
     */
    public static void createLayoutForLocation(LevelData data, String locationName) {
        switch (locationName.toLowerCase()) {
            case "village square":
                createVillageSquareLayout(data);
                break;
            case "dark forest clearing":
                createDarkForestClearingLayout(data);
                break;
            case "crystal cavern":
                createCrystalCavernLayout(data);
                break;
            case "abandoned castle":
                createAbandonedCastleLayout(data);
                break;
            case "mystic shop":
                createMysticShopLayout(data);
                break;
            case "boss arena":
                createBossArenaLayout(data);
                break;
            default:
                // Fall back to generic layout based on type
                if (data.levelType != null) {
                    LevelLayouts.createLayoutForType(data, data.levelType);
                }
                break;
        }
    }
} 