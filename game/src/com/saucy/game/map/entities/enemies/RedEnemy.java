package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;
import com.saucy.game.map.entities.Bullet;

public abstract class RedEnemy extends Enemy {

	public RedEnemy(Assets assets, Grid grid) {
		super(assets, grid);
	}
	
	@Override
	public void getShotBy(Bullet b) {
		if (b.type.equals("Fire"))
			return;
		else
			super.getShotBy(b);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/RedMan.png");
		assets.idle2 = allAssets.get("assets/Enemy/RedManSquished.png");
		assets.dead = allAssets.get("assets/Enemy/RedManDead.png");
		return assets;
	}
	
}