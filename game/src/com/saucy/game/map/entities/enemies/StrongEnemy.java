package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class StrongEnemy extends Enemy {
	
	private static EnemyAssets cache;

	public StrongEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 12;
		//super.motor.scaleSpeed(1.5f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cache == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/GreenMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/GreenManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cache = assets;
		}
		return cache;
	}

}