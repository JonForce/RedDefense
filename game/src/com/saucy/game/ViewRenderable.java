package com.saucy.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.game.map.Position;

public interface ViewRenderable {
	public abstract void renderTo(SpriteBatch batch, Position position);
}