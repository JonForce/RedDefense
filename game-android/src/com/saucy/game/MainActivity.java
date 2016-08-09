package com.saucy.game;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import com.flurry.android.FlurryAgent;
import android.os.Bundle;

public class MainActivity extends AndroidApplication {
	
	private static final int
		WINDOW_WIDTH = 800, WINDOW_HEIGHT = 600,
		VIRTUAL_WIDTH = 1024, VIRTUAL_HEIGHT = 768;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FlurryAgent.init(this, "TC4B4MK3KV7BPGKV483V");
//        new FlurryAgent.Builder()
//	        .withLogEnabled(false)
//	        .build(this, "TC4B4MK3KV7BPGKV483V");
        
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        initialize(new Game(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, true), cfg);
    }
}