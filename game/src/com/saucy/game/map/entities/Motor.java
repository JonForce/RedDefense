package com.saucy.game.map.entities;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.Updatable;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.game.map.Tile;

public class Motor implements Updatable {
	
	private Vector2
		nodeA = new Vector2(),
		nodeB = new Vector2();
	private long
		timePerMove = 500,
		lastUpdate = System.currentTimeMillis();
	private Index[] path = new Index[0];
	int current = 0;
	private boolean
		paused = false;
	private long
		pausedDelta;
	
	public float x() { return nodeA.x + (nodeB.x - nodeA.x) * (updateDelta() / (float)timePerMove); }
	public float y() { return nodeA.y + (nodeB.y - nodeA.y) * (updateDelta() / (float)timePerMove); }
	
	public void pause() {
		pausedDelta = updateDelta();
		paused = true;
	}
	public void resume() {
		paused = false;
		lastUpdate = System.currentTimeMillis() - pausedDelta;
	}
	
	@Override
	public void updateWith(InputProxy input) {
		if (updateDelta() > timePerMove) {
			current ++;
			if (current < path.length - 1) {
				nodeA = new Vector2(x(), y());
				nodeB = indexToVector(path[current + 1]);
			} else {	nodeA = nodeB;
			}
			lastUpdate = System.currentTimeMillis();
		}
	}
	
	public int pathLength() {
		return path.length - current;
	}
	
	public void setPath(Index[] path) {
		this.path = path;
		current = -1;
		nodeA = new Vector2(x(), y());
		nodeB = indexToVector(path[0]);
		lastUpdate = System.currentTimeMillis();
	}
	
	public void scaleSpeed(float scalar) {
		timePerMove *= 1/scalar;
	}
	
	public void setGridPosition(int x, int y) {
		nodeA = new Vector2(x * Tile.WIDTH, y * Tile.HEIGHT);
		nodeB = nodeA;
	}
	
	public long updateDelta() { return paused? pausedDelta : System.currentTimeMillis() - lastUpdate; }
	
	private Vector2 indexToVector(Index i) {
		return new Vector2(i.x * Tile.WIDTH + Tile.WIDTH/2f, i.y * Tile.HEIGHT + Tile.HEIGHT/2f);
	}
}