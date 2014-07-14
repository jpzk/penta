package com.example.happ.highscores;

import com.example.happ.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HighscoreAdapter extends ArrayAdapter<Highscore> {

	public HighscoreAdapter(Context context) {
		super(context, 0);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Highscore score = getItem(position);
	    if (convertView == null) {
	    	convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_highscore, parent, false);
	    }
	    
	    TextView rankv = (TextView) convertView.findViewById(R.id.tvRank);
	    TextView playerv = (TextView) convertView.findViewById(R.id.tvPlayer);
	    TextView scorev = (TextView) convertView.findViewById(R.id.tvHighscore);
	    
	    rankv.setText(String.valueOf(position));
	    playerv.setText(score.player);
	    scorev.setText(score.score);
		
		return convertView;
	}
}
