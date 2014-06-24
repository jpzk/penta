package com.example.happ.game;

import android.os.SystemClock;
import android.util.Log;

public class TimerRunnable implements Runnable {
	
	private Timer mTimer;
	private long mLastRealtime = 0;
	private long mElapsedRealtime = 0;
	
	private static final String TAG = "TimerRunnable";
	
	public TimerRunnable(Timer timer) {
		mTimer = timer;
		mLastRealtime = SystemClock.elapsedRealtime();
	}
	
	@Override
	public void run() {
		// Check if time is up.
		long now = SystemClock.elapsedRealtime();
		if(mTimer.getTimeBudget() - (now - mLastRealtime) < 0) {
			Log.v(TAG, "Time is up, sending end match signal");
			mTimer.endMatchSignal();
			return;
		}
		
		now = SystemClock.elapsedRealtime();
		mElapsedRealtime = now - mLastRealtime;
		mTimer.setTimeBudget(mTimer.getTimeBudget() - mElapsedRealtime);
		mTimer.updateUISignal();
		mLastRealtime = SystemClock.elapsedRealtime();
	}
	

}
