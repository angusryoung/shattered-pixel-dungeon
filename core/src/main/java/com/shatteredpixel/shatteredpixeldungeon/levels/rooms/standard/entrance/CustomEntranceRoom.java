package com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.entrance;

import com.shatteredpixel.shatteredpixeldungeon.levels.Level;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.levels.features.LevelTransition;
import com.shatteredpixel.shatteredpixeldungeon.levels.painters.Painter;
import com.shatteredpixel.shatteredpixeldungeon.levels.rooms.standard.CustomRoom;
import com.watabou.utils.Point;

public class CustomEntranceRoom extends CustomRoom {

	@Override
	public boolean isEntrance() {
		return true;
	}

	@Override
	public void paint(Level level) {
		super.paint(level);

		int entrance = -1;
		for ( Point p : getPoints()){
			if (level.map[level.pointToCell(p)] == Terrain.STATUE_SP){
				entrance = level.pointToCell(p);
				break;
			}
		}
		Painter.set( level, entrance, Terrain.ENTRANCE_SP );
		level.transitions.add(new LevelTransition(level, entrance, LevelTransition.Type.REGULAR_ENTRANCE));

	}
}