package com.example.happ;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GameFragment extends Fragment {

	// Helper lists for game logic
	private List<Integer> guess;

	// UI component list
	private List<ImageView> cellsTopView, cellsBottomView;
	private ImageView topIndicator, bottomIndicator;
	private TextView score, bestScore;
	private ImageButton muteButton;
	private TimeBar mTimeBar;
	
	// Game logic
	private Game game;

	// UI helper variables
	private boolean topLine = true;
	private boolean started = false;
	private int cursor = 0;

	// SoundManager
	private SoundManager soundManager;
	
	// ViewPager
	private ViewPager mViewPager;
	
	// Thread Pool
	private ScheduledThreadPoolExecutor mStpe;
	private Runnable timer;
	
	// Has to be empty
	public GameFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		this.guess = new ArrayList<Integer>();
		this.cellsTopView = new ArrayList<ImageView>();
		this.cellsBottomView = new ArrayList<ImageView>();
		this.game = new Game();
		
		// Inflate the view
		View root = inflater.inflate(R.layout.fragment_game, container, false);

		// Initialize all UI elements
		initCells(root);
		initNumpad(root);
		initSidebar(root);
		initIndicator(root);
		initScore(root);
		initHighscoreButton(root);
		initMuteButton(root);
		initTimeBar(root);
		
		// Start a new match
		initMatch();
		
		return root;
	}

	private void initTimeBar(View root) {
		// TODO Auto-generated method stub
		ImageView bar = (ImageView) root.findViewById(R.id.timebar);
		mTimeBar = new TimeBar(bar);
	}

	/*
	 * Initialize score
	 */
	private void initScore(View root) {
		score = (TextView) root.findViewById(R.id.score_number);
		bestScore = (TextView) root.findViewById(R.id.bestscore_number);
	}
	
	/*
	 * Initialize highscores button and set logic
	 */
	private void initHighscoreButton(View root) {
		ImageButton btn = (ImageButton) root.findViewById(R.id.highscore_btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initMatch();
				mViewPager.setCurrentItem(1);	
			}
		});
	}
	
	/*
	 * Initialize code and logic for mute button
	 */
	private void initMuteButton(View root) {
		muteButton = (ImageButton) root.findViewById(R.id.mute_btn);
		if(!soundManager.getMuteState()) {
			Drawable sound = getResources().getDrawable(R.drawable.sound);
			muteButton.setImageDrawable(sound);
		} else {
			Drawable sound = getResources().getDrawable(R.drawable.sound2);
			muteButton.setImageDrawable(sound);
		}
		
		// Get previous mute state
		muteButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean state = soundManager.muteunmute();
				if(!state) {
					Drawable sound = getResources().getDrawable(R.drawable.sound);
					muteButton.setImageDrawable(sound);
				} else {
					Drawable sound = getResources().getDrawable(R.drawable.sound2);
					muteButton.setImageDrawable(sound);
				}
			}
		});
	}
	
	/*
	 * Initialize the indicators UI
	 */
	private void initIndicator(View root) {
		int amountOfRows = 2;
		
		ViewGroup lines = (ViewGroup) root.findViewById(R.id.input);
		// Each line
		for(int i = 0; i < amountOfRows; i++) {
			ViewGroup row = (ViewGroup) ((ViewGroup) lines).getChildAt(i);
			ImageView indicatorRef = (ImageView) row.getChildAt(0);
			if(i == 0) {
				this.topIndicator = indicatorRef;
			} else {
				this.bottomIndicator = indicatorRef;
			}
		}
	}
	
	/*
	 * Initialize the cells UI
	 */
	private void initCells(View root) {
		ViewGroup lines = (ViewGroup) root.findViewById(R.id.input);
		int amountOfRows = 2;
		int cellsInRow = 5;
		
		// Each line
		for(int i = 0; i < amountOfRows; i++) {
			ViewGroup row;
			row = (ViewGroup) ((ViewGroup) lines.getChildAt(i)).getChildAt(1);
			// Each cell
			for(int k = 0; k < cellsInRow; k++) {
				ImageView cell = (ImageView) row.getChildAt(k);
				List<ImageView> list = (i == 0) ? cellsTopView : cellsBottomView;
				list.add(cell);
			}
		}		
	}
	
	/*
	 * Initialize the numpad UI
	 */
	private void initNumpad(View root) {
		ViewGroup numpad = (ViewGroup) root.findViewById(R.id.numpad_ref);
		int number = 1;
		int amountOfRows = 3;
		int btnsInRow = 3;
		
		// Each row
		for(int i = 0; i < amountOfRows; i++) {
			ViewGroup numpadRow = (ViewGroup) numpad.getChildAt(i);
			// Each button
			for (int k = 0; k < btnsInRow; k++) {
				ImageButton btn = (ImageButton) numpadRow.getChildAt(k);
				Drawable image = getNumberDrawable(number, 1);
				btn.setTag(number); // for onClickListener
				btn.setImageDrawable(image);
				
				// onClickListener for each button
				btn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						soundManager.playClick();
						ImageButton buttonClicked = (ImageButton) v;
						Integer number = (Integer) buttonClicked.getTag();
						writeNumber(number);
					}
				});
				number += 1;
			}
		}
	}
	
	/*
	 * Initialize the sidebar UI
	 */
	private void initSidebar(View root) {
		ImageButton initGameBtn = (ImageButton) root.findViewById(R.id.initGameBtn);
		initGameBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				initMatch();
			}
		});
	}

	
	private Drawable getNumberDrawable(int number, int type) {
		String fieldName = "@drawable/n" + String.valueOf(number) + "_"
				+ String.valueOf(type);
		String pkgName = getActivity().getPackageName();
		int identifier = getResources().getIdentifier(fieldName, null, pkgName);
		return getResources().getDrawable(identifier);
	}

	private void writeNumber(int number) {
		// First try? Start timer if first try
		if(!started) {
			// Start the thread for timer
			// Initial thread pool for game logic
			mStpe = new ScheduledThreadPoolExecutor(2);
			this.timer = new Runnable() {
				@Override
				public void run() {
					System.out.println("timer called");
					float pf = mTimeBar.getPercentage();
					mTimeBar.setPercentage(pf * 0.95f);
				}
			};
			mStpe.scheduleAtFixedRate(this.timer, 0, 1000, TimeUnit.MILLISECONDS);
			started = true;
		}
		
		// Get current line
		List<ImageView> line = topLine ? cellsTopView : cellsBottomView;
		
		// Get cell and set the number
		ImageView view = line.get(cursor);
		view.setImageDrawable(getNumberDrawable(number, 1));
		
		// Cursor for writing in cells
		cursor += 1;

		// Write number in guess
		guess.add(number);

		// Perform check
		if (cursor == 5) {
			boolean success = game.performCheck(guess);
			if (success) {
				System.out.println("Not implemented");
				soundManager.playSuccess();
				initGame();
			} else {
				// show response, clear guess and set cursor to 0 and switch
				// lines.
				soundManager.playFail();
				List<Integer> mask = game.getHint(guess);
				showHint(mask);
				prepareNextTry();
			}
		}
	}

	private void showIndicator(boolean top) {
		ImageView indicatorShow = (top) ? topIndicator : bottomIndicator;
		ImageView indicatorHide = (!top) ? topIndicator : bottomIndicator;
		indicatorShow.setVisibility(View.VISIBLE);
		indicatorHide.setVisibility(View.INVISIBLE);
	}
	
	private void showHint(List<Integer> mask) {
		List<ImageView> line = topLine ? cellsTopView : cellsBottomView;
		for (int i = 0; i < guess.size(); i++) {
			int gnum = guess.get(i);
			int type = mask.get(i);
			Drawable rDrawable = getNumberDrawable(gnum, type);
			line.get(i).setImageDrawable(rDrawable);
		}
	}

	private void prepareNextTry() {
		guess.clear();
		cursor = 0;
		// Switch lines, show indicator and erase content
		topLine = topLine ? false : true;
		showIndicator(topLine);
		
		List<ImageView> line = topLine ? cellsTopView : cellsBottomView;
		for (ImageView cell : line) {
			cell.setImageDrawable(getResources().getDrawable(R.drawable.empty));
		}
	}

	private void initGame() {
		// Start with top line
		this.topLine = true;
		this.cursor = 0;

		// Emptify all cells
		for (ImageView cell : this.cellsTopView) {
			Drawable empty = getResources().getDrawable(R.drawable.empty);
			cell.setImageDrawable(empty);
		}
		// Emptify all cells
		for (ImageView cell : this.cellsBottomView) {
			Drawable empty = getResources().getDrawable(R.drawable.empty);
			cell.setImageDrawable(empty);
		}
		
		// Set the indicator
		showIndicator(true);
		
		this.guess.clear();
		this.game.initGame();
	}
	
	private void initMatch() {
		score.setText(String.valueOf(0));
		bestScore.setText(String.valueOf(0));
		initGame(); // new secret etc.
		mTimeBar.setPercentage(1.0f);
		
		if(started) {
			mStpe.shutdown();
			this.timer = null;
		}
		started = false;
	}

	
	public void setSoundManager(SoundManager manager) {
		this.soundManager = manager;
	}

	public void setViewPager(ViewPager pager) {
		mViewPager = pager;
	}

}
