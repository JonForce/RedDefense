package com.saucy.game.levels;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.io.InputProxy;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Game;
import com.saucy.game.TouchManager;
import com.saucy.game.effects.ScoreChange;
import com.saucy.game.gui.Score;
import com.saucy.game.gui.buttons.MuteButton;
import com.saucy.game.gui.buttons.PauseButton;
import com.saucy.game.states.GameOverState;
import com.saucy.game.states.MenuState;
import com.saucy.game.states.OverlayState;
import com.saucy.game.states.PausedState;
import com.saucy.game.Direction;

public abstract class LevelState implements ApplicationState {
	
	protected final Game game;
	
	private Button pauseButton, muteButton;
	
	/** The Level's score-keeping and score-rendering mechanism. */
	private Score score;
	/** The Level's TouchManager handles the detecting and calling of touch-related events. */
	private TouchManager touchManager;
	/** The State to enter when the Level is failed. */
	protected GameOverState gameOverState;
	/** The State to enter when the Level is paused. */
	private OverlayState pausedState;
	
	private ArrayList<ScoreChange> scoreChanges;
	private Iterator<ScoreChange> scoreChangeIterator;
	
	private boolean
		/** True when the Tutorial has shown. */
		tutorialHasShown = false,
		/** True if the Level has been initialized. */
		initialized = false;
	
	/**
	 * Create a LevelState, an Object that is designed as the foundation of all TurboFingerSwipe
	 * Levels. The LevelState handles activities that are common across all Levels.
	 */
	public LevelState(Game game) {
		this.game = game;
	}
	
	@Override
	public final void enterState() {
		System.out.println("Entering " + levelName());
		
//		// If the tutorial has not shown yet,
//		// and the user has not opted out of seeing this Level's tutorials,
//		if (!tutorialHasShown && !game.settings().hasOptedOutOfTutorial() && game.settings().numberOfLaunches() < 5) {
//			game.setState(createTutorial());
//			tutorialHasShown = true;
//			// Return without initializing.
//			return;
//		}

		if (!initialized)
			initialize();
		else
			onResume();
		//this.tweenBarOffset(new Vector2(0,150), 300f);
	}
	
	@Override
	public final void updateApplication(Application app) {
		touchManager.update(app.input);
		pauseButton.updateWith(app.input);
		muteButton.updateWith(app.input);
		
		updateLevelWith(app.input);
	}
	
	@Override
	public final void renderTo(SpriteBatch batch) {
		renderLevelTo(batch);
		
		muteButton.renderTo(batch);
		pauseButton.renderTo(batch);
		score.renderTo(batch);
		
		scoreChangeIterator = scoreChanges.iterator();
		while (scoreChangeIterator.hasNext()) {
			ScoreChange scoreChange = scoreChangeIterator.next();
			if (scoreChange != null)
				scoreChange.renderTo(batch);
		}
	}
	
	/** Initialize the Level's base components. */
	public final void initialize() {
		this.touchManager = new TouchManager(4); // 4 is the max possible concurrent touches to track.
		
		 // Initialize the score-keeping mechanism.
		this.score = new Score(game()) {
			@Override
			public void decrement() {
				if (count() == 0)
					fail();
				else
					super.decrement();
			}
		};
		
		this.gameOverState = createGameOverState(); // Retrieve the GameOverState from the subclass.
		this.pausedState = createPausedState(); // Retrieve the PausedState from the subclass.
		
		this.initializeGUI();
		
		scoreChanges = new ArrayList<ScoreChange>();
		
		// Initialize abstract components.
		create();
		initialized = true;
	}
	
	/** Reset the Level to its initial state, then start it. */
	public final void restart() {
		// Reset the score.
		score.reset();
		
		// Reset abstract Level components.
		reset();
	}
	
	/** Set the Game to the game-over state. */
	public void fail() {
		game.setState(gameOverState);
	}
	
	/** Set the Game to the paused state. */
	public final void pause() {
		game.setState(pausedState);
		onPause();
	}
	
	/** @return the Level's TouchManager, a mechanism that notifies listeners of touch events. */
	public final TouchManager touchManager() {
		return this.touchManager;
	}
	
	/** @return the Level's score as an integer. */
	public final Score score() {
		return this.score;
	}
	
	public int coinsEarned() {
		return 0;
	}
	
	public final boolean initialized() {
		return initialized;
	}
	
	/** @return the Level's Game. */
	public final Game game() {
		return this.game;
	}
	
	/**
	 * Submit a score, if it's higher than the high-score, this score will
	 * become the new highscore.
	 * @param score The score to submit.
	 */
	protected final void submitScore(int score) {
		if (score > highscore())
			setHighscore(score);
	}
	
	/** Set the highscore of the Level to the specified value. */
	protected final void setHighscore(int newHighscore) {
		game.user().setHighScore(newHighscore);
	}
	
	/** Render the abstract Renderables to the specified SpriteBatch. */
	protected abstract void renderLevelTo(SpriteBatch batch);
	/** Update the abstract components of the Level. */
	protected abstract void updateLevelWith(InputProxy input);
	/** Instantiate the Level's Components. */
	protected abstract void create();
	/** Reset the Level to its initial state. */
	protected abstract void reset();
	/** @return the new tutorial state of the Level. */
	protected abstract TutorialState createTutorial();
	/** @return the name of the Level. */
	protected abstract String levelName();
	
	protected void onPause() {}
	protected void onResume() {}
	
	@Override
	public void exitState() { /* Do nothing */ }
	
	/** @return the Level's highscore. */
	private int highscore() {
		return game.user().highScore();
	}
	
	/** Create and @return the State to enter when the Level is failed. */
	private GameOverState createGameOverState() {
		return new GameOverState(game, this) {
			@Override
			public int currentScore() {
				return score.count();
			}
			
			@Override
			public int highestScore() {
				return highscore();
			}
		};
	}
	
	/** Create and @return the State to enter when the Level is paused. */
	private OverlayState createPausedState() {
		return new PausedState(game, this);
	}
	
	/** Initialize the Level's visual components. */
	private void initializeGUI() {
		pauseButton = new PauseButton(game);
		
		muteButton = new MuteButton(game, new Vector2(pauseButton.x(), pauseButton.y()));
		muteButton.translate(- muteButton.width(), 0);
	}
}