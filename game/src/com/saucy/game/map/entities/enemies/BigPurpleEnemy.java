package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class BigPurpleEnemy extends Enemy {
	
	private static EnemyAssets cachedAssets;
	
	public BigPurpleEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 70;
		super.motor.scaleSpeed(1.5f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cachedAssets == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/BigPurpleMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/BigPurpleManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cachedAssets = assets;
		}
		return cachedAssets;
	}

}