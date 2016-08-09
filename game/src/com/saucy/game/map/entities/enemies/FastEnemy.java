package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class FastEnemy extends Enemy {

	public FastEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= .5f;
		super.motor.scaleSpeed(2.5f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/Enemy2Idle.png");
		assets.idle2 = allAssets.get("assets/Enemy/Enemy2Squished.png");
		assets.dead = allAssets.get("assets/Enemy/Enemy2Dead.png");
		return assets;
	}
}