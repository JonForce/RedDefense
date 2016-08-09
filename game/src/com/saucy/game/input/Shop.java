package com.saucy.game.input;

import java.util.ArrayList;

import javax.swing.text.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.Renderable;
import com.saucy.framework.util.Updatable;
import com.saucy.framework.util.pathfinding.Index;
import com.saucy.game.Assets;
import com.saucy.game.ViewRenderable;
import com.saucy.game.gui.Font;
import com.saucy.game.map.Position;
import com.saucy.game.map.StrongWall;
import com.saucy.game.map.Tile;
import com.saucy.game.map.TileRenderer;
import com.saucy.game.map.ViewController;
import com.saucy.game.map.Wall;
import com.saucy.game.map.entities.enemies.EnemySpawner;
import com.saucy.game.map.entities.turrets.Turret;
import com.saucy.game.map.entities.turrets.TurretProperties;

public abstract class Shop implements ViewRenderable {
	
	public final int
		WINDOW_HEIGHT = 600,
		WINDOW_WIDTH = 500;
	private final float
		TILE_SCALE = 4f;
	
	private Texture window;
	private ArrayList<Turret> turrets;
	private ArrayList<Wall> walls;
	private ArrayList<TryWallBuild> builds;
	private Font font;
	private Integer grabbed;
	private Index grabbedWallPosition;
	private TileRenderer renderer;
	
	protected abstract boolean trySpendCoins(int amount);
	/** Create a Turret, but do not manage it. */
	protected abstract Turret createTurret(TurretProperties properties);
	/** Check if a Turret can be built at (gridX, gridY). */
	protected abstract boolean canBuildTurretAt(int gridX, int gridY);
	/** Check if a wall can be built at (gridX, gridY). */
	protected abstract boolean canBuildWallAt(int gridX, int gridY);
	/** Add the Turret to the Level. */
	protected abstract void addTurret(Turret t);
	/** Add the Wall to the Level. */
	protected abstract void addWall(Wall w, Index position);
	
	public Shop(Assets assets, ArrayList<TurretProperties> turretProperties, TileRenderer renderer) {
		this.renderer = renderer;
		this.turrets = new ArrayList<Turret>();
		for (TurretProperties properties : turretProperties)
			turrets.add(createTurret(properties));
		this.walls = new ArrayList<Wall>();
		this.builds = new ArrayList<TryWallBuild>();
		walls.add(new Wall(Wall.texture(assets, 0)));
		walls.add(new StrongWall(assets));
		this.window = assets.get("assets/Windows/Overlay.png");
		this.font = new Font(new TextureRegion(assets.get("assets/GUI/Font/Digits2.png", Texture.class)));
		font.setScale(.5f);
		font.setAlignment(Font.ALIGNMENT_CENTER);
		positionTurretsInMenu();
	}
	
	@Override
	public void renderTo(SpriteBatch batch, Position view) {
		batch.draw(window, 0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
		for (Turret t : turrets) {
			t.renderTo(batch, (t == grabbedTurret())? view : null);
			
			// Render the price of the Turret below the Turret if it is not grabbed.
			if (!hasGrabbedTurret() || t != grabbedTurret())
				font.renderIntegerTo(batch, t.properties().cost, t.screenX() + 2, t.screenY() - t.screenHeight() - 5f);
		}
		
		for (int i = 0; i != walls.size(); i ++) {
			Wall w = walls.get(i);
			final Vector2 pos = wallPosition(i);
			if (hasGrabbedWall() && grabbed == i)
				TileRenderer.render(w, grabbedWallPosition.x, grabbedWallPosition.y, batch, view);
			else
				batch.draw(w.texture(), pos.x, pos.y, wallWidth(i), wallHeight(i));
			font.renderIntegerTo(batch, w.cost(), pos.x + wallWidth(i) / 2f, pos.y - font.digitHeight() - 3);
		}
	}
	
	public void renderPendingWallsTo(SpriteBatch batch) {
		for (TryWallBuild b : builds)
			b.renderTo(batch);
	}
	
	public void updatePendingWalls() {
		for (TryWallBuild t : builds)
			t.update();
		for (int i = builds.size() - 1; i >= 0; i --)
			if (builds.get(i).shouldRemove())
				builds.remove(i);
	}
	
	public void updateWith(InputProxy input, ViewController view) {
		if (!hasGrabbedTurret() && !hasGrabbedWall()) {
			if (input.isTouched()) {
				for (Turret t : turrets) {
					if (new Rectangle(t.screenX() - t.screenWidth()/2f, t.screenY() - t.screenHeight()/2f, t.screenWidth(), t.screenHeight()).contains(input.getX(), input.getY())) {
						grabbed = turrets.indexOf(t);
						// Reset the Turret's tile size to the true tile size so that it will render correctly.
						t.setTileSize(Tile.WIDTH, Tile.HEIGHT);
						// Update the position of the newly grabbed Turret immediately.
						grabbedTurret().setPosition(view.getCursorIndexX(input), view.getCursorIndexY(input));
					}
				}
				for (int i = 0; i != walls.size(); i ++) {
					Vector2 pos = wallPosition(i);
					if (new Rectangle(pos.x, pos.y, wallWidth(i), wallHeight(i)).contains(input.getX(), input.getY())) {
						grabbed = i;
						grabbedWallPosition = Index.of(view.getCursorIndexX(input), view.getCursorIndexY(input));
					}
				}
			}
		} else {
			if (input.isTouched()) {
				if (hasGrabbedTurret())
					// User has not released the grabbed Turret yet.
					grabbedTurret().setPosition(view.getCursorIndexX(input), view.getCursorIndexY(input));
				else
					grabbedWallPosition = Index.of(view.getCursorIndexX(input), view.getCursorIndexY(input));
			} else
				// The User just released the grabbed Turret.
				release();
		}
	}
	
	/** Position the Turrets evenly in the menu. */
	private void positionTurretsInMenu() {
		final int
			WIDTH = 3,
			HEIGHT = 5;
		for (int y = 0; y != HEIGHT; y ++) {
			for (int x = 0; x != WIDTH; x ++) {
				int index = y * WIDTH + x;
				if (index < turrets.size()) {
					Turret t = turrets.get(index);
					// Make the Turret think the tiles are bigger so we have a margin.
					t.setTileSize(Tile.WIDTH * 2.5f, Tile.HEIGHT * 2);
					t.setPosition(x, HEIGHT - y);
				}
			}
		}
	}
	
	/** Release the turret grabbed. */
	private void release() {
		if (hasGrabbedTurret()) {
			if (canBuildTurretAt(grabbedTurret().gridX(), grabbedTurret().gridY()) && trySpendCoins(grabbedTurret().properties().cost)) {
				// Add the Turret to the Level.
				addTurret(grabbedTurret());
			}
			// Remove the grabbed Turret from the list, the Level will manage it from now on.
			turrets.set(grabbed, createTurret(grabbedTurret().properties()));
			grabbed = null;
			positionTurretsInMenu();
		} else if (hasGrabbedWall()) {
			int x = grabbedWallPosition.x;
			int y = grabbedWallPosition.y;
			if (
					canBuildWallAt(x, y)
					&& trySpendCoins(walls.get(grabbed).cost()))
				addWall(walls.get(grabbed), grabbedWallPosition);
			else if (!canBuildWallAt(x, y))
				builds.add(new TryWallBuild(walls.get(grabbed), x, y, this.renderer) {
					@Override
					protected boolean tryBuildAt(int x, int y, Wall wall) {
						if (canBuildWallAt(x, y)) {
							addWall(wall, Index.of(x, y));
							return true;
						}
						return false;
					}
				});
			grabbed = null;
			grabbedWallPosition = null;
		} else
			throw new RuntimeException("Nothing to release.");
	}
	
	private Turret grabbedTurret() {
		if (!hasGrabbedTurret())
			return null;
		else
			return turrets.get(grabbed);
	}
	
	private boolean hasGrabbedWall() {
		return grabbedWallPosition != null;
	}
	private boolean hasGrabbedTurret() {
		return grabbed != null && !hasGrabbedWall();
	}
	
	private Vector2 wallPosition(int i) {
		final float
			vMargin = 130,
			hMargin = 30,
			x = WINDOW_WIDTH - wallWidth(i) - hMargin,
			y = WINDOW_HEIGHT - (i+1) * (wallHeight(i) + vMargin);
		return new Vector2(x, y);
	}
	
	private float wallWidth(int i) {
		return walls.get(i).texture().getWidth() * TILE_SCALE;
	}
	private float wallHeight(int i) {
		return walls.get(i).texture().getHeight() * TILE_SCALE;
	}
}