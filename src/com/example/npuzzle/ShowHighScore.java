package com.example.npuzzle;

import java.util.StringTokenizer;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class ShowHighScore extends Activity {

	public static final String HIGHSCORE = "HighScore";
	public static final String HIGHNAME = "HighName";
	public static final String HIGHSTEPS = "HighSteps";
	public static final String DIF_CHOSEN = "DifficultyChosen";
	
	String[] sHighNames;
	int[] iHighScores;
	int iDif;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	Bundle extras = getIntent().getExtras();
    	iDif = extras.getInt(DIF_CHOSEN);
    	
    	setContentView(R.layout.hs_overview);
    	boolean bValues = GetHighScore();
    	
    	if(bValues) {
	    	TextView[] txtStep = new TextView[iHighScores.length];
	    	TextView[] txtName = new TextView[iHighScores.length];
	    	
	    	int count = 0;
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs1);
	    		txtStep[count] = (TextView) findViewById(R.id.steps1);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs2);
	    		txtStep[count] = (TextView) findViewById(R.id.steps2);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs3);
	    		txtStep[count] = (TextView) findViewById(R.id.steps3);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs4);
	    		txtStep[count] = (TextView) findViewById(R.id.steps4);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs5);
	    		txtStep[count] = (TextView) findViewById(R.id.steps5);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs6);
	    		txtStep[count] = (TextView) findViewById(R.id.steps6);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs7);
	    		txtStep[count] = (TextView) findViewById(R.id.steps7);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs8);
	    		txtStep[count] = (TextView) findViewById(R.id.steps8);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs9);
	    		txtStep[count] = (TextView) findViewById(R.id.steps9);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
	    	
	    	if(iHighScores.length>count) {
	    		txtName[count] = (TextView) findViewById(R.id.hs10);
	    		txtStep[count] = (TextView) findViewById(R.id.steps10);
	    		
	    		txtName[count].setText(sHighNames[count]);
	    		txtStep[count].setText(String.valueOf(iHighScores[count]));
	    		count++;
	    	}
    	}
    	
    	
    }

    String[] ReadHSData() {
    	// restore local data return string to be read
        SharedPreferences prefs = getSharedPreferences(HIGHSCORE + Integer.toString(iDif) , 0);
        
        String[] sHighScore = new String[2];
        
        sHighScore[0] = prefs.getString(HIGHNAME, "");
        sHighScore[1] = prefs.getString(HIGHSTEPS, "");
        
        return sHighScore;
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
    
    
}
