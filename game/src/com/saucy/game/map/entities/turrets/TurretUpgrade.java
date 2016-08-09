package com.saucy.game.map.entities.turrets;

import com.badlogic.gdx.graphics.Texture;
import com.saucy.game.Assets;

public class TurretUpgrade extends TurretProperties {
	
	public String image;
	public Texture texture;
	
	public TurretUpgrade() {
		bullets = 0;
		range = 0;
		inaccuracy = 0;
		firePeriod = 0;
		cost = 0;
	}
	
	public final void applyTo(Turret t, Assets assets) {
		if (super.bulletProperties != null) {
			t.properties().bulletProperties.damage += super.bulletProperties.damage;
			t.properties().bulletProperties.pierce += super.bulletProperties.pierce;
			t.properties().bulletProperties.speed += super.bulletProperties.speed;
			t.properties().bulletProperties.lifeTime += super.bulletProperties.lifeTime;
			if (super.bulletProperties.source != null)
				t.properties().bulletProperties.source = super.bulletProperties.source;
		}
		
		t.properties().dragonsBreath += super.dragonsBreath;
		t.properties().bullets += super.bullets;
		t.properties().cost += super.cost;
		t.properties().firePeriod += super.firePeriod;
		t.properties().inaccuracy += super.inaccuracy;
		t.properties().range += super.range;
		if (super.turretAssets != null) {
			t.properties().turretAssets = super.turretAssets;
			t.reloadTextures(assets);
		}
	}
	
}