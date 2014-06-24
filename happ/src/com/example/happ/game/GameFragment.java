package com.example.happ.game;

import java.util.ArrayList;
import java.util.List;

import com.example.happ.MainActivity;
import com.example.happ.R;
import com.example.happ.sound.SoundManager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment {

	private static final String TAG = "GameFragment";

	// Helper lists for game logic
	private List<Integer> guess;
	
	// Components
	private IOBar mIOBar;
	private Numpad mNumpad;
	private Timer mTimer;
	private Scorer mScorer;
	
	// Game logic
	private Game game;
	private boolean started = false;

	// SoundManager
	private SoundManager mSoundManager;
	
	// Has to be parameter-less
	public GameFragment() {
	}

	/**
	 * Called when the Fragment view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.guess = new ArrayList<Integer>();
		this.game = new Game();
		
		// Reference the sound manager
		mSoundManager = ((MainActivity) getActivity()).getSoundManger();
		
		// Inflate the view
		int resId = R.layout.fragment_game;
		View root = inflater.inflate(resId, container, false);

		// Initialize components;
		mScorer = new Scorer(root);
		mIOBar = new IOBar(this, root);
		mNumpad = new Numpad(this, root);
		mTimer = new Timer(this, root);
		
		endMatch();		
		return root;
	}
	
	/**
	 * Called when number on numpad pressed.
	 * 
	 * @param number
	 */
	public void writeNumber(int number) {
		// First try? Start timer if first try
		if(!started) {
			if(mTimer.isMeasuring) {
				mTimer.stopMeasuring();
			}
			mTimer.startMeasuring();
			started = true;
		}
		
		mIOBar.writeNumber(number);
		
		// Write number in guess
		guess.add(number);

		// Perform check
		if (mIOBar.getCursor() == 5) {
			boolean success = game.performCheck(guess);
			if (success) {
				mSoundManager.playSuccess();
				mTimer.increaseTimeBudgetCap();
				nextNumber();
			} else {
				// show response, clear guess and set cursor to 0 and switch
				// lines.
				mSoundManager.playFail();
				List<Integer> mask = game.getHint(guess);
				mIOBar.showHint(guess, mask);
				guess.clear();
				mIOBar.prepareNext();
			}
		}
	}	

	/**
	 * Prepare UI for the next number.
	 */
	public void nextNumber() {
		guess.clear();
		game.nextNumber();
		mIOBar.prepareGame();
	}
	
	/**
	 * Initialize a new match.
	 */
	public void initMatch() {
		started = false;
		mScorer.resetScore();
		nextNumber(); // new secret etc.
	}
	
	/**
	 * End a match due to time out or restart.
	 */
	public void endMatch() {
		Log.v(TAG, "Called endMatch in GameFragment");
		mTimer.stopMeasuring();
		initMatch();
	}
}
