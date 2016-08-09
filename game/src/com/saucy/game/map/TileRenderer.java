package com.saucy.game.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** A utility for rendering Tiles. */
public abstract class TileRenderer {
	
	public final static int
		SCALE = 3,
		RENDER_WIDTH = SCALE * Tile.WIDTH,
		RENDER_HEIGHT = SCALE * Tile.HEIGHT;
	
	protected abstract float viewX();
	protected abstract float viewY();
	
	/** Render the Tile onto the batch at the specific Grid index. */
	public void render(Tile tile, int indexX, int indexY, SpriteBatch batch, float opacity) {
		batch.setColor(1, 1, 1, opacity);
		batch.draw(
				tile.texture(),
				-viewX() + indexX * RENDER_WIDTH,
				-viewY() + indexY * RENDER_HEIGHT,
				RENDER_WIDTH, RENDER_HEIGHT);
		batch.setColor(1, 1, 1, 1);
	}
	
	/** Render all of the Grid's tiles to the batch. */
	public void render(Grid grid, SpriteBatch batch) {
		for (int x = 0; x != grid.width(); x ++) {
			for (int y = 0; y != grid.height(); y ++) {
				render(grid.get(x, y), x, y, batch, 1);
			}
		}
	}
	
	public static void render(Tile tile, int indexX, int indexY, SpriteBatch batch, Position view) {
		batch.draw(
				tile.texture(),
				-view.x() + indexX * RENDER_WIDTH,
				-view.y() + indexY * RENDER_HEIGHT,
				RENDER_WIDTH, RENDER_HEIGHT);
	}
}