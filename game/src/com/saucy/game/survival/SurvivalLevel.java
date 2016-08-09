package com.saucy.game.survival;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Json;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.Graphic;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.framework.util.pathfinding.Pathfinder;
import com.saucy.framework.util.pathfinding.PathfindingException;
import com.saucy.game.Game;
import com.saucy.game.input.UserController;
import com.saucy.game.levels.LevelState;
import com.saucy.game.levels.TutorialState;
import com.saucy.game.map.Grid;
import com.saucy.game.map.Position;
import com.saucy.game.map.Tile;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.ViewController;
import com.saucy.game.map.Wall;
import com.saucy.game.map.entities.Bullet;
import com.saucy.game.map.entities.BulletProperties;
import com.saucy.game.map.entities.Manager;
import com.saucy.game.map.entities.TempSprite;
import com.saucy.game.map.entities.TrackerBullet;
import com.saucy.game.map.entities.enemies.Enemy;
import com.saucy.game.map.entities.enemies.EnemySpawner;
import com.saucy.game.map.entities.turrets.Turret;
import com.saucy.game.map.entities.turrets.Factory;
import com.saucy.game.map.entities.turrets.TurretManager;
import com.saucy.game.map.entities.turrets.TurretProperties;
import com.saucy.game.map.entities.turrets.TurretUpgrade;

public class SurvivalLevel extends LevelState {
	
	private static final int
		STARTING_COINS = 100,
		STARTING_HEALTH = 100;
	
	private Grid grid;
	private ViewController vc;
	
	private TileRenderer renderer = new TileRenderer() {
		@Override protected float viewX() { return vc.x(); }
		@Override protected float viewY() { return vc.y(); }
	};
	private EnemySpawner spawner;
	private Manager<Bullet> bullets = new Manager<Bullet>();
	private Manager<Turret> turrets = new Manager<Turret>();
	private ArrayList<TempSprite> effects = new ArrayList<TempSprite>();
	private UserController userController;
	private int
		coins = STARTING_COINS,
		health = STARTING_HEALTH;
	private Graphic flagHealth;
	private HashMap<String, TurretUpgrade> upgrades = new HashMap<String, TurretUpgrade>();
	private Tutorial tutorial;
	
	public SurvivalLevel(Game game, int width, int height) {
		super(game);
		grid = new Grid(width, height);
		vc = new ViewController(game.USE_TOUCH_CONTROLS, grid.width(), grid.height(), super.game.screenWidth() + 40, super.game.screenHeight());
	}
	
	@Override
	protected void renderLevelTo(SpriteBatch batch) {
		renderer.render(grid, batch);

		turrets.renderTo(batch, vc);
		for (TempSprite s : effects)
			s.renderTo(batch);
		bullets.renderTo(batch, vc);
		spawner.renderTo(batch, vc);
		userController.renderTo(batch, vc);
		
		flagHealth.translate(-vc.x(), -vc.y());
		flagHealth.renderTo(batch);
		flagHealth.translate(vc.x(), vc.y());
		
		if (tutorial != null)
			tutorial.renderTo(batch);
	}
	
	@Override
	protected void updateLevelWith(InputProxy input) {
		if (Game.DEVELOPER_MODE && Gdx.input.isKeyPressed(Keys.E))
			coins += 3;
		
		vc.updateWith(input);
		
		// Only update the enemy spawner if the User is done with the tutorial.
		if (tutorial == null)
			spawner.updateWith(input);
		
		bullets.updateWith(input);
		
		for (TempSprite s : effects)
			s.updateWith(input);
		
		for (int i = 0; i < effects.size(); i ++)
			if (effects.get(i).shouldRemove)
				effects.remove(i);
		for (Bullet b : bullets.list())
			for (Enemy e : spawner.enemies())
				b.attemptCollideWith(e);
		
		turrets.updateWith(input);
		userController.updateWith(input);
		super.score().set(coins);
		
		if (tutorial != null) {
			tutorial.updateWith(input);
			if (tutorial.finished()) {
				tutorial = null;
				spawner.resume();
			}
		}
	}
	
	@Override
	protected void create() {
		buildMap();
		
		userController = new UserController(this, game.USE_TOUCH_CONTROLS, vc, game.assets());
		
		spawner = new EnemySpawner(game.assets(), grid) {
			@Override
			protected void addCoins(int amount) {
				coins += amount;
			}
			@Override
			protected void spawnBlood(final int x, final int y) {
				final float scale = TileRenderer.SCALE;
				final int rand = game.random().nextInt(3);
				TempSprite sprite = new TempSprite(x * scale, y * scale, game.assets().get("assets/Enemy/Enemy0Dead" + rand + ".png"), vc, 1000L);
				sprite.setScale(scale, scale);
				effects.add(sprite); 
			}
			@Override
			protected void damageFlag(int damage) {
				SurvivalLevel.this.damageFlag(damage);
			}
		};
		
		turrets = new TurretManager(spawner.enemies());
		
		flagHealth = new Graphic(
				EnemySpawner.TARGET_X * TileRenderer.RENDER_WIDTH + TileRenderer.RENDER_WIDTH/2f,
				EnemySpawner.TARGET_Y * TileRenderer.RENDER_HEIGHT + TileRenderer.RENDER_HEIGHT/2f + 10,
				game.assets().get("assets/FlagHealth.png")) {
			@Override
			public float width() {
				return SurvivalLevel.this.health / 2f;
			}
		};
		
		Json json = new Json();
		for (FileHandle f : Gdx.files.internal("assets/Tower/JSON/Upgrades/").list()) {
			if (!f.isDirectory() && f.extension().equals(".json")) {
				TurretUpgrade upgrade = json.fromJson(TurretUpgrade.class, f);
				this.upgrades.put(f.nameWithoutExtension(), upgrade);
			}
		}
		
		if (game.settings().numberOfLaunches() == 1) {
			System.out.println("Starting tutorial!");
			spawner.pause();
			tutorial = new Tutorial(game) {
				@Override protected boolean hasMovedView() {
					return vc.hasMoved();
				}
				@Override protected boolean shopOpen() {
					return userController.shopOpen();
				}
				@Override protected int coins() {
					return coins;
				}
				@Override protected boolean upgradeMenuOpen() {
					return userController.upgradeMenuOpen();
				}
				@Override protected boolean isFocusFiring() {
					return userController.focusFiring();
				}
			};
		}
	}
	
	@Override
	public void fail() {
		this.coins = spawner.getWave();
		submitScore(spawner.getWave());
		game.user().addCoins(spawner.goldCoinsForWave(spawner.getWave()));
		
		game.setState(gameOverState);
	}
	
	@Override
	protected void reset() {
		spawner.reset();
		turrets.list().clear();
		bullets.list().clear();
		health = STARTING_HEALTH;
		coins = STARTING_COINS;
		effects.clear();
		
		buildMap();
	}
	
	@Override
	protected TutorialState createTutorial() {
		return null;
	}
	
	@Override
	protected String levelName() {
		return "Survival";
	}
	
	@Override
	protected void onPause() {
		spawner.pause();
	}
	@Override
	protected void onResume() {
		spawner.resume();
	}
	
	public Position view() {
		return this.vc;
	}
	
	public TileRenderer renderer() { return this.renderer; }
	
	/** Instantiate (but do not begin managing) a turret with the specified properties. */
	public Turret createTurret(TurretProperties properties) {
		if (properties.type.equals("Factory")) {
			return new Factory(game.assets(), vc, properties) {
				@Override
				protected void addMoney(int amount) {
					coins += amount;
				}
				@Override
				protected void addTempSprite(TempSprite sprite) {
					effects.add(sprite);
				}
				@Override
				public float random(float max) {
					return ((float)game().random().nextInt() + game().random().nextFloat()) % max;
				}
			};
		} else if (properties.type.equals("BarbedWire")) {
			return new Turret(game.assets(), properties) {
				@Override
				public void fire(float fromX, float fromY, float toX, float toY, Position target, float rotateBy, BulletProperties properties) {
					if (this.target != null) {
						Bullet b = new Bullet(game.assets(), properties);
						this.target.getShotBy(b);
					}
				}
				@Override
				public float random(float max) {
					return ((float)game().random().nextInt() + game().random().nextFloat()) % max;
				}
				@Override
				public void playSound(String filename) {
					game.audio().playSound(Gdx.files.internal(filename));
				}
				@Override
				public boolean canRotate() {
					return false;
				}
			};
		} else
			return new Turret(game.assets(), properties) {
				@Override
				public void fire(float fromX, float fromY, float toX, float toY, Position target, float rotateBy, BulletProperties properties) {
					Bullet b;
					if (properties.type.equals("Tracker") && this.superTarget == null)
						b = new TrackerBullet(game.assets(), properties, this.target);
					else
						b = new Bullet(game.assets(), properties);
					b.set(fromX, fromY, toX, toY, rotateBy);
					bullets.add(b);
				}
				@Override
				public float random(float max) {
					return ((float)game().random().nextInt() + game().random().nextFloat()) % max;
				}
				@Override
				public void playSound(String filename) {
					game.audio().playSound(Gdx.files.internal(filename));
				}
			};
	}
	
	/** Begin managing the specified Turret. */
	public void manage(Turret t) {
		turrets.add(t);
	}
	
	/** Add a wall to the Level. */
	public void addWall(Wall wall, Index position) {
		if (spawner.isAnyEnemyOn(position.x, position.y))
			throw new RuntimeException("Cannot build wall there, there is an enemy there.");
		
		try {
			grid.set(position.x, position.y, wall);
			spawner.updateEnemyPaths();
		} catch (PathfindingException e) {
			System.out.println("Caught pathfinding exception, recovering");
			grid.set(position.x, position.y, new Tile(grid.get(2, 2).texture()));
		}
	}
	
	public ArrayList<Turret> turrets() {
		return turrets.list();
	}
	
	public HashMap<String, TurretUpgrade> upgrades() {
		return this.upgrades;
	}
	
	@Override
	public int coinsEarned() {
		return spawner.goldCoinsForWave(spawner.getWave());
	}
	
	/** Spend the coins and return true if the User has the $ */
	public boolean trySpendCoins(int amount) {
		if (coins >= amount) {
			coins -= amount;
			return true;
		} else
			return false;
	}
	
	/** @return true if it is acceptable to build a wall at (gridX, gridY). */
	public boolean canBuildWallAt(int gridX, int gridY) {
		if (gridX < 2 || gridX > grid.width() - 3)
			return false;
		if (gridX == EnemySpawner.TARGET_X && gridY == EnemySpawner.TARGET_Y)
			return false;
		
		for (Index i : spawner.spawnLocations())
			if (
				spawner.isAnyEnemyOn(gridX, gridY) ||
				new Pathfinder(grid.createWalkableMatrix())
					.pathImpossibleAfterWallAddedAt(
							gridX, gridY,
							Index.of(EnemySpawner.TARGET_X, EnemySpawner.TARGET_Y),
							Index.of(i.x, i.y)))
				return false;
		return true;
	}
	
	public void damageFlag(int damage) {
		this.health -= damage;
		if (health <= 0)
			fail();
	}
	
	private void buildMap() {
		Texture texture = game.getTexture("assets/Tiles/Ground/Floor0.png");
		
		for (int x = 0; x != grid.width(); x ++) {
 			for (int y = 0; y != grid.height(); y ++) {
 				grid.set(x, y, new Tile(texture));;
 			}
		}
		
		for (int x = 0; x != grid.width(); x ++) {
 			for (int y = 0; y != grid.height(); y ++) {
 				if (x == grid.width()-1 || x == 0 || y == grid.height()-1 || y == 0)
 					grid.set(x, y, new Wall(Wall.texture(game.assets(), 0)));;
 			}
		}
	}
}