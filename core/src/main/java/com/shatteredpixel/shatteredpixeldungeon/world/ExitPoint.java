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

public class ExitPoint implements Bundlable {

    public int targetLocationId;
    public int sourceCell;      // Cell in current level where exit is located
    public int targetCell;      // Cell in target level where player arrives
    public WorldLocation.ExitType exitType;
    public String exitName;     // Optional name for the exit (e.g., "Forest Path", "Cave Entrance")
    public String exitDesc;     // Optional description
    public String requiredItem; // Item required to use this exit (for LOCKED exits)
    public String requiredQuest; // Quest required to use this exit (for QUEST_GATED exits)
    public boolean isVisible;   // Whether this exit is visible to the player
    public boolean isDiscovered; // Whether this exit has been discovered

    public ExitPoint() {
        exitType = WorldLocation.ExitType.NORMAL;
        isVisible = true;
        isDiscovered = false;
    }

    public ExitPoint(int targetLocationId, int sourceCell, int targetCell) {
        this();
        this.targetLocationId = targetLocationId;
        this.sourceCell = sourceCell;
        this.targetCell = targetCell;
    }

    public ExitPoint(int targetLocationId, int sourceCell, int targetCell, WorldLocation.ExitType exitType) {
        this(targetLocationId, sourceCell, targetCell);
        this.exitType = exitType;
    }

    public boolean canUse() {
        switch (exitType) {
            case NORMAL:
                return true;
            case LOCKED:
                return hasRequiredItem();
            case QUEST_GATED:
                return isQuestCompleted();
            case CONDITIONAL:
                return checkConditions();
            case ONE_WAY:
                return true;
            case HIDDEN:
                return isDiscovered;
            default:
                return false;
        }
    }

    private boolean hasRequiredItem() {
        if (requiredItem == null || requiredItem.isEmpty()) {
            return true;
        }
        // TODO: Implement item checking logic
        return false;
    }

    private boolean isQuestCompleted() {
        if (requiredQuest == null || requiredQuest.isEmpty()) {
            return true;
        }
        // TODO: Implement quest completion checking
        return false;
    }

    private boolean checkConditions() {
        // TODO: Implement conditional logic
        return true;
    }

    public void discover() {
        isDiscovered = true;
        if (exitType == WorldLocation.ExitType.HIDDEN) {
            isVisible = true;
        }
    }

    // Bundling for save/load
    private static final String TARGET_LOCATION_ID = "target_location_id";
    private static final String SOURCE_CELL = "source_cell";
    private static final String TARGET_CELL = "target_cell";
    private static final String EXIT_TYPE = "exit_type";
    private static final String EXIT_NAME = "exit_name";
    private static final String EXIT_DESC = "exit_desc";
    private static final String REQUIRED_ITEM = "required_item";
    private static final String REQUIRED_QUEST = "required_quest";
    private static final String IS_VISIBLE = "is_visible";
    private static final String IS_DISCOVERED = "is_discovered";

    @Override
    public void storeInBundle(Bundle bundle) {
        bundle.put(TARGET_LOCATION_ID, targetLocationId);
        bundle.put(SOURCE_CELL, sourceCell);
        bundle.put(TARGET_CELL, targetCell);
        bundle.put(EXIT_TYPE, exitType);
        bundle.put(EXIT_NAME, exitName);
        bundle.put(EXIT_DESC, exitDesc);
        bundle.put(REQUIRED_ITEM, requiredItem);
        bundle.put(REQUIRED_QUEST, requiredQuest);
        bundle.put(IS_VISIBLE, isVisible);
        bundle.put(IS_DISCOVERED, isDiscovered);
    }

    @Override
    public void restoreFromBundle(Bundle bundle) {
        targetLocationId = bundle.getInt(TARGET_LOCATION_ID);
        sourceCell = bundle.getInt(SOURCE_CELL);
        targetCell = bundle.getInt(TARGET_CELL);
        exitType = bundle.getEnum(EXIT_TYPE, WorldLocation.ExitType.class);
        exitName = bundle.getString(EXIT_NAME);
        exitDesc = bundle.getString(EXIT_DESC);
        requiredItem = bundle.getString(REQUIRED_ITEM);
        requiredQuest = bundle.getString(REQUIRED_QUEST);
        isVisible = bundle.getBoolean(IS_VISIBLE);
        isDiscovered = bundle.getBoolean(IS_DISCOVERED);
    }
} 