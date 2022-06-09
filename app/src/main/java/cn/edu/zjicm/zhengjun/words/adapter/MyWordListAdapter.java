package cn.edu.zjicm.zhengjun.words.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.activity.WordDetailActivity;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class MyWordListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private View view;
	public List<OneWord> _listData = null;

	private static final String [] degrees ={"0","1","2","3","4","5","6","7","8","9","10"};

	public MyWordListAdapter(Context context, List<OneWord> list) {
		_listData = list;
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			view = mInflater.inflate(R.layout.view_my_word_note_item, null);
			final Holder holder = new Holder();
			holder.word = (TextView) view.findViewById(R.id.word_note_item_word);
			holder.phe = (TextView) view.findViewById(R.id.word_note_item_phe);
			holder.sense = (TextView) view.findViewById(R.id.word_note_item_translation);
			holder.senseDegree = (Spinner) view.findViewById(R.id.word_note_item_sense_degree);
			holder.timeSet = (TextView) view.findViewById(R.id.word_note_item_time_set);
			view.setTag(holder);
			convertView = view;
		}
		final OneWord word = getItem(position);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				WordDetailActivity.startActivity(mContext, word.getIndex_word_id());
			}
		});

		final Holder holder = (Holder) convertView.getTag();
		holder.word.setText(word.getLemma());
		holder.phe.setText(word.getPhonetic());
		holder.sense.setText(word.getSenses());
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		holder.timeSet.setText(formatter.format(word.getTime_set()));

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, degrees);
		//将适配器添加到spinner中去
		holder.senseDegree.setAdapter(adapter);
		holder.senseDegree.setSelection(word.getDegree_fm());
		holder.senseDegree.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				// TODO Auto-generated method stub
				if(word.getDegree_fm() != position) {
					word.setDegree_fm(position);
					word.setTime_set((new Date()).getTime());
					WordFactory.getInstance(mContext).updateDegreeOfMywords(word.getIndex_word_id(), word.getDegree_fm(), word.getTime_set());
					holder.timeSet.setText(formatter.format(word.getTime_set()));
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return _listData == null ? 0 : _listData.size();
	}

	@Override
	public OneWord getItem(int position) {
		// TODO Auto-generated method stub
		return _listData == null ? null : _listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return _listData == null ? 0 : position;
	}

	public class Holder {
		public TextView word;
		public TextView phe;//发音
		public TextView sense;

		public Spinner senseDegree;
		public TextView timeSet;
	}
}


