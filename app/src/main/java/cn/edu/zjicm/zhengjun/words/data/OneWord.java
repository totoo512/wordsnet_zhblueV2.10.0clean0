package cn.edu.zjicm.zhengjun.words.data;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.io.Serializable;

public class OneWord implements Serializable{

	private int index_word_id;
	private String lemma;
	private String senses;
	private String phonetic;

	private long time_set;
	private int degree_fm;

	public OneWord(int index_word_id, String lemma, String senses,String phonetic) {
		this.index_word_id = index_word_id;
		this.lemma = lemma;
		this.senses = senses;
		this.phonetic = phonetic;
	}

	/**
	 * 如果游标只有 4 列，则是对 words 表进行查询返回的数据；
	 * 如果游标有 6列，则是对 words 和 mywords 进行联合查询返回的数据，还需将后 2 两列设为时间和熟悉度
	 * @param c 游标
	 */
	@SuppressLint("Range")
	public OneWord(Cursor c) {
		this(c.getInt(c.getColumnIndex("word_id")),
				c.getString(c.getColumnIndex("lemma")),
				c.getString(c.getColumnIndex("senses")),
				c.getString(c.getColumnIndex("phonetic")));
		if(c.getColumnCount() == 6) {
			setTime_set(c.getLong(c.getColumnIndex("time_set")));
			setDegree_fm(c.getInt(c.getColumnIndex("degree_fm")));
		}
	}

	public int getIndex_word_id() {
		return index_word_id;
	}

	public void setIndex_word_id(int index_word_id) {
		this.index_word_id = index_word_id;
	}

	public String getLemma() {
		return lemma;
	}

	public void setLemma(String lemma) {
		this.lemma = lemma;
	}

	public String getPhonetic() {
		return phonetic;
	}

	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}

	public String getSenses() {
		return senses;
	}

	public void setSenses(String senses_senior) {
		this.senses = senses;
	}

	public long getTime_set() {
		return time_set;
	}

	public void setTime_set(long time_set) {
		this.time_set = time_set;
	}

	public int getDegree_fm() {
		return degree_fm;
	}

	public void setDegree_fm(int degree_fm) {
		this.degree_fm = degree_fm;
	}


}
