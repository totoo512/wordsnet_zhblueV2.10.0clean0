package cn.edu.zjicm.zhengjun.words.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import cn.edu.zjicm.zhengjun.words.R;
import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.data.RelationShip;
import cn.edu.zjicm.zhengjun.words.db.WordFactory;



public class WordDetailActivity extends FragmentActivity {
	private static final String BUNDLE_KEY_WORD_ID = "bundle_key_word_id";
	public static void startActivity(Context context, int wordId){
		Intent i = new Intent(context, WordDetailActivity.class);
		i.putExtra(BUNDLE_KEY_WORD_ID, wordId);
		context.startActivity(i);
	}

	private OneWord mOneWord;

	private TextView mWordText;
	private TextView mWordPhonetic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		int wordId = getIntent().getIntExtra(BUNDLE_KEY_WORD_ID, -1);
		mOneWord = WordFactory.getInstance(this).getOneWordById(wordId);
		if(mOneWord == null) {
			Toast.makeText(this, "单词不存在", Toast.LENGTH_SHORT).show();
			finish();
		}

		setTitle(mOneWord.getLemma());
		setContentView(R.layout.activity_word_detail);

		mWordText = (TextView) findViewById(R.id.word_detail_word);
		mWordPhonetic = (TextView) findViewById(R.id.word_detail_phonetic);

		mWordText.setText(mOneWord.getLemma());
		mWordPhonetic.setText(mOneWord.getPhonetic());

		TextView wordCn = (TextView) findViewById(R.id.word_inner_cn);
		wordCn.setText(mOneWord.getSenses());

		initViewForRelationship((LinearLayout)findViewById(R.id.word_inner_relation_ship_item_container));
		initViewForSameShape((LinearLayout)findViewById(R.id.word_inner_sameshape_detail));
	}

	private void initViewForRelationship(LinearLayout container) {
		List<RelationShip> mRelationShipList = WordFactory.getInstance(this).getRelationshipOfWord(mOneWord.getIndex_word_id());

		if (mRelationShipList != null) {
			container.removeAllViews();

			for (int i = 0; i < mRelationShipList.size(); i++) {
				RelationShip rs = mRelationShipList.get(i);
				View rsItem = createRelationshipItemView(rs, this);
				container.addView(rsItem);
			}
		}
	}

	/* 创建搭配视图 */
	private static View createRelationshipItemView(RelationShip rs, Context context) {
		View itemView = View.inflate(context, R.layout.view_relationship_item, null);

		// fill eng
		TextView enText = (TextView) itemView.findViewById(R.id.relation_en_string);
		createRelationEnContainerWithoutLink(enText, rs.getColloc_eng(), context);

		// fill cn
		TextView cnText = (TextView) itemView.findViewById(R.id.relation_cn_string);
		cnText.setText(rs.getColloc_cn());

		itemView.setPadding(0, 0, 0, 10);
		return itemView;
	}

	static void createRelationEnContainerWithoutLink(TextView tv, String str, Context context) {
		tv.setText(str);
		LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		para.rightMargin = (int) (10 * context.getResources().getDisplayMetrics().density);
		tv.setLayoutParams(para);
	}

	private void initViewForSameShape(LinearLayout container) {
		List<OneWord> wordList = WordFactory.getInstance(this).getSameShape(mOneWord.getIndex_word_id());

		if (wordList.size() > 0) {
			LayoutInflater mInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			container.removeAllViews();

			for (int i = 0; i < wordList.size(); i++) {
				OneWord word = wordList.get(i);
				View item = createWordView(mInflater, word, this);
				container.addView(item);
			}
		}
	}

	public static View createWordView(LayoutInflater mInflater, final OneWord word, final Context context) {
		View view = mInflater.inflate(R.layout.view_word_note_item, null);

		TextView lemma = (TextView) view.findViewById(R.id.word_note_item_word);
		TextView phe = (TextView) view.findViewById(R.id.word_note_item_phe);
		TextView traslation = (TextView) view.findViewById(R.id.word_note_item_translation);

		lemma.setText(word.getLemma());
		phe.setText(word.getPhonetic());
		traslation.setText(word.getSenses());

		return view;
	}
	

}
