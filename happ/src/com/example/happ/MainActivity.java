package com.example.happ;

import java.io.IOException;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	// Game UI
	Fragment gameFragment;

	// SoundManager
	SoundManager soundManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    // Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);

		this.soundManager = new SoundManager(getApplicationContext(),
				getAssets());
		try {
			this.soundManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		} // load sound, pool etc.
		
		this.soundManager.startSound(); // start thread

		this.gameFragment = new GameFragment();
		((GameFragment) this.gameFragment).setSoundManager(this.soundManager);
		
		// set listFragement view
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, this.gameFragment).commit();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Stop sound engine
		this.soundManager.stopSound();
	}

}
