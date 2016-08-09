package com.saucy.game.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.Updatable;
import com.saucy.game.Game;

public class ViewController implements Updatable, Position {
	
	private final boolean USE_TOUCH;
	
	private Vector2 position = new Vector2();
	
	private float
		scrollSpeed = 2.5f,
		speedMultiplier = 3f;
	private int
		tilesX, tilesY, screenWidth, screenHeight;
	private boolean hasMoved = false;
	
	public ViewController(boolean useTouchControls, int tilesX, int tilesY, int screenWidth, int screenHeight) {
		USE_TOUCH = useTouchControls;
		this.tilesX = tilesX;
		this.tilesY = tilesY;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
	}
	
	@Override
	public void updateWith(InputProxy input) {
		float dx, dy;
		
		if (!USE_TOUCH) {
			dx =
					Gdx.input.isKeyPressed(Keys.D)? scrollSpeed() :
					Gdx.input.isKeyPressed(Keys.A)? -scrollSpeed() : 0f;
			dy =
					Gdx.input.isKeyPressed(Keys.W)? scrollSpeed() :
					Gdx.input.isKeyPressed(Keys.S)? -scrollSpeed() : 0f;
		} else {
			// Use touch controls.
			if (!(input.isTouched(0) && input.isTouched(1))) {
				dx = 0;
				dy = 0;
			} else {
				dx = -input.getDeltaX() * .9f;
				dy = -input.getDeltaY() * .9f;
			}
		}
		
		if (dx != 0 || dy != 0)
			hasMoved = true;
		
		position.add(dx, dy);
		position.x = Math.min(Math.max(position.x, 0), TileRenderer.RENDER_WIDTH * (tilesX - screenWidth/TileRenderer.RENDER_WIDTH));
		position.y = Math.min(Math.max(position.y, 0), TileRenderer.RENDER_HEIGHT * (tilesY - screenHeight/TileRenderer.RENDER_HEIGHT));
	}
	
	public int getCursorIndexX(InputProxy input) {
		return (int)(position.x + input.getX()) / TileRenderer.RENDER_WIDTH;
	}
	public int getCursorIndexY(InputProxy input) {
		return (int)(position.y + input.getY()) / TileRenderer.RENDER_HEIGHT;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	@Override
	public float x() { return position.x; }
	@Override
	public float y() { return position.y; }
	
	/** @return the speed at which the view can move. */
	private float scrollSpeed() {
		return (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))?
				scrollSpeed * speedMultiplier :
				scrollSpeed;
	}
}