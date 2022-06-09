package cn.edu.zjicm.zhengjun.words.data;

import android.database.Cursor;

public class RelationShip {
	private int index_word_id;
	private int node_index;
	private String colloc_eng;
	private String colloc_cn;

	public RelationShip(Cursor c) {
		this(c.getInt(0), c.getInt(1), c.getString(2), c.getString(3));
	}

	public RelationShip(int index_word_id,	int node_index, String colloc_eng,	String colloc_cn){
		this.index_word_id = index_word_id;
		this.node_index = node_index;
		this.colloc_eng = colloc_eng;
		this.colloc_cn = colloc_cn;
	}
	
	public int getIndex_word_id() {
		return index_word_id;
	}

	public int getNode_index() {
		return node_index;
	}
		
	public String getColloc_eng() {
		return colloc_eng;
	}

	public String getColloc_cn() {
		return colloc_cn;
	}
	

}


