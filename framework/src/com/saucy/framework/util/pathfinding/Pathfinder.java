package com.saucy.framework.util.pathfinding;

import java.util.ArrayList;

public class Pathfinder {
	
	private final ArrayList<Index> openSet;
	private final boolean[][] grid;
	private final float[][] costs;
	
	public Pathfinder(boolean[][] grid) {
		this.grid = grid;
		this.costs = new float[grid.length][grid[0].length];
		openSet = new ArrayList<Index>();
	}
	
	public Index[] findPath(Index start, Index end) {
		if (grid[start.x][start.y])
			throw new PathfindingException();
		if (grid[end.x][end.y])
			throw new PathfindingException();
		
		Index[] path = null;
		reset();
		openSet.add(start);
		costs[start.x][start.y] = 0;
		
		dance: while(!openSet.isEmpty()) {
			int currentIndex = indexOfLowestCost(openSet);
			Index current = openSet.get(currentIndex);
			
			for (Index adjacent : adjacentTo(current)) {
				if (adjacent.equals(end)) {
					//System.out.println("Found End");
					path = createPath(start, end, (int)costOf(current) + 1);
					break dance;
				}
				
				float tentativeCost = costOf(current) + 1;
				if (tentativeCost < costOf(adjacent)) {
					costs[adjacent.x][adjacent.y] = tentativeCost;
					openSet.add(adjacent);
				}
			}
			
			openSet.remove(currentIndex);
		}
		
//		for (float[] r : costs) {
//			for (int i = 0; i != r.length; i ++) {
//				int c = (int)r[i] % (10 * 10 * 10);
//				//System.out.println((int)Math.log10(c));
//				for (int d = 0; d < 3 - (int)Math.log10(c); d ++)
//					System.out.print(" ");
//				System.out.print(((int) c) + " ");
//			}
//			System.out.println();
//		}
		
//		for (int x = 0; x != width(); x ++) {
//			for (int y = 0; y != height(); y ++) {
//				
//				if (grid[x][y])
//					System.out.print("III ");
//				else if (Index.of(x, y).equals(end))
//					System.out.print(" E  ");
//				else if (Index.of(x, y).equals(start))
//					System.out.print(" S  ");
//				else if (pathHas(Index.of(x, y), path))
//					System.out.print(" .  ");
//				else
//					System.out.print("    ");
//			}
//			System.out.println();
//		}
		
		return path;
	}
	
	/** Change the values of the grid to equal those of newGrid. NewGrid should
	 * have the same dimensions as the old grid. */
	public final void updateGrid(boolean[][] newGrid) {
		for (int x = 0; x != width(); x ++) {
			for (int y = 0; y != height(); y ++) {
				grid[x][y] = newGrid[x][y];
			}
		}
	}
	
	/* The name of this function makes me sad. */
	public boolean pathImpossibleAfterWallAddedAt(int x, int y, Index start, Index end) {
		boolean originalValue = grid[x][y];
		grid[x][y] = true;
		boolean pathCreated = findPath(start, end) == null;
		grid[x][y] = originalValue;
		return pathCreated;
	}
	
	private boolean pathHas(Index i, Index[] path) {
		for (int j = 0; j != path.length; j ++)
			if (path[j].equals(i))
				return true;
		return false;
	}
	
	private Index[] createPath(Index start, Index end, int length) {
		Index[] path = new Index[length];
		path[length - 1] = end;
		int next = length - 2;
		Index current = end;
		
		while (next >= 0) {
			float dist = Float.MAX_VALUE;
			for (Index adj : adjacentTo(current)) {
				if (costOf(adj) == next + 1 && dist(adj, end) < dist) {
					current = adj;
					dist = dist(adj, end);
					path[next] = adj;
				}
			}
			next--;
		}
		
		return path;
	}
	
	private float dist(Index a, Index b) {
		return (float)Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
	}
	
	private Index[] adjacentTo(Index index) {
		int count = 0;
		for (int dx = -1; dx != 2; dx ++)
			for (int dy = -1; dy != 2; dy ++) {
				if (dx == 0 && dy == 0)
					continue;
				int x = index.x + dx;
				int y = index.y + dy;
				// Check for out of bounds.
				if (x < width() && x >= 0 && y < height() && y >= 0 && !grid[x][y])
					if (!grid[index.x][index.y + dy] && !grid[index.x + dx][index.y])
						count ++;
			}
		Index[] adjacent = new Index[count];
		int i = 0;
		for (int dx = -1; dx != 2; dx ++)
			for (int dy = -1; dy != 2; dy ++) {
				if (dx == 0 && dy == 0)
					continue;
				int x = index.x + dx;
				int y = index.y + dy;
				// Check for out of bounds.
				if (x < width() && x >= 0 && y < height() && y >= 0 && !grid[x][y])
					if (!grid[index.x][index.y + dy] && !grid[index.x + dx][index.y])
						adjacent[i++] = Index.of(x, y);
			}
		
		return adjacent;
	}
	
	private int indexOfLowestCost(ArrayList<Index> set) {
		int lowestIndex = 0;
		for (int i = 0; i != set.size(); i ++) {
			if (costOf(set.get(i)) < costOf(set.get(lowestIndex)))
				lowestIndex = i;
		}
		return lowestIndex;
	}
	
	private float costOf(Index i) {
		return costs[i.x][i.y];
	}
	
	private void reset() {
		openSet.clear();
		for (float[] r : costs)
			for (int i = 0; i != r.length; i ++)
				r[i] = Float.MAX_VALUE;
	}
	
	private int height() { return grid[0].length; }
	private int width() { return grid.length; }
	
}