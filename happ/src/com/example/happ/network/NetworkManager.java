package com.example.happ.network;

import java.util.ArrayList;
import java.util.List;

import com.example.happ.highscores.Highscore;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	
	private String URL = "https://highscore.madewithtea.com/";
	private Activity mActivity;
	
	public NetworkManager(Activity activity) {
		mActivity = activity;
	}
	
	public boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) 
				mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
	
	public boolean registerPlayer() {
		return false;
	}
	
	public ArrayList<Highscore> getHighscorePage(int page) {
		return new ArrayList<Highscore>();
	}
}
