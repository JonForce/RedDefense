package com.saucy.game.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.Graphic;
import com.saucy.game.Game;
import com.saucy.game.NullState;
import com.saucy.game.states.MenuState;

public abstract class TutorialState extends MenuState {
	
	private ApplicationState exitState;
	private int tipIndex = 0; // The index of the current tip.
	private boolean created = false; // True if the Tutorial has been initialized.
	
	/**
	 * The base class for a Tutorial.
	 */
	public TutorialState(Game game) {
		super(game);
		this.exitState = new NullState();
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		super.renderTo(batch);
		//game.background().renderTo(batch);
		tips()[tipIndex].renderTo(batch);
	}
	
	@Override
	public void updateApplication(Application app) {
		
	}
	
	@Override
	public void enterState() {
		if (!created)
			create();
		created = true;
		super.enterState();
	}
	@Override
	public void exitState() { }
	
	public final Graphic currentTip() {
		return tips()[tipIndex];
	}
	
	public final void reset() {
		tipIndex = 0;
	}
	
	public final void setExitState(ApplicationState newExitState) {
		this.exitState = newExitState;
	}
	
	protected final void useNextTip() {
		if (++tipIndex == tips().length)
			game.setState(exitState);
		else
			useTip(tipIndex);
	}
	
	protected void create() { }
	
	abstract public void useTip(int tip);
	abstract public Graphic[] tips();
	abstract public String tutorialName();
	
}