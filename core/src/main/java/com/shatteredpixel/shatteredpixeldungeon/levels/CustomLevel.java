/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2024 Evan Debenham
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

 package com.shatteredpixel.shatteredpixeldungeon.levels;

 import com.shatteredpixel.shatteredpixeldungeon.Assets;
 import com.shatteredpixel.shatteredpixeldungeon.Bones;
 import com.shatteredpixel.shatteredpixeldungeon.Challenges;
 import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
 import com.shatteredpixel.shatteredpixeldungeon.Statistics;
 import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
 import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
 import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Blob;
 import com.shatteredpixel.shatteredpixeldungeon.actors.blobs.Electricity;
 import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.DM300;
 import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Mob;
 import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Pylon;
 import com.shatteredpixel.shatteredpixeldungeon.effects.BlobEmitter;
 import com.shatteredpixel.shatteredpixeldungeon.effects.CellEmitter;
 import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
 import com.shatteredpixel.shatteredpixeldungeon.effects.particles.BlastParticle;
 import com.shatteredpixel.shatteredpixeldungeon.effects.particles.SparkParticle;
 import com.shatteredpixel.shatteredpixeldungeon.items.Heap;
 import com.shatteredpixel.shatteredpixeldungeon.items.Item;
 import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
 import com.shatteredpixel.shatteredpixeldungeon.levels.painters.CavesPainter;
 import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
 import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
 import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
 import com.shatteredpixel.shatteredpixeldungeon.scenes.PixelScene;
 import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
 import com.shatteredpixel.shatteredpixeldungeon.sprites.PylonSprite;
 import com.shatteredpixel.shatteredpixeldungeon.tiles.CustomTilemap;
 import com.shatteredpixel.shatteredpixeldungeon.ui.BossHealthBar;
 import com.shatteredpixel.shatteredpixeldungeon.utils.GLog;
 import com.watabou.noosa.Game;
 import com.watabou.noosa.Group;
 import com.watabou.noosa.Image;
 import com.watabou.noosa.Tilemap;
 import com.watabou.noosa.audio.Music;
 import com.watabou.noosa.audio.Sample;
 import com.watabou.noosa.particles.Emitter;
 import com.watabou.utils.Bundle;
 import com.watabou.utils.Callback;
 import com.watabou.utils.GameMath;
 import com.watabou.utils.PathFinder;
 import com.watabou.utils.Point;
 import com.watabou.utils.Random;
 import com.watabou.utils.Rect;
 
 import java.util.ArrayList;
 
 public class CustomLevel extends Level {
 
     {
         color1 = 0x534f3e;
         color2 = 0xb9d661;
     }
 

     @Override
	protected void createMobs() {
	}
    @Override
	protected void createItems() {
	}

     @Override
     public String tilesTex() {
         return Assets.Environment.TILES_CAVES;
     }
 
     @Override
     public String waterTex() {
         return Assets.Environment.WATER_CAVES;
     }
 
     private static int WIDTH = 33;
     private static int HEIGHT = 42;
 
     public static Rect mainArena = new Rect(5, 14, 28, 37);
 
     @Override
     protected boolean build() {
 
         setSize(WIDTH, HEIGHT);
 
         return true;
 
     }
 
     @Override
     public void restoreFromBundle(Bundle bundle) {
         super.restoreFromBundle(bundle);
 
         for (CustomTilemap c : customTiles){
             if (c instanceof ArenaVisuals){
                 customArenaVisuals = (ArenaVisuals) c;
             }
         }
     }
 

     @Override
     public Group addVisuals() {
         super.addVisuals();
         CavesLevel.addCavesVisuals(this, visuals);
         return visuals;
     }
 
     /**
      * semi-randomized setup for entrance and corners
      */
 


     /**
      * Visual Effects
      */
 
     public static class CityEntrance extends CustomTilemap{
 
         {
             texture = Assets.Environment.CAVES_BOSS;
         }
 
         private static short[] entryWay = new short[]{
                 -1,  7,  7,  7, -1,
                 -1,  1,  2,  3, -1,
                  8,  1,  2,  3, 12,
                 16,  9, 10, 11, 20,
                 16, 16, 18, 20, 20,
                 16, 17, 18, 19, 20,
                 16, 16, 18, 20, 20,
                 16, 17, 18, 19, 20,
                 16, 16, 18, 20, 20,
                 16, 17, 18, 19, 20,
                 24, 25, 26, 27, 28
         };
 
         @Override
         public Tilemap create() {
             Tilemap v = super.create();
             int[] data = new int[tileW*tileH];
             int entryPos = 0;
             for (int i = 0; i < data.length; i++){
                data[i] = entryWay[entryPos++];
             }
             v.map( data, tileW );
             return v;
         }
 
     }
 

    }
