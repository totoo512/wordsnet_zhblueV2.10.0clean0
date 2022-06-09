package cn.edu.zjicm.zhengjun.words.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class WelcomeActivity extends Activity {
	private final int MIN_SHOW_TIME = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_welcome);

		InitTask initTask = new InitTask();
		initTask.execute();
	}

	private class InitTask extends AsyncTask<Object, String, Boolean> {
		@Override
		protected Boolean doInBackground(Object... params) {
			boolean result = false;
			long delay = MIN_SHOW_TIME - doSomething();
			if (delay > 0) {
				try {
					Thread.sleep(delay);
					Log.e("slepp", delay + "");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			MainActivity.startActivity(WelcomeActivity.this);

			finish();
			return result;
		}
	}

	private long doSomething() {
		long start = System.currentTimeMillis();
		// do sth

		WordFactory.getInstance(this); // 初始化数据库

		long end = System.currentTimeMillis();
		Log.e("inittime", end - start + "");


		return end - start;
	}


}
