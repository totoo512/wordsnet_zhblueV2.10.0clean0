package cn.edu.zjicm.zhengjun.words.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;


public class AboutUsActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("关于");
		setContentView(R.layout.activity_about_us);
		TextView version = (TextView) findViewById(R.id.version_tv);
		int curVersionCode;
		String curVersionName;
		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo("cn.edu.zjicm.zhengjun.words", 0);
			curVersionCode = packageInfo.versionCode;
			curVersionName = packageInfo.versionName;
			String str = "版本号：" + curVersionCode + "\n"
					+ "版本名：" + curVersionName + "\n"
					+ "数据库版本号：" + WordFactory.getInstance(this).getVersion();
			version.setText(str);
		} catch (PackageManager.NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
