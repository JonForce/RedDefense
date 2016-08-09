package com.saucy.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;

public class NullState implements ApplicationState {

	@Override
	public void enterState() {
		System.out.println("Entering null state.");
	}

	@Override
	public void exitState() {
		System.out.println("Exiting null state.");
	}

	@Override
	public void renderTo(SpriteBatch arg0) {
		
	}

	@Override
	public void updateApplication(Application arg0) {
		
	}
}