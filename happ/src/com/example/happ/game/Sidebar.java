package com.example.happ.game;

import com.example.happ.MainActivity;
import com.example.happ.R;
import com.example.happ.sound.SoundManager;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Sidebar {
	
	private static final String TAG = "Sidebar";
	
	private GameFragment mParent;
	private SoundManager mSoundManager;
	private Resources mRes;
	
	public Sidebar(GameFragment fragment, View root) {	
	
		mParent = fragment;
		mRes = fragment.getResources();
		
		MainActivity activity = ((MainActivity) fragment.getActivity());
		mSoundManager = activity.getSoundManger();
		
		final ImageButton muteButton;
		
		// Mute button
		muteButton = (ImageButton) root.findViewById(R.id.mute_btn);
		
		if(!mSoundManager.getMuteState()) {
			Drawable sound = mRes.getDrawable(R.drawable.sound);
			muteButton.setImageDrawable(sound);
		} else {
			Drawable sound = mRes.getDrawable(R.drawable.sound2);
			muteButton.setImageDrawable(sound);
		}
		
		// Get previous mute state
		muteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSoundManager.playSystemClick();
				boolean state = mSoundManager.muteunmute();
				mSoundManager.playSystemClick();
				if(!state) {
					Drawable sound = mRes.getDrawable(R.drawable.sound);
					muteButton.setImageDrawable(sound);
				} else {
					Drawable sound = mRes.getDrawable(R.drawable.sound2);
					muteButton.setImageDrawable(sound);
				}
			}
		});
		
		// Restart game
		ImageButton initGameBtn = (ImageButton) root.findViewById(R.id.initGameBtn);
		initGameBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSoundManager.playSystemClick();
				mParent.endMatch();
			}
		});
		
		// Highscore button
		ImageButton btn = (ImageButton) root.findViewById(R.id.highscore_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSoundManager.playSystemClick();
				mParent.endMatch();
				((MainActivity) mParent.getActivity()).changeToHighscore();
			}
		});
	}
}
