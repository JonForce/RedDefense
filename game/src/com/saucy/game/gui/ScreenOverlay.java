package com.saucy.game.gui;

import com.saucy.framework.rendering.Graphic;
import com.saucy.game.Game;

public class ScreenOverlay extends Graphic {
	
	private final static String
		TEXTURE_SOURCE = "assets/Windows/Overlay.png";
	
	public ScreenOverlay(Game game) {
		super(game.screenCenter(), game.screenSize(), game.getTexture(TEXTURE_SOURCE));
	}
}