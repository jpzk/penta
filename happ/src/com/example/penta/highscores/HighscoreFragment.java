package com.example.penta.highscores;

import java.util.ArrayList;
import java.util.List;

import com.example.penta.R;
import com.example.penta.LocalStore;
import com.example.penta.MainActivity;
import com.example.penta.highscores.Highscore;
import com.example.penta.highscores.HighscoreAdapter;
import com.example.penta.network.NetworkManager;
import com.example.penta.sound.SoundManager;

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
import android.widget.TextView;

public class HighscoreFragment extends Fragment {

	private static final String TAG = "HighscoreFragment";
	
	// UI Elements
	private TextView mRank, mPlayerName, mBestScore;
	
	// Activiy
	private MainActivity mActivity;
	
	// SoundManager
	private SoundManager mSoundManager;
	
	// Network Manager
	private NetworkManager mNetwork;
	private HighscoreAdapter mAdapter;
	
	// Store
	private LocalStore mStore;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		mActivity = (MainActivity) getActivity();
		
		// Reference the sound manager, local store
		mSoundManager = mActivity.getSoundManger();
		mNetwork = mActivity.getNetworkManager();
		mStore = mActivity.getLocalStore();
		
		// Inflate the view
		View root = inflater.inflate(R.layout.fragment_highscore, container, false);
	
		mRank = (TextView) root.findViewById(R.id.myrank);
		mPlayerName = (TextView) root.findViewById(R.id.myName);
		mBestScore = (TextView) root.findViewById(R.id.myBestscore);
		
		// Set the List Adapter
		mAdapter = new HighscoreAdapter(getActivity());
		ListView lv = (ListView) root.findViewById(R.id.highscore_list);
		lv.setAdapter(mAdapter);
		lv.setDivider(null);
		
		// Async
		getHighscorePage(0);
		getHighscore();
	
		return root;
	}
	
	public void getHighscorePage(int pPage) {
		if(mNetwork.isOnline()) {
			mNetwork.getHighscorePage(pPage, this);
		} else {
			return;
		}
	}

	public void getHighscore() {
		String username = mStore.getPlayerName();
		String password = mStore.getPassword();
		mNetwork.getAuthTokenH(username, password, this);
	}
	
	public void onAuthToken(String pAuthToken) {
		mNetwork.getHighscore(pAuthToken, this);
	}
	
	public void onPageResult(final ArrayList<Highscore> scores) {
		mAdapter.clear();
		mAdapter.addAll(scores);
	}
	
	public void onHighscoreResult(int score, int rank) {
		mPlayerName.setText(mStore.getPlayerName());
		mRank.setText(String.valueOf(rank));
		mBestScore.setText(String.valueOf(score));
	}

	public void onNewPlayer() {
		return;
	}

	public void onEndOfMatch() {
		this.getHighscorePage(0);
		this.getHighscore();
		return;
	}
}
