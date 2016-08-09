package com.saucy.game.map.entities.enemies;

import com.saucy.game.Assets;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Wall;

public abstract class BossBreaker extends Breaker {

	public BossBreaker(EnemySpawner spawner, Assets assets, Grid grid) {
		super(spawner, assets, grid);
		dieOnWallBreak = false;
		super.health = 700;
		super.motor.scaleSpeed(.1f);
		
		boolean[][] matrix = grid.createWalkableMatrix();
		for (int x = 0; x != matrix.length; x ++)
			for (int y = 0; y != matrix[0].length; y ++)
				matrix[x][y] = false;
		super.pathfinder.updateGrid(matrix);
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/BossBreaker.png");
		assets.idle2 = allAssets.get("assets/Enemy/BossBreakerSquished.png");
		assets.dead = allAssets.get("assets/Enemy/RedManDead.png");
		return assets;
	}
	
}