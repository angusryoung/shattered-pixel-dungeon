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
import com.shatteredpixel.shatteredpixeldungeon.actors.hero.Hero;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.windows.WndOptions;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.util.ArrayList;

public class WorldLevel extends Level {

    private LevelData levelData;
    private WorldLocation worldLocation;

    public WorldLevel(LevelData data, WorldLocation location) {
        this.levelData = data;
        this.worldLocation = location;
    }

    // Default constructor for serialization
    public WorldLevel() {
        this.levelData = null;
        this.worldLocation = null;
    }

    @Override
    protected boolean build() {
        if (levelData != null) {
            // Use the pre-designed layout
            setSize(levelData.width, levelData.height);

            // Copy the pre-set layout to the level map
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    map[y * width + x] = levelData.getTile(x, y);
                }
            }

            // Add transitions for exits
            for (ExitPoint exit : levelData.exits) {
                LevelTransition transition = new LevelTransition(
                    this, 
                    exit.sourceCell, 
                    LevelTransition.Type.REGULAR_EXIT,
                    exit.targetLocationId, // Use location ID instead of depth
                    0, // No branch system in world exploration
                    LevelTransition.Type.REGULAR_ENTRANCE
                );
                transitions.add(transition);
            }

            return true;
        } else if (worldLocation != null) {
            // Create layout based on location type
            levelData = new LevelData(worldLocation.locationId, 32, 32);
            levelData.createLayoutForType(worldLocation.levelType);
            
            // Use the generated layout
            setSize(levelData.width, levelData.height);

            // Copy the layout to the level map
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    map[y * width + x] = levelData.getTile(x, y);
                }
            }

            // Add transitions for exits
            for (ExitPoint exit : levelData.exits) {
                LevelTransition transition = new LevelTransition(
                    this, 
                    exit.sourceCell, 
                    LevelTransition.Type.REGULAR_EXIT,
                    exit.targetLocationId,
                    0,
                    LevelTransition.Type.REGULAR_ENTRANCE
                );
                transitions.add(transition);
            }

            return true;
        } else {
            // Fallback to default level generation
            return false;
        }
    }

    @Override
    protected void createMobs() {
        // Create mobs based on location type and difficulty
        if (worldLocation != null) {
            int mobCount = calculateMobCount();
            for (int i = 0; i < mobCount; i++) {
                Mob mob = createMobForLocation();
                if (mob != null) {
                    mobs.add(mob);
                }
            }
        }
    }

    @Override
    protected void createItems() {
        // Create items based on location type and properties
        if (worldLocation != null) {
            createItemsForLocation();
        }
    }

    @Override
    public Mob createMob() {
        return createMobForLocation();
    }

    private Mob createMobForLocation() {
        if (worldLocation == null) {
            return null;
        }

        // Create mobs based on location type and difficulty
        switch (worldLocation.levelType) {
            case VILLAGE:
                return createVillageMob();
            case FOREST:
                return createForestMob();
            case CAVES:
                return createCaveMob();
            case CITY:
                return createCityMob();
            case RUINS:
                return createRuinsMob();
            case BOSS:
                return createBossMob();
            case SPECIAL:
                return createSpecialMob();
            default:
                return createForestMob(); // Default fallback
        }
    }

    private Mob createVillageMob() {
        // Village should be mostly safe, with few enemies
        if (Math.random() < 0.1) { // 10% chance of mob
            // TODO: Import and create appropriate village mobs
            return null;
        }
        return null;
    }

    private Mob createForestMob() {
        // TODO: Import and create forest mobs
        return null;
    }

    private Mob createCaveMob() {
        // TODO: Import and create cave mobs
        return null;
    }

    private Mob createCityMob() {
        // TODO: Import and create city mobs
        return null;
    }

    private Mob createRuinsMob() {
        // TODO: Import and create ruins mobs
        return null;
    }

    private Mob createBossMob() {
        // TODO: Import and create boss mobs
        return null;
    }

    private Mob createSpecialMob() {
        // TODO: Import and create special mobs
        return null;
    }

    private int calculateMobCount() {
        if (worldLocation == null) {
            return 0;
        }

        int baseCount = 3;
        int difficultyBonus = worldLocation.difficulty - 1;
        
        // Adjust based on location type
        switch (worldLocation.levelType) {
            case VILLAGE:
                return Math.max(0, baseCount - 2); // Fewer mobs in village
            case BOSS:
                return 1; // Single boss
            case SPECIAL:
                return baseCount + difficultyBonus;
            default:
                return baseCount + difficultyBonus;
        }
    }

    private void createItemsForLocation() {
        if (worldLocation == null) {
            return;
        }

        // Create items based on location properties
        if (worldLocation.getBooleanProperty("hasShop")) {
            // TODO: Add shop items
        }

        if (worldLocation.getBooleanProperty("hasWildlife")) {
            // TODO: Add wildlife-related items
        }

        if (worldLocation.getBooleanProperty("isUnderground")) {
            // TODO: Add cave-related items
        }
    }

    @Override
    public String tilesTex() {
        if (levelData != null && levelData.tilesTexture != null) {
            return levelData.tilesTexture;
        }
        
        // Default textures based on location type
        if (worldLocation != null) {
            switch (worldLocation.levelType) {
                case VILLAGE:
                    return Assets.Environment.TILES_CITY;
                case FOREST:
                    return Assets.Environment.TILES_CAVES; // Using caves tiles for forest for now
                case CAVES:
                    return Assets.Environment.TILES_CAVES;
                case CITY:
                    return Assets.Environment.TILES_CITY;
                case RUINS:
                    return Assets.Environment.TILES_HALLS;
                case BOSS:
                    return Assets.Environment.TILES_HALLS;
                default:
                    return Assets.Environment.TILES_CAVES;
            }
        }
        
        return Assets.Environment.TILES_CAVES;
    }

    @Override
    public String waterTex() {
        if (levelData != null && levelData.waterTexture != null) {
            return levelData.waterTexture;
        }
        
        // Default water textures
        if (worldLocation != null) {
            switch (worldLocation.levelType) {
                case VILLAGE:
                    return Assets.Environment.WATER_CITY;
                case FOREST:
                    return Assets.Environment.WATER_CAVES;
                case CAVES:
                    return Assets.Environment.WATER_CAVES;
                case CITY:
                    return Assets.Environment.WATER_CITY;
                case RUINS:
                    return Assets.Environment.WATER_HALLS;
                default:
                    return Assets.Environment.WATER_CAVES;
            }
        }
        
        return Assets.Environment.WATER_CAVES;
    }

    @Override
    public boolean activateTransition(Hero hero, LevelTransition transition) {
        // Handle world-specific transition logic
        if (worldLocation != null) {
            // Mark location as explored when leaving
            if (Dungeon.worldMap != null) {
                Dungeon.worldMap.markLocationExplored(worldLocation.locationId);
            }
        }

        // Show transition dialog for important locations
        if (worldLocation != null && worldLocation.levelType == WorldLocation.LevelType.BOSS) {
            Game.runOnRenderThread(new Callback() {
                @Override
                public void call() {
                    GameScene.show(new WndOptions(
                        Messages.get(WorldLevel.class, "boss_warning_title"),
                        Messages.get(WorldLevel.class, "boss_warning_desc"),
                        Messages.get(WorldLevel.class, "boss_warning_yes"),
                        Messages.get(WorldLevel.class, "boss_warning_no")
                    ) {
                        @Override
                        protected void onSelect(int index) {
                            if (index == 0) {
                                WorldLevel.super.activateTransition(hero, transition);
                            }
                        }
                    });
                }
            });
            return false;
        }

        return super.activateTransition(hero, transition);
    }

    @Override
    public String tileName(int tile) {
        switch (tile) {
            case Terrain.WALL:
                return Messages.get(WorldLevel.class, "wall_name");
            case Terrain.WALL_DECO:
                return Messages.get(WorldLevel.class, "wall_deco_name");
            case Terrain.HIGH_GRASS:
                return Messages.get(WorldLevel.class, "high_grass_name");
            case Terrain.GRASS:
                return Messages.get(WorldLevel.class, "grass_name");
            default:
                return super.tileName(tile);
        }
    }

    @Override
    public String tileDesc(int tile) {
        switch (tile) {
            case Terrain.WALL:
                return Messages.get(WorldLevel.class, "wall_desc");
            case Terrain.WALL_DECO:
                return Messages.get(WorldLevel.class, "wall_deco_desc");
            case Terrain.HIGH_GRASS:
                return Messages.get(WorldLevel.class, "high_grass_desc");
            case Terrain.GRASS:
                return Messages.get(WorldLevel.class, "grass_desc");
            default:
                return super.tileDesc(tile);
        }
    }

    public WorldLocation getWorldLocation() {
        return worldLocation;
    }

    public LevelData getLevelData() {
        return levelData;
    }

    @Override
    public void storeInBundle(Bundle bundle) {
        super.storeInBundle(bundle);
        if (levelData != null) {
            bundle.put("levelData", levelData);
        }
        if (worldLocation != null) {
            bundle.put("worldLocation", worldLocation);
        }
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        super.restoreFromBundle(bundle);
        if (bundle.contains("levelData")) {
            levelData = (LevelData) bundle.get("levelData");
        }
        if (bundle.contains("worldLocation")) {
            worldLocation = (WorldLocation) bundle.get("worldLocation");
        }
    }
} 