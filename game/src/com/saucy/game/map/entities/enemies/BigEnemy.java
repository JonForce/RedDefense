package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class BigEnemy extends Enemy {

	public BigEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 4;
		super.motor.scaleSpeed(.5f);
	}

	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/BigEnemyIdle.png");
		assets.idle2 = allAssets.get("assets/Enemy/BigEnemySquished.png");
		assets.dead = allAssets.get("assets/Enemy/Enemy1Dead.png");
		return assets;
	}
}