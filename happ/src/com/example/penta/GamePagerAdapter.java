package com.example.penta;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.penta.game.GameFragment;
import com.example.penta.game.RegisterFragment;
import com.example.penta.highscores.HighscoreFragment;
import com.example.penta.sound.SoundManager;

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
    	mHighscoresFragment.onNewPlayer();
    }
    
    @Override
    public int getCount() {
        return 3;
    }
}
