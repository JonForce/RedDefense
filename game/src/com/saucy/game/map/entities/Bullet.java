package com.saucy.game.map.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;
import com.saucy.game.map.entities.enemies.Enemy;

public class Bullet extends Entity {
	
	private Texture texture;
	private ArrayList<Enemy> enemiesShot = new ArrayList<Enemy>();
	protected float dx, dy, x, y, radius, rotation, speed, damping = 1f;
	private long
		spawnTime,
		lifeTime;
	public final float damage;
	public int pierce;
	public final String type;
	
	public Bullet(Assets assets, BulletProperties properties) {
		this.radius = properties.radius;
		this.type = properties.type;
		this.lifeTime = properties.lifeTime;
		this.pierce = properties.pierce;
		this.damage = properties.damage;
		this.texture = assets.get(properties.source);
		this.speed = properties.speed;
		this.spawnTime = System.currentTimeMillis();
		this.damping = properties.damping;
		if (damping != 1f)
			damping += (System.currentTimeMillis() % 100)/10000f;
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		super.centerRender(batch, texture, view);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		x += dx;
		y += dy;
		dx *= damping;
		dy *= damping;
	}
	
	public void attemptCollideWith(Enemy e) {
		if (e.distanceTo(this) < radius && !enemiesShot.contains(e)) {
			if (pierce > 0)
				pierce --;
			else
				// Remove asap.
				lifeTime = 0;
			e.getShotBy(this);
			enemiesShot.add(e);
		}
	}
	
	/** Set the position, target, and the speed of the bullet. */
	public void set(float fromX, float fromY, float toX, float toY) {
		x = fromX;
		y = fromY;
		final float
			deltaX = toX - fromX,
			deltaY = toY - fromY,
			dist = (float)Math.sqrt(deltaX * deltaX + deltaY * deltaY);
		dx = (deltaX / dist) * speed;
		dy = (deltaY / dist) * speed;
		if (deltaY > 0)
			rotation = (float)Math.toDegrees(Math.acos(deltaX / dist));
		else
			rotation = 180 - (float)Math.toDegrees(Math.acos(deltaX / dist));
	}
	
	/** Set the position, target, and the speed of the bullet. Rotate the target
	 * a specified number of radians around fromX, fromy */
	public void set(float fromX, float fromY, float toX, float toY, float rotateBy) {
		final float
			dx = toX - fromX,
			dy = toY - fromY,
			magnitude = (float)Math.sqrt(dx * dx + dy * dy),
			signX = (dx > 0)? 1 : -1,
			signY = (dy > 0)? 1 : -1,
			currentAngle = (float) Math.atan(dy / dx),
			newAngle = currentAngle + rotateBy;
		toX = fromX + (float)Math.cos(newAngle) * magnitude * signX * ((dx < 0 && dy > 0)? 1 : 1);
		toY = fromY + (float)Math.sin(newAngle) * magnitude * signY * ((dx > 0 && dy < 0)? -1 : 1) * ((dx < 0 && dy > 0)? -1 : 1);
		set(fromX, fromY, toX, toY);
	}
	
	@Override
	public boolean shouldRemove() {
		return
				System.currentTimeMillis() - spawnTime > lifeTime ||
				outOfBounds();
	}
	
	@Override
	public float rotation() {
		return rotation;
	}
	
	@Override
	public float x() {
		return x;
	}
	
	@Override
	public float y() {
		return y;
	}
	
	public boolean outOfBounds() {
		return x > 700 || x < -30 || y > 700 || y < -30;
	}
}