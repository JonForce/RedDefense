package com.saucy.game.map.entities.turrets;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.map.Position;
import com.saucy.game.map.Tile;
import com.saucy.game.map.ViewController;
import com.saucy.game.map.entities.Entity;
import com.saucy.game.map.entities.Manager;
import com.saucy.game.map.entities.enemies.Enemy;

public class TurretManager extends Manager<Turret> {
	
	private final ArrayList<Enemy> enemies;
	
	public TurretManager(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	@Override
	public void updateWith(InputProxy input) {
		for (int i = list.size() - 1; i >= 0; i --) {
			Turret t = list.get(i);
			if (t.shouldRemove())
				list.remove(i);
			else
				t.updateWith(enemies);
		}
	}
	
}