package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;

public abstract class PurpleEnemy extends Enemy {
	
	private static EnemyAssets cachedAssets;
	
	public PurpleEnemy(Assets assets, Grid grid) {
		super(assets, grid);
		super.health *= 40;
		super.motor.scaleSpeed(1.5f);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		if (cachedAssets == null) {
			EnemyAssets assets = new EnemyAssets();
			assets.idle = allAssets.get("assets/Enemy/PurpleMan.png");
			assets.idle2 = allAssets.get("assets/Enemy/PurpleManSquished.png");
			assets.dead = allAssets.get("assets/Enemy/GreenManDead.png");
			cachedAssets = assets;
		}
		return cachedAssets;
	}

}