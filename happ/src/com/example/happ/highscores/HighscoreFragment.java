package com.example.happ.highscores;

import com.example.happ.R;
import com.example.happ.MainActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class HighscoreFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Inflate the view
		View root = inflater.inflate(R.layout.fragment_highscore, container, false);
		initBackButton(root);
		
		return root;
	}
	
	private void initBackButton(View root) {
		ImageButton btn = (ImageButton) root.findViewById(R.id.highscore_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).changeToGame();
			}
		});
	}
}
