package com.example.happ.game;

import com.example.happ.R;
import com.example.happ.MainActivity;
import com.example.happ.sound.SoundManager;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class Numpad {
	
	private static final String TAG = "Numpad";
	
	private GameFragment mParent;
	private SoundManager mSoundManager;
	private Resources mRes;
	
	/**
	 * 
	 * @param fragment
	 * @param root
	 */
	public Numpad(GameFragment fragment, View root) {
		
		mParent = fragment;
		mRes = mParent.getResources();
	
		MainActivity activity = ((MainActivity) fragment.getActivity());
		mSoundManager = activity.getSoundManger();
		
		ViewGroup numpad = (ViewGroup) root.findViewById(R.id.numpad_ref);
		int number = 1;
		int amountOfRows = 3;
		int btnsInRow = 3;
		
		// Each row
		for(int i = 0; i < amountOfRows; i++) {
			ViewGroup numpadRow = (ViewGroup) numpad.getChildAt(i);
			// Each button
			for (int k = 0; k < btnsInRow; k++) {
				ImageButton btn = (ImageButton) numpadRow.getChildAt(k);
				Drawable image = getNumberDrawable(number, 1);
				btn.setTag(number); // for onClickListener
				btn.setImageDrawable(image);
				
				// onClickListener for each button
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mSoundManager.playClick();
						ImageButton buttonClicked = (ImageButton) v;
						Integer number = (Integer) buttonClicked.getTag();
						mParent.writeNumber(number);
					}
				});
				number += 1;
			}
		}
	}
	
	/**
	 * Helper function to get number drawable.
	 * 
	 * @param number
	 * @param type
	 * @return
	 */
	private Drawable getNumberDrawable(int number, int type) {
		String fieldName = "@drawable/n" + String.valueOf(number) + "_"
				+ String.valueOf(type);
		
		String pkgName = mParent.getActivity().getPackageName();
		int identifier = mRes.getIdentifier(fieldName, null, pkgName);
		return mRes.getDrawable(identifier);
	}
}
