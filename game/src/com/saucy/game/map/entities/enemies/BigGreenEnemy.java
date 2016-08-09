package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class BigGreenEnemy extends Enemy {
	
	private static EnemyAssets cache;

	public BigGreenEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 35;
	}

	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cache == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/BigGreenMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/BigGreenManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cache = assets;
		}
		return cache;
	}

}