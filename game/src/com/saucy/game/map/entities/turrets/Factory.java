package com.saucy.game.map.entities.turrets;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;
import com.saucy.game.map.ViewController;
import com.saucy.game.map.entities.BulletProperties;
import com.saucy.game.map.entities.TempSprite;
import com.saucy.game.map.entities.enemies.Enemy;

public abstract class Factory extends Turret {
	
	protected abstract void addTempSprite(TempSprite t);
	protected abstract void addMoney(int amount);
	 
	private Texture moneyTexture;
	private ViewController vc;

	public Factory(Assets assets, ViewController vc, TurretProperties properties) {
		super(assets, properties);
		this.vc = vc;
		moneyTexture = assets.get("assets/MoneySymbol.png");
	}
	
	@Override
	public void fire(float fromX, float fromY, float toX, float toY, Position target, float angle, BulletProperties properties) {
		throw new RuntimeException("Factories do not shoot!");
	}
	
	@Override
	public void updateWith(ArrayList<Enemy> enemies) {
		if (System.currentTimeMillis() - lastShotTime > properties.firePeriod) {
			printMoney();
			lastShotTime = System.currentTimeMillis();
		}
	}
	
	@Override
	public void playSound(String filename) { }
	
	protected void printMoney() {
		System.out.println("PRINTING MONIEZ");
		addMoney(properties.bullets);
		TempSprite money = new TempSprite(screenX(), screenY() + height(), moneyTexture, vc, 750) {
			@Override
			public void updateWith(InputProxy input) {
				super.updateWith(input);
			}
		};
		money.setScale(2, 2);
		addTempSprite(money);
	}
}