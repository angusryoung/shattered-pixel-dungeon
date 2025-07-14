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

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class WorldLocation implements Bundlable {

    public enum LevelType {
        VILLAGE,        // Starting area (like SewerLevel)
        FOREST,         // Outdoor areas (like PrisonLevel)
        CAVES,          // Underground areas (like CavesLevel)
        CITY,           // Urban areas (like CityLevel)
        RUINS,          // Ancient areas (like HallsLevel)
        BOSS,           // Boss areas
        SPECIAL,        // Unique areas
        SHOP,           // Commercial areas
        QUEST           // Quest-specific areas
    }

    public enum ExitType {
        NORMAL,         // Always accessible
        LOCKED,         // Requires key or item
        QUEST_GATED,    // Requires quest completion
        CONDITIONAL,    // Requires specific conditions
        ONE_WAY,        // Can't return the same way
        HIDDEN          // Not visible until discovered
    }

    public int locationId;
    public String locationName;
    public String locationDesc;
    public LevelType levelType;
    public ArrayList<ExitPoint> exits;
    public boolean isExplored;
    public boolean isDiscovered;
    public int difficulty; // 1-10 scale for enemy scaling
    public HashMap<String, Object> properties; // For quest flags, special conditions, etc.

    public WorldLocation() {
        exits = new ArrayList<>();
        properties = new HashMap<>();
        isExplored = false;
        isDiscovered = false;
        difficulty = 1;
    }

    public WorldLocation(int id, String name, LevelType type) {
        this();
        this.locationId = id;
        this.locationName = name;
        this.levelType = type;
    }

    public void addExit(int targetLocationId, int sourceCell, int targetCell, ExitType exitType) {
        ExitPoint exit = new ExitPoint();
        exit.targetLocationId = targetLocationId;
        exit.sourceCell = sourceCell;
        exit.targetCell = targetCell;
        exit.exitType = exitType;
        exits.add(exit);
    }

    public void addExit(int targetLocationId, int sourceCell, int targetCell) {
        addExit(targetLocationId, sourceCell, targetCell, ExitType.NORMAL);
    }

    public ArrayList<ExitPoint> getAccessibleExits() {
        ArrayList<ExitPoint> accessible = new ArrayList<>();
        for (ExitPoint exit : exits) {
            if (exit.exitType == ExitType.NORMAL || 
                (exit.exitType == ExitType.LOCKED && hasRequiredKey(exit)) ||
                (exit.exitType == ExitType.QUEST_GATED && isQuestCompleted(exit))) {
                accessible.add(exit);
            }
        }
        return accessible;
    }

    private boolean hasRequiredKey(ExitPoint exit) {
        // TODO: Implement key checking logic
        return false;
    }

    private boolean isQuestCompleted(ExitPoint exit) {
        // TODO: Implement quest completion checking
        return false;
    }

    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    public Object getProperty(String key) {
        return properties.get(key);
    }

    public boolean getBooleanProperty(String key) {
        Object value = properties.get(key);
        return value instanceof Boolean ? (Boolean) value : false;
    }

    public int getIntProperty(String key) {
        Object value = properties.get(key);
        return value instanceof Integer ? (Integer) value : 0;
    }

    public String getStringProperty(String key) {
        Object value = properties.get(key);
        return value instanceof String ? (String) value : "";
    }

    // Bundling for save/load
    private static final String LOCATION_ID = "location_id";
    private static final String LOCATION_NAME = "location_name";
    private static final String LOCATION_DESC = "location_desc";
    private static final String LEVEL_TYPE = "level_type";
    private static final String EXITS = "exits";
    private static final String IS_EXPLORED = "is_explored";
    private static final String IS_DISCOVERED = "is_discovered";
    private static final String DIFFICULTY = "difficulty";
    private static final String PROPERTIES = "properties";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(LOCATION_ID, locationId);
        bundle.put(LOCATION_NAME, locationName);
        bundle.put(LOCATION_DESC, locationDesc);
        bundle.put(LEVEL_TYPE, levelType);
        bundle.put(EXITS, (Collection<Bundlable>) (Collection<?>) exits);
        bundle.put(IS_EXPLORED, isExplored);
        bundle.put(IS_DISCOVERED, isDiscovered);
        bundle.put(DIFFICULTY, difficulty);
        
        // Store properties as a separate bundle
        Bundle propsBundle = new Bundle();
        for (String key : properties.keySet()) {
            Object value = properties.get(key);
            if (value instanceof String) {
                propsBundle.put(key, (String) value);
            } else if (value instanceof Integer) {
                propsBundle.put(key, (Integer) value);
            } else if (value instanceof Boolean) {
                propsBundle.put(key, (Boolean) value);
            } else if (value instanceof Float) {
                propsBundle.put(key, (Float) value);
            } else if (value instanceof Long) {
                propsBundle.put(key, (Long) value);
            } else {
                // Convert to string for other types
                propsBundle.put(key, value.toString());
            }
        }
        bundle.put(PROPERTIES, propsBundle);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        locationId = bundle.getInt(LOCATION_ID);
        locationName = bundle.getString(LOCATION_NAME);
        locationDesc = bundle.getString(LOCATION_DESC);
        levelType = bundle.getEnum(LEVEL_TYPE, LevelType.class);
        
        exits.clear();
        for (Bundlable b : bundle.getCollection(EXITS)) {
            exits.add((ExitPoint) b);
        }
        
        isExplored = bundle.getBoolean(IS_EXPLORED);
        isDiscovered = bundle.getBoolean(IS_DISCOVERED);
        difficulty = bundle.getInt(DIFFICULTY);
        
        // Restore properties
        properties.clear();
        Bundle propsBundle = bundle.getBundle(PROPERTIES);
        if (propsBundle != null) {
            for (String key : propsBundle.getKeys()) {
                properties.put(key, propsBundle.get(key));
            }
        }
    }
} 