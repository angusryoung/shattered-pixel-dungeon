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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Test world data for demonstrating the world exploration system.
 * This creates a simple interconnected world with different location types.
 */
public class TestWorldData {

    public static void createTestWorld(WorldMap worldMap) {
        // Create locations
        Map<Integer, WorldLocation> locations = new HashMap<>();
        
        // Starting location - Village
        WorldLocation village = new WorldLocation();
        village.locationId = 1;
        village.locationName = "Village";
        village.locationDesc = "A peaceful village where your journey begins.";
        village.levelType = WorldLocation.LevelType.VILLAGE;
        village.difficulty = 1;
        locations.put(1, village);
        
        // Forest location
        WorldLocation forest = new WorldLocation();
        forest.locationId = 2;
        forest.locationName = "Dark Forest";
        forest.locationDesc = "A dense forest filled with dangerous creatures.";
        forest.levelType = WorldLocation.LevelType.FOREST;
        forest.difficulty = 3;
        locations.put(2, forest);
        
        // Cave location
        WorldLocation cave = new WorldLocation();
        cave.locationId = 3;
        cave.locationName = "Ancient Cave";
        cave.locationDesc = "A mysterious cave with ancient secrets.";
        cave.levelType = WorldLocation.LevelType.CAVES;
        cave.difficulty = 5;
        locations.put(3, cave);
        
        // Mountain location
        WorldLocation mountain = new WorldLocation();
        mountain.locationId = 4;
        mountain.locationName = "Mountain Peak";
        mountain.locationDesc = "A treacherous mountain path leading to the summit.";
        mountain.levelType = WorldLocation.LevelType.FOREST;
        mountain.difficulty = 7;
        locations.put(4, mountain);
        
        // Castle location
        WorldLocation castle = new WorldLocation();
        castle.locationId = 5;
        castle.locationName = "Abandoned Castle";
        castle.locationDesc = "A once-mighty castle now in ruins.";
        castle.levelType = WorldLocation.LevelType.RUINS;
        castle.difficulty = 10;
        locations.put(5, castle);
        
        // Create exits between locations
        // Village -> Forest
        ExitPoint villageToForest = new ExitPoint();
        villageToForest.exitName = "Forest Path";
        villageToForest.targetLocationId = 2;
        villageToForest.exitType = WorldLocation.ExitType.NORMAL;
        village.exits.add(villageToForest);
        
        // Forest -> Cave
        ExitPoint forestToCave = new ExitPoint();
        forestToCave.exitName = "Cave Entrance";
        forestToCave.targetLocationId = 3;
        forestToCave.exitType = WorldLocation.ExitType.NORMAL;
        forest.exits.add(forestToCave);
        
        // Forest -> Mountain
        ExitPoint forestToMountain = new ExitPoint();
        forestToMountain.exitName = "Mountain Trail";
        forestToMountain.targetLocationId = 4;
        forestToMountain.exitType = WorldLocation.ExitType.NORMAL;
        forest.exits.add(forestToMountain);
        
        // Cave -> Castle
        ExitPoint caveToCastle = new ExitPoint();
        caveToCastle.exitName = "Secret Passage";
        caveToCastle.targetLocationId = 5;
        caveToCastle.exitType = WorldLocation.ExitType.HIDDEN;
        cave.exits.add(caveToCastle);
        
        // Mountain -> Castle
        ExitPoint mountainToCastle = new ExitPoint();
        mountainToCastle.exitName = "Castle Gate";
        mountainToCastle.targetLocationId = 5;
        mountainToCastle.exitType = WorldLocation.ExitType.NORMAL;
        mountain.exits.add(mountainToCastle);
        
        // Castle -> Village (one-way return)
        ExitPoint castleToVillage = new ExitPoint();
        castleToVillage.exitName = "Teleport Crystal";
        castleToVillage.targetLocationId = 1;
        castleToVillage.exitType = WorldLocation.ExitType.ONE_WAY;
        castle.exits.add(castleToVillage);
        
        // Create level data for each location using the new layout system
        for (WorldLocation location : locations.values()) {
            LevelData levelData = new LevelData(location.locationId, 32, 32);
            levelData.createLayoutForType(location.levelType);
            WorldManager.cacheLevelData(location.locationId, levelData);
        }
        
        // Add all locations to the world map
        for (WorldLocation location : locations.values()) {
            worldMap.addLocation(location);
        }
        
        // Set starting location
        worldMap.setCurrentLocation(1);
    }
} 