package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mr.Nobody43 on 16.01.2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE1);
        db.execSQL(CREATE_TABLE2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME1);
        db.execSQL("DROP TABLE IF EXISTS" + TABLE_NAME2);
        onCreate(db);
    }

    public static final String DATABASE_NAME = "Scheduledb";
    public static final String TABLE_NAME1 = "schedule";
    public static final String TABLE_NAME2 = "options";
    public static final String ID = "id";
    public static final String HTML_CODE = "html";
    public static final String OPTION = "option";

    private static final String CREATE_TABLE1 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME1 + " (" + ID + " TEXT PRIMARY KEY," +
            HTML_CODE + " TEXT)";

    private static final String CREATE_TABLE2 = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME2 + " (" + ID + " TEXT PRIMARY KEY," +
            OPTION + " TEXT)";
}

