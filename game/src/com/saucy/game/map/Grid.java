package com.saucy.game.map;

public final class Grid {
	
	private Tile[][] map;
	
	public Grid(int width, int height) {
		map = new Tile[width][height];
		
		// Initialize all the entities in the map to empty entities.
		for (int x = 0; x != width(); x ++)
			for (int y = 0; y != height(); y ++)
				set(x, y, new Tile(null));
	}
	
	public void set(int x, int y, Tile newTile) {
		map[x][y] = newTile;
	}
	
	public Tile get(int x, int y) {
		return map[x][y];
	}
	
	public boolean[][] createWalkableMatrix() {
		boolean[][] matrix = new boolean[width()][height()];
		for (int x = 0; x != width(); x ++) {
			for (int y = 0; y != height(); y ++) {
				matrix[x][y] = !map[x][y].walkable();
			}
		}
		return matrix;
	}
	
	public int width() { return map.length; }
	public int height() { return map[0].length; }
	
	public static int x(float virtual) {
		return (int) (virtual / TileRenderer.RENDER_WIDTH);
	}
	public static int y(float virtual) {
		return (int) (virtual / TileRenderer.RENDER_HEIGHT);
	}
}