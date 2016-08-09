package com.saucy.game.input;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.Graphic;
import com.saucy.framework.rendering.Renderable;
import com.saucy.framework.util.Updatable;
import com.saucy.game.Assets;
import com.saucy.game.map.Position;

public class HardTouch implements Updatable, Renderable, Position {
	
	public enum Action {
		NONE,
		BUILD_TURRET,
		BUILD_WALL
	}
	
	private static final float
		ICON_DIST = 55f,
		TWEEN_SPEED = .25f,
		SELECTION_DELAY = 300f,
		SELECTION_DISTANCE = 30f,
		DEFAULT_SCALE = 3f,
		EXPANDED_SCALE = 5f;
	
	private final Graphic turretIcon, wallIcon;
	private final long touchTime;
	private final float x, y;
	private Action selected = Action.NONE;
	
	public HardTouch(Assets assets, float x, float y) {
		this.x = x; this.y = y;
		this.turretIcon = new Graphic(new Vector2(x, y), assets.get("assets/GUI/Icons/Build.png"));
		turretIcon.setScale(DEFAULT_SCALE, DEFAULT_SCALE);
		this.wallIcon = new Graphic(new Vector2(x, y), assets.get("assets/GUI/Icons/Wall.png"));
		wallIcon.setScale(DEFAULT_SCALE, DEFAULT_SCALE);
		this.touchTime = System.currentTimeMillis();
	}

	@Override
	public void updateWith(InputProxy input) {
		translate(turretIcon, (float)Math.PI / 15f);
		translate(wallIcon, ((float)Math.PI / 2f));
		
		if (isSelected(turretIcon, input)) {
			selected = Action.BUILD_TURRET;
			turretIcon.setScale(EXPANDED_SCALE, EXPANDED_SCALE);
		}	else if (isSelected(wallIcon, input)) {
			selected = Action.BUILD_WALL;
			wallIcon.setScale(EXPANDED_SCALE, EXPANDED_SCALE);
		} else {
			selected = Action.NONE;
			turretIcon.setScale(DEFAULT_SCALE, DEFAULT_SCALE);
			wallIcon.setScale(DEFAULT_SCALE, DEFAULT_SCALE);
		}
	}

	@Override
	public void renderTo(SpriteBatch batch) {
		turretIcon.renderTo(batch);
		wallIcon.renderTo(batch);
	}
	
	@Override
	public float x() {
		return x;
	}

	@Override
	public float y() {
		return y;
	}
	
	/** Return the action selected by the User from the Radial menu. */
	public Action selectedAction() {
		return selected;
	}
	
	private void translate(Graphic g, float angle) {
		final float
			targetX = x + (float)Math.cos(angle) * ICON_DIST,
			targetY = y + (float)Math.sin(angle) * ICON_DIST;
		g.translate((targetX - g.x()) * TWEEN_SPEED, (targetY - g.y()) * TWEEN_SPEED);
	}
	
	private float distanceBetween(InputProxy input, float x, float y) {
		final float
			dx = input.getX() - x,
			dy = input.getY() - y;
		return (float)Math.sqrt(dx*dx + dy*dy);
	}
	
	private boolean isSelected(Graphic g, InputProxy input) {
		return distanceBetween(input, g.x(), g.y()) < SELECTION_DISTANCE && (System.currentTimeMillis() - touchTime) > SELECTION_DELAY;
	}
}