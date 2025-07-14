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

package com.shatteredpixel.shatteredpixeldungeon.windows;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.ShatteredPixelDungeon;
import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.ui.RenderedTextBlock;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;
import com.shatteredpixel.shatteredpixeldungeon.world.WorldLocation;
import com.shatteredpixel.shatteredpixeldungeon.world.ExitPoint;
import com.shatteredpixel.shatteredpixeldungeon.world.WorldManager;


import java.util.ArrayList;

public class WndWorldMap extends Window {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 160;

    public WndWorldMap() {
        super();

        resize(WIDTH, HEIGHT);

        if (Dungeon.worldExplorationEnabled && Dungeon.worldMap != null) {
            createWorldMapContent();
        } else {
            createFallbackContent();
        }
    }

    private void createWorldMapContent() {
        // Title
        RenderedTextBlock title = new RenderedTextBlock(Messages.get(this, "title"), 8);
        title.hardlight(TITLE_COLOR);
        title.setPos((WIDTH - title.width()) / 2, 2);
        add(title);

        // Current location info
        WorldLocation currentLocation = Dungeon.worldMap.getCurrentLocation();
        if (currentLocation != null) {
            RenderedTextBlock currentTitle = new RenderedTextBlock(Messages.get(this, "current_location"), 6);
            currentTitle.setPos(2, title.bottom() + 4);
            add(currentTitle);

            RenderedTextBlock locationName = new RenderedTextBlock(currentLocation.locationName, 6);
            locationName.hardlight(0x44CCFF);
            locationName.setPos(2, currentTitle.bottom() + 2);
            add(locationName);

            if (currentLocation.locationDesc != null && !currentLocation.locationDesc.isEmpty()) {
                RenderedTextBlock locationDesc = new RenderedTextBlock(currentLocation.locationDesc, 5);
                locationDesc.maxWidth(WIDTH - 4);
                locationDesc.setPos(2, locationName.bottom() + 4);
                add(locationDesc);
            }

            // Exploration stats
            RenderedTextBlock stats = new RenderedTextBlock(
                Messages.format("exploration_stats", 
                    Dungeon.worldMap.getDiscoveredLocationCount(),
                    Dungeon.worldMap.getExploredLocationCount(),
                    Dungeon.worldMap.getLocationCount(),
                    Math.round(Dungeon.worldMap.getExplorationPercentage() * 100)
                ), 5);
            stats.setPos(2, locationName.bottom() + 12);
            add(stats);

            // Available exits
            ArrayList<ExitPoint> exits = Dungeon.worldMap.getAccessibleExits();
            if (!exits.isEmpty()) {
                RenderedTextBlock exitsTitle = new RenderedTextBlock(Messages.get(this, "available_exits"), 6);
                exitsTitle.setPos(2, stats.bottom() + 8);
                add(exitsTitle);

                int yPos = (int)(exitsTitle.bottom() + 2);
                for (ExitPoint exit : exits) {
                    WorldLocation targetLocation = Dungeon.worldMap.getLocation(exit.targetLocationId);
                    if (targetLocation != null) {
                        String exitText = targetLocation.locationName;
                        if (exit.exitName != null && !exit.exitName.isEmpty()) {
                            exitText = exit.exitName + " → " + exitText;
                        }
                        
                        RenderedTextBlock exitBlock = new RenderedTextBlock(exitText, 5);
                        exitBlock.hardlight(0x88CC44);
                        exitBlock.setPos(4, yPos);
                        add(exitBlock);
                        yPos += exitBlock.height() + 1;
                    }
                }
            }
        }
    }

    private void createFallbackContent() {
        RenderedTextBlock title = new RenderedTextBlock(Messages.get(this, "title"), 8);
        title.hardlight(TITLE_COLOR);
        title.setPos((WIDTH - title.width()) / 2, 2);
        add(title);

        RenderedTextBlock message = new RenderedTextBlock(Messages.get(this, "not_available"), 6);
        message.maxWidth(WIDTH - 4);
        message.setPos(2, title.bottom() + 10);
        add(message);
    }

    @Override
    public void onBackPressed() {
        hide();
    }
} 