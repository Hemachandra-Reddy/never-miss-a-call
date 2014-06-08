package com.nevermissacall.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.nevermissacall.R;
import com.nevermissacall.utils.Fonts;


public class SplashScreenActivity extends Activity {	
	ProgressDialog dialog;
	private TextView logoText;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		try {
			this.requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.splash);
			logoText = (TextView) findViewById(R.id.neverMissTxtID);
			logoText.setTypeface(Fonts.BOOK_ANTIQUA);
			final Animation a = AnimationUtils.loadAnimation(this, R.anim.set);
			a.reset();
			logoText.clearAnimation();
			logoText.startAnimation(a);
			handler.sendEmptyMessageDelayed(0, 2500);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			super.onActivityResult(requestCode, resultCode, data);
			if (requestCode == 123) {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Intent intent = new Intent(SplashScreenActivity.this,
							MainActivity.class);
					startActivityForResult(intent, 123);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	@Override
	public boolean isFinishing() {
		try {
			if (dialog != null) {
				dialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.isFinishing();
	}
}
