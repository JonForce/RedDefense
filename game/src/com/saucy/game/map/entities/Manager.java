package com.saucy.game.map.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.Updatable;
import com.saucy.game.ViewRenderable;
import com.saucy.game.map.Position;

public class Manager<Type extends Entity> implements Updatable, ViewRenderable {
	
	protected final ArrayList<Type> list = new ArrayList<Type>();
	
	public final void add(Type e) { list.add(e); }
	public final void remove(Type e) { list.remove(e); }
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		for (Entity e : list)
			e.renderTo(batch, view);
	}
	@Override
	public void updateWith(InputProxy input) {
		for (int i = list.size() - 1; i >= 0; i --) {
			Entity e = list.get(i);
			if (e.shouldRemove())
				list.remove(i);
			else
				e.updateWith(input);
		}
	}
	
	public ArrayList<Type> list() { return list; }
	
}