package com.saucy.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.control.Application;
import com.saucy.framework.rendering.Graphic;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.effects.Animator;
import com.saucy.game.gui.MainMenuBG;
import com.saucy.game.gui.buttons.ShopButton;
import com.saucy.game.gui.buttons.SpinningButton;
import com.saucy.game.gui.buttons.StartButton;
import com.saucy.game.Direction;

public abstract class MainMenuState extends MenuState {
	final float SLIDE_DURATION = 500;
	
	final String TITLE_SOURCE = "assets/GUI/MainMenu/Logo.png";
	
	public final float
		START_BUTTON_SCALE = 1f;
	
	public final int
		START_BUTTON_BOT_MARGIN = 150,
		TITLE_TOP_MARGIN = 100;

	protected SpinningButton startButton, shopButton, exitButton;

	protected Graphic title;

	private MainMenuBG background;
	
	public MainMenuState(Game game, int width, int height) {
		super(game);
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering MainMenuState.");
		
		initialize();
		super.enterState();
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting MainMenuState.");
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		background.renderTo(batch);
		
		startButton.renderTo(batch);
		shopButton.renderTo(batch);
//		exitButton.renderTo(batch);
		
		title.renderTo(batch);
	}
	
	@Override
	public void updateApplication(Application app) {
		startButton.updateWith(app.input);
		shopButton.updateWith(app.input);
//		exitButton.updateWith(app.input);
	}
	
	protected abstract void exitMainMenu();
	
	/** Initialize the MainMenu's components. */
	protected void initialize() {
		game.playBackgroundMusic(true); // True because the background Music should play looped.
		background = new MainMenuBG(game);
		initializeTitle();
		initializeStartButton();
		initializeShopButton();
	}
	
	public final MainMenuBG getBG() {
		return this.background;
	}
	
	/** @return the Texture of the title logo. */
	public final Texture titleTexture() {
		return game.getTexture(TITLE_SOURCE);
	}
	
	/** @return the top center of the MainMenu. */
	protected final Vector2 menuTopCenter() {
		return new Vector2(menuCenter.x, menuSize.y);
	}
	
	/** Create and position the MainMenu's title. */
	private void initializeTitle() {
		// Create the title graphic at its position with the title's texture.
		title = new Graphic(new Vector2(), titleTexture());
		title.scale(1.5f);
		title.setPosition(initialTitlePosition());
		
		new Animator(game).tweenGraphicTo(title, 
				menuTopCenter().sub(new Vector2(0,title.height()/2+TITLE_TOP_MARGIN)), SLIDE_DURATION);
	}
	
	/** Create and position the MainMenu's StartButton. */
	private void initializeStartButton() {
		// Create the StartButton at the position of the Title with the specified scale.
		startButton = new StartButton(game, Vector2.Zero, START_BUTTON_SCALE) {
			@Override
			public void onTrigger() {
				exitMainMenu();
			}
			@Override
			public void onPress() {
				new Animator(game)
					.slideGraphicOffscreen(SLIDE_DURATION, Direction.LEFT, title);
			}
		};
		startButton.setPosition(initialStartButtonPosition().x, initialStartButtonPosition().y);
	}
	
	private void initializeShopButton() {
		shopButton = new ShopButton(game, Vector2.Zero, "assets/GUI/Icons/Shop.png");
		// Scale the ShopButton down.
		shopButton.scale(5f);
		Vector2 p = initialShopButtonPosition();
		shopButton.setPosition(p.x, p.y);
		
		
//		exitButton = new ShopButton(game, Vector2.Zero, "assets/GUI/Icons/Shop.png"){
//			@Override
//			public void onTrigger() {
//				Gdx.app.exit();
//			}
//		};
//		// Scale the ShopButton down.
//		exitButton.scale(0.7f);
//		p = initialExitButtonPosition();
//		exitButton.setPosition(p.x, p.y);
	}
	
	private Vector2 initialTitlePosition() {
		return new Vector2(game.screenWidth()+title.width(),game.screenHeight()).add(0,-title.height()/2-TITLE_TOP_MARGIN);
	}
	
	private Vector2 initialStartButtonPosition() {
		return new Vector2(game.screenWidth()/2 - startButton.width()/2-50, startButton.height()/2+START_BUTTON_BOT_MARGIN);
	}
	
	private Vector2 initialShopButtonPosition() {
		return new Vector2(game.screenWidth()/2 + shopButton.width()/2+50, startButton.height()/2+START_BUTTON_BOT_MARGIN);
	}

}