package com.saucy.game.map.entities.enemies;

public class Wave {
	public int
		basicEnemies, bigEnemies,
		breakers, bossBreakers,
		fastEnemies, redEnemies,
		strongEnemies, bigGreenEnemies,
		blueEnemies, bigBlueEnemies,
		purpleEnemies, bigPurpleEnemies,
		yellowEnemies, bigYellowEnemies;
	public float spawnDelay, waveStartDelay;
	public Wave(float spawnDelay, int basicEnemies) {
		this.spawnDelay = spawnDelay;
		this.basicEnemies = basicEnemies;
	}
	public Wave() {}
}