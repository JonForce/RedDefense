package com.saucy.game.map.entities;

import com.badlogic.gdx.graphics.Texture;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.Graphic;
import com.saucy.framework.util.Updatable;
import com.saucy.game.map.ViewController;

public class TempSprite extends Graphic implements Updatable {

	private long startTime, lifespan;
	public boolean shouldRemove = false;
	private ViewController vc;
	
	public TempSprite(float x, float y, Texture texture, ViewController vc, long lifespan) {
		super(x, y, texture);
		this.vc = vc;
		this.startTime = System.currentTimeMillis();
		this.lifespan = lifespan;
	}
	
	@Override
	public float x() {
		return super.position.x - vc.x();
	}
	
	@Override
	public float y() {
		return super.position.y - vc.y();
	}

	@Override
	public void updateWith(InputProxy input) {
		if (System.currentTimeMillis() - startTime > lifespan)
			shouldRemove = true;
	}
	
}