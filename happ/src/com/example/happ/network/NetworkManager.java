package com.example.happ.network;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkManager {
	
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
}
