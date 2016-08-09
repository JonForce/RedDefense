package com.saucy.game.map.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.Assets;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Tile;
import com.saucy.game.map.Wall;

public abstract class Breaker extends Enemy {
	
	private Texture floor;
	private EnemySpawner spawner;
	
	protected boolean dieOnWallBreak = true;
	
	public Breaker(EnemySpawner spawner, Assets assets, Grid grid) {
		super(assets, grid);
		super.motor.scaleSpeed((spawner.getWave() < 20)? 1f : 2f);
		super.health = spawner.getWave() / 2;
		this.spawner = spawner;
		super.updatePath();
		floor = assets.get("assets/Tiles/Ground/Floor0.png");
		boolean[][] matrix = grid.createWalkableMatrix();
		for (int x = 0; x != matrix.length; x ++)
			for (int y = 0; y != matrix[0].length; y ++)
				if (matrix[x][y] && !((Wall)grid.get(x, y)).strong())
					matrix[x][y] = false;
		super.pathfinder.updateGrid(matrix);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		super.updateWith(input);
		if (!grid.get(gridX(), gridY()).walkable()) {
			grid.set(gridX(), gridY(), new Tile(floor));
			if (dieOnWallBreak)
				super.health = 0;
			spawner.updateEnemyPaths();
		}
	}
	
	@Override
	public void updatePath() {
		
	}
	
	@Override
	protected EnemyAssets createEnemyAssets(Assets allAssets) {
		EnemyAssets assets = new EnemyAssets();
		assets.idle = allAssets.get("assets/Enemy/Breaker.png");
		assets.idle2 = allAssets.get("assets/Enemy/Breaker.png");
		assets.dead = allAssets.get("assets/Enemy/RedManDead.png");
		return assets;
	}

}