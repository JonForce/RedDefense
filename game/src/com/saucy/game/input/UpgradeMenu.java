package com.saucy.game.input;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Json;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.Updatable;
import com.saucy.game.Assets;
import com.saucy.game.ViewRenderable;
import com.saucy.game.gui.Font;
import com.saucy.game.map.Position;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.entities.turrets.Turret;
import com.saucy.game.map.entities.turrets.TurretUpgrade;

public abstract class UpgradeMenu implements ViewRenderable, Updatable {
	
	private Position view;
	private TurretUpgrade[][] upgrades;
	private Assets assets;
	private Turret turret;
	private Texture window, pathClosed;
	private Font font;
	private boolean released = true, firstTouch = true;
	
	public abstract void closeMenu();
	protected abstract boolean trySpendCoins(int coins);
	
	public UpgradeMenu(HashMap<String, TurretUpgrade> map, Turret turret, Assets assets, Position view) {
		this.upgrades = new TurretUpgrade[turret.properties().upgrades.length][turret.properties().upgrades[0].length];
		this.view = view;
		this.assets = assets;
		this.turret = turret;
		this.window = assets.get("assets/Tower/UpgradeWindow.png");
		this.pathClosed = assets.get("assets/Tower/PathClosed.png");
		this.font = new Font(new TextureRegion(assets.get("assets/GUI/Font/Digits2.png", Texture.class)));
		
		Json json = new Json();
		int r = 0, c = 0;
		for (String[] upgradePathSource : turret.properties().upgrades) {
			for (String upgradePath : upgradePathSource) {
				upgrades[r][c] = json.fromJson(TurretUpgrade.class, Gdx.files.internal("assets/Tower/JSON/Upgrades/" + upgradePath + ".json"));
				upgrades[r][c].texture = assets.get(upgrades[r][c].image);
				c ++;
			}
			c = 0;
			r ++;
		}
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position position) {
		final float
			x = x(view),
			y = y(view),
			horizontalMargin = (window.getWidth()/2f - pathClosed.getWidth()) / 2f,
			bottomMargin = 90;
		batch.draw(window, x, y);
		if (turret.pathAClosed())
			batch.draw(pathClosed, x + horizontalMargin, y + bottomMargin);
		else
			batch.draw(upgradeA().texture, x + horizontalMargin, y + bottomMargin);
		
		if (turret.pathBClosed())
			batch.draw(pathClosed, x + horizontalMargin*2 + pathClosed .getWidth(), y + bottomMargin);
		else
			batch.draw(upgradeB().texture, x + horizontalMargin*2 + upgradeB().texture.getWidth(), y + bottomMargin);
		if (!turret.pathAClosed())
			font.renderIntegerTo(batch, upgradeA().cost, x, y + 10);
		if (!turret.pathBClosed())
			font.renderIntegerTo(batch, upgradeB().cost, x + upgradeB().texture.getWidth() - 5, y + 10);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		if (input.isTouched() && released) {
			if (!turret.pathAClosed() && checkInputOn(0, .5f, input) && trySpendCoins(upgradeA().cost)) {
				upgradeA().applyTo(turret, assets);
				turret.incrementPathA();
			} else if (!turret.pathBClosed() && checkInputOn(.5f, .5f, input) && trySpendCoins(upgradeB().cost)) {
				upgradeB().applyTo(turret, assets);
				turret.incrementPathB();
			}
			released = false;
		}
		if (!input.isTouched() && !released) {
			if (!checkInputOn(0, 1, input) && !firstTouch)
				closeMenu();
			else {
				firstTouch = false;
				released = true;
			}
		}
	}
	
	public final float x(Position position) {
		return turret.screenX() - window.getWidth()/2f - position.x();
	}
	public final float y(Position position) {
		return turret.screenY() + turret.height()/2f + TileRenderer.RENDER_HEIGHT/2f - position.y();
	}
	
	private TurretUpgrade upgradeA() { return upgrades[0][turret.pathADist()]; }
	private TurretUpgrade upgradeB() { return upgrades[1][turret.pathBDist()]; }
	
	private boolean checkInputOn(float startNormal, float widthNormal, InputProxy input) {
		return 	input.getX() > x(view) + startNormal * window.getWidth() &&
				input.getX() < x(view) + startNormal * window.getWidth() + widthNormal * window.getWidth() &&
				input.getY() > y(view) &&
				input.getY() < y(view) + window.getHeight();
	}
}