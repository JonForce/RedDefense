package com.saucy.game.input;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.framework.util.Updatable;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.ViewRenderable;
import com.saucy.game.input.HardTouch.Action;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Position;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.ViewController;
import com.saucy.game.map.Wall;
import com.saucy.game.map.entities.turrets.Turret;
import com.saucy.game.map.entities.turrets.TurretProperties;
import com.saucy.game.survival.SurvivalLevel;

public class UserController implements Updatable, ViewRenderable {
	
	private final long doubleTapTime = 100L;
	
	private final SurvivalLevel level;
	private Shop shop;
	private Assets assets;
	private ViewController view;
	private Button openShopButton, closeShopButton;
	private UpgradeMenu upgradeMenu;
	boolean shopOpen = false, focusFiring = false;
	private boolean useTouchControls;
	
	private boolean wasTouched;
	private long lastTouchRelease;
	
	public UserController(final SurvivalLevel level, boolean useTouchControls, ViewController view, Assets assets) {
		this.view = view;
		this.level = level;
		this.useTouchControls = useTouchControls;
		this.assets = assets;
		ArrayList<TurretProperties> turretProperties = new ArrayList<TurretProperties>();
		Json json = new Json();
		for (FileHandle f : Gdx.files.internal("assets/Tower/JSON/").list()) {
			if (!f.isDirectory()) {
				TurretProperties p = json.fromJson(TurretProperties.class, f);
				if (level.game().user().ownsTurret(p.ID))
					turretProperties.add(p);
			}
		}
		this.shop = new Shop(assets, turretProperties, level.renderer()) {
			@Override
			protected boolean trySpendCoins(int amount) {
				return level.trySpendCoins(amount);
			}
			@Override
			protected Turret createTurret(TurretProperties properties) {
				return level.createTurret(properties);
			}
			@Override
			protected void addTurret(Turret t) {
				level.manage(t);
			}
			@Override
			protected void addWall(Wall w, Index position) {
				level.addWall(w, position);
			}
			@Override
			protected boolean canBuildTurretAt(int gridX, int gridY) {
				for (Turret t : level.turrets())
					if (t.gridX() == gridX && t.gridY() == gridY)
						return false;
				return true;
			}
			@Override
			protected boolean canBuildWallAt(int gridX, int gridY) {
				return level.canBuildWallAt(gridX, gridY);
			}
		};
		this.openShopButton = new Button(0, 0, assets.get("assets/GUI/InGameShop/OpenShopButtonReleased.png"), assets.get("assets/GUI/InGameShop/OpenShopButtonPressed.png")) {
			@Override
			protected void onRelease() {
				shopOpen = true;
			}
		};
		openShopButton.scale(2f);
		openShopButton.translate(openShopButton.width() / 2f + 5, openShopButton.height() / 2f + 5);
		this.closeShopButton = new Button(0, 0, assets.get("assets/GUI/InGameShop/CloseShopButton.png"), assets.get("assets/GUI/InGameShop/CloseShopButton.png")) {
			@Override
			protected void onRelease() {
				shopOpen = false;
			}
		};
		closeShopButton.scale(1.5f);
		closeShopButton.translate(shop.WINDOW_WIDTH - closeShopButton.width() / 2f - 1, shop.WINDOW_HEIGHT - closeShopButton.height() / 2f - 1);
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		shop.renderPendingWallsTo(batch);
		if (upgradeMenu != null)
			upgradeMenu.renderTo(batch, view);
		if (shopOpen) {
			shop.renderTo(batch, view);
			closeShopButton.renderTo(batch);
		} else
			openShopButton.renderTo(batch);
	}
	
	@Override
	public void updateWith(final InputProxy input) {
		if (useTouchControls) {
			if ((input.isTouched() && System.currentTimeMillis() - lastTouchRelease < doubleTapTime))
				enableSuperTargeting(input);
			if (!input.isTouched())
				disableSuperTargeting();
		} else {
			if (Gdx.input.isButtonPressed(1))
				enableSuperTargeting(input);
			else
				disableSuperTargeting();
		}
		
		shop.updatePendingWalls();
		if (shopOpen) {
			shop.updateWith(input, view);
			closeShopButton.updateWith(input);
		} else {
			openShopButton.updateWith(input);
			
			if (upgradeMenu == null) {
				final float
					x = level.view().x() + input.getX(),
					y = level.view().y() + input.getY();
				for (Turret t : level.turrets()) {
					if (
							Gdx.input.isButtonPressed(0) &&
							x > t.screenX() - t.screenWidth()/2f &&
							x < t.screenX() + t.screenWidth()/2f &&
							y > t.screenY() - t.screenHeight()/2f &&
							y < t.screenY() + t.screenHeight()/2f)
						upgradeMenu = new UpgradeMenu(level.upgrades(), t, assets, level.view()) {
							@Override
							public void closeMenu() {
								upgradeMenu = null;
							}
							@Override
							protected boolean trySpendCoins(int coins) {
								return level.trySpendCoins(coins);
							}
						};
				}
			} else
				upgradeMenu.updateWith(input);
		}
		
		if (!input.isTouched() && wasTouched)
			lastTouchRelease = System.currentTimeMillis();
		wasTouched = input.isTouched();
	}
	
	public boolean shopOpen() {
		return this.shopOpen;
	}
	
	public boolean upgradeMenuOpen() {
		return upgradeMenu != null;
	}
	
	public boolean focusFiring() {
		return focusFiring;
	}
	
	private void enableSuperTargeting(final InputProxy input) {
		focusFiring = true;
		for (Turret t : level.turrets()) {
			Position mousePosition = new Position() {
				@Override
				public float x() {
					return (1f/TileRenderer.SCALE) * (view.x() + input.getX());
				}
				@Override
				public float y() {
					return (1f/TileRenderer.SCALE) * (view.y() + input.getY());
				}
			};
			t.enableSuperTarget(mousePosition);
		}
	}
	
	private void disableSuperTargeting() {
		focusFiring = false;
		for (Turret t : level.turrets()) {
			t.disableSuperTarget();
		}
	}
}