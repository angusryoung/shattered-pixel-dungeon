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
import com.watabou.utils.Bundlable;
import com.watabou.utils.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LevelData implements Bundlable {

    public int locationId;
    public int width;
    public int height;
    public int[][] layout; // Pre-set tile layout
    public ArrayList<ExitPoint> exits;
    public WorldLocation.LevelType levelType;
    public String tilesTexture;
    public String waterTexture;
    public int color1;
    public int color2;
    public int viewDistance;
    public HashMap<String, Object> properties;

    public LevelData() {
        exits = new ArrayList<>();
        properties = new HashMap<>();
        color1 = 0x004400;
        color2 = 0x88CC44;
        viewDistance = 8;
    }

    public LevelData(int locationId, int width, int height) {
        this();
        this.locationId = locationId;
        this.width = width;
        this.height = height;
        this.layout = new int[height][width];
        initializeDefaultLayout();
    }

    public void initializeDefaultLayout() {
        // Fill with walls by default
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                layout[y][x] = Terrain.WALL;
            }
        }
    }

    public void setTile(int x, int y, int terrain) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            layout[y][x] = terrain;
        }
    }

    public int getTile(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return layout[y][x];
        }
        return Terrain.WALL;
    }

    public void addExit(int targetLocationId, int sourceX, int sourceY, int targetX, int targetY) {
        int sourceCell = sourceY * width + sourceX;
        int targetCell = targetY * width + targetX;
        ExitPoint exit = new ExitPoint(targetLocationId, sourceCell, targetCell);
        exits.add(exit);
    }

    public void addExit(int targetLocationId, int sourceX, int sourceY, int targetX, int targetY, WorldLocation.ExitType exitType) {
        int sourceCell = sourceY * width + sourceX;
        int targetCell = targetY * width + targetX;
        ExitPoint exit = new ExitPoint(targetLocationId, sourceCell, targetCell, exitType);
        exits.add(exit);
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

    // Create a simple room layout
    public void createSimpleRoom(int roomX, int roomY, int roomWidth, int roomHeight) {
        // Create floor
        for (int y = roomY; y < roomY + roomHeight; y++) {
            for (int x = roomX; x < roomX + roomWidth; x++) {
                setTile(x, y, Terrain.EMPTY);
            }
        }
        
        // Create walls around the room
        for (int x = roomX - 1; x <= roomX + roomWidth; x++) {
            setTile(x, roomY - 1, Terrain.WALL);
            setTile(x, roomY + roomHeight, Terrain.WALL);
        }
        for (int y = roomY - 1; y <= roomY + roomHeight; y++) {
            setTile(roomX - 1, y, Terrain.WALL);
            setTile(roomX + roomWidth, y, Terrain.WALL);
        }
    }

    // Create a corridor between two points
    public void createCorridor(int startX, int startY, int endX, int endY) {
        // Simple L-shaped corridor
        int midX = startX;
        int midY = endY;
        
        // Horizontal part
        int stepX = startX < endX ? 1 : -1;
        for (int x = startX; x != endX + stepX; x += stepX) {
            setTile(x, startY, Terrain.EMPTY);
        }
        
        // Vertical part
        int stepY = startY < endY ? 1 : -1;
        for (int y = startY; y != endY + stepY; y += stepY) {
            setTile(midX, y, Terrain.EMPTY);
        }
    }

    // Create a layout based on the location type
    public void createLayoutForType(WorldLocation.LevelType levelType) {
        LevelLayouts.createLayoutForType(this, levelType);
    }

    // Create a default forest layout
    public void createForestLayout() {
        // Main path
        for (int x = 0; x < width; x++) {
            setTile(x, height/2, Terrain.EMPTY);
        }
        
        // Forest areas
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (layout[y][x] == Terrain.WALL) {
                    if (Math.random() < 0.3) {
                        setTile(x, y, Terrain.HIGH_GRASS);
                    }
                }
            }
        }
        
        // Add exits
        addExit(1, width/2, 0, width/2, 0); // Back to Village
        addExit(4, width/2, height-1, width/2, height-1); // To Dark Forest
        addExit(5, width-1, height/2, width-1, height/2); // To Mountain Pass
        
        // Set properties
        setProperty("hasWildlife", true);
        setProperty("isOutdoor", true);
    }

    // Create a default cave layout
    public void createCaveLayout() {
        // Cave entrance
        createSimpleRoom(width/2 - 3, height/2 - 2, 6, 4);
        
        // Cave passages
        for (int x = 0; x < width; x++) {
            if (x % 3 == 0) {
                setTile(x, height/2, Terrain.EMPTY);
            }
        }
        
        // Add some cave features
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (layout[y][x] == Terrain.WALL && Math.random() < 0.1) {
                    setTile(x, y, Terrain.WALL_DECO);
                }
            }
        }
        
        // Add exits
        addExit(1, width/2, 0, width/2, 0); // Back to Village
        addExit(6, width/2, height-1, width/2, height-1); // To Deep Caves
        
        // Set properties
        setProperty("isUnderground", true);
        setProperty("hasCrystals", false);
    }

    // Bundling for save/load
    private static final String LOCATION_ID = "location_id";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String LAYOUT = "layout";
    private static final String EXITS = "exits";
    private static final String LEVEL_TYPE = "level_type";
    private static final String TILES_TEXTURE = "tiles_texture";
    private static final String WATER_TEXTURE = "water_texture";
    private static final String COLOR1 = "color1";
    private static final String COLOR2 = "color2";
    private static final String VIEW_DISTANCE = "view_distance";
    private static final String PROPERTIES = "properties";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(LOCATION_ID, locationId);
        bundle.put(WIDTH, width);
        bundle.put(HEIGHT, height);
        
        // Store layout as 1D array
        int[] layoutArray = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                layoutArray[y * width + x] = layout[y][x];
            }
        }
        bundle.put(LAYOUT, layoutArray);
        
        bundle.put(EXITS, (Collection<Bundlable>) (Collection<?>) exits);
        bundle.put(LEVEL_TYPE, levelType);
        bundle.put(TILES_TEXTURE, tilesTexture);
        bundle.put(WATER_TEXTURE, waterTexture);
        bundle.put(COLOR1, color1);
        bundle.put(COLOR2, color2);
        bundle.put(VIEW_DISTANCE, viewDistance);
        
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
        width = bundle.getInt(WIDTH);
        height = bundle.getInt(HEIGHT);
        
        // Restore layout from 1D array
        int[] layoutArray = bundle.getIntArray(LAYOUT);
        layout = new int[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                layout[y][x] = layoutArray[y * width + x];
            }
        }
        
        exits.clear();
        for (Bundlable b : bundle.getCollection(EXITS)) {
            exits.add((ExitPoint) b);
        }
        
        levelType = bundle.getEnum(LEVEL_TYPE, WorldLocation.LevelType.class);
        tilesTexture = bundle.getString(TILES_TEXTURE);
        waterTexture = bundle.getString(WATER_TEXTURE);
        color1 = bundle.getInt(COLOR1);
        color2 = bundle.getInt(COLOR2);
        viewDistance = bundle.getInt(VIEW_DISTANCE);
        
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