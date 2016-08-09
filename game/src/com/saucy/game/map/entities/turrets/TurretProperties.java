package com.saucy.game.map.entities.turrets;

import com.saucy.game.map.entities.BulletProperties;

public class TurretProperties {
	
	public TurretProperties(TurretProperties properties) {
		this.range = properties.range;
		this.inaccuracy = properties.inaccuracy;
		this.firePeriod = properties.firePeriod;
		this.bullets = properties.bullets;
		this.cost = properties.cost;
		this.bulletProperties = new BulletProperties(properties.bulletProperties);
		this.turretAssets = new TurretAssets(properties.turretAssets);
		this.upgrades = properties.upgrades.clone();
		this.type = properties.type;
		this.dragonsBreath = properties.dragonsBreath;
		this.shotNoise = properties.shotNoise;
		this.shopCost = properties.shopCost;
		this.ID = properties.ID;
		this.shopDescription = properties.shopDescription;
	}
	
	public TurretProperties() {  }
	
	public String type = "Basic", shotNoise = "assets/Sounds/gunshot.wav";
	public float
		range = 70,
		inaccuracy = .13f;
	public long firePeriod = 200L;
	public int bullets = 1, ID = -1, dragonsBreath = 0;
	public int cost = 500;
	public int shopCost = 0;
	public String shopDescription = "";
	
	public BulletProperties bulletProperties = null;
	public TurretAssets turretAssets = null;
	public String[][] upgrades = { {}, {} };
	
	public BulletProperties dragonsBreath() {
		BulletProperties properties = new BulletProperties();
		properties.source = "assets/FlameAmmo.png";
		properties.damage = 1;
		properties.pierce = 0;
		properties.damage = 1;
		properties.damping = .95f;
		properties.speed = 4;
		properties.lifeTime = 1000;
		return properties;
	}
}