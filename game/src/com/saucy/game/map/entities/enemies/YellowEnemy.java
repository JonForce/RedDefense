package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class YellowEnemy extends Enemy {
	
	private static EnemyAssets cache;
	
	public YellowEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 100;
		super.motor.scaleSpeed(2f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cache == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/YellowMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/YellowManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cache = assets;
		}
		return cache;
	}

}