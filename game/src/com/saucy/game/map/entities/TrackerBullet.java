package com.saucy.game.map.entities;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;
import com.saucy.game.map.entities.enemies.Enemy;

public class TrackerBullet extends Bullet {

	private Enemy target;
	
	public TrackerBullet(Assets assets, BulletProperties properties, Enemy target) {
		super(assets, properties);
		this.target = target;
	}
	
	@Override
	public void updateWith(InputProxy input) {
		Vector2 delta = new Vector2(target.x() - x, target.y() - y).nor();
		if (!target.shouldRemove()) {
			x += delta.x * speed;
			y += delta.y * speed;
		}
			x += dx;
			y += dy;
			dx *= damping;
			dy *= damping;
	}
	
}