package com.saucy.game.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.Graphic;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Game;
import com.saucy.game.gui.Font;
import com.saucy.game.gui.ScreenOverlay;
import com.saucy.game.gui.buttons.HomeButton;
import com.saucy.game.gui.buttons.RestartButton;
import com.saucy.game.levels.LevelState;
import com.saucy.game.shop.ShopFont;

public abstract class GameOverState implements ApplicationState {
	
	public static final String
		OVERLAY_SOURCE = "assets/Windows/Overlay.png",
		WINDOW_SOURCE = "assets/Windows/GameOver.png",
		FONT_SOURCE = "assets/GUI/Font/Digits.png";
	
	private static final int
		BUTTON_SPACING = 10,
		BUTTON_BOTTOM_MARGIN = 10,
		CURRENT_SCORE_VERTICAL_OFFSET = 100,
		HIGHEST_SCORE_VERTICAL_OFFSET = -150;
	
	protected Button restartButton, homeButton;
	protected Font font;
	protected LevelState level;
	protected Game game;
	
	private ShopFont fontB;
	
	private Graphic
		overlay; // The gray texture that renders over the Level.
	
	public GameOverState(Game game, LevelState level) {
		this.level = level;
		this.game = game;
		this.fontB = new ShopFont();
		fontB.setScale(.5f);
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering GameOverState.");
		
		// Stop the background music.
		game.stopBackgroundMusic();
		
		// Initialize the gray overlay Graphic.
		overlay = new ScreenOverlay(game);
		// Initialize the Screen's Font.
		font = new Font(new TextureRegion(game.getTexture(FONT_SOURCE)));
		font.setAlignment(Font.ALIGNMENT_CENTER);
		
		// If the buttons have not been initialized,
		if (!buttonsInitialized())
			initializeButtons();
	}
	
	@Override
	public void updateApplication(Application app) {
		restartButton.updateWith(app.input);
		homeButton.updateWith(app.input);
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		level.renderTo(batch);
		
		overlay.renderTo(batch);
		//window.renderTo(batch);
		
		//renderCurrentScoreTo(batch, currentScoreCenter());
		//renderHighestScoreTo(batch, highestScoreCenter());
		
		Vector2 window = game.screenCenter();
		
		fontB.setScale(2);
		fontB.draw(batch, "Game Over", new Vector2(window.x, window.y + 200));
		fontB.setScale(.5f);
		
		fontB.draw(batch, "You survived to wave " + currentScore(), new Vector2(window.x, window.y + 70));
		fontB.draw(batch, "Your highest wave is " + highestScore(), new Vector2(window.x, window.y));
		fontB.draw(batch, "You earned " + level.coinsEarned(), new Vector2(window.x, window.y - 100));
		fontB.draw(batch, "gold coins!", new Vector2(window.x, window.y - 150));
		
		homeButton.renderTo(batch);
		restartButton.renderTo(batch);
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting GameOverState.");
		game.playBackgroundMusic(true);
	}
	
	public abstract int currentScore();
	public abstract int highestScore();
	
	/* Render the high-score to the SpriteBatch with the GameOverScreen's Font. */
	protected void renderHighestScoreTo(SpriteBatch batch, Vector2 renderingPosition) {
		font.renderIntegerTo(batch, highestScore(), (int)renderingPosition.x, (int)renderingPosition.y);
	}
	
	/* Render the high-score to the SpriteBatch with the GameOverScreen's Font. */
	protected void renderCurrentScoreTo(SpriteBatch batch, Vector2 renderingPosition) {
		font.renderIntegerTo(batch, currentScore(), (int)renderingPosition.x, (int)renderingPosition.y);
	}
	
	/* @return true if the GameOverScreen's Buttons have been initialized. */
	protected final boolean buttonsInitialized() {
		return (restartButton != null && homeButton != null);
	}
	
	/* Initialize the Buttons. */
	protected final void initializeButtons() {
		final Vector2 SCREEN_CENTER = game.screenCenter();
		
		// Initialize the Buttons at the center of the screen.
		homeButton = new HomeButton(game, SCREEN_CENTER);
		restartButton = new RestartButton(game, SCREEN_CENTER) {
			@Override
			public void resetLevel() {
				level.restart();
			}
			@Override
			public void resumeLevel() {
				game.setState(level);
			}
		};
		
		// Translate the Buttons' bottom edges to reside on the middle of the bottom of the screen.
		homeButton.setPosition((int)SCREEN_CENTER.x, homeButton.height() / 2);
		restartButton.setPosition((int)SCREEN_CENTER.x, restartButton.height() / 2);
		
		// Translate the Buttons vertically by the desired bottom margin.
		homeButton.translate(0, BUTTON_BOTTOM_MARGIN);
		restartButton.translate(0, BUTTON_BOTTOM_MARGIN);
		
		// Separate the Buttons.
		homeButton.translate(homeButton.width() / 2, 0);
		restartButton.translate(- restartButton.width() / 2, 0);
		
		// Translate the Buttons away from each other to achieve the desired spacing.
		homeButton.translate(BUTTON_SPACING / 2, 0);
		restartButton.translate(- BUTTON_SPACING / 2, 0);
	}
}