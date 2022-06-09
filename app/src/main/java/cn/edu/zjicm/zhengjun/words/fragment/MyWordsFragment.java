package cn.edu.zjicm.zhengjun.words.fragment;

import android.os.Bundle;
//TODO:无法导入
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.adapter.MyWordListAdapter;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class MyWordsFragment extends Fragment {

	private List<OneWord> _list_date = new ArrayList<OneWord>();
	private ListView listView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_mywords, parent, false);
		listView = (ListView) v.findViewById(R.id.listview);

		return v;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			getData();
		}else{
			// fragment is no longer visible
		}
	}

	public void getData() {
		_list_date = WordFactory.getInstance(getActivity()).getAllMyWords();
		MyWordListAdapter adapter = new MyWordListAdapter(getActivity(), _list_date);
		listView.setAdapter(adapter);
	}

}
