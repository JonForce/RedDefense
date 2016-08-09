package com.saucy.game.map;

import com.badlogic.gdx.graphics.Texture;
import com.saucy.game.Assets;

public class Wall extends Tile {
	
	public Wall(Texture texture) {
		super(texture);
	}
	
	@Override
	public boolean walkable() {
		return false;
	}
	
	public boolean strong() { return false; }
	
	public int cost() {
		return 5;
	}
	
	public static Texture texture(Assets assets, int id) {
		return assets.get("assets/Tiles/Walls/Wall"+id+".png");
	}
}