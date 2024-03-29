package com.saucy.game.map.entities.turrets;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;
import com.saucy.game.map.Tile;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.entities.BulletProperties;
import com.saucy.game.map.entities.Entity;
import com.saucy.game.map.entities.enemies.Enemy;

public abstract class Turret extends Entity {
	
	private static final long ENEMY_SEARCH_PERIOD = 600;
	
	protected final TurretProperties properties;
	private Texture bottom, top;
	
	private Index position;
	protected Enemy target;
	protected Position superTarget;
	protected long lastShotTime, lastEnemySearchTime;
	private float
		rotation,
		tileWidth = Tile.WIDTH,
		tileHeight = Tile.HEIGHT;
	
	private int
		pathADist = 0, pathBDist = 0;
	private final int
		pathAMax, pathBMax;
	
	public abstract void fire(float fromX, float fromY, float toX, float toY, Position target, float angle, BulletProperties properties);
	public abstract float random(float max);
	public abstract void playSound(String file);
	
	public Turret(Assets assets, TurretProperties properties) {
		this.properties = new TurretProperties(properties);
		this.bottom = assets.get(properties.turretAssets.bottomSource);
		this.top = assets.get(properties.turretAssets.topSource);
		this.position = Index.of(0, 0);
		this.pathAMax = properties.upgrades[0].length - 1;
		this.pathBMax = properties.upgrades[1].length - 1;
	}
	
	/** Set the turret's position in grid coordinates. */
	public void setPosition(int gridX, int gridY) {
		position = Index.of(gridX, gridY);
	}
	
	/** Set the width and height of the tiles so the Turret knows how to translate
	 * grid coordinates to screen coordinates. */
	public void setTileSize(float width, float height) {
		tileWidth = width;
		tileHeight = height;
	}
	
	/** Force the turret to shoot at supertarget until disable superTarget is called. */
	public void enableSuperTarget(Position superTarget) {
		this.superTarget = superTarget;
	}
	/** Stop shooting at the superTarget. */
	public void disableSuperTarget() {
		this.superTarget = null;
	}
	
	/** Reload the textures. */
	public void reloadTextures(Assets assets) {
		this.bottom = assets.get(properties.turretAssets.bottomSource);
		this.top = assets.get(properties.turretAssets.topSource);
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		super.centerRender(batch, bottom, view, 0);
		super.centerRender(batch, top, view, rotation);
	}
	
	@Override
	public void updateWith(InputProxy input) {}
	
	public void updateWith(ArrayList<Enemy> enemies) {
		boolean foundTarget = false;
		int foundLength = 99999999;
		if (superTarget == null && (target == null || System.currentTimeMillis() - lastEnemySearchTime > ENEMY_SEARCH_PERIOD)) {
			lastEnemySearchTime = System.currentTimeMillis();
			for (Enemy e : enemies) {
				if (e.distanceTo(this) < properties.range && superTarget == null) {
					if (e.pathLength() < foundLength) {
						target = e;
						foundLength = e.pathLength();
					}
					foundTarget = true;
				}
			}
		}
		if (!foundTarget)
			target = null;
		if ((target != null || superTarget != null) && canRotate())
			pointBarrelAt((superTarget != null)? superTarget : target);
		
		if (System.currentTimeMillis() - lastShotTime > properties.firePeriod && (target != null || superTarget != null)) {
			Position target = (superTarget == null)? Turret.this.target : superTarget;
			final float 
				dx = target.x() - x(),
				dy = target.y() - y(),
				offset = (dx == 0)? .1f : 0,
				barrelRotation = (float)Math.PI / 2 - (float)Math.toRadians(rotation),
				signX = (dx > 0)? 1 : -1,
				signY = (dy > 0)? 1 : -1,
				barrelEndX = x() + (float)Math.abs(Math.cos(barrelRotation) * 10) * signX,
				barrelEndY = y() + (float)Math.abs(Math.sin(barrelRotation) * 10) * signY;
			for (int i = 0; i != this.properties.bullets; i ++) {
				float angle = random(properties.inaccuracy);
				fire(barrelEndX, barrelEndY, target.x() + offset, target.y(), target, angle, properties.bulletProperties);
				//playSound(properties.shotNoise);
				for (int x = 0; x != properties.dragonsBreath; x ++)
					fire(barrelEndX, barrelEndY, target.x() + offset, target.y(), target, random(.27f), properties.dragonsBreath());
			}
			lastShotTime = System.currentTimeMillis();
		}
	}
	
	@Override
	public float rotation() {
		return rotation;
	}
	
	@Override
	public float x() { return position.x * tileWidth + tileWidth / 2f; }
	@Override
	public float y() { return position.y * tileHeight + tileHeight / 2f; }
	
	public boolean pathAClosed() { return pathADist > pathAMax; }
	public boolean pathBClosed() { return pathBDist > pathBMax; }
	public int pathADist() { return this.pathADist; }
	public int pathBDist() { return this.pathBDist; }
	public void incrementPathA() { if (!pathAClosed()) pathADist++; }
	public void incrementPathB() { if (!pathBClosed()) pathBDist++; }
	
	public float width() { return Math.max(bottom.getWidth(), top.getWidth()); }
	public float height() { return Math.max(bottom.getHeight(), top.getHeight()); }
	public float screenWidth() { return width() * TileRenderer.SCALE; }
	public float screenHeight() { return height() * TileRenderer.SCALE; }
	
	/** @return the properties of this Turret. Modifying these properties will have result in undefined behavior. */
	public TurretProperties properties() { return this.properties; }
	
	protected boolean canRotate() { return true; }
	
	private void pointBarrelAt(Position p) {
		if (p.y() - y() > 0)
			rotation = (float)Math.toDegrees(Math.acos((p.x() - x()) / this.distanceTo(p))) - 90;
		else
			rotation = 270 - (float)Math.toDegrees(Math.acos((p.x() - x()) / this.distanceTo(p)));
	}
}