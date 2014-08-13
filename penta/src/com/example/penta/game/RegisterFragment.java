package com.example.penta.game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.example.penta.R;
import com.example.penta.LocalStore;
import com.example.penta.MainActivity;
import com.example.penta.network.NetworkManager;
import com.example.penta.sound.SoundManager;

public class RegisterFragment extends Fragment {

	private static final String TAG = "RegisterFragment";
	
	// UI elements
	private EditText mPlayerEditText;
	private TextView mOutput;
	private Button mSkip, mGo;
	
	// Activiy
	private MainActivity mActivity;

	// SoundManager
	private SoundManager mSoundManager;

	// Local store
	private LocalStore mStore;

	// Runnable for Registration
	class RegisterRunnable implements Runnable {
		@Override
		public void run() {
			MainActivity activity = ((MainActivity) getActivity());
			NetworkManager network = activity.getNetworkManager();

			String playerName = mPlayerEditText.getText().toString();
			if (playerName.equals("")) {
				mOutput.setText("What is your Player Name?");
				return;
			}
			if (!network.isOnline()) {
				mOutput.setText("You have to be online to register.");
				return;
			}	
			network.registerPlayer(playerName, RegisterFragment.this);			
		}
	} 
	
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

		// References UI elements
		mPlayerEditText = (EditText) root.findViewById(R.id.playerbox);
		mOutput = (TextView) root.findViewById(R.id.register_output);
		mGo = (Button) root.findViewById(R.id.go_btn);
		mSkip = (Button) root.findViewById(R.id.skip_btn);
		
		// Setup of Listeners
		
		// Skip button
		mSkip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).changeToGame();
			}
		});
		
		// When pressed Done and Enter on EditText
		mPlayerEditText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				(new RegisterRunnable()).run();
				return false;
			}
		});
		// Setting the Go Buttons Listener
		mGo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				(new RegisterRunnable()).run();
			}
		});
		
		// Skip to game if network is not available or player registered.
		NetworkManager network = mActivity.getNetworkManager();
		if (!network.isOnline() || mStore.hasPlayerName()) {
			mActivity.changeToGame();
		}

		return root;
	}
	
	public void onUsernameTaken(final String pPlayerName) {
		mOutput.setText("The player name is already taken.");
	}
	
	public void onInvalid() {
		mOutput.setText("The player name must be longer than 4" +
		" and at most 8 characters.");
	}
	
	public void onRegistered(final String pPlayerName, final String pPassword) {
		// Store password etc.
		mStore.putPassword(pPassword);
		mStore.putPlayerName(pPlayerName);
		
		// Change to the Game Fragment
		mActivity.changeToGame();	
	}
}
