package com.example.penta;

import java.io.IOException;

import com.example.penta.R;
import com.example.penta.network.NetworkManager;
import com.example.penta.sound.SoundManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MainActivity extends FragmentActivity {

	// Network
	NetworkManager mNetwork;

	// LocalStore
	LocalStore mStore;

	// SoundManager
	SoundManager mSoundManager;

	// Pager Adapter
	GamePagerAdapter mPagerAdapter;

	// ViewPager
	ViewPager mViewPager;

	// AdView
	private AdView mAdView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Remove title bar
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);

		// Remove notification bar
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_main);

		// Initialize local storage
		this.mStore = new LocalStore(this);

		// Initialize SoundManager
		this.mSoundManager = new SoundManager(getApplicationContext(),
				getAssets(), mStore);
		try {
			this.mSoundManager.load();
		} catch (IOException e) {
			e.printStackTrace();
		} // load sound, pool etc.

		this.mSoundManager.startSound(); // start thread

		// Initialize the network manager
		this.mNetwork = new NetworkManager(this);

		// ViewPager and its adapters use support library
		// fragments, so use getSupportFragmentManager.
		FragmentManager fm = getSupportFragmentManager();
		mPagerAdapter = new GamePagerAdapter(fm, this.mSoundManager);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);

		// Set Ads
		mAdView = new AdView(this);
		mAdView.setAdUnitId("ca-app-pub-2989103197995605/5567369774");
		mAdView.setAdSize(AdSize.SMART_BANNER);

		// Initiate a generic request.
		AdRequest request = new AdRequest.Builder()
	    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
	    .addTestDevice("EAC7E252F6AAEFA378521AC1ECE3829B")  // Moto G
	    .build();
		
		mAdView.loadAd(request);

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/mainLayout".
		LinearLayout layout = (LinearLayout) findViewById(R.id.container);
		layout.addView(mAdView);
	}

	// Standard Activity Lifecyle
	@Override
	protected void onDestroy() {
		super.onDestroy();

		mAdView.destroy();
		// Stop sound engine
		this.mSoundManager.stopSound();
	}

	@Override
	public void onPause() {
		mAdView.pause();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		mAdView.resume();
	}

	@Override
	public void onBackPressed() {
		changeToGame();
	}

	public void changeToRegister() {
		mViewPager.setCurrentItem(0);
	}

	public void changeToGame() {
		mViewPager.setCurrentItem(1);
	}

	public void changeToHighscore() {
		mViewPager.setCurrentItem(2);
	}

	public SoundManager getSoundManger() {
		return mSoundManager;
	}

	public LocalStore getLocalStore() {
		return mStore;
	}

	public NetworkManager getNetworkManager() {
		return mNetwork;
	}

	public void onNewPlayer() {
		mPagerAdapter.onNewPlayer();
	}
}
