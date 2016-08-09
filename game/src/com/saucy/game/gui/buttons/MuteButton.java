package com.saucy.game.gui.buttons;

import com.badlogic.gdx.math.Vector2;
import com.saucy.framework.rendering.ui.Button;
import com.saucy.game.Game;

public class MuteButton extends Button {
	
	public static final String
		UNMUTED_ICON_SOURCE = "assets/GUI/Icons/Mute/On.png",
		MUTED_ICON_SOURCE = "assets/GUI/Icons/Mute/Off.png";
	
	private Game game;
	
	public MuteButton(Game game, Vector2 center) {
		super(center, game.audio().isMuted()? game.getTexture(MUTED_ICON_SOURCE) : game.getTexture(UNMUTED_ICON_SOURCE));
		this.game = game;
	}
	
	@Override
	public void onRelease() {
		System.out.println("MuteButton released!");
		// If the Button displaying the muted icon,
		if (this.isUsingMutedIcon()) {
			System.out.println("Unmuting...");
			// Unmute the audio.
			game.audio().unmute();
			System.out.println(super.texture());
			// Use the unmuted-icon.
			super.setTexture(game.getTexture(UNMUTED_ICON_SOURCE));
			System.out.println(super.texture());
		} else {
			System.out.println("Muting...");
			// Mute the audio.
			game.audio().mute();
			// Use the muted icon.
			super.setTexture(game.getTexture(MUTED_ICON_SOURCE));
		}
	}
	
	/* @return true if the Button is using the muted-icon. */
	protected boolean isUsingMutedIcon() {
		// Return true when the Button's Texture equals the muted-icon Texture.
		return texture().getTexture() == game.getTexture(MUTED_ICON_SOURCE);
	}
	
	/* @return true if the Button is using the unmuted-icon. */
	protected boolean isUsingUnmutedIcon() {
		// Return true when the Button's Texture equals the unmuted-icon Texture.
		return texture().getTexture() == game.getTexture(UNMUTED_ICON_SOURCE);
	}
}