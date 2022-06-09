package cn.edu.zjicm.zhengjun.words.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.activity.AboutUsActivity;
import cn.edu.zjicm.zhengjun.words.activity.SearchActivity;
import cn.edu.zjicm.zhengjun.words.adapter.WordListAdapter;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class DictionaryFragment extends Fragment {

	private List<OneWord> _list_date;
	private ListView listView;
	private LinearLayout searchLayout;

	WordListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dictionary, parent, false);

		searchLayout = (LinearLayout) v.findViewById(R.id.search_layout);
		searchLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SearchActivity.startActivity(getActivity());
			}
		});

		listView = (ListView) v.findViewById(R.id.listview);
		getData(true);

		return v;
	}

	public void getData(boolean sortMode){
		_list_date = WordFactory.getInstance(getActivity()).getAllWords(sortMode);
		adapter = new WordListAdapter(getActivity(), _list_date);
		listView.setAdapter(adapter);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_all_word_list, menu);
	}

	boolean sortMode;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()) {
			case R.id.menu_item_sort:
				getData(sortMode);
				sortMode = !sortMode;
				return true;
			case R.id.menu_item_version:
				Intent i = new Intent(getActivity(), AboutUsActivity.class);
				getActivity().startActivity(i);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}
