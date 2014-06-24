package com.example.happ.game;

import com.example.happ.R;

import android.view.View;
import android.widget.TextView;

public class Scorer {
	
	private static final String TAG = "Scorer";
	
	private TextView mScoreTV, mBestScoreTV;
	private int mScore, mBestScore;
	
	/**
	 * 
	 * @param fragment
	 * @param root
	 */
	public Scorer(View root) {
		mScoreTV = (TextView) root.findViewById(R.id.score_number);
		mBestScoreTV = (TextView) root.findViewById(R.id.bestscore_number);
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
	 * Set the best score
	 * 
	 * @param score
	 */
	public void setBestScore(int score) {
		mBestScore = score;
		mBestScoreTV.setText(String.valueOf(mBestScore));
	}
}
