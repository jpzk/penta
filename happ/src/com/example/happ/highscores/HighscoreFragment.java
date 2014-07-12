package com.example.happ.highscores;

import java.util.ArrayList;
import java.util.List;

import com.example.happ.LocalStore;
import com.example.happ.R;
import com.example.happ.MainActivity;
import com.example.happ.highscores.Highscore;
import com.example.happ.highscores.HighscoreAdapter;
import com.example.happ.network.NetworkManager;
import com.example.happ.sound.SoundManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;

public class HighscoreFragment extends Fragment {

	private static final String TAG = "HighscoreFragment";
	
	// Activiy
	private MainActivity mActivity;
	
	// SoundManager
	private SoundManager mSoundManager;
	
	// Network Manager
	private NetworkManager mNetwork;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mActivity = (MainActivity) getActivity();
		
		// Reference the sound manager, local store
		mSoundManager = mActivity.getSoundManger();
		mNetwork = mActivity.getNetworkManager();
		
		// Inflate the view
		View root = inflater.inflate(R.layout.fragment_highscore, container, false);
		initBackButton(root);
	
		// Load the first page of highscores
		ListAdapter adapter = new HighscoreAdapter(getActivity());
		
		// Async
		mNetwork.getHighscorePage(0, (ArrayAdapter<Highscore>) adapter);
		
		ListView lv = (ListView) root.findViewById(R.id.highscore_list);
		lv.setAdapter(adapter);
		
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
