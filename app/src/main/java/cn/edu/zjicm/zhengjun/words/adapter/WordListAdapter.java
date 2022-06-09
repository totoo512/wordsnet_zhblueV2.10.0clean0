package cn.edu.zjicm.zhengjun.words.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.activity.WordDetailActivity;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class WordListAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private View view;
	public List<OneWord> _listData = null;

	public WordListAdapter(Context context, List<OneWord> list) {
		_listData = list;
		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			view = mInflater.inflate(R.layout.view_word_note_item, null);
			Holder holder = new Holder();
			holder.word = (TextView) view.findViewById(R.id.word_note_item_word);
			holder.phe = (TextView) view.findViewById(R.id.word_note_item_phe);
			holder.sense = (TextView) view.findViewById(R.id.word_note_item_translation);
			holder.senseDegree = (TextView) view.findViewById(R.id.word_note_item_sense_degree);
			holder.wordCheckBox = (CheckBox) view.findViewById(R.id.word_note_item_checkbox);
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
		//显示单选按钮
		holder.wordCheckBox.setVisibility(View.VISIBLE);
		//为单选按钮设置初始状态
		holder.wordCheckBox.setOnCheckedChangeListener(null);
		holder.wordCheckBox.setChecked(WordFactory.getInstance(mContext).isInTableMywords(word.getIndex_word_id()));
		//为单选按钮添加 OnCheckedChangeListener 监听器
		holder.wordCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					WordFactory.getInstance(mContext).insertIntoMywords(word.getIndex_word_id());
					Toast.makeText(mContext, word.getLemma() + "已加入我的单词本", Toast.LENGTH_SHORT).show();
				} else {
					WordFactory.getInstance(mContext).deleteFromMywords(word.getIndex_word_id());
					Toast.makeText(mContext, word.getLemma() + "已从我的单词本中删除", Toast.LENGTH_SHORT).show();
				}
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

		public TextView senseDegree;
		public CheckBox wordCheckBox;
	}
}


