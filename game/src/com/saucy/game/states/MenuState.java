package com.saucy.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.Graphic;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.effects.Animator;

public class MenuState implements ApplicationState {

	public final String
		BACKGROUND_SOURCE = "assets/GUI/MainMenu/bg.png",
		BG_TOP_SOURCE = "MainMenu/top",
		BG_BOT_SOURCE = "MainMenu/bottom";
	
	protected Game game;
	protected Graphic background, title, bgTop, bgBot;
	
	protected Vector2
		menuCenter, // The center of the MainMenu.
		menuSize; // The width and height of the MainMenu.
	
	protected boolean initialized = false;
	
	public MenuState(Game game) {
		this.game = game;
		this.menuCenter = game.screenCenter();
		this.menuSize = game.screenSize();
	}
	
	/** Create and position the MainMenu's background. */
	protected void initializeBackground() {
		background = new Graphic(menuCenter, menuSize, game.getTexture(BACKGROUND_SOURCE));
//		bgTop = new Graphic(new Vector2(menuCenter.x,menuSize.y), menuSize, game.getTexture(BG_TOP_SOURCE));
//		bgBot = new Graphic(new Vector2(menuCenter.x,0), menuSize, game.getTexture(BG_BOT_SOURCE));
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		background.renderTo(batch);
//		bgTop.renderTo(batch);
//		bgBot.renderTo(batch);
	}

	public void tweenBarOffset(Vector2 offset, float duration) {
//		new Animator(game).tweenGraphicTo(bgTop, 
//				bgTop.getPosition().add(offset), duration);
//		new Animator(game).tweenGraphicTo(bgBot, 
//				bgBot.getPosition().sub(offset), duration);
	}
	@Override
	public void enterState() {
		//if(!initialized) {
			initialized = true;
			initializeBackground();
		//}
	}

	@Override
	public void updateApplication(Application app) {
		
	}

	@Override
	public void exitState() {
		
	}

}
