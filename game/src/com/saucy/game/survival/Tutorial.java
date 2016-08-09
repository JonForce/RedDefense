package com.saucy.game.survival;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.Renderable;
import com.saucy.framework.util.Updatable;
import com.saucy.game.Game;
import com.saucy.game.gui.PopupText;

public abstract class Tutorial implements Renderable, Updatable {
	
	private PopupText
		currentInstructions,
		moveInstructions,
		openShopInstructions,
		buildTurretInstructions,
		closeShopInstructions,
		upgradeTurretInstructions,
		buyUpgradeInstructions,
		focusFireInstructions;
	
	private int	previousCoins;
	
	protected abstract boolean hasMovedView();
	protected abstract int coins();
	protected abstract boolean shopOpen();
	protected abstract boolean upgradeMenuOpen();
	protected abstract boolean isFocusFiring();
	
	public Tutorial(Game game) {
		previousCoins = coins();
		
		String actionWord = game.USE_TOUCH_CONTROLS? "Touch" : "Click";
		
		this.moveInstructions = new PopupText(game.USE_TOUCH_CONTROLS? "Touch with two fingers\nand drag to move" : "Use WASD to move", game.screenCenter());
		this.openShopInstructions = new PopupText(actionWord + " the box in the bottom\nleft corner of the screen\nto open the shop.", game.screenCenter().add(0, 100));
		this.buildTurretInstructions = new PopupText("Drag a turret onto\nthe map to build it.", game.screenCenter());
		this.closeShopInstructions = new PopupText(actionWord + " the red box in the\ntop right corner to close\nthe shop.", game.screenCenter().add(0, 100));
		this.upgradeTurretInstructions = new PopupText(actionWord + " the turret to open\nits upgrade menu.", game.screenCenter());
		this.buyUpgradeInstructions = new PopupText(actionWord + " an upgrade to buy it.", game.screenCenter());
		this.focusFireInstructions = new PopupText(
				(game.USE_TOUCH_CONTROLS)?
						"Double tap and hold\nto control\nyour turrets manually." :
						"Right click and\nhold to control\nyour turrets manually.",
				game.screenCenter());
		
		currentInstructions = moveInstructions;
	}
	
	@Override
	public void updateWith(InputProxy input) {
		if (currentInstructions == moveInstructions) {
			if (hasMovedView()) {
				currentInstructions = openShopInstructions;
			}
		} else if (currentInstructions == openShopInstructions) {
			if (shopOpen()) {
				currentInstructions = buildTurretInstructions;
			}
		} else if (currentInstructions == buildTurretInstructions) {
			if (coins() != previousCoins) {
				currentInstructions = closeShopInstructions;
			}
		} else if (currentInstructions == closeShopInstructions) {
			if (!shopOpen()) {
				currentInstructions = upgradeTurretInstructions;
			}
		} else if (currentInstructions == upgradeTurretInstructions) {
			if (upgradeMenuOpen()) {
				currentInstructions = buyUpgradeInstructions;
			}
		} else if (currentInstructions == buyUpgradeInstructions) {
			if (coins() != previousCoins) {
				currentInstructions = focusFireInstructions;
			}
		} else if (currentInstructions == focusFireInstructions) {
			if (isFocusFiring()) {
				currentInstructions = null;
			}
		}
		
		previousCoins = coins();
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		if (currentInstructions != null)
			currentInstructions.renderTo(batch);
	}
	
	/** @return true when the tutorial is done. */
	public boolean finished() {
		return currentInstructions == null;
	}
}