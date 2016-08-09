package com.saucy.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.saucy.game.Game;

public class Main {
	
	private static final int
		WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600,
		VIRTUAL_WIDTH = 1024, VIRTUAL_HEIGHT = 768;
	
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Game";
		cfg.useGL20 = false;
		cfg.width = WINDOW_WIDTH;
		cfg.height = WINDOW_HEIGHT;
		
		new LwjglApplication(new Game(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, false), cfg);
	}
}