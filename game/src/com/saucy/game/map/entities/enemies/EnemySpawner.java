package com.saucy.game.map.entities.enemies;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.util.Updatable;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.ViewRenderable;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Position;
import com.saucy.game.map.entities.Manager;

public abstract class EnemySpawner implements Updatable, ViewRenderable {
	
	public enum EnemyType {
		basic, big, fast, red,
		breaker, bossBreaker,
		strong, bigGreen,
		blue, bigBlue,
		purple, bigPurple,
		yellow, bigYellow
	}
	
	public static final int
		TARGET_X = 14,
		TARGET_Y = 15;
	
	private final Assets assets;
	private final Grid grid;
	private Manager<Enemy> enemies;
	private Flag flag;
	
	private long lastWaveEnd, lastEnemySpawn;
	private Wave currentWave;
	private int enemiesSpawned = 0, wave = 0;
	
	private Index[] spawnLocations;
	
	int spawnCount = 0;
	
	protected abstract void addCoins(int amount);
	protected abstract void damageFlag(int damage);
	protected abstract void spawnBlood(int x, int y);
	
	public EnemySpawner(Assets assets, Grid grid) {
		this.assets = assets;
		this.grid = grid;
		this.flag = new Flag(assets, TARGET_X, TARGET_Y);
		enemies = new Manager<Enemy>();
		currentWave = getWave(0);
		spawnLocations = new Index[(grid.height() - 2)];
		for (int i = 0; i != spawnLocations.length; i ++) {
			spawnLocations[i] = Index.of((i % 2 == 0)? 2 : grid.width() - 2, 2 + (i % ((spawnLocations.length - 2)) ));
		}
		lastWaveEnd = System.currentTimeMillis();
	}
	
	public ArrayList<Enemy> enemies() { return enemies.list(); }
	
	public void skipToWave(int n) {
		for (int i = 0; i != n; i ++)
			addCoins(coinsForWave(i));
		wave = n;
		currentWave.waveStartDelay = 60000;
		lastWaveEnd = System.currentTimeMillis();
	}
	
	public void spawnEnemy(int x, int y, EnemyType type) {
		Enemy enemy = null;
		if (type == EnemyType.basic) {
			enemy = new TestEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)this.x(), (int)this.y());
				}
			};
		} else if (type == EnemyType.big) {
			enemy = new BigEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)this.x(), (int)this.y());
				}
			};
		} else if (type == EnemyType.fast) {
			enemy = new FastEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.red) {
			enemy = new RedEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.strong) {
			enemy = new StrongEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.bigGreen) {
			enemy = new BigGreenEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.blue) {
			enemy = new BlueEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.bigBlue) {
			enemy = new BigBlueEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.breaker) {
			enemy = new Breaker(this, assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.bossBreaker) {
			enemy = new BossBreaker(this, assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.purple) {
			enemy = new PurpleEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.bigPurple) {
			enemy = new BigPurpleEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.yellow) {
			enemy = new YellowEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		} else if (type == EnemyType.bigYellow) {
			enemy = new BigYellowEnemy(assets, grid) {
				@Override
				protected void onDeath() {
					spawnBlood((int)x(), (int)y());
				}
			};
		}
		enemy.motor.setGridPosition(x, y);
		enemy.target(TARGET_X,  TARGET_Y);
		enemies.add(enemy);
	}
	public void spawnEnemy(EnemyType type) {
		Index i = spawnLocations()[enemiesSpawned % spawnLocations().length];
		spawnEnemy(i.x, i.y, type);
	}
	
	public void reset() {
		enemies.list().clear();
		wave = 0;
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		enemies.renderTo(batch, view);
		flag.renderTo(batch, view);
	}
	
	@Override
	public void updateWith(InputProxy input) {
		enemies.updateWith(input);
		for (int i = enemies.list().size() - 1; i >= 0; i --) {
			Enemy e = enemies.list().get(i);
			if (e.gridX() == TARGET_X && e.gridY() == TARGET_Y) {
				enemies.remove(e);
				damageFlag(5);
			}
		}
		if (Game.DEVELOPER_MODE && Gdx.input.isKeyPressed(Keys.Q))
			this.wave ++;
		
		if (waveDelta() > currentWave.waveStartDelay) {
			currentWave = getWave(++wave);
			lastWaveEnd = System.currentTimeMillis();
			System.out.println("Starting Wave " + wave);
			addCoins(coinsForWave(wave));
			System.out.println("Spawned " + spawnCount + " last wave.");
			spawnCount = 0;
		}
		
		if (System.currentTimeMillis() - lastEnemySpawn > currentWave.spawnDelay) {
			boolean spawnedEnemy = false;
			if (currentWave.basicEnemies > 0) {
				currentWave.basicEnemies --;
				spawnEnemy(EnemyType.basic);
				spawnedEnemy = true;
			}
			if (currentWave.bigEnemies > 0) {
				currentWave.bigEnemies --;
				spawnEnemy(EnemyType.big);
				spawnedEnemy = true;
			}
			if (currentWave.fastEnemies > 0) {
				currentWave.fastEnemies --;
				spawnEnemy(EnemyType.fast);
				spawnedEnemy = true;
			}
			if (currentWave.redEnemies > 0) {
				currentWave.redEnemies --;
				spawnEnemy(EnemyType.red);
				spawnedEnemy = true;
			}
			if (currentWave.strongEnemies > 0) {
				currentWave.strongEnemies --;
				spawnEnemy(EnemyType.strong);
				spawnedEnemy = true;
			}
			if (currentWave.bigGreenEnemies > 0) {
				currentWave.bigGreenEnemies --;
				spawnEnemy(EnemyType.bigGreen);
				spawnedEnemy = true;
			}
			if (currentWave.blueEnemies > 0) {
				currentWave.blueEnemies --;
				spawnEnemy(EnemyType.blue);
				spawnedEnemy = true;
			}
			if (currentWave.bigBlueEnemies > 0) {
				currentWave.bigBlueEnemies --;
				spawnEnemy(EnemyType.bigBlue);
				spawnedEnemy = true;
			}
			if (currentWave.breakers > 0) {
				currentWave.breakers --;
				spawnEnemy(EnemyType.breaker);
				spawnedEnemy = true;
			}
			if (currentWave.bossBreakers > 0) {
				currentWave.bossBreakers --;
				spawnEnemy(EnemyType.bossBreaker);
				spawnedEnemy = true;
			}
			if (currentWave.purpleEnemies > 0) {
				currentWave.purpleEnemies --;
				spawnEnemy(EnemyType.purple);
				spawnedEnemy = true;
			}
			if (currentWave.bigPurpleEnemies > 0) {
				currentWave.bigPurpleEnemies --;
				spawnEnemy(EnemyType.bigPurple);
				spawnedEnemy = true;
			}
			if (currentWave.yellowEnemies > 0) {
				currentWave.yellowEnemies --;
				spawnEnemy(EnemyType.yellow);
				spawnedEnemy = true;
			}
			if (currentWave.bigYellowEnemies > 0) {
				currentWave.bigYellowEnemies --;
				spawnEnemy(EnemyType.bigYellow);
				spawnedEnemy = true;
			}
			if (spawnedEnemy) {
				enemiesSpawned ++;
				spawnCount ++;
				lastEnemySpawn = System.currentTimeMillis();
			}
		}
	}
	
	public final int getWave() { return wave; }
	
	public final boolean isAnyEnemyOn(int x, int y) {
		boolean flag = false;
		for (Enemy e : enemies.list())
			if (e.gridX() == x && e.gridY() == y)
				flag = true;
		return flag;
	}
	
	public final Index[] spawnLocations() {
		return spawnLocations;
	}
	
	public final void updateEnemyPaths() {
		for (Enemy e : enemies.list()) {
			e.updatePath();
		}
	}
	
	public void pause() {
		for (Enemy e : enemies.list())
			e.motor.pause();
	}
	public void resume() {
		for (Enemy e : enemies.list())
			e.motor.resume();
	}
	
	public long waveDelta() {
		return System.currentTimeMillis() - this.lastWaveEnd;
	}
	
	private Wave getWave(int i) {
		Wave w = new Wave();
		w.waveStartDelay = 10000 + Math.min(5000, i * 500);
//		w.spawnDelay = (i < 15)? 200 : 100;
		w.spawnDelay = 300;
		
		if (i == 20) {
			w.bossBreakers = 1;
			w.waveStartDelay = 40000;
		}
		else if (i < 20) {
			w.spawnDelay = (i < 5)? 700 : (i < 10)? 400 : 300;
			w.basicEnemies = (i < 15)? i * (i / 10 + 2) : (i < 30)? i : 0;
			w.bigEnemies = (i < 15)? i / 2 : (i < 30)? i * (i / 10 + 2) : (i < 37)? i / 2 : 0;
			w.fastEnemies = (i == 5)? 10 : (i == 10)? 20 : (i > 15)? i * (i / 10 + 2)/2 : 0;
			w.redEnemies = (i % 5 == 0)? i : 0;
		} else if (i < 30) {
			w.breakers = 1;
			if (i < 25)
				w.strongEnemies = 3*i - 3*17;
			if (i > 24)
				w.bigGreenEnemies = (i - 20) * 2;
		} else if (i < 40) {
			w.breakers = 2;
			if (i < 35)
				w.bigGreenEnemies = Math.max(0, 10 - (i - 40));
			w.redEnemies = (i % 2 == 0)? 5 : 10;
			w.blueEnemies = Math.min(4*i - 4*12*3 + 3, 20);
			w.bigBlueEnemies = 1*i - 1*10*3 + 0;
		} else if (i < 50) {
			w.breakers = 3;
			if (i < 45) {
				w.blueEnemies = 4*i - 4*10*3 + 3;
				w.bigBlueEnemies = 1*i - 1*10*3 + 0;
			} else
				w.bigBlueEnemies = 3*i - 1*10*2;
		} else if (i < 70) {
			if (i < 60)
				w.bossBreakers = 1;
			else
				w.bossBreakers = 2;
			w.spawnDelay = 500;
			w.bigPurpleEnemies = (i - 47) * 2;
			if (i < 60)
				w.purpleEnemies = 3*i - 3*45;
		} else if (i < 90) {
			if (i < 80)
				w.bossBreakers = 3;
			else
				w.bossBreakers = 4;
			w.yellowEnemies = 3*i - 3*65;
			w.bigYellowEnemies = (i - 67) * 2;
		}
		return w;
	}
	
	private int coinsForWave(int i) {
		return (i < 10)? 35 : (i < 20)? 40 : (i < 40)? 50 : (i < 60)? 55 : 65;
	}
	
	public int goldCoinsForWave(int i) {
		if (i < 21)
			return (i / 4);
		else if (i < 41)
			// Got to wave 21
			return 10 + (i - 21)/6;
		else if (i < 51)
			// Got to wave 41
			return 20 + (i - 41)/6;
		else if (i < 71)
			// Got to wave 51
			return 25 + (i - 51)/6;
		else if (i < 90)
			// Got to wave 71
			return 45 + (i - 71)/5;
		else
			return 60 + (i - 100);
	}
}