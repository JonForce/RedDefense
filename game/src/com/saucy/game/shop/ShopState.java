package com.saucy.game.shop;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.User;
import com.saucy.game.map.entities.turrets.TurretProperties;

public abstract class ShopState implements ApplicationState, InputProcessor {
	
	private final Game game;
	private ShopFont font;
	private ArrayList<TurretProperties> properties;
	private ArrayList<Texture> textures;
	private ArrayList<Button> buttons;
	private Button exitButton;
	private Texture overlay;
	
	private float scroll = 0, scrollSpeed = 20f;
	private final float
		INIT_MARGIN_Y = 400,
		MARGIN_Y = 450,
		SCALE = 10;
	
	protected abstract int coins();
	protected abstract boolean trySpendCoins(int coins);
	protected abstract void drawBackgroundTo(SpriteBatch batch);
	
	public ShopState(final Game game) {
		this.game = game;
		this.font = new ShopFont();
		font.setScale(.5f);
		this.overlay = game.getTexture("assets/Windows/Overlay.png");
		
		Texture
			buttonPressed = game.getTexture("assets/GUI/Shop/ButtonPressed.png"),
			buttonReleased = game.getTexture("assets/GUI/Shop/Button.png");
		this.properties = new ArrayList<TurretProperties>();
		this.textures = new ArrayList<Texture>();
		this.buttons = new ArrayList<Button>();
		
		Json json = new Json();
		for (final FileHandle f : Gdx.files.internal("assets/Tower/JSON/").list()) {
			if (!f.isDirectory()) {
				final TurretProperties p = json.fromJson(TurretProperties.class, f);
				if (!game.user().ownsTurret(p.ID)) {
					properties.add(p);
					textures.add(game.getTexture(p.turretAssets.topSource));
					buttons.add(new Button(new Vector2(), buttonReleased, buttonPressed) {
						@Override
						public void onRelease() {
							tryBuy(p, p.ID, game.user());
						}
					});
				}
			}
		}
		
		exitButton = new Button(new Vector2(), buttonReleased, buttonPressed) {
			@Override
			public void onRelease() {
				game.returnToMainMenu();
			}
		};
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		drawBackgroundTo(batch);
		batch.draw(overlay, 0, 0, game.screenWidth(), game.screenHeight());
		
		// Draw Title
		font.setScale(2f);
		font.draw(batch, "Shop", new Vector2(game.screenWidth() / 2f, game.screenHeight() - 140 + scroll));
		font.setScale(.5f);
		
		// Draw everything else
		for (int i = 0; i != properties.size(); i ++) {
			Texture t = textures.get(i);
			final float
				IMAGE_WIDTH = t.getWidth() * SCALE,
				IMAGE_HEIGHT = t.getHeight() * SCALE,
				IMAGE_X = game.screenWidth()/2 - IMAGE_WIDTH/2,
				IMAGE_Y = game.screenHeight() - INIT_MARGIN_Y - MARGIN_Y * i;
			// Draw Image
			batch.draw(t, IMAGE_X, IMAGE_Y + scroll, IMAGE_WIDTH, IMAGE_HEIGHT);
			
			// Draw the text for the current thingy for sale.
			String[] lines = properties.get(i).shopDescription.split("\n");
			for (int l = 0; l != lines.length; l ++)
				font.draw(batch, lines[l], new Vector2(IMAGE_X + IMAGE_WIDTH/2, scroll + IMAGE_Y - MARGIN_Y/6 - l * (font.font.getCapHeight() * 1.5f)));
			
			// Draw the buy button for the current thingy for sale.
			Vector2 buttonCenter = new Vector2(IMAGE_X + IMAGE_WIDTH + buttons.get(i).width() / 2 + 50, IMAGE_Y + scroll + IMAGE_HEIGHT/2);
			buttons.get(i).setPosition(buttonCenter);
			buttons.get(i).renderTo(batch);
			font.draw(batch, "Buy for " + properties.get(i).shopCost, buttonCenter);
		}
		
		// Draw the text that indicates how many coins the user has
		font.draw(batch, "Coins : " + coins(), new Vector2(150, 50));
		
		Vector2 exitCenter = new Vector2(exitButton.width() / 2 + 5, game.screenHeight() - exitButton.height() / 2 - 5);
		exitButton.setPosition(exitCenter);
		exitButton.renderTo(batch);
		font.draw(batch, "Main Menu", exitCenter);
	}
	
	protected void tryBuy(TurretProperties turret, int turretID, User user) {
		if (trySpendCoins(turret.shopCost)) {
			user.buyTurret(turretID);
			user.save();
		}
	}
	
	@Override
	public void enterState() {
		Gdx.input.setInputProcessor(this);
	}
	
	@Override
	public void updateApplication(Application app) {
		// Squirreling!! YAS
		if (scroll < 0) scroll /= 1.25;
		
		if (game.USE_TOUCH_CONTROLS && app.input.isTouched())
			scroll += app.input.getDeltaY();
		
		for (Button b : buttons)
			b.updateWith(app.input);
		exitButton.updateWith(app.input);
	}

	@Override
	public void exitState() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer,
			int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		scroll += amount*scrollSpeed;
		return false;
	}
}