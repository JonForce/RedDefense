package com.saucy.game.map;

import com.badlogic.gdx.graphics.Texture;

public class Tile {
	
	public static final int
		WIDTH = 16,
		HEIGHT = 16;
	
	private Texture texture;
	
	public Tile(Texture texture) {
		this.texture = texture;
	}
	
	public Texture texture() { return texture; }
	public void setTexture(Texture newTex) { texture = newTex; }
	
	public boolean walkable() { return true; }
	
}