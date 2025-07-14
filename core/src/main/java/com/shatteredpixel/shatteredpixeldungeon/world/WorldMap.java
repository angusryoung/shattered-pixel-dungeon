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

import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WorldMap implements Bundlable {

    private HashMap<Integer, WorldLocation> locations;
    private int currentLocationId;
    private HashSet<Integer> discoveredLocations;
    private HashSet<Integer> exploredLocations;
    private ArrayList<Integer> locationHistory; // Track player's path through the world

    public WorldMap() {
        locations = new HashMap<>();
        discoveredLocations = new HashSet<>();
        exploredLocations = new HashSet<>();
        locationHistory = new ArrayList<>();
        currentLocationId = 1; // Default starting location
    }

    public void addLocation(WorldLocation location) {
        locations.put(location.locationId, location);
        if (location.locationId == currentLocationId) {
            discoveredLocations.add(location.locationId);
        }
    }

    public WorldLocation getLocation(int locationId) {
        return locations.get(locationId);
    }

    public WorldLocation getCurrentLocation() {
        return locations.get(currentLocationId);
    }

    public int getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocation(int locationId) {
        if (locations.containsKey(locationId)) {
            currentLocationId = locationId;
            discoveredLocations.add(locationId);
            if (!locationHistory.contains(locationId)) {
                locationHistory.add(locationId);
            }
        }
    }

    public boolean moveToLocation(int targetLocationId) {
        WorldLocation current = getCurrentLocation();
        if (current == null) {
            return false;
        }

        // Check if there's a valid exit to the target location
        for (ExitPoint exit : current.exits) {
            if (exit.targetLocationId == targetLocationId && exit.canUse()) {
                setCurrentLocation(targetLocationId);
                return true;
            }
        }
        return false;
    }

    public boolean moveToLocation(int targetLocationId, int sourceCell) {
        WorldLocation current = getCurrentLocation();
        if (current == null) {
            return false;
        }

        // Check if there's a valid exit from the specific cell
        for (ExitPoint exit : current.exits) {
            if (exit.targetLocationId == targetLocationId && 
                exit.sourceCell == sourceCell && 
                exit.canUse()) {
                setCurrentLocation(targetLocationId);
                return true;
            }
        }
        return false;
    }

    public ArrayList<ExitPoint> getAccessibleExits() {
        WorldLocation current = getCurrentLocation();
        if (current == null) {
            return new ArrayList<>();
        }
        return current.getAccessibleExits();
    }

    public ArrayList<ExitPoint> getExitsFromCell(int cell) {
        ArrayList<ExitPoint> cellExits = new ArrayList<>();
        WorldLocation current = getCurrentLocation();
        if (current == null) {
            return cellExits;
        }

        for (ExitPoint exit : current.exits) {
            if (exit.sourceCell == cell && exit.canUse()) {
                cellExits.add(exit);
            }
        }
        return cellExits;
    }

    public ExitPoint getExitToLocation(int targetLocationId) {
        WorldLocation current = getCurrentLocation();
        if (current == null) {
            return null;
        }

        for (ExitPoint exit : current.exits) {
            if (exit.targetLocationId == targetLocationId && exit.canUse()) {
                return exit;
            }
        }
        return null;
    }

    public void markLocationExplored(int locationId) {
        exploredLocations.add(locationId);
        WorldLocation location = locations.get(locationId);
        if (location != null) {
            location.isExplored = true;
        }
    }

    public void markLocationDiscovered(int locationId) {
        discoveredLocations.add(locationId);
        WorldLocation location = locations.get(locationId);
        if (location != null) {
            location.isDiscovered = true;
        }
    }

    public boolean isLocationExplored(int locationId) {
        return exploredLocations.contains(locationId);
    }

    public boolean isLocationDiscovered(int locationId) {
        return discoveredLocations.contains(locationId);
    }

    public ArrayList<WorldLocation> getDiscoveredLocations() {
        ArrayList<WorldLocation> discovered = new ArrayList<>();
        for (Integer id : discoveredLocations) {
            WorldLocation location = locations.get(id);
            if (location != null) {
                discovered.add(location);
            }
        }
        return discovered;
    }

    public ArrayList<WorldLocation> getExploredLocations() {
        ArrayList<WorldLocation> explored = new ArrayList<>();
        for (Integer id : exploredLocations) {
            WorldLocation location = locations.get(id);
            if (location != null) {
                explored.add(location);
            }
        }
        return explored;
    }

    public ArrayList<Integer> getLocationHistory() {
        return new ArrayList<>(locationHistory);
    }

    public int getLocationCount() {
        return locations.size();
    }

    public int getDiscoveredLocationCount() {
        return discoveredLocations.size();
    }

    public int getExploredLocationCount() {
        return exploredLocations.size();
    }

    public float getExplorationPercentage() {
        if (locations.isEmpty()) {
            return 0f;
        }
        return (float) exploredLocations.size() / locations.size();
    }

    // Create a default world structure
    public void createDefaultWorld() {
        // Starting Village
        WorldLocation village = new WorldLocation(1, "Starting Village", WorldLocation.LevelType.VILLAGE);
        village.locationDesc = "A peaceful village where your journey begins.";
        village.difficulty = 1;
        village.addExit(2, 0, 0); // To Forest Path
        village.addExit(3, 0, 0); // To Cave Entrance
        addLocation(village);

        // Forest Path
        WorldLocation forest = new WorldLocation(2, "Forest Path", WorldLocation.LevelType.FOREST);
        forest.locationDesc = "A winding path through dense forest.";
        forest.difficulty = 2;
        forest.addExit(1, 0, 0); // Back to Village
        forest.addExit(4, 0, 0); // To Dark Forest
        forest.addExit(5, 0, 0); // To Mountain Pass
        addLocation(forest);

        // Cave Entrance
        WorldLocation cave = new WorldLocation(3, "Cave Entrance", WorldLocation.LevelType.CAVES);
        cave.locationDesc = "The entrance to a mysterious cave system.";
        cave.difficulty = 2;
        cave.addExit(1, 0, 0); // Back to Village
        cave.addExit(6, 0, 0); // To Deep Caves
        addLocation(cave);

        // Dark Forest
        WorldLocation darkForest = new WorldLocation(4, "Dark Forest", WorldLocation.LevelType.FOREST);
        darkForest.locationDesc = "A foreboding forest shrouded in darkness.";
        darkForest.difficulty = 3;
        darkForest.addExit(2, 0, 0); // Back to Forest Path
        darkForest.addExit(7, 0, 0); // To Ancient Ruins
        addLocation(darkForest);

        // Mountain Pass
        WorldLocation mountain = new WorldLocation(5, "Mountain Pass", WorldLocation.LevelType.FOREST);
        mountain.locationDesc = "A treacherous path through the mountains.";
        mountain.difficulty = 3;
        mountain.addExit(2, 0, 0); // Back to Forest Path
        mountain.addExit(8, 0, 0); // To Mountain Peak
        addLocation(mountain);

        // Deep Caves
        WorldLocation deepCaves = new WorldLocation(6, "Deep Caves", WorldLocation.LevelType.CAVES);
        deepCaves.locationDesc = "The depths of the cave system.";
        deepCaves.difficulty = 4;
        deepCaves.addExit(3, 0, 0); // Back to Cave Entrance
        deepCaves.addExit(9, 0, 0); // To Crystal Caverns
        addLocation(deepCaves);

        // Ancient Ruins
        WorldLocation ruins = new WorldLocation(7, "Ancient Ruins", WorldLocation.LevelType.RUINS);
        ruins.locationDesc = "The remains of an ancient civilization.";
        ruins.difficulty = 5;
        ruins.addExit(4, 0, 0); // Back to Dark Forest
        ruins.addExit(10, 0, 0); // To Boss Arena
        addLocation(ruins);

        // Mountain Peak
        WorldLocation peak = new WorldLocation(8, "Mountain Peak", WorldLocation.LevelType.SPECIAL);
        peak.locationDesc = "The summit of the highest mountain.";
        peak.difficulty = 4;
        peak.addExit(5, 0, 0); // Back to Mountain Pass
        addLocation(peak);

        // Crystal Caverns
        WorldLocation crystal = new WorldLocation(9, "Crystal Caverns", WorldLocation.LevelType.CAVES);
        crystal.locationDesc = "Caverns filled with glowing crystals.";
        crystal.difficulty = 5;
        crystal.addExit(6, 0, 0); // Back to Deep Caves
        addLocation(crystal);

        // Boss Arena
        WorldLocation boss = new WorldLocation(10, "Boss Arena", WorldLocation.LevelType.BOSS);
        boss.locationDesc = "An arena where a powerful boss awaits.";
        boss.difficulty = 6;
        boss.addExit(7, 0, 0); // Back to Ancient Ruins
        addLocation(boss);
    }

    // Bundling for save/load
    private static final String LOCATIONS = "locations";
    private static final String CURRENT_LOCATION_ID = "current_location_id";
    private static final String DISCOVERED_LOCATIONS = "discovered_locations";
    private static final String EXPLORED_LOCATIONS = "explored_locations";
    private static final String LOCATION_HISTORY = "location_history";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(LOCATIONS, (Collection<Bundlable>) (Collection<?>) locations.values());
        bundle.put(CURRENT_LOCATION_ID, currentLocationId);
        
        // Convert sets to arrays for bundling
        int[] discoveredArray = new int[discoveredLocations.size()];
        int i = 0;
        for (Integer id : discoveredLocations) {
            discoveredArray[i++] = id;
        }
        bundle.put(DISCOVERED_LOCATIONS, discoveredArray);
        
        int[] exploredArray = new int[exploredLocations.size()];
        i = 0;
        for (Integer id : exploredLocations) {
            exploredArray[i++] = id;
        }
        bundle.put(EXPLORED_LOCATIONS, exploredArray);
        
        int[] historyArray = new int[locationHistory.size()];
        i = 0;
        for (Integer id : locationHistory) {
            historyArray[i++] = id;
        }
        bundle.put(LOCATION_HISTORY, historyArray);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        locations.clear();
        for (Bundlable b : bundle.getCollection(LOCATIONS)) {
            WorldLocation location = (WorldLocation) b;
            locations.put(location.locationId, location);
        }
        
        currentLocationId = bundle.getInt(CURRENT_LOCATION_ID);
        
        discoveredLocations.clear();
        for (Integer id : bundle.getIntArray(DISCOVERED_LOCATIONS)) {
            discoveredLocations.add(id);
        }
        
        exploredLocations.clear();
        for (Integer id : bundle.getIntArray(EXPLORED_LOCATIONS)) {
            exploredLocations.add(id);
        }
        
        locationHistory.clear();
        for (Integer id : bundle.getIntArray(LOCATION_HISTORY)) {
            locationHistory.add(id);
        }
    }
} 