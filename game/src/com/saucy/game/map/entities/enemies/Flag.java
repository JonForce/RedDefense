package com.saucy.game.map.entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;
import com.saucy.game.map.Tile;
import com.saucy.game.map.entities.Entity;

public class Flag extends Entity {
	
	private float x, y;
	private Texture texture;
	
	public Flag(Assets assets, int gridX, int gridY) {
		this.texture = assets.get("assets/Flag.png");
		this.x = gridX * Tile.WIDTH + texture.getWidth() - 1;
		this.y = gridY * Tile.HEIGHT + texture.getHeight() - 4;
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		super.centerRender(batch, texture, view);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		
	}
	
	@Override
	public float x() {
		return x;
	}
	
	@Override
	public float y() {
		return y;
	}
	
}