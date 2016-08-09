package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class BigBlueEnemy extends Enemy {
	
	private static EnemyAssets cache;
	
	public BigBlueEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 50;
		super.motor.scaleSpeed(1.5f);
	}

	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cache == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/BigBlueMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/BigBlueManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cache = assets;
		}
		return cache;
	}

}