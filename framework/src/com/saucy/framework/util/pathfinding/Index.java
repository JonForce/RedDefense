package com.saucy.framework.util.pathfinding;

public class Index {
	
	public final int x, y;
	
	public Index(final int x, final int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other) {
		return other instanceof Index && ((Index)other).x == x && ((Index)other).y == y;
	}
	
	public static Index of(final int x, final int y) {
		return new Index(x, y);
	}
	
}