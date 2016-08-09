package com.saucy.game.map.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.rendering.Renderable;
import com.saucy.framework.util.Updatable;
import com.saucy.game.ViewRenderable;
import com.saucy.game.map.Position;
import com.saucy.game.map.Tile;
import com.saucy.game.map.TileRenderer;

public abstract class Entity implements ViewRenderable, Updatable, Position {
	
	public final float screenX() { return x() * TileRenderer.SCALE; }
	public final float screenY() { return y() * TileRenderer.SCALE; }
	public final int gridX() { return (int) (x() / Tile.WIDTH); }
	public final int gridY() { return (int) (y() / Tile.HEIGHT); }
	
	public boolean shouldRemove() {
		return false;
	}
	
	public float rotation() {
		return 0f;
	}
	
	public final void centerRender(SpriteBatch batch, Texture texture, Position view) {
		final int
			viewX = (view == null)? 0 : (int)view.x(),
			viewY = (view == null)? 0 : (int)view.y(),
			x = (int)(screenX() - viewX - (texture.getWidth() * TileRenderer.SCALE)/2f),
			y = (int)(screenY() - viewY - (texture.getHeight() * TileRenderer.SCALE)/2f),
			scaleX = TileRenderer.SCALE,
			scaleY = TileRenderer.SCALE,
			srcX = 0,
			srcY = 0,
			srcWidth = texture.getWidth(),
			srcHeight = texture.getHeight(),
			originX = srcWidth / 2,
			originY = srcHeight / 2,
			width = texture.getWidth(),
			height = texture.getHeight();
		final boolean
			flipX = false,
			flipY = false;
		batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation(), srcX, srcY, srcWidth, srcHeight, flipX, flipY);
	}
	
	public final void centerRender(SpriteBatch batch, Texture texture, Position view, float rotation) {
		final int
			viewX = (view == null)? 0 : (int)view.x(),
			viewY = (view == null)? 0 : (int)view.y(),
			x = (int)(screenX() - viewX - (texture.getWidth())/2f),
			y = (int)(screenY() - viewY - (texture.getHeight())/2f),
			scaleX = TileRenderer.SCALE,
			scaleY = TileRenderer.SCALE,
			srcX = 0,
			srcY = 0,
			srcWidth = texture.getWidth(),
			srcHeight = texture.getHeight(),
			originX = srcWidth / 2,
			originY = srcHeight / 2,
			width = texture.getWidth(),
			height = texture.getHeight();
		final boolean
			flipX = false,
			flipY = false;
		batch.draw(texture, x, y, originX, originY, width, height, scaleX, scaleY, rotation, srcX, srcY, srcWidth, srcHeight, flipX, flipY);
	}
	
	public final float distanceTo(float x, float y) {
		return (float)Math.sqrt(Math.pow(x - x(), 2) + Math.pow(y - y(), 2));
	}
	public final float distanceTo(Position e) {
		return distanceTo(e.x(), e.y());
	}
}