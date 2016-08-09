package com.saucy.game.gui.buttons;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Game;

public abstract class ResumeButton extends Button {
	
	public static final String
		TEXTURE_SOURCE = "assets/GUI/Icons/Play.png";
	
	public ResumeButton(Game game, Vector2 buttonCenter) {
		super(buttonCenter, game.getTexture(TEXTURE_SOURCE));
	}
	
	@Override
	public final void onRelease() {
		resume();
	}
	
	public abstract void resume();
}