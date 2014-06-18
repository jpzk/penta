package com.example.happ;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {
	
	private float volume;
	private SoundThread st;
	private SoundPool pool;
	private AssetManager manager;
	private int onClick, onFail, onSuccess;
	private Context context;

	public SoundManager(Context context, AssetManager manager) {
		this.context = context;
		this.manager = manager;
		this.volume = 1.0f;
	}

	public void load() throws IOException {
		AssetFileDescriptor sfd;

		this.pool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		this.st = new SoundThread(this.pool);
		
		sfd = this.manager.openFd("digitpressed.mp3");
		this.onClick = this.pool.load(sfd, 1);

		sfd = this.manager.openFd("fail.mp3");
		this.onFail = this.pool.load(sfd, 1);
		
		sfd = this.manager.openFd("success.mp3");
		this.onSuccess = this.pool.load(sfd, 1);
	}

	public void mute() {
		this.volume = 0.0f;
	}
	
	public void unmute() {
		this.volume = 1.0f;
	}
	
	public void stopSound() {
		this.st.sounds.add(new SoundItem(true)); // sending kill event
		this.pool.release(); 
	}
	
	public void startSound() {
		this.st.start(); // start thread
	}
	
	public void playClick() {
		this.st.sounds.add(new SoundItem(this.onClick, this.volume));
	}
	
	public void playSuccess() {
		this.st.sounds.add(new SoundItem(this.onSuccess, this.volume));
	}
	
	public void playFail() {
		this.st.sounds.add(new SoundItem(this.onFail, this.volume));
	}
}
