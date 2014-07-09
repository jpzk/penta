package com.example.happ.highscores;

import java.util.ArrayList;

public class Highscore {
	
	String player;
	String score;
	
	public Highscore(String player, String score) {
		this.player = player;
		this.score = score;
	}
	
	static public ArrayList<Highscore> getHighscores() {
		ArrayList<Highscore> entries = new ArrayList<Highscore>();
		entries.add(new Highscore(new String("player1"), new String("100")));
		entries.add(new Highscore(new String("player2"), new String("3242")));
		entries.add(new Highscore(new String("player3"), new String("342")));
		entries.add(new Highscore(new String("player4"), new String("234")));
		entries.add(new Highscore(new String("player5"), new String("5234")));
		entries.add(new Highscore(new String("player6"), new String("5234")));
		entries.add(new Highscore(new String("player7"), new String("523")));
		entries.add(new Highscore(new String("player8"), new String("52")));
		
		return entries;
	}
}
