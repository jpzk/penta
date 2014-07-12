package com.example.happ.network;

import com.loopj.android.http.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Queue;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.example.happ.LocalStore;
import com.example.happ.MainActivity;
import com.example.happ.game.GameFragment;
import com.example.happ.game.RegisterFragment;
import com.example.happ.game.Scorer;
import com.example.happ.highscores.Highscore;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkManager {

	private static final String TAG = "NetworkManager";

	private String URL = "https://highscore.madewithtea.com/";
	private Activity mActivity;
	private AsyncHttpClient mClient;

	public NetworkManager(Activity activity) {
		mActivity = activity;
		mClient = new AsyncHttpClient();

		// for not checking the SSL certs signature
		mClient.setSSLSocketFactory(MySSLSocketFactory.getFixedSocketFactory());
	}

	public boolean isOnline() {
		ConnectivityManager connMgr = (ConnectivityManager) mActivity
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}

	public void getAuthToken(final String pUsername, final String pPassword,
			final int score, final Scorer caller) {
		
		String url = URL + "/oauth2/access_token/";
		
		// Get the Auth Token
		RequestParams params = new RequestParams();
		params.put("username", pUsername);
		params.put("password", pPassword);
		params.put("grant_type", "password");
		params.put("client_id", pUsername);
		
		mClient.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				try {
					String authToken = response.getString("access_token");
					caller.onAuthToken(authToken, score);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
		});	
	}
	
	public void sendMatch(final String pAuthToken, final int score, final Scorer caller) {
		
		String url = URL + "user/matches/";
		
		RequestParams params = new RequestParams();
		params.put("score", score);
		
		mClient.removeAllHeaders();
		mClient.addHeader("Authorization", " bearer " + pAuthToken);
		mClient.post(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
					Log.v(TAG, String.valueOf(statusCode));
			}
		});
	}
	
	public void registerPlayer(final String pPlayerName,
			final RegisterFragment caller) {

		String url = URL + "registration/";

		RequestParams params = new RequestParams();
		params.put("username", pPlayerName);

		mClient.post(url, params, new JsonHttpResponseHandler() {
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {

				if (response.has("password")) {
					String pw;
					try {
						pw = response.getString("password");
						caller.onRegistered(pPlayerName, pw);

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject response) {
				if (response.has("error")) {
					try {
						int code = response.getInt("error");
						if(code == 0) {
							caller.onUsernameTaken(pPlayerName);
							Log.v(TAG, "Playername is taken.");
						}
						if(code == 2) {
							caller.onInvalid();
							Log.v(TAG, "Playername is invalid.");
						}
						Log.v(TAG, response.getString("error"));
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		});

	}

	public void getHighscorePage(int page, final ArrayAdapter<Highscore> adapter) {

		String url = URL + "highscores/" + String.valueOf(page);

		AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Log.e(TAG, arg3.getMessage());
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] json) {

				try {
					JSONArray jsonArray = new JSONArray(new String(json));
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject obj = jsonArray.getJSONObject(i);
						String name = obj.getString("player_name");
						String score = obj.getString("score");
						adapter.add(new Highscore(name, score));
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		mClient.get(url, handler);

		return;
	}
}
