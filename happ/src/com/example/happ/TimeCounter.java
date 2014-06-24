package com.example.happ;

import android.widget.TextView;

public class TimeCounter {
	private TextView ref;
	
	public TimeCounter(TextView ref) {
		this.ref = ref;
	}

	public void setTime(final float time) {
		ref.post(new Runnable() {
			@Override
			public void run() {
				ref.setText(String.valueOf((time / 1000.0f)) + " s");
			}
		});
	}
	
	
}
