package cn.edu.zjicm.zhengjun.words.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "words.db";
    static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override    // 创建数据库后，对数据库的操作
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();

        try {
            init(db);
            updateFrom1To2(db);
            updateFrom2To3(db);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        } finally {
            db.endTransaction(); // 处理完成
        }
    }

    void updateFrom1To2(SQLiteDatabase db) {
        String sql = "CREATE UNIQUE INDEX [IDX_WORDS_] ON [words]([lemma]  DESC)";
        boolean success = true;
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("create index", "error");
            success = false;
        }
        Log.e("index", "创建索引完成,success:" + success);
    }

    void updateFrom2To3(SQLiteDatabase db) {
        String sql = "CREATE TABLE [mywords] (" + "[word_id] INTEGER  PRIMARY KEY NOT NULL," + "[time_set] INTEGER DEFAULT '0' NOT NULL," + "[degree_fm] INTEGER DEFAULT '0' NOT NULL)";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("create table", "error");
        }
        Log.e("create table", "创建 mywords 表完成");
    }

    @Override   // 更改数据库版本的操作
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1) {
            updateFrom1To2(db);
            updateFrom2To3(db);
        } else if (oldVersion == 2) {
            updateFrom2To3(db);
        }
    }

    void init(SQLiteDatabase db) {
        Log.e("init", "初始化开始");

        String sqls[] = {
                "DROP TABLE IF EXISTS words",
                "CREATE TABLE [words] ([word_id] INTEGER  PRIMARY KEY NOT NULL, " +
                        "[lemma] VARCHAR(60) DEFAULT '' NOT NULL, " +
                        "[senses] VARCHAR(200) DEFAULT '' NOT NULL," +
                        "[phonetic] VARCHAR(50) DEFAULT '' NOT NULL)",
                "INSERT INTO words VALUES(1000,'abandon','vt. 丢弃；放弃，抛弃','[ə''bændən]')",
                "INSERT INTO words VALUES(1003,'aboard','ad.&prep. 在船(飞机、车)上#nad. 上船(飞机)','[ə''bɔːrd]')",
                "INSERT INTO words VALUES(1004,'abroad','ad. 到国外，在国外；在传播，在流传','[ə''brɔːd]')",
                "INSERT INTO words VALUES(1006,'absent','a. 缺席的；缺乏的，不存在的；心不在焉的','[''æbsənt]')",
                "INSERT INTO words VALUES(1011,'abundant','a. 大量(充足)的；丰富(富裕)的','[ə''bʌndənt]')",
                "INSERT INTO words VALUES(1017,'accent','n. 口音，腔调；重音(符号)#nvt. 重读','[''æksent]')",
                "DROP TABLE IF EXISTS colloc",
                "CREATE TABLE [colloc] ([word_id] INTEGER  NOT NULL, " +
                        "[node_index] INTEGER  NOT NULL, " +
                        "[colloc_eng] VARCHAR(100) DEFAULT '' NOT NULL, " +
                        "[colloc_cn] VARCHAR(100) DEFAULT '' NOT NULL)",
                "INSERT INTO colloc VALUES(1000,0,'abandon a friend in trouble','抛弃处于危难中的朋友')",
                "INSERT INTO colloc VALUES(1000,1,'abandon one''s career','放弃事业')",
                "INSERT INTO colloc VALUES(1006, 4, 'an absent look', '心不在焉的样子')",
                "INSERT INTO colloc VALUES(1003, 0, 'There are 200 passengers aboard.', '机上有200名乘客。')",
                "INSERT INTO colloc VALUES(1003, 1, 'go aboard the plane', '登机')",
                "INSERT INTO colloc VALUES(1004, 3, 'His success is spreaded abroad.', '他的成功被传播开了。')",
                "INSERT INTO colloc VALUES(1006, 0, 'be absent at the meeting', '缺席会议')",
                "INSERT INTO colloc VALUES(1011, 1, 'a land abundant in minerals', '一块矿藏丰富的土地')",
                "INSERT INTO colloc VALUES(1011, 0, 'an abundant supply of fruit', '水果的大量供应')",
                "INSERT INTO colloc VALUES(1017, 0, 'speak with a strong Beijing accent', '带着浓重的京腔说话')",
                "INSERT INTO colloc VALUES(1000, 2, 'She abandoned her stage career.', '她放弃了自己的舞台事业。')",
                "INSERT INTO colloc VALUES(1017, 2, 'accent mark', '重音符号')",
                "INSERT INTO colloc VALUES(1017, 1, 'have an American accent', '带有美国口音')",
                "INSERT INTO colloc VALUES(1011, 4, 'reap an abundant harvest', '获取丰硕的收成')",
                "INSERT INTO colloc VALUES(1011, 3, 'abundant year', '丰年')",
                "INSERT INTO colloc VALUES(1004, 2, 'The news spread abroad.', '消息四处传播。')",
                "INSERT INTO colloc VALUES(1004, 1, 'go abroad for further education', '出国深造')",
                "INSERT INTO colloc VALUES(1011, 2, 'abundant evidences', '充足的证据')",
                "INSERT INTO colloc VALUES(1003, 2, 'step aboard the ship', '登船')",
                "INSERT INTO colloc VALUES(1003, 3, 'Welcome aboard!', '欢迎上船！')",
                "INSERT INTO colloc VALUES(1006, 3, 'Water is absent in this country.', '这个国家缺水。')",
                "INSERT INTO colloc VALUES(1006, 1, 'be absent from class', '旷课')",
                "INSERT INTO colloc VALUES(1006, 2, 'Love is absent in his family.', '他的家中缺少爱。')",
                "INSERT INTO colloc VALUES(1017, 3, 'primary accent', '主重音')",
                "INSERT INTO colloc VALUES(1004, 0, 'travel abroad', '出国旅游')",
                "INSERT INTO colloc VALUES(1017, 4, 'an accented word', '重读的单词')",
                "DROP TABLE IF EXISTS sameshape",
                "CREATE TABLE [sameshape]([word_id] INTEGER PRIMARY KEY NOT NULL," +
                        "[sameshape_word_id]VARCHAR(200) NOT NULL)",
                "INSERT INTO sameshape VALUES(1000, '1011')",
                "INSERT INTO sameshape VALUES(1003, '1004')",
                "INSERT INTO sameshape VALUES(1004, '1003')",
                "INSERT INTO sameshape VALUES(1006, '1017')",
                "INSERT INTO sameshape VALUES(1011, '1000')",
                "INSERT INTO sameshape VALUES(1017, '1006')"};

        for (String sql : sqls) {
            db.execSQL(sql);
        }

        StringBuffer sbf = new StringBuffer("baaa");
        for (int id = 10000; id < 30000; id++) {
            String str = sbf.toString();
            String sql = "INSERT INTO words(word_id, lemma, senses, phonetic) select " + id + ",'" + str + "','" + str + "','" + str + "' " +
                    "where not exists(select * from words where lemma='" + str + "')";
            db.execSQL(sql);

            int index = sbf.length() - 1;
            while(index>=0 && sbf.charAt(index)=='z'){
                sbf.setCharAt(index, 'a');
                index--;
            }
            sbf.setCharAt(index, (char)(sbf.charAt(index) + 1));
        }
        Log.e(sbf.toString(), "30000");
        Log.e("init", "初始化完成");
    }

    @Override   // 每次成功打开数据库后首先被执行
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }


}
