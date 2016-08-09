package com.saucy.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader.BitmapFontParameter;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.saucy.game.gui.Font;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	
	public AssetManager manager;
	
	public void init() {
		manager = new AssetManager();
	}
	
	/**
	 * Add all assets from an resource file pointed by resource to the AssetManagers queue.
	 * Supported Texture file extensions are : .png, .jpeg, and .jpg
	 * Supported Music file extensions are : .mp3
	 * Supported Sound file extendions are : .wav
	 * If the file has another extension, it is not added to the AssetManager's
	 * loading queue and instead prints a warning in the console.
	 */
	public void loadAssetsFrom(FileHandle folder) {
		if (!folder.isDirectory())
			throw new RuntimeException("Cannot load assets from a file.");
		
		for (FileHandle fh : folder.list()) {
			if (fh.isDirectory())
				loadAssetsFrom(fh);
			else {
				if (
					fh.extension().equals("png") ||
					fh.extension().equals("jpeg") ||
					fh.extension().equals("jpg"))
					manager.load(fh.path(), Texture.class);
				else if (fh.extension().equals("mp3"))
					manager.load(fh.path(), Music.class);
				else if (fh.extension().equals("wav"))
					manager.load(fh.path(), Sound.class);
				else if (fh.extension().equals("fnt")) {
					BitmapFontParameter fontParams = new BitmapFontParameter();
					//libgdx has a typo in minFilter lol
//					fontParams.minFitler = fontParams.maxFilter = TextureFilter.Linear; //lets make smooth textures the default yay!
					manager.load(fh.path(), BitmapFont.class, fontParams);
				} else
					System.out.println("Couldn't load " + fh.path() + ". Unknown extension.");
			}
		}
	}
	
	public boolean update(){
		if(manager.update()){
			return true;
		}
		return false;
	}
	
	public <T> T get(String path, Class<T> type){
		return manager.get(path, type);
	}
	
	public Texture get(String path) { return get(path, Texture.class); }
	
	public float getProgress() {
		return manager.getProgress();
	}
}
