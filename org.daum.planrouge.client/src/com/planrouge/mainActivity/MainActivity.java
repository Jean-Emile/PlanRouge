package com.planrouge.mainActivity;

import org.apache.cordova.DroidGap;


import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


public class MainActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.loadUrl("file:///android_asset/www/pages/login/index.html");     
	}
}
