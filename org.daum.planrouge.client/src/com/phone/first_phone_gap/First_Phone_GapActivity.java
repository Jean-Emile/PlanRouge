package com.phone.first_phone_gap;

import org.apache.cordova.DroidGap;


import android.os.Bundle;


public class First_Phone_GapActivity extends DroidGap {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.init();

		super.loadUrl("file:///android_asset/www/login/index.html");
	
	}
	
//	public void onClose(Bundle savedInstanceState){
//		super.finish();
//	}
	
	//	public void onResume(Bundle savedInstanceState){
	//		super.onCreate(savedInstanceState);
	//		super.init();
	//	}
}
