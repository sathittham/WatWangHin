package com.sathittham.watwanghin;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CalendarActivity extends Activity {

	private static final String url = "http://www.sathittham.com/watwanghin/wanghin_calendar.html";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);
		
		

		// Threading
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// WebView1
				WebView myWebView = (WebView) findViewById(R.id.webView1);
				myWebView.setScrollbarFadingEnabled(true);
				myWebView.loadUrl(url);
				
				WebSettings webSettings = myWebView.getSettings();
				webSettings.setJavaScriptEnabled(true);
				
				
				
			}

		});
	}

}
