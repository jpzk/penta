package com.example.happ;

import android.widget.ImageView;

public class TimeBar {
	private ImageView bar;
	private int width; 
	
	public TimeBar(ImageView bar) {
		this.bar = bar;
	}
	
	public void setPercentage(final float percent) {
		bar.post(new Runnable() {
			@Override
			public void run() {
				bar.setScaleX(percent);
			}
		});
	}
	
	public float getPercentage() {
		return bar.getScaleX();
	}
}
