package com.example.happ;

import java.io.IOException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends FragmentActivity {

	// Game UI
	GameFragment mGameFragment;
	HighscoreFragment mHighscoreFragment;

	// SoundManager
	SoundManager mSoundManager;
	
	// Pager Adapter
	GamePagerAdapter mPagerAdapter;
	
	// ViewPager
	ViewPager mViewPager;

	public class GamePagerAdapter extends FragmentStatePagerAdapter {
		
		private GameFragment mGameFragment;
		private HighscoreFragment mHighscoresFragment;
		
	    public GamePagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	    
	    private void setGameFragment(GameFragment fragment) {
	    	this.mGameFragment = fragment;
	    }
	    
	    private void setHighscoresFragment(HighscoreFragment fragment) {
	    	this.mHighscoresFragment = fragment;
	    }
	    
	    @Override
	    public Fragment getItem(int i) {
	    	if(i == 0) {
	    		return mGameFragment;
	    	} else {
	    		return mHighscoreFragment;
	    	}
	    }

	    @Override
	    public int getCount() {
	        return 2;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	        return "OBJECT " + (position + 1);
	    }
	}

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
		
		// Initialize fragments
		mGameFragment = new GameFragment();
		mGameFragment.setSoundManager(this.mSoundManager);
		mHighscoreFragment = new HighscoreFragment();
		mHighscoreFragment.setSoundManager(this.mSoundManager);
		
        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mPagerAdapter = new GamePagerAdapter(getSupportFragmentManager());
        mPagerAdapter.setGameFragment(this.mGameFragment);
        mPagerAdapter.setHighscoresFragment(this.mHighscoreFragment);
        
        // Initialize ViewPaper
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mPagerAdapter);
        
        // Add ViewPager to Fragments
        mGameFragment.setViewPager(mViewPager);
        mHighscoreFragment.setViewPager(mViewPager);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		// Stop sound engine
		this.mSoundManager.stopSound();
	}

}
