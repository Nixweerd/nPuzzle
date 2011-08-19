package com.example.npuzzle;


import java.util.StringTokenizer;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class NPuzzleGame extends Activity implements OnClickListener, OnTouchListener {
	
	// define a "file name", of sorts, in which to store the preferences
	public static final String NPUZZLE_PREF = "NPuzzlePref";
	public static final String DIF_CHOSEN = "DifficultyChosen";
	public static final String PIC_CHOSEN = "PictureChosen";
	public static final String GAME_PLAY = "GamePlay";
	public static final String STEPCOUNT = "StepCount";
	
	public static final String HIGHSCORE = "HighScore";
	public static final String HIGHNAME = "HighName";
	public static final String HIGHSTEPS = "HighSteps";
	
	
	public static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	public static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	public static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	
	// define some nice names for our Dialog IDs
	public static final int DIALOG_EXIT = 0;
	public static final int NEW_GAME = 1;
	public static final int DIALOG_FINISHED = 2;
	public static final int DIALOG_HSNAME = 3;
	
	public int iPicture, iDifficulty, picAddr, iStepCount;
	private int[][] iGameBoard;
	Bitmap[] imgPic;
	
	int iWidth, iHeight;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.game_layout);
    	
    	if(getIntent().hasExtra(PIC_CHOSEN)) {
        	Bundle extras = getIntent().getExtras();
        	iPicture = extras.getInt(PIC_CHOSEN);
            iDifficulty = extras.getInt(DIF_CHOSEN);
            
            switch(iPicture) {
            	case 0: picAddr = R.drawable.puzzle_0;	break;
            	case 1: picAddr = R.drawable.puzzle_1;	break;
            	case 2: picAddr = R.drawable.puzzle_2;	break;
            	case 3: picAddr = R.drawable.puzzle_3;	break;
            	default:	picAddr = R.drawable.puzzle_0;
            }
            
            
            
            GenerateNew();
            
            StorePrefData();
            
            
        }
    	else {
            String sGamePlay = ReadPrefData();

            
            if(sGamePlay.length()<=1) {
            	Intent p = new Intent(this, NPuzzleActivity.class);
    			startActivity(p);
    			this.finish();
            }
            else {
            	restoreProgress(sGamePlay);
            }
    	}
    	
    	
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	setContentView(R.layout.game_layout);

    	DivideImage(picAddr, iDifficulty);
    	GenerateScreen();
    	
    }
    
    @Override
    public void onPause() {
        super.onPause();
        StorePrefData();
    }

    @Override
    protected void onStop(){
       super.onStop();
       StorePrefData();
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	String sGamePlay = ReadPrefData();
    	
        if(sGamePlay.length()<=1)	GenerateNew();
        else {
        	restoreProgress(sGamePlay);
        }
        
    }
    
    void GenerateNew() {
    	int i, j;
    	
    	iGameBoard = new int[iDifficulty][iDifficulty];
    	
    	for(i=0;i<iDifficulty;i++) {
    		for(j=0;j<iDifficulty && (i<iDifficulty-1 || j<iDifficulty-2);j++) {
    			
    			if( i==iDifficulty-1 && j==iDifficulty-3) {
    				if(iDifficulty%2 == 0){
    					iGameBoard[i][j] = iDifficulty*(iDifficulty-1-i)+1;
        				iGameBoard[i][j+1] = iDifficulty*(iDifficulty-1-i)+2;
    				}
    				else {
    					iGameBoard[i][j] = iDifficulty*(iDifficulty-1-i)+2;
        				iGameBoard[i][j+1] = iDifficulty*(iDifficulty-1-i)+1;
    				}
    			}
    			else {
    				iGameBoard[i][j] = iDifficulty*(iDifficulty-1-i)+iDifficulty-j-1;
    			}
    			
    			//iGameBoard[i][j] = i*iDifficulty + j + 1;
    			
    		}
    	}
    	
    	iGameBoard[iDifficulty-1][iDifficulty-1] = 0;
    	iStepCount = 0;
    	
    	DivideImage(picAddr, iDifficulty);
    	
    	StorePrefData();
    	
    	ShowPreview();
    	
    	//GenerateScreen();
    	
    	
    	
    }
    
    
    void restoreProgress(String sGamePlay) {
    	
    	picAddr = getPicAddr(iPicture);
    	
    	iGameBoard = new int[iDifficulty][iDifficulty];
    	
    	StringTokenizer tokens = new StringTokenizer(sGamePlay, ".");
    	
    	//String sInt;
    	
    	int i, j;
    	
    	for(i=0;i<iDifficulty;i++) {
        	for(j=0;j<iDifficulty;j++) {
        		//sInt = tokens.nextToken();
        		iGameBoard[i][j] = Integer.parseInt(tokens.nextToken());
        	}
        }
    	
    	DivideImage(picAddr, iDifficulty);
    	
    	GenerateScreen();
    	
    }
    
    public void GenerateScreen() {

    	
    	int i, j;
    	int count=0;
    	
    	//int HORIZONTAL = 0;
    	
    	RelativeLayout rLayout = (RelativeLayout)this.findViewById(R.id.root_layout);
        
    	if(rLayout.getChildCount() > 0)	rLayout.removeAllViews();
    	
    	//RelativeLayout[] gameRow = new LinearLayout[iDifficulty];
        
    	ImageView imgView;
        
    	
        
        rLayout.setGravity(0x11);
        //rLayout.setLayoutParams(new RelativeLayout.LayoutParams(, RelativeLayout.LayoutParams.WRAP_CONTENT));
        // create the layouts to hold buttons/pictures
    	for(i=0;i<iDifficulty;i++) {
    		//gameRow[i] = new LinearLayout(this);
    		
    		//gameRow[i].setPadding(5, 5, 5, 5);
    		//gameRow[i].setId(101);
            //gameRow[i].setOrientation(HORIZONTAL);
            // create buttons in a loop
            for (j = 0; j < iDifficulty; j++) {
            	if(iGameBoard[i][j]!= 0) {
	            	imgView = new ImageView(this);
	            	
	            	//if(iGameBoard[i][j]==0) 	imgPic[j].setImageBitmap(getBlackImage(picAddr, iDifficulty));
	            	//else	imgPic[j].setImageBitmap(getImage(picAddr, iDifficulty, iGameBoard[i][j]-1));
	            	
	            	imgView.setImageBitmap(imgPic[iGameBoard[i][j]]);
	            	
	            	// R.id won't be generated for us, so we need to create one
	            	imgView.setId(count+1);
	            	imgView.setPadding(1, 1, 1, 1);
	            	//imgPic[j].setAdjustViewBounds(true);
	            	
	            	// add our event handler (less memory than an anonymous inner class)
	            	
	            	imgView.setOnTouchListener(this);
	            	imgView.setOnClickListener(this);
	
	            	imgView.setLayoutParams(GetParams(count));
	            	rLayout.addView(imgView);
	            	
	            	
            	}
            	count++;
            }
            
            
            //gameRow[i].setGravity(0x11);
    		//gameRow[i].setWeightSum(iDifficulty);
            //rLayout.addView(gameRow[i]);
            
            
    	}
    }

    RelativeLayout.LayoutParams GetParams(int iNr) {
    	RelativeLayout.LayoutParams par = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	
    	//if(iNr==0)	{
    	//	par.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    	//	par.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    	//}
    	boolean bemptyb=false, bemptyl=false;
    	int i;
    	
    	for(i=0;i<iNr/iDifficulty;i++) {
    		if(iGameBoard[i][iNr%iDifficulty]==0)	bemptyb = true;
    	}
    	
    	for(i=0;i<iNr%iDifficulty;i++) {
    		if(iGameBoard[iNr/iDifficulty][i]==0)	bemptyl = true;
    	}
    	
    	if(iNr/iDifficulty == 0)	par.addRule(RelativeLayout.ALIGN_PARENT_TOP, R.id.root_layout);
    	else {
    		//if(iNr/iDifficulty == iDifficulty-1)	par.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    		//else	
    			
    			if(!bemptyb)	par.addRule(RelativeLayout.BELOW, iNr-iDifficulty+1);
    			else {
    				//if(iNr%iDifficulty == iDifficulty-1) {
    						if(iNr%iDifficulty == 0) par.addRule(RelativeLayout.ALIGN_BOTTOM, iNr+2);
    						else	par.addRule(RelativeLayout.ALIGN_BOTTOM, iNr);
    				//}
    				//else par.addRule(RelativeLayout.ABOVE, iNr+iDifficulty+1);
    			}
    	}
    	
    	
    	
    	if(iNr%iDifficulty != 0)	//par.addRule(RelativeLayout.ALIGN_PARENT_LEFT, R.id.root_layout);
    	 {
    		//if(iNr%iDifficulty == iDifficulty-1)	par.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    		//else	
    			if(!bemptyl)	par.addRule(RelativeLayout.RIGHT_OF, iNr);
    			else {
    				if(iNr%iDifficulty == iDifficulty-1) {
    					if((iNr/iDifficulty) != 0) par.addRule(RelativeLayout.ALIGN_RIGHT, iNr+1-iDifficulty);
						else	par.addRule(RelativeLayout.ALIGN_RIGHT, iNr+1+iDifficulty);
    				}
    				else par.addRule(RelativeLayout.LEFT_OF, iNr+2);
    			}
    	}
    	
    	
    	
    	return par;
    }
    
    
    
    void StorePrefData() {
    	// store data locally
        SharedPreferences prefs = getSharedPreferences(NPUZZLE_PREF, 0); 
        SharedPreferences.Editor editor = prefs.edit();
        //prefs.edit().clear().commit();
        editor.putInt(PIC_CHOSEN, iPicture);
        editor.putInt(DIF_CHOSEN, iDifficulty);
        editor.putInt(STEPCOUNT, iStepCount);
        
        String sGameBoard= new String();
        int i, j;
        
        for(i=0;i<iDifficulty;i++) {
        	for(j=0;j<iDifficulty;j++) {
        		sGameBoard += Integer.toString(iGameBoard[i][j]);
        		if(i!=iDifficulty-1 || j!= iDifficulty-1) sGameBoard += ".";
        		//sGameBoard.concat(".");
        	}
        }
        
        editor.putString(GAME_PLAY, sGameBoard);


        editor.commit();
    	
    }
    
    String[] ReadHSData() {
    	// restore local data return string to be read
        SharedPreferences prefs = getSharedPreferences(HIGHSCORE + Integer.toString(iDifficulty), 0);
        
        String[] sHighScore = new String[2];
        
        sHighScore[0] = prefs.getString(HIGHNAME, "");
        sHighScore[1] = prefs.getString(HIGHSTEPS, "");

        return sHighScore;
    }
    
    void StoreHSData(String[] HsNames, int[] HsSteps) {
    	
    	String tmpName = new String();
    	String tmpSteps = new String();
    	// store data locally
        int i;

        for(i=0;i<HsSteps.length;i++) {
    		tmpName += HsNames[i];
    		tmpSteps += Integer.toString(HsSteps[i]);
    		if(i!=HsSteps.length-1) {
    			tmpName += "!$&";
    			tmpSteps += "!$&";
    		}
        	
        }
        
        SharedPreferences prefs = getSharedPreferences(HIGHSCORE + Integer.toString(iDifficulty), 0); 
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(HIGHNAME, tmpName);
        editor.putString(HIGHSTEPS, tmpSteps);

        editor.commit();
    	
    }
    
    String ReadPrefData() {
    	// restore local data return string to be read
        SharedPreferences prefs = getSharedPreferences(NPUZZLE_PREF, 0);
        
        iPicture = prefs.getInt(PIC_CHOSEN, 0);
        iDifficulty = prefs.getInt(DIF_CHOSEN, 3);
        iStepCount = prefs.getInt(STEPCOUNT, 0);
        return prefs.getString(GAME_PLAY, "");
    }

    
    
    
    int getPicAddr(int iPic) {
    	int iPicAddr;
    	
    	
    	switch(iPic) {
	    	case 0: iPicAddr = R.drawable.puzzle_0;	break;
	    	case 1: iPicAddr = R.drawable.puzzle_1;	break;
	    	case 2: iPicAddr = R.drawable.puzzle_2;	break;
	    	case 3: iPicAddr = R.drawable.puzzle_3;	break;
	    	default:	iPicAddr = R.drawable.puzzle_0;
    	}
    	
    	return iPicAddr;
    }
    
    
    
    public void DivideImage(int imgAddr, int iSize) {
    	int i, j;
    	
    	imgPic = new Bitmap[iDifficulty*iDifficulty];
    	
    	int devWidth = getResources().getDisplayMetrics().widthPixels;
		int devHeight = GetScreenHeight(); 
		float sizeChange;
		 
		Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), imgAddr);
		 
		if((float)bMap.getWidth()/devWidth > (float)bMap.getHeight()/devHeight)	sizeChange = (float)bMap.getWidth()/devWidth;
		else	sizeChange = (float)bMap.getHeight()/devHeight;
		 
		iWidth = (int)(bMap.getWidth()/sizeChange/iSize);
		iHeight = (int)(bMap.getHeight()/sizeChange/iSize);
		 
		Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, iWidth*iSize, iHeight*iSize, false);
		bMap.recycle();
    	
    	
    	for(i=0;i<iDifficulty;i++) {
    		for (j = 0; j < iDifficulty; j++) {
    			if(i==iDifficulty-1 && j==iDifficulty-1)	imgPic[0] = Bitmap.createBitmap(iWidth-2, iHeight-2, bMapScaled.getConfig());
    			else imgPic[i*iDifficulty+j+1] = Bitmap.createBitmap(bMapScaled, iWidth*j+1, iHeight*i+1, iWidth-2, iHeight-2);
    			
    		}
    	}
    	
    	bMapScaled.recycle();
    	
    	
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
        		builder.setMessage(getString(R.string.dialog_exitgame))
        			   .setPositiveButton(getString(R.string.btnYes), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   NPuzzleGame.this.finish();
        				   }
        			   })
        			   .setNegativeButton(getString(R.string.btnNo), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   dialog.cancel();
        				   }
        			   });
        		// generate the dialog object from the options
        		// passed to the builder
        		dialog = builder.create();
    			break;
    		case NEW_GAME:
    			// build a dialog with a "Are you sure" message and two buttons:
        		// 'yes' will restart the game
        		// 'no' will close the dialog
        		builder.setMessage(getString(R.string.dialog_newgame))
        			   .setPositiveButton(getString(R.string.btnYes), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   GenerateNew();
        				   }
        			   })
        			   .setNegativeButton(getString(R.string.btnNo), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   dialog.cancel();
        				   }
        			   });
        		// generate the dialog object from the options
        		// passed to the builder
        		dialog = builder.create();
    			break;
    		case DIALOG_FINISHED:
    			// build a dialog with a "Are you sure" message and two buttons:
        		// 'yes' will kill the app
        		// 'no' will close the dialog
    			if(!IsHighScore()) {
    				builder.setMessage(getString(R.string.game_finished1) + iStepCount + getString(R.string.game_finished2));
        		}
    			else {
    				builder.setMessage(getString(R.string.game_finished1) + iStepCount + getString(R.string.game_finished2HS))
    					   .setNeutralButton(getString(R.string.btnSaveHS), new DialogInterface.OnClickListener() {
		     				   public void onClick(DialogInterface dialog, int which) {
		     					   showDialog(DIALOG_HSNAME);
		     					   
		     				   }
    					   });
    			}
        		//builder.setMessage("You have finished the game in " + iStepCount + " steps!\nDo you want to play another game?")
        		builder.setPositiveButton(getString(R.string.btnYes), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   GenerateNew();
        				   }
        			   })
        			   .setNegativeButton(getString(R.string.btnNo), new DialogInterface.OnClickListener() {
        				   public void onClick(DialogInterface dialog, int id) {
        					   NPuzzleGame.this.finish();
        				   }
        			   });
        		
        		// generate the dialog object from the options
        		// passed to the builder
        		dialog = builder.create();
    			break;
    		case DIALOG_HSNAME:
    			LayoutInflater li = LayoutInflater.from(this);
                View categoryDetailView = li.inflate(R.layout.dialog_hsname, null);
                
                builder.setTitle(getString(R.string.hsEnterName))
                		.setView(categoryDetailView)
                
                		.setPositiveButton(getString(R.string.btnOk), new DialogInterface.OnClickListener(){
                			@Override
			                public void onClick(DialogInterface dialog, int which) {
			                    AlertDialog categoryDetail = (AlertDialog)dialog;
			                    EditText et = (EditText)categoryDetail.findViewById(R.id.hsName);
			                    sNewHsName = new String();
			                    sNewHsName = et.getText().toString();
			                    
			                    SaveHighScore();
		     					ShowHighScore();
		                    	}
			                })
			            .setNegativeButton(getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				                return;
				            }}); 
                
                /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }});*/
                dialog = builder.create();
                
    			break;
    	}
    	
    	
		return dialog;
    	
    	
    }
    
    
    
    boolean CheckComplete() {
    	int i, j;
    	
    	for(i=0;i<iDifficulty;i++) {
    		for(j=0;j<iDifficulty && (i<iDifficulty-1 || j<iDifficulty-1);j++) {
    			if(iGameBoard[i][j] != i*iDifficulty+j+1)	return false;
    		}
    	}
    	
    	return true;
    	
    }
    
    
    public int GetScreenHeight() {
    	DisplayMetrics displayMetrics = new DisplayMetrics();
    	getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    	
    	int statusBarHeight;

    	switch (displayMetrics.densityDpi) {
    	    case DisplayMetrics.DENSITY_HIGH:
    	        statusBarHeight = HIGH_DPI_STATUS_BAR_HEIGHT;
    	        break;
    	    case DisplayMetrics.DENSITY_MEDIUM:
    	        statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
    	        break;
    	    case DisplayMetrics.DENSITY_LOW:
    	        statusBarHeight = LOW_DPI_STATUS_BAR_HEIGHT;
    	        break;
    	    default:
    	        statusBarHeight = MEDIUM_DPI_STATUS_BAR_HEIGHT;
    	}
    	
    	int iScreenheight = getResources().getDisplayMetrics().heightPixels - statusBarHeight - 10;
    	
    	return iScreenheight;
    }
    
    
    /** override the onCreateOptionsMenu that gets
     * called when the user requests a menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {

    	// can also use the add() method to programmatically create a menu
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.game_menu, menu);
    	return true;
    }
    
    
    // when an item is selected in the menu, onOptionsItemSelected
   	// is called and passes the selected 'item'
	public boolean onOptionsItemSelected(MenuItem item) {
	
		switch(item.getItemId()) {
			case R.id.exitgame: 	showDialog(DIALOG_EXIT); 	break;
			case R.id.reshuffle:	GenerateNew();				break;
			case R.id.difChange:	
				Intent d = new Intent(this, NPuzzleDifficulty.class);
				d.putExtra(PIC_CHOSEN, iPicture);
				startActivity(d);
				this.finish();
				break;
			case R.id.picChange:
				Intent p = new Intent(this, NPuzzleActivity.class);
				startActivity(p);
				this.finish();
				break;
			case R.id.preview:
				ShowPreview();
				break;
			case R.id.highScore:
				ShowHighScore();
				break;
		}

		return true;
	}
	
	void ShowPreview() {
    	Intent v = new Intent(this, ShowPreview.class);
		v.putExtra(PIC_CHOSEN, picAddr);
		startActivity(v);
    }
	
	
	String[] sHighNames;
	int[] iHighScores;
	public String sNewHsName;
	
	boolean IsHighScore() {
		int i;
		if( GetHighScore() && iHighScores.length==10) {
			for(i=0;i<iHighScores.length;i++) {
				if(iHighScores[i] < iStepCount)	return true;
			}
			return false;
		}
		else return true;
	}
	
	boolean GetHighScore() {
		String[] sHighScores = ReadHSData();
		
		if(sHighScores[0].length()>0) {
			
			StringTokenizer tokens = new StringTokenizer(sHighScores[1], "!$&");
	    	StringTokenizer tNames = new StringTokenizer(sHighScores[0], "!$&");
	    	iHighScores = new int[tokens.countTokens()];
	    	sHighNames = new String[tokens.countTokens()];
	    	int i;
	    	
	    	for(i=0;tokens.countTokens()>0;i++) {
	    		sHighNames[i] = tNames.nextToken();
	    		//sHighNames[i] = sHsNames[i];
	    		iHighScores[i] = Integer.parseInt(tokens.nextToken());
	    		
	    	}
	    	return true;
		}
		else	return false;
    }
	
	
	void SaveHighScore() {
		int[] iNewHsSteps;
		String[] sNewHsNames;
		int i, j;
		
		if(GetHighScore()) {
		
			if(iHighScores.length == 10) {
				sNewHsNames = new String[10];
				iNewHsSteps = new int[10];
			}
			else{
				sNewHsNames = new String[iHighScores.length+1];
				iNewHsSteps = new int[iHighScores.length+1];
			}
			
			
			
			for(i=0;i<iNewHsSteps.length-1;i++) {
				sNewHsNames[i] = sHighNames[i];
				iNewHsSteps[i] = iHighScores[i];
			}
			
			for(i=0;iHighScores.length>i && iHighScores[i]<=iStepCount;i++) {}
			
			for(j=iNewHsSteps.length-1;j>i;j--) {
				iNewHsSteps[j] = iHighScores[j-1];
				sNewHsNames[j] = sHighNames[j-1];
			}
		}
		else {
			sNewHsNames = new String[1];
			iNewHsSteps = new int[1];
			i=0;
		}
		
		iNewHsSteps[i] = iStepCount;
		sNewHsNames[i] = sNewHsName;
		
		StoreHSData(sNewHsNames, iNewHsSteps);
		
	}
	
	void ShowHighScore() {
    	Intent v = new Intent(this, TabHighScore.class);
    	v.putExtra(DIF_CHOSEN, iDifficulty);
		startActivity(v);
    	
    }

	private final static int START_DRAGGING = 0;
	private final static int STOP_DRAGGING = 1;
	
	private int iDraggingStatus;
	private ImageView image;
	private int iStartx, iStarty;
	
	
	@Override
	public boolean onTouch(View v, MotionEvent me) {
		
		if(v.getId()-1 <(iDifficulty*iDifficulty))	{
    		int x, y;
    		boolean bRegen = false;
    		y = (v.getId()-1)/iDifficulty;
    		x = (v.getId()-1)%iDifficulty;
    		
    		 
    		if(x+1 < iDifficulty && iGameBoard[y][x+1] == 0 ) {
    			
    			DraggingStuff(v, me, 1);
    			if(iDraggingStatus==STOP_DRAGGING ) {
    				if(DraggedEnough(me, 1)) {
	    				iGameBoard[y][x+1] = iGameBoard[y][x];
	    				iGameBoard[y][x] = 0;
    				}
    				bRegen = true;
    			}
    			
    		}
    		else if(x-1 >= 0 && iGameBoard[y][x-1] == 0) {
    			DraggingStuff(v, me, 2);
    			if(iDraggingStatus==STOP_DRAGGING) {
	    			if(DraggedEnough(me, 2)) {
	    				iGameBoard[y][x-1] = iGameBoard[y][x];
						iGameBoard[y][x] = 0;
	    			}
					bRegen = true;
    			}
    		}
    		else if(y+1 < iDifficulty && iGameBoard[y+1][x] == 0) {
    			DraggingStuff(v, me, 3);
    			if(iDraggingStatus==STOP_DRAGGING ) {
	    			if(DraggedEnough(me, 3)) {
	    				iGameBoard[y+1][x] = iGameBoard[y][x];
						iGameBoard[y][x] = 0;
	    			}
					bRegen = true;
    			}
    		}
    		else if(y-1 >= 0 && iGameBoard[y-1][x] == 0) {
    			DraggingStuff(v, me, 4);
    			if(iDraggingStatus==STOP_DRAGGING) {
    				if( DraggedEnough(me, 4)) {
    					iGameBoard[y-1][x] = iGameBoard[y][x];
    					iGameBoard[y][x] = 0;
    					
    				}
    				bRegen = true;
    			}
    		}
    		
    		if(bRegen)	{
    			iStepCount++;
    			GenerateScreen();
    			if(CheckComplete())	{
    				showDialog(DIALOG_FINISHED);
    			}
    			
    		}
    	}
		return false;
	}
    
	
	void DraggingStuff(View v, MotionEvent me, int iDir) {
		
		if (me.getAction() == MotionEvent.ACTION_MOVE) {
			int iOffset;
			if (iDraggingStatus == START_DRAGGING) {
				System.out.println("Dragging");
				
				int iROffset;
				switch(iDir) {
				
				// CHECK DIT SPUL!!!!
					case 1:
						
						iROffset = (int)me.getRawX();
						if((iROffset-iStartx) > iWidth+1) iOffset = iWidth+1;
						else {
							if((int)(iROffset-iStartx) < 1)	iOffset = 1;
							else iOffset = iROffset-iStartx;
						}
						
						image.setPadding(iOffset, 1, 0, 1);
						break;
						
					case 2:
						
						iROffset = (int)me.getRawX();
						if((iROffset-iStartx) < -(iWidth+1)) iOffset = iWidth+1;
						else {
							if((int)(iROffset-iStartx) > 1)	iOffset = 1;
							else iOffset = -(iROffset-iStartx);
						}
						
						image.setPadding(0, 1, iOffset, 1);
						break;
						
					case 3:
						
						iROffset = (int)me.getRawY();
						if((iROffset-iStarty) > iHeight+1) iOffset = iHeight+1;
						else {
							if((int)(iROffset-iStarty) < 1)	iOffset = 1;
							else iOffset = iROffset-iStarty;
						}
						
						image.setPadding(1, iOffset, 1, 0);
						break;
						
					case 4:
						
						iROffset = (int)me.getRawY();
						if((iROffset-iStarty) < -(iHeight+1)) iOffset = iHeight+1;
						else {
							if((int)(iROffset-iStarty) > 1)	iOffset = 1;
							else iOffset = -(iROffset-iStarty);
						}
						
						image.setPadding(1, 0, 1, iOffset);
						break;
						
				}
				
				image.invalidate();
			}
		}
		if (me.getAction() == MotionEvent.ACTION_DOWN) {
			iDraggingStatus = START_DRAGGING;
			image = (ImageView) findViewById(v.getId());
			iStartx = (int)me.getRawX();
			iStarty = (int)me.getRawY();
			
			//image.setImageBitmap(imgPic[v.getId()]);
			//layout.addView(image, params);
		}
		if (me.getAction() == MotionEvent.ACTION_UP) {
			iDraggingStatus = STOP_DRAGGING;
			Log.i("Drag", "Stopped Dragging");
		}
		
	}
	
	private final int TRESHOLDVAL = 20;
	
	boolean DraggedEnough(MotionEvent me, int ax) {
		
		switch(ax) {
			case 1:
				if( (me.getRawX()-iStartx)*100/iWidth > TRESHOLDVAL ||  java.lang.Math.abs((me.getRawX()-iStartx)*100/iWidth) < 1) return true;
				break;
			
			case 2:
				if( (me.getRawX()-iStartx)*100/iWidth < -TRESHOLDVAL ||  java.lang.Math.abs((me.getRawX()-iStartx)*100/iWidth) < 1) return true;
				break;
				
			case 3:
				if( (me.getRawY()-iStarty)*100/iHeight > TRESHOLDVAL ||  java.lang.Math.abs((me.getRawY()-iStarty)*100/iHeight) < 1) return true;
				break;
				
			case 4:
				if( (me.getRawY()-iStarty)*100/iHeight < -TRESHOLDVAL ||  java.lang.Math.abs((me.getRawY()-iStarty)*100/iHeight) < 1) return true;
				break;
		}
		
		return false;
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
