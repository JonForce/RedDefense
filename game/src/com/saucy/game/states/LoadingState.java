package com.saucy.game.states;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.saucy.framework.control.Application;
import com.saucy.framework.control.ApplicationState;
import com.saucy.framework.rendering.Renderable;
import com.saucy.game.Assets;
import com.saucy.game.Game;
import com.saucy.game.gui.LoadingScreen;

public class LoadingState implements ApplicationState {
	/* The state to exit to when our loading is complete */
	private ApplicationState exitState;
	
	/* The object to defer rendering to while loading */
	private Renderable loadingScreen;
	private float oldPercent;
	
	private Assets assets;
	
	public LoadingState(Game game, final Assets assets, FileHandle resources) {
		// Assert 'resources' is exists and is a directory.
		if (!resources.exists())
			throw new RuntimeException("Error in LoadingState constructor : Resource folder not found at \"" + resources.path() + "\"");
		this.assets = assets;

		// Load all the textures from our resource directory.
		assets.loadAssetsFrom(resources);
		
		this.loadingScreen = new LoadingScreen(game, game.screenWidth(), game.screenHeight()) {
			public float percentComplete() {
				return assets.getProgress();
			}
		};
	}
	
	@Override
	public void enterState() {
		System.out.println("Entering LoadingState.");
	}
	
	@Override
	public void exitState() {
		System.out.println("Exiting LoadingState.");
	}
	
	@Override
	public void renderTo(SpriteBatch batch) {
		loadingScreen.renderTo(batch);
	}
	
	@Override
	public void updateApplication(Application app) {
		// If the AssetManager is not finished loading, load some of them and print our progress to the console.
		if (!assets.update()) {
			float percent = (int)(assets.getProgress()*100);
			if(oldPercent!= percent) { 
				oldPercent = percent;
				System.out.println("Assets are " + percent + "% loaded.");
			}
		}
		else {
			System.out.println("Assets are 100% loaded.");
			// Switch the app's state to the state designated to be switched to when we are finished loading assets.
			app.setState(exitState);
		}
	}
	
	/**
	 * @return the percentage of loading completion in decimal form.
	 */
	public float percentComplete() {
		return assets.getProgress();
	}
	
	/**
	 * @return the LoadingState's assetManager.
	 */
	public AssetManager assetManager() {
		return assets.manager;
	}
	
	/**
	 * Set the state to enter when loading is complete.
	 */
	public void setExitState(ApplicationState newExitState) {
		this.exitState = newExitState;
	}
	
}