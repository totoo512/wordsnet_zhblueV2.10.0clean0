package cn.edu.zjicm.zhengjun.words.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.fragment.DictionaryFragment;
import cn.edu.zjicm.zhengjun.words.fragment.MyWordsFragment;


public class MainActivity extends FragmentActivity {
	public static void startActivity(Context context) {
		Intent i = new Intent(context, MainActivity.class);
		context.startActivity(i);
	}

	private IndicatorViewPager indicatorViewPager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_main);

		final ViewPager viewPager = (ViewPager) findViewById(R.id.tabmain_viewPager);

		Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
		indicator.setScrollBar(new ColorBar(getApplicationContext(),
				getResources().getColor(R.color.pink), 5));

		int selectColor = getResources().getColor(R.color.pink);
		int unSelectColor = getResources().getColor(R.color.tab_text_color);
		indicator.setOnTransitionListener(new OnTransitionTextListener()
				.setColor(selectColor, unSelectColor).setSize(17, 17));

		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {
		private String[] tabNames = { "词典", "我的单词本"};
		private LayoutInflater inflater;

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			inflater = LayoutInflater.from(getApplicationContext());
		}

		@Override
		public int getCount() {
			return tabNames.length;
		}

		@Override
		public View getViewForTab(int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = (TextView) inflater.inflate(R.layout.tab_main,
						container, false);
			}

			TextView textView = (TextView) convertView;
			textView.setText(tabNames[position]);

			return textView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new DictionaryFragment();
				break;
			case 1:
				fragment = new MyWordsFragment();
				break;
			default:
				break;
			}
			return fragment;
		}
	}

}
