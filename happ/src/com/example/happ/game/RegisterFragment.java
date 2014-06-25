package com.example.happ.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.happ.LocalStore;
import com.example.happ.MainActivity;
import com.example.happ.R;
import com.example.happ.network.NetworkManager;
import com.example.happ.sound.SoundManager;

public class RegisterFragment extends Fragment {
	
	private static final String TAG = "RegisterFragment";

	// Activiy
	private MainActivity mActivity;
	
	// SoundManager
	private SoundManager mSoundManager;

	// Local store
	private LocalStore mStore;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		// Reference the sound manager, local store
		mActivity = (MainActivity) getActivity();
		mSoundManager = mActivity.getSoundManger();
		mStore = mActivity.getLocalStore();
		
		// Inflate the view
		int resId = R.layout.fragment_register;
		View root = inflater.inflate(resId, container, false);

		// Register Output
		final TextView registerOutput = (TextView) root.findViewById(R.id.register_output);
		
		// Skip button
		Button skip = (Button) root.findViewById(R.id.skip_btn);
		skip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).changeToGame();
				
			}
		});
		
		// Go button
		Button go = (Button) root.findViewById(R.id.go_btn);
		final EditText playerbox = (EditText) root.findViewById(R.id.playerbox);
		
		go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				MainActivity activity = ((MainActivity) getActivity());
				NetworkManager network = activity.getNetworkManager();
				
				String playerName = playerbox.getText().toString();
				if(playerName.equals("")) {
					registerOutput.setText("What is your Player Name?");
					return;
				}
				
				if(network.registerPlayer()) {
					mStore.putPlayerName(playerName);
					activity.changeToGame();
				} else {
					registerOutput.setText("Username is already taken.");
				}
			}
		});
		
		// Skip to game if network is not available.
		NetworkManager network = mActivity.getNetworkManager();
		if(!network.isOnline()) {
			mActivity.changeToGame();
		}
		
		return root;
	}
	
}
