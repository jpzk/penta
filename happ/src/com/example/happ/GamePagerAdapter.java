package com.example.happ;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.happ.game.GameFragment;
import com.example.happ.game.RegisterFragment;
import com.example.happ.highscores.HighscoreFragment;
import com.example.happ.sound.SoundManager;

public class GamePagerAdapter extends FragmentStatePagerAdapter {
	
	private RegisterFragment mRegisterFragment;
	private GameFragment mGameFragment;
	private HighscoreFragment mHighscoresFragment;
	
    public GamePagerAdapter(FragmentManager fm, SoundManager soundManager) {
        super(fm);
        
		// Initialize fragments
        mRegisterFragment = new RegisterFragment();
		mGameFragment = new GameFragment();
		mHighscoresFragment = new HighscoreFragment();
    }
    
    @Override
    public Fragment getItem(int i) {
    	if(i == 0) {
    		return mRegisterFragment;
    	} 
    	if(i == 1) {
    		return mGameFragment;
    	}
    	else {
    		return mHighscoresFragment;
    	}
    }

    public void onNewPlayer() {
    	mGameFragment.onNewPlayer();
    }
    
    @Override
    public int getCount() {
        return 3;
    }
}
