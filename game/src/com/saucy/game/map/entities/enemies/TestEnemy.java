package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Position;

public abstract class TestEnemy extends Enemy {

	public TestEnemy(Assets assets, Grid grid) {
		super(assets, grid);
	}

	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/Enemy0Idle.png");
		assets.idle2 = allAssets.get("assets/Enemy/Enemy0Squished.png");
		assets.dead = allAssets.get("assets/Enemy/Enemy1Dead.png");
		return assets;
	}

}