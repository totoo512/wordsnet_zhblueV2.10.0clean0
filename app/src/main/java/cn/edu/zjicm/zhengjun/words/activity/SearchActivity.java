package cn.edu.zjicm.zhengjun.words.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ViewFlipper;

import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;

public class SearchActivity extends FragmentActivity implements OnClickListener {
	public static void startActivity(Context context) {
		Intent i = new Intent(context, SearchActivity.class);
		context.startActivity(i);
	}

	private ViewFlipper mainFlipper;
	private ListView listView;

	public EditText searchEditText;
	public TextView searchClearButton;
	public boolean searchTextEmpty;
	private boolean isFirstNotEmpty = true;

	private WordFactory wf;
	private InputMethodManager imm;
	private OneWord word = null;
	private List<HashMap<String, Object>> data;
	private boolean toSearch = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle("搜索");

		wf = WordFactory.getInstance(this);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);// 获得软键盘的对象

		setContentView(R.layout.activity_search);
		findViews();
		initViews();
		initSearchView();
	}

	private void findViews() {
		searchEditText = (EditText) findViewById(R.id.title_bar_search_edit);
		searchClearButton = (TextView) findViewById(R.id.title_bar_search_clear_button);// 清空搜索内容按钮

		mainFlipper = (ViewFlipper) findViewById(R.id.search_main_flipper);// 显示搜索到的单词的界面
		listView = (ListView) findViewById(R.id.listView_search);// 显示搜索到的单词的界面
	}

	public void initViews() {
		listView.setOnItemClickListener(listViewItemClickListener);
	}

	public void initSearchView() {
		searchEditText.addTextChangedListener(mTextWatcher);
		searchEditText.setOnClickListener(this);
		searchEditText.setOnEditorActionListener(enterKeyToSearch);
		setSearchTextEmpty(true);

		searchClearButton.setOnClickListener(this);
	}

	public boolean isSearchTextEmpty() {
		return searchTextEmpty;
	}

	public void setSearchTextEmpty(boolean searchTextEmpty) {
		if (this.searchTextEmpty == searchTextEmpty)
			return;

		this.searchTextEmpty = searchTextEmpty;
		if (searchTextEmpty) {
			searchEditText.setText("");

			searchClearButton.setVisibility(View.INVISIBLE);
			isFirstNotEmpty = true;
		} else if (isFirstNotEmpty) {
			searchClearButton.setVisibility(View.VISIBLE);
			isFirstNotEmpty = false;
		}
	}

	private void showWordsSearched() {// 显示listView中搜索到的内容conqueror() {
		mainFlipper.setDisplayedChild(0);
		if (searchEditText.getText().toString().length() == 0) {
			listView.setAdapter(null);
			return;
		}

		List<OneWord> searchs = wf.getSearchData(searchEditText.getText().toString());

		data = new ArrayList<HashMap<String, Object>>();
		for (OneWord word : searchs) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("lemma", word.getLemma());
			item.put("senses", word.getSenses());
			item.put("phonetic", word.getPhonetic());
			data.add(item);
		}

		BaseAdapter mBaseAdapter = new BaseAdapter() {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = View.inflate(SearchActivity.this, R.layout.view_word_note_item, null);
				TextView lemma = (TextView) view.findViewById(R.id.word_note_item_word);
				TextView phonetic = (TextView) view.findViewById(R.id.word_note_item_phe);
				TextView senses = (TextView) view.findViewById(R.id.word_note_item_translation);
				lemma.setText(data.get(position).get("lemma") + "");
				phonetic.setText(data.get(position).get("phonetic") + "");
				senses.setText(data.get(position).get("senses") + "");
				return view;
			}

			@Override
			public long getItemId(int position) {
				return 0;
			}

			@Override
			public Object getItem(int position) {
				return null;
			}

			@Override
			public int getCount() {
				return data.size();
			}
		};
		listView.setAdapter(mBaseAdapter);
	}

	private void showParaphrase(String lemma) {
		word = wf.getOneWordByLemma(lemma);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);// 点击按钮后将输入法自动隐藏掉
		if (word == null) {// 数据库中没有相应要查询的单词
			mainFlipper.setDisplayedChild(1);
		} else {
			cn.edu.zjicm.zhengjun.words.activity.WordDetailActivity.startActivity(SearchActivity.this, word.getIndex_word_id());
		}
	}

	@Override
	public void onClick(View view) {
		if (view == searchClearButton) {
			setSearchTextEmpty(true);
			showWordsSearched();
			imm.showSoftInput(searchEditText, 0); // 显示键盘
		} else if (view == searchEditText) {
			if (!toSearch) {
				showWordsSearched();
				mainFlipper.setDisplayedChild(0);
				if (isSearchTextEmpty()) {
					searchEditText.setSelection(0);// 设置光标位置
				}
			} else
				toSearch = false;
		}
	}

	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			setSearchTextEmpty(searchEditText.getText().toString().length() == 0);
			String str = searchEditText.getText().toString();
			int strLength = str.length();
			if (strLength > 0) {
				showWordsSearched();
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	OnEditorActionListener enterKeyToSearch = new OnEditorActionListener() {
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (actionId == EditorInfo.IME_ACTION_SEARCH) {
				showParaphrase(searchEditText.getText().toString());
				toSearch = true;
			}
			return false;
		}
	};

	OnItemClickListener listViewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			final TextView tv = (TextView) view.findViewById(R.id.word_note_item_word);
			searchEditText.removeTextChangedListener(mTextWatcher);
			searchEditText.setText("");
			searchEditText.append(tv.getText().toString());
			searchEditText.addTextChangedListener(mTextWatcher);
			showParaphrase(tv.getText().toString());
		}
	};


}
