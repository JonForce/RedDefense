package com.saucy.game.map.entities.turrets;

public class TurretAssets {
	
	public TurretAssets(TurretAssets turretAssets) {
		bottomSource = turretAssets.bottomSource;
		topSource = turretAssets.topSource;
	}
	public TurretAssets() { }
	public String bottomSource;
	public String topSource;
	
}