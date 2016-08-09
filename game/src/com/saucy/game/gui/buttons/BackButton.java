package com.saucy.game.gui.buttons;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Assets;
import com.saucy.game.Game;

public class BackButton extends Button {
	
	private static final String SOURCE = "Shop/BackButton";
	
	private Game game;
	
	public BackButton(Game game, Vector2 position) {
		super(position, game.getTexture(SOURCE));
		this.game = game;
	}
	
	@Override
	public void onRelease() {
		game.returnToMainMenu();
	}
}