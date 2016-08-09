package com.saucy.game.map.entities;

public class BulletProperties {
	public BulletProperties(BulletProperties properties) {
		damage = properties.damage;
		speed = properties.speed;
		radius = properties.radius;
		pierce = properties.pierce;
		lifeTime = properties.lifeTime;
		source = properties.source;
		damping = properties.damping;
		type = properties.type;
	}
	public BulletProperties() { }
	public float
		damage = 1f,
		speed = 5f,
		radius = 5f,
		damping = 1f;
	public int pierce = 0;
	public long lifeTime = 5000L;
	public String source = "assets/Bullet.png", type = "Basic";
}