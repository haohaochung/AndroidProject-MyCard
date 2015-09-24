package com.cardcamera;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.app.ActionBar actionBar = getActionBar();
		actionBar.hide();
		setContentView(R.layout.activity_home);
		
		ImageView iv = (ImageView)this.findViewById(R.id.imageView_pic);
		/*Animation am1 = new TranslateAnimation(0.0f, 100.0f, 1.0f, 100.0f);
		Animation am2 = new AlphaAnimation( 0, 1 );
		Animation am3 = new RotateAnimation( 0, 360, 0, 0 );
		AnimationSet am = new AnimationSet( false );
	    am.addAnimation( am1 );
	    am.addAnimation( am2 );
	    am.addAnimation( am3 );*/
		Animation am = new ScaleAnimation(0.0f, 0.9f, 1.0f, 1.0f);
		am.setDuration( 1000 );
		iv.setAnimation(am);
		am.start();
		
		new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                final Intent mainIntent = new Intent(HomeActivity.this, MainActivity.class);
	                startActivity(mainIntent);
	                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	                finish();        // 當跳到另一 Activity 時，讓 MainActivity 結束。
	                                // 這樣可以避免使用者按 back 後，又回到該 activity。
	            }
	        }, 3000);    
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	} 
	
}
