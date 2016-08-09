package com.saucy.game.map.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.framework.util.pathfinding.Pathfinder;
import com.saucy.game.Assets;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Position;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.entities.Bullet;
import com.saucy.game.map.entities.Entity;
import com.saucy.game.map.entities.Motor;

public abstract class Enemy extends Entity {
	
	protected final Motor motor;
	protected float health = 4;
	protected final Pathfinder pathfinder;
	protected final Grid grid;
	
	private final EnemyAssets assets;
	private Index target;
	
	protected abstract EnemyAssets createEnemyAssets(Assets allAssets);
	protected abstract void onDeath();
	
	public Enemy(Assets assets, Grid grid) {
		this.grid = grid;
		this.motor = new Motor();
		motor.setGridPosition(1, 1);
		this.pathfinder = new Pathfinder(grid.createWalkableMatrix());
		this.assets = createEnemyAssets(assets);
		this.target = Index.of(1, 1);
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		batch.draw(
				texture(),
				screenX() - view.x() - (texture().getWidth() * TileRenderer.SCALE)/2f,
				screenY() - view.y() - (texture().getHeight() * TileRenderer.SCALE)/2f,
				texture().getWidth() * TileRenderer.SCALE,
				texture().getHeight() * TileRenderer.SCALE);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		motor.updateWith(input);
	}
	
	public int pathLength() {
		return motor.pathLength();
	}
	
	public void getShotBy(Bullet b) {
		health -= b.damage;
		if (health <= 0)
			onDeath();
	}
	
	public void updatePath() {
		pathfinder.updateGrid(grid.createWalkableMatrix());
		motor.setPath(pathfinder.findPath(Index.of(gridX(), gridY()), target));
	}
	
	public void target(int gridX, int gridY) {
		target = Index.of(gridX, gridY);
		Index[] path = pathfinder.findPath(Index.of(this.gridX(), this.gridY()), target);
		motor.setPath(path);
	}
	
	@Override
	public boolean shouldRemove() {
		return health <= 0;
	}
	
	@Override
	public float x() { return motor.x(); }
	@Override
	public float y() { return motor.y(); }
	
	private Texture texture() {
		return ((motor.updateDelta()) % 500 > 250)? assets.idle : assets.idle2;
	}
}