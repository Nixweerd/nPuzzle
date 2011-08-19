package com.example.npuzzle;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class NPuzzleActivity extends ListActivity implements OnItemClickListener {
	
	// define some nice names for our Dialog IDs
	static final int DIALOG_EXIT = 0;
	static final int NEW_GAME = 1;
	static final int OPTIONS_MENU = 2;
	static final int SHOW_HIGHSCORES = 3;
	
	// define a "file name", of sorts, in which to store the preferences
	public static final String PIC_CHOSEN = "PictureChosen";
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //initialize the pictures
        Field[] picfields = R.drawable.class.getFields();
		
		int count=0;
		
		for(int i=0;i<picfields.length;i++)
		{
			if(picfields[i].getName().startsWith("puzzle_")) {
				//picstring[count] = new String ( picfields[i].getName() );
				count++;
			}
		}
		
		String[] picstring = new String[count];
		count=0;
		for(int i=0;i<picfields.length;i++)
		{
			if(picfields[i].getName().startsWith("puzzle_")) {
				picstring[count] = new String ( picfields[i].getName() );
				count++;
			}
		}
		
		
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, picstring));
		
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		
		lv.setOnItemClickListener(this);
        
        
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
        					   NPuzzleActivity.this.finish();
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
   
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		TextView item = (TextView)v;
		int iPuzzleNr;

		switch(item.getText().toString().charAt(7)) {
			case '0': iPuzzleNr = 0; break;
			case '1': iPuzzleNr = 1; break;
			case '2': iPuzzleNr = 2; break;
			case '3': iPuzzleNr = 3; break;
			case '4': iPuzzleNr = 4; break;
			case '5': iPuzzleNr = 5; break;
			case '6': iPuzzleNr = 6; break;
			case '7': iPuzzleNr = 7; break;
			case '8': iPuzzleNr = 8; break;
			case '9': iPuzzleNr = 9; break;
			default: iPuzzleNr = 0;
		}
		
		Intent i = new Intent(this, NPuzzleDifficulty.class);
		i.putExtra(PIC_CHOSEN, iPuzzleNr);
		startActivity(i);
		this.finish();
		
	}
}