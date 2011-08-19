package com.example.npuzzle;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class TabHighScore extends TabActivity {
	public static final String DIF_CHOSEN = "DifficultyChosen";
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tabhighscore);
	    
	    
	    Bundle extras = getIntent().getExtras();
    	int iDif = extras.getInt(DIF_CHOSEN);
	    
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Reusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, ShowHighScore.class);
	    
	    
	    // Initialize a TabSpec for each tab and add it to the TabHost
	    spec = tabHost.newTabSpec("easy").setIndicator(getString(R.string.hsEasy))
	                  .setContent(intent);
	    intent.putExtra(DIF_CHOSEN,3);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ShowHighScore.class);
	    
	    
	    spec = tabHost.newTabSpec("medium").setIndicator(getString(R.string.hsMedium))
	                  .setContent(intent);
	    intent.putExtra(DIF_CHOSEN, 4);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, ShowHighScore.class);
	    
	    
	    spec = tabHost.newTabSpec("hard").setIndicator(getString(R.string.hsHard))
	                  .setContent(intent);
	    intent.putExtra(DIF_CHOSEN, 5);
	    tabHost.addTab(spec);

	    tabHost.setCurrentTab(iDif-3);
	}
}
