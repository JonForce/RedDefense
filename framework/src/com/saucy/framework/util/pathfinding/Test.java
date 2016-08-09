package com.saucy.framework.util.pathfinding;

public class Test {
	
	public static void main(String[] args) {
		boolean[][] map = {
				{false, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false},
				{false, false, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false},
				{true, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false, false, false},
				{false, false, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false, false, false},
				{false, false, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false, false, false},
				{false, true, false, false, false, true, false, false, false, false, false, false, false, true, false, false, false, false, false},
				{false, true, false, false, false, true, false, true, false, false, false, false, false, true, false, false, false, false, false},
				{false, true, false, false, false, true, false, true, false, false, false, false, false, true, false, false, false, false, false},
				{false, true, false, false, true, true, false, true, false, false, false, false, false, true, false, false, false, false, false},
				{false, false, false, true, true, false, false, true, false, true, false, false, false, true, false, false, false, false, false},
				{false, false, false, false, false, false, false, false, false, true, false, false, false, true, false, false, false, false, false},
				{false, false, false, true, false, false, false, false, false, true, false, false, false, true, false, true, false, false, false},
				{false, false, false, true, true, false, false, false, false, true, false, false, false, true, false, true, false, false, false},
				{false, false, false, false, true, false, false, false, false, true, false, false, false, true, false, true, false, false, false},
				{false, false, false, false, true, true, false, true, true, true, true, false, false, true, false, true, false, false, false},
				{false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false},
				{false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, false, false, false},
				{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false}};
		System.out.println("MapSize : " + map.length + " " + map[0].length);
		Pathfinder p = new Pathfinder(map);
		p.findPath(Index.of(0, 0), Index.of(17, 18));
		
	}
	
}