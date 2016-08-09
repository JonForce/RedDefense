package com.saucy.game.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.rendering.Renderable;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.Wall;

public abstract class TryWallBuild implements Renderable {
	
	private final static long TIMEOUT = 10000;
	
	protected abstract boolean tryBuildAt(int x, int y, Wall wall);
	
	private long startTime;
	private int x, y;
	private Wall wall;
	private TileRenderer renderer;
	private boolean shouldRemove = false;
	
	/** A class that will keep trying to build a wall at a certain location
	 * when it is updated until it can. For when enemies are in the way.
	 */
	public TryWallBuild(Wall wall, int x, int y, TileRenderer renderer) {
		this.renderer = renderer;
		this.x = x;
		this.y = y;
		this.wall = wall;
		this.startTime = System.currentTimeMillis();
	}
	
	public void update() {
		if (tryBuildAt(x, y, wall))
			shouldRemove = true;
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		renderer.render(wall, x, y, batch, .5f);
	}
	
	public boolean shouldRemove() {
		return shouldRemove || System.currentTimeMillis() - startTime > TIMEOUT;
	}
}