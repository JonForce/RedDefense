package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class BlueEnemy extends Enemy {
	
	private static EnemyAssets cache;
	
	public BlueEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 30;
		super.motor.scaleSpeed(1.5f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cache == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/BlueMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/BlueManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cache = assets;
		}
		return cache;
	}

}