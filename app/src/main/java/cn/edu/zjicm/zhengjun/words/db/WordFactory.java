package cn.edu.zjicm.zhengjun.words.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.zjicm.zhengjun.words.data.OneWord;
import cn.edu.zjicm.zhengjun.words.data.RelationShip;


public class WordFactory {
    public static final String TABLE_WORDS = "words";
    public static final String TABLE_COLLOC = "colloc";
    public static final String TABLE_SAME_SHAPE = "sameshape";

    private static SQLiteDatabase db;
    public static WordFactory instance = null;

    private WordFactory(Context context) {
        if (db == null || !db.isOpen()) {
            try {
                db = new DatabaseHelper(context).getWritableDatabase();
            } catch (Throwable t) {
                Log.e("error", "exit");
                System.exit(0);
            }
        }
        if (instance == null) {
            instance = this;
        }
    }

    public synchronized static WordFactory getInstance(Context context) {
        if (instance == null) {
            instance = new WordFactory(context);
        }
        return instance;
    }

    // 获取数据库版本号
    public int getVersion() {
        return db.getVersion();
    }

    public OneWord getOneWordById(int id) {
        String sql = "select * from " + TABLE_WORDS + " where word_id=" + id;
        Cursor c = db.rawQuery(sql, null);
        OneWord word = null;

        if (c.moveToNext())
            word = new OneWord(c);
        c.close();

        return word;
    }

    public OneWord getOneWordByLemma(String lemma) {
        String sql = "select * from " + TABLE_WORDS + " where lemma='" + lemma + "'";
        Cursor c = db.rawQuery(sql, null);
        OneWord word = null;

        if (c.moveToFirst()) {
            word = new OneWord(c);
        }
        c.close();

        return word;
    }

    /**
     * @param sortMode true为正向，false为反向
     * @return
     */
    public ArrayList<OneWord> getAllWords(boolean sortMode) {
        String sql = "select * from " + TABLE_WORDS + " order by lemma " + (sortMode ? "" : "desc");
        Cursor c;
        ArrayList<OneWord> words = new ArrayList<OneWord>();

        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            OneWord word = new OneWord(c);
            words.add(word);
        }
        c.close();
        return words;
    }

    // 搜索单词，按lemma排序
    public List<OneWord> getSearchData(String in) {
        List<OneWord> searchs = new ArrayList<OneWord>();
        long start = System.currentTimeMillis();

        String in1 = in.substring(0, in.length() - 1) + (char) (in.charAt(in.length() - 1) + 1);
        String sql = "select * from " + TABLE_WORDS + " where lemma>='" + in + "'" + " and lemma<'" + in1 + "'"
                + " order by lemma";

        //String sql = "select * from " + TABLE_WORDS + " where lemma like '" + in + "%'" + " order by lemma";

        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            searchs.add(new OneWord(cursor));
        }
        cursor.close();

        long end = System.currentTimeMillis();
        Log.e("search", in + (end - start));

        return searchs;
    }

    /**************** RelationShip *****************************/
    public List<RelationShip> getRelationshipOfWord(int id) {
        List<RelationShip> list = new ArrayList<RelationShip>();

        // 查询colloc表中word_id字段等于id的记录，并用node_index字段的升序排列
        String sql = "select * from colloc where word_id = " + id + " order by node_index";
        Cursor c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            list.add(new RelationShip(c));
        }
        c.close();
        return list;
    }

    /*********** 形近词 *************************/
    public List<OneWord> getSameShape(int id) {
        List<OneWord> wordlist = new ArrayList<OneWord>();

        // 查询sameshape表中word_id字段等于id的记录
        String sql = "select * from sameshape where word_id = " + id;
        Cursor c = db.rawQuery(sql, null);

        if (c.moveToFirst()) {
            OneWord word = getOneWordById(Integer.parseInt(c.getString(1)));
            if (word != null) {
                wordlist.add(word);
            }
        }

        c.close();
        return wordlist;
    }

    /*********** 我的单词 ********************/
    public ArrayList<OneWord> getAllMyWords() {
        String sql = "select * from mywords inner join words using(word_id)";
        Cursor c;
        ArrayList<OneWord> words = new ArrayList<OneWord>();
        c = db.rawQuery(sql, null);
        while (c.moveToNext()) {
            OneWord word = new OneWord(c);
            words.add(word);
        }
        c.close();
        return words;
    }

    public boolean isInTableMywords(int word_id) {
        boolean result = false;
        String sql = "select * from mywords where word_id = " + word_id;
        Cursor c;
        c = db.rawQuery(sql, null);
        if (c.moveToNext()) {
            result = true;
        }
        c.close();
        return result;
    }

    public void insertIntoMywords(int word_id) {
        Date date = new Date();
        long time = date.getTime();
        String sql = "insert into mywords values(" + word_id + ", " + time + ", 0)";
        db.execSQL(sql);
    }

    public void deleteFromMywords(int word_id) {
        String sql = "delete from mywords where word_id = " + word_id;
        db.execSQL(sql);
    }

    public void updateDegreeOfMywords(int word_id, int degree_fm, long time_set) {
        String sql = "update mywords set degree_fm = " + degree_fm + ", time_set = " + time_set + " where word_id = " + word_id;
        db.execSQL(sql);
    }
}