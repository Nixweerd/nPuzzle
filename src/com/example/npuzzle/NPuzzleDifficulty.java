package com.example.npuzzle;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class NPuzzleDifficulty extends Activity implements OnClickListener {

	
	// define some nice names for our Dialog IDs
	static final int DIALOG_EXIT = 0;
	
	
	// define a "file name", of sorts, in which to store the preferences
	//private final String NPUZZLE_PREF = "NPuzzlePref";
	public static final String DIF_CHOSEN = "DifficultyChosen";
	public static final String PIC_CHOSEN = "PictureChosen";
	
	private int iPic;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ldifficulty);
        
     // retrieve the set of data passed to us by the Intent
        Bundle extras = getIntent().getExtras(); 
        iPic = extras.getInt(PIC_CHOSEN);
        
        Button btnEasy = (Button)findViewById(R.id.btn_D3);
        Button btnMedium = (Button)findViewById(R.id.btn_D4);
        Button btnHard = (Button)findViewById(R.id.btn_D5);
        
        btnEasy.setOnClickListener(this);
        btnMedium.setOnClickListener(this);
        btnHard.setOnClickListener(this);
        
     // get our SharedPreferences object
    	//SharedPreferences prefs = getSharedPreferences(NPUZZLE_PREF, MODE_PRIVATE); 
		
		//prefs.edit().putInt(PIC_CHOSEN, iPic);
		//prefs.edit().commit();
        
    }
    
    public void onResume() {
    	
    	
    	// get our SharedPreferences object
//    	SharedPreferences prefs = getSharedPreferences(NPUZZLE_PREF, MODE_PRIVATE); 
//		
//		iPic = prefs.getInt(PIC_CHOSEN, 99);
    	
		super.onResume();
    }
    
    public void onPause() {
    	
    	
    	// get our SharedPreferences object
//    	SharedPreferences prefs = getSharedPreferences(NPUZZLE_PREF, MODE_PRIVATE); 
//		
//    	prefs.edit().clear();
//		prefs.edit().putInt(PIC_CHOSEN, iPic);
//		prefs.edit().commit();
    	
    	super.onPause();
    }
    
    public void onClick(View v) {
    	
    	int iDif=0;
    	
		switch(v.getId()) {
		
			case R.id.btn_D3: 	iDif = 3;	break;
			case R.id.btn_D4: 	iDif = 4;	break;
			case R.id.btn_D5: 	iDif = 5;	break;
		}
		
		Intent i = new Intent(this, NPuzzleGame.class);
		i.putExtra(DIF_CHOSEN, iDif);
		i.putExtra(PIC_CHOSEN, iPic);
		startActivity(i);
		this.finish();
		
		
    }
    
    /* onCreateDialog function that gets called by the system the first
     * time a dialog is requested.
     */
    protected Dialog onCreateDialog(int id)
    {
    	// we must return a dialog, so create one in memory
    	AlertDialog dialog = null;

    	// specifically, we'll build an "AlertDialog";
    	// the most common type of dialog.
    	AlertDialog.Builder builder = new AlertDialog.Builder(this); 
    	
    	switch(id) {
    		case DIALOG_EXIT:
    			// build a dialog with a "Are you sure" message and two buttons:
        		// 'yes' will kill the app
        		// 'no' will close the dialog
        		builder.setMessage("Are you sure you want to exit?")
        			   .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   NPuzzleDifficulty.this.finish();
        				   }
        			   })
        			   .setNegativeButton("No", new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   dialog.cancel();
        				   }
        			   });
        		// generate the dialog object from the options
        		// passed to the builder
        		dialog = builder.create();
    			break;
    	}
    	
    	
		return dialog;
    	
    	
    }
    
    
    /** override the onCreateOptionsMenu that gets
     * called when the user requests a menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {

    	// can also use the add() method to programmatically create a menu
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.puzzle_select_menu, menu);
    	return true;
    }
    
    
    // when an item is selected in the menu, onOptionsItemSelected
   	// is called and passes the selected 'item'
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()) {
			case R.id.exitgame: showDialog(DIALOG_EXIT); break;
		}

		return true;
	}
	
    
}
