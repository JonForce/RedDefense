package com.saucy.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.Graphic;
import com.saucy.framework.rendering.Renderable;
import com.saucy.game.Game;

public abstract class OverlayState implements ApplicationState {
	
	protected final Game game;
	private final Graphic overlay;
	
	/** Create a State that renders over another State.  */
	public OverlayState(Game game) {
		this.game = game;
		this.overlay = new Graphic(game.screenCenter(), game.screenSize(), game.getTexture("assets/Windows/Overlay.png"));
	}
	
	@Override
	public final void renderTo(SpriteBatch batch) {
		subScreen().renderTo(batch);
		overlay.renderTo(batch);
		superScreen().renderTo(batch);
	}

	@Override
	public void enterState() { }
	@Override
	public void exitState() { }
	
	protected abstract Renderable superScreen();
	protected abstract Renderable subScreen();
}