package com.saucy.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.rendering.Renderable;
import com.saucy.game.Game;

public class MainMenuBG implements Renderable {
	
	private static final int BODIES = 40;
	
	private Game game;
	private Texture tile;
	private long startTime;
	
	private Texture[] corpses;
	private float[] deadBodyPositions;
	private int[] deadBodyIDs;
	
	public MainMenuBG(Game game) {
		this.game = game;
		tile = game.assets().get("assets/Tiles/Ground/Floor0.png");
		startTime = System.currentTimeMillis();
		
		corpses = new Texture[3];
		deadBodyPositions = new float[BODIES];
		deadBodyIDs = new int[BODIES];
		for (int i = 0; i != BODIES; i ++) {
			deadBodyPositions[i] = game.random().nextFloat() * game.screenHeight();
			deadBodyIDs[i] = i % 2;
		}
		for (int i = 0; i != 3; i ++)
			corpses[i] = game.getTexture("assets/Enemy/Enemy0Dead"+i+".png");
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		final float
			d = delta(),
			speed = .07f;
		final int
			scale = 3,
			TILE_WIDTH = tile.getWidth() * scale,
			TILE_HEIGHT = tile.getHeight() * scale,
			tiles_x = game.screenWidth() / TILE_WIDTH + 2,
			tiles_y = game.screenHeight() / TILE_HEIGHT;
		for (int a = 0; a != tiles_y; a ++)
			for (int i = 0; i != tiles_x; i ++)
				batch.draw(tile, (i * TILE_WIDTH + d * speed) % (game.screenWidth() + TILE_WIDTH*1.5f) - TILE_WIDTH, a * TILE_HEIGHT, TILE_WIDTH, TILE_HEIGHT);
		for (int i = 0; i != BODIES; i ++)
			batch.draw(corpses[deadBodyIDs[i]], (i * 75 + d * speed) % (game.screenWidth() + TILE_WIDTH) - TILE_WIDTH, deadBodyPositions[i], corpses[deadBodyIDs[i]].getWidth() * scale, corpses[deadBodyIDs[i]].getHeight() * scale);
	}
	
	private long delta() {
		return System.currentTimeMillis() - startTime;
	}
}