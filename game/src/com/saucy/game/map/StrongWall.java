package com.saucy.game.map;

import com.saucy.game.Assets;

public class StrongWall extends Wall {

	public StrongWall(Assets assets) {
		super(assets.get("assets/Tiles/Walls/Wall1.png"));
	}
	
	@Override
	public int cost() { return 10; }
	
	@Override
	public boolean strong() { return true; }
	
}