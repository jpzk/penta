package com.example.happ;

import java.io.IOException;

import com.example.happ.game.GameFragment;
import com.example.happ.highscores.HighscoreFragment;
import com.example.happ.network.NetworkManager;
import com.example.happ.sound.SoundManager;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends FragmentActivity {

	// Game UI
	GameFragment mGameFragment;
	HighscoreFragment mHighscoreFragment;

	// Network
	NetworkManager mNetwork;
	
	// LocalStore
	LocalStore mStore;
	
	// SoundManager
	SoundManager mSoundManager;
	
	// Pager Adapter
	GamePagerAdapter mPagerAdapter;
	
	// ViewPager
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Remove title bar
	    this.requestWindowFeature(Window.FEATURE_NO_TITLE);

	    // Remove notification bar
	    this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
	    		WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);

		// Initialize SoundManager
		this.mSoundManager = new SoundManager(getApplicationContext(),
				getAssets());
		try {
			this.mSoundManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		} // load sound, pool etc.
		
		this.mSoundManager.startSound(); // start thread
		
		// Initialize local storage
		this.mStore = new LocalStore(this);
		
		// Initialize the network manager
		this.mNetwork = new NetworkManager();
		
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
		FragmentManager fm = getSupportFragmentManager();
        mPagerAdapter = new GamePagerAdapter(fm, this.mSoundManager);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
	}
	
	public void changeToRegister() {
		mViewPager.setCurrentItem(0);
	}
	
	public void changeToGame() {
		mViewPager.setCurrentItem(1);
	}
	
	public void changeToHighscore() {
		mViewPager.setCurrentItem(2);
	}
	
	public SoundManager getSoundManger() {
		return mSoundManager;
	}
	
	public LocalStore getLocalStore() {
		return mStore;
	}
	
	public NetworkManager getNetworkManager() {
		return mNetwork;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Stop sound engine
		this.mSoundManager.stopSound();
	}

}
