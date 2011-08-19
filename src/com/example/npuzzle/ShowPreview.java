package com.example.npuzzle;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ShowPreview extends Activity implements OnClickListener {
	

	public static final String PIC_CHOSEN = "PictureChosen";
	private int picAddr;
	
	public static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
	public static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
	public static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.show_preview);
    	
    	Bundle extras = getIntent().getExtras();
    	picAddr = extras.getInt(PIC_CHOSEN);
    	
    	
    	SetImage();
    	
    	
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	//setContentView(R.layout.game_layout);

    	//DivideImage(picAddr, iDifficulty);
    	SetImage();
    	
    }


	@Override
	public void onClick(View v) {
		this.finish();
		
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
	
	public void SetImage() {
		int devWidth = getResources().getDisplayMetrics().widthPixels;
		int devHeight = GetScreenHeight();
		
    	float sizeChange;
    	Bitmap bMap = BitmapFactory.decodeResource(this.getResources(), picAddr);
    	
    	if((float)bMap.getWidth()/devWidth > (float)bMap.getHeight()/devHeight)	sizeChange = (float)bMap.getWidth()/devWidth;
		else	sizeChange = (float)bMap.getHeight()/devHeight;
		 
		int iWidth = (int)(bMap.getWidth()/sizeChange);
		int iHeight = (int)(bMap.getHeight()/sizeChange);
		 
		Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, iWidth, iHeight, false);
		bMap.recycle();
    	
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.layout_preview);
		
		
    	ImageView img = (ImageView) findViewById(R.id.preview);
    	img.setImageBitmap(bMapScaled);
    	
    	rl.setOnClickListener(this);
    	
    	//img.setOnClickListener(this);
    	//txt.setOnClickListener(this);
    	
    	
	}
	
}
