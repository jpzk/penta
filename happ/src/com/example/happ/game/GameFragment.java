package com.example.happ.game;

import java.util.ArrayList;
import java.util.List;

import com.example.happ.LocalStore;
import com.example.happ.MainActivity;
import com.example.happ.R;
import com.example.happ.network.NetworkManager;
import com.example.happ.sound.SoundManager;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GameFragment extends Fragment {

	private static final String TAG = "GameFragment";

	// Activiy
	private MainActivity mActivity;
	
	// Helper lists for game logic
	private List<Integer> guess;
	
	// Components
	private IOBar mIOBar;
	private Numpad mNumpad;
	private Timer mTimer;
	private Scorer mScorer;
	private Sidebar mSidebar;
	
	// Game logic
	private Game game;
	private boolean started = false;

	// SoundManager
	private SoundManager mSoundManager;
	
	// Local store
	private LocalStore mStore;
	
	// Network Manager
	private NetworkManager mNetwork;
	
	// Has to be parameter-less
	public GameFragment() {
	}

	/**
	 * Called when the Fragment view is created.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mActivity = (MainActivity) getActivity();
		
		guess = new ArrayList<Integer>();
		game = new Game();
		
		// Reference the sound manager, local store
		mSoundManager = mActivity.getSoundManger();
		mStore = mActivity.getLocalStore();
		mNetwork = mActivity.getNetworkManager();
		
		// Inflate the view
		int resId = R.layout.fragment_game;
		View root = inflater.inflate(resId, container, false);

		// Get Playername
		String playerName;
		if(mStore.hasPlayerName()) {
			playerName = mStore.getPlayerName();
		} else {
			playerName = new String("player");
		}
		
		// Initialize components;
		mScorer = new Scorer(root, mNetwork, mStore);
		mIOBar = new IOBar(this, root);
		mNumpad = new Numpad(this, root);
		mTimer = new Timer(this, root);
		mSidebar = new Sidebar(this, root);
		
		// Set the best score.
		mScorer.setBestScore(mStore.getBestScore());
		
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
				mIOBar.prepareNext();
				mTimer.increaseTimeBudgetCap();
				mScorer.incrementScore();
				guess.clear();
				game.nextNumber();
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
	 * Initialize a new match.
	 */
	public void initMatch() {
		started = false;
		mScorer.resetScore();
		mIOBar.prepareGame();
		guess.clear();
		game.nextNumber();
	}
	
	/**
	 * End a match due to time out or restart.
	 */
	public void endMatch() {
		Log.v(TAG, "Called endMatch in GameFragment");
		mTimer.stopMeasuring();
		initMatch();
	}
	
	/**
	 * Called when the game ends naturally by time-out.
	 */
	public void endMatchByTimeout() {
		Log.v(TAG, "Called endMatch in GameFragment by Time out");
		mTimer.stopMeasuring();
		
		// Update the best score.
		int score = mScorer.getScore();
		
		if(mNetwork.isOnline() && mStore.hasPlayerName()) {
			mScorer.uploadScore(score);
		}
		
		if(score > mScorer.getBestScore()) {
			mScorer.setBestScore(score);
			mStore.putBestScore(score);
		}
		
		initMatch();
	}
	
	public void onNewPlayer() {
		mScorer.onNewPlayer();
	}
}
