package com.madewithtea.penta.game;

import com.madewithtea.penta.R;
import com.madewithtea.penta.LocalStore;
import com.madewithtea.penta.network.NetworkManager;

import android.view.View;
import android.widget.TextView;

public class Scorer {
	
	private static final String TAG = "Scorer";
	
	private TextView mScoreTV, mBestScoreTV, mPlayerName;
	private int mScore, mBestScore;
	private NetworkManager mNetwork;
	private LocalStore mStore;
	
	/**
	 * 
	 * @param fragment
	 * @param root
	 */
	public Scorer(View root, NetworkManager pNetwork, LocalStore pStore) {
		
		mScoreTV = (TextView) root.findViewById(R.id.score_number);
		mBestScoreTV = (TextView) root.findViewById(R.id.bestscore_number);
		mPlayerName = (TextView) root.findViewById(R.id.player_name);
		mStore = pStore;
		mNetwork = pNetwork;
		
		if(pStore.hasPlayerName()) { 
			mPlayerName.setText(mStore.getPlayerName());
		} else {
			mPlayerName.setText("Player");
		}
	}
	
	/**
	 * Reset the score.
	 */
	public void resetScore() {
		mScore = 0;
		mScoreTV.setText(String.valueOf(mScore));
	}
	
	/**
	 * Increment the score by one.
	 */
	public void incrementScore() {
		mScore += 1;
		mScoreTV.setText(String.valueOf(mScore));
	}
	
	/**
	 * Reset the best score.
	 */
	public void resetBestScore() {
		mBestScore = 0;
		mBestScoreTV.setText(String.valueOf(mBestScore));
	}
	
	/**
	 * Get the score.
	 * 
	 * @return
	 */
	public int getScore() {
		return mScore;
	}
	
	/**
	 * Get the best score.
	 * 
	 * @return
	 */
	public int getBestScore() {
		return mBestScore;
	}
	
	/**
	 * Set the best score
	 * 
	 * @param score
	 */
	public void setBestScore(int score) {
		mBestScore = score;
		mBestScoreTV.setText(String.valueOf(mBestScore));
	}

	public void setPlayerName(String player) {
		mPlayerName.setText(player);
	}
	
	public void onAuthToken(final String pAuthToken, final int pScore) {
		mNetwork.sendMatch(pAuthToken, pScore, this);
	}

	public void onNewPlayer() {
		if(mStore.hasPlayerName()) { 
			mPlayerName.setText(mStore.getPlayerName());
		} else {
			mPlayerName.setText("Player");
		}
	}
	
	public void uploadScore(final int score) {
		String pUsername = mStore.getPlayerName();
		String pPassword = mStore.getPassword();
		
		mNetwork.getAuthToken(pUsername, pPassword, score, this);
	}
}
