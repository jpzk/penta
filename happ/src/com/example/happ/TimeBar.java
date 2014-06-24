package com.example.happ;

import android.widget.ImageView;

public class TimeBar {
	private ImageView bar;
	private int width; 
	private float percentage;
	
	public TimeBar(ImageView bar) {
		this.bar = bar;
	}
	
	public void setPercentage(final float percent) {
		this.percentage = percent;
		bar.post(new Runnable() {
			@Override
			public void run() {
				bar.setScaleX(percent);
				float offset = (bar.getWidth() * percent) / 2;
				bar.setTranslationX(offset - (bar.getWidth() / 2));
			}
		});
	}
	
	public float getPercentage() {
		return this.percentage;
	}
}
