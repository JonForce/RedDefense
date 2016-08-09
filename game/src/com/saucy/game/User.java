package com.saucy.game;

import com.badlogic.gdx.Preferences;

public class User {
	
	private static final String
		// User account properties :
		COIN_COUNT = ":CoinCount",
		HIGH_SCORE = ":HighScore";
	
	private final int ID;
	private final Preferences preferences;
	
	/** Create an access point to save User data.
	 * @param ID The User's unique ID. */
	public User(int ID, Preferences preferences) {
		this.ID = ID;
		this.preferences = preferences;
	}
	
	/** @return the User's highest score. */
	public int highScore() {
		return preferences().getInteger(username() + HIGH_SCORE);
	}
	
	/** @return the number of jbs coins that the User has. */
	public int coins() {
		return preferences().getInteger(username() + COIN_COUNT);
	}
	
	/** Set the User's High Score. */
	public void setHighScore(int newScore) {
		preferences().putInteger(username() + HIGH_SCORE, newScore);
		save();
	}
	
	/** Set the number of coins that User has. */
	public void setCoinCount(int newCount) {
		preferences().putInteger(username() + COIN_COUNT, newCount);
		save();
	}
	
	/** Add n number of coins to the User's account. */
	public final void addCoins(int coinsToAdd) {
		setCoinCount(coins() + coinsToAdd);
		
		if (coins() < 0)
			throw new RuntimeException("User cannot have a negative number of jbs coins.");
	}
	
	/** Remove n number of coins from the User's account. */
	public final void removeCoins(int coinsToRemove) {
		addCoins(-coinsToRemove);
	}
	
	/** Save the preferences to disk. */
	public void save() {
		preferences().flush();
	}
	
	public void buyTurret(int turretId) {
		preferences.putBoolean("HasTurret"+turretId, true);
	}
	
	public void unBuyTurret(int turret) {
		preferences.putBoolean("HasTurret"+turret, false);
	}
	
	public boolean ownsTurret(int turret) {
		return preferences.getBoolean("HasTurret"+turret);
	}
	
	/** @return the Preferences that User data is saved to. */
	protected Preferences preferences() {
		return preferences;
	}
	
	/** @return the User's name, a unique string that can be used to store data to. */
	protected String username() {
		return "User_" + ID;
	}
}