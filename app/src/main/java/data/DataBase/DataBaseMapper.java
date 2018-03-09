package data.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import Utils.Constants;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class DataBaseMapper {

    public DataBaseMapper(Context context)
    {
        _myDb = new DataBaseHelper(context);
    }

    public void setNewQuery(String newQuery) {
        _db = _myDb.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ID, Constants.CUR_QUERY_DB_ID);
        cv.put(DataBaseHelper.OPTION, newQuery);

        int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.CUR_QUERY_DB_ID });
        if(was == 0)
        {
            _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
        }

        _myDb.close();
    }

    public void setNewSchedule(Document doc, String query) {
        _db = _myDb.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ID, query);
        cv.put(DataBaseHelper.HTML_CODE, doc.toString());

        int was = _db.update(DataBaseHelper.TABLE_NAME1, cv, DataBaseHelper.ID + " = ?", new String[] { query });
        if(was == 0)
        {
            _db.insert(DataBaseHelper.TABLE_NAME1, null, cv);
        }

        _myDb.close();
    }

    public void setPotok(String potok) {
        _db = _myDb.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ID, Constants.YEARS_DB_ID);
        cv.put(DataBaseHelper.OPTION, potok);

        int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.YEARS_DB_ID });
        if(was == 0)
        {
            _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
        }

        _myDb.close();
    }

    public void setSemestr(String semestr) {
        _db = _myDb.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ID, Constants.SEMESTR_DB_ID);
        cv.put(DataBaseHelper.OPTION, semestr);

        int was = _db.update(DataBaseHelper.TABLE_NAME2, cv, DataBaseHelper.ID + " = ?", new String[] { Constants.SEMESTR_DB_ID });
        if(was == 0)
        {
            _db.insert(DataBaseHelper.TABLE_NAME2, null, cv);
        }

        _myDb.close();
    }

    public ArrayList<Integer> getSpinnerParams(){

        ArrayList<Integer> ret = new ArrayList<Integer>();

        String semestr = "1";
        String potok = Constants.FIRST_POTOK.toString();

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME2, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int optionIndex = c.getColumnIndex(DataBaseHelper.OPTION);

                String offlineData = c.getString(optionIndex);
                String bdId = c.getString(idIndex);

                if (Constants.SEMESTR_DB_ID.equals(bdId)) {
                    semestr = offlineData;
                } else if (Constants.YEARS_DB_ID.equals(bdId)) {
                    potok = offlineData;
                }

                c.moveToNext();

            }
        }
        _myDb.close();

        ret.add(Integer.parseInt(semestr) - 1);
        ret.add(Integer.parseInt(potok) - Constants.FIRST_POTOK);

        return ret;
    }

    public ArrayList<String> getParseOptions() {

        ArrayList<String> ret = new ArrayList<String>();
        ret.add("1");
        ret.add(Constants.FIRST_POTOK.toString());

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME2, null, null, null, null, null, null);

        if (c.moveToFirst()) {

            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int optionIndex = c.getColumnIndex(DataBaseHelper.OPTION);

                String offlineData = c.getString(optionIndex);
                String bdId = c.getString(idIndex);

                if (Constants.SEMESTR_DB_ID.equals(bdId)) {
                    ret.set(0, offlineData);
                } else if (Constants.YEARS_DB_ID.equals(bdId)) {
                    ret.set(1, offlineData);
                }

                c.moveToNext();
            }
        }
        _myDb.close();

        return ret;
    }

    public String getCurruntQuery() {
        String s = "Расписание занятий ИТА ЮФУ";

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME2, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            boolean flag = true;
            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int optionIndex = c.getColumnIndex(DataBaseHelper.OPTION);

                String offlineData = c.getString(optionIndex);
                String bdId = c.getString(idIndex);
                if (Constants.CUR_QUERY_DB_ID.equals(bdId)) {
                    s = offlineData;
                    flag = false;
                    break;
                } else c.moveToNext();
            }
        }
        _myDb.close();

        return s;
    }

    public Document getDbSchedule(String query){
        Document doc = null;

        _db = _myDb.getReadableDatabase();

        Cursor c = _db.query(DataBaseHelper.TABLE_NAME1, null, null, null, null, null, null);

        if (c.moveToFirst()) {
            while (true) {
                if (c.isAfterLast()) break;

                int idIndex = c.getColumnIndex(DataBaseHelper.ID);
                int htmlIndex = c.getColumnIndex(DataBaseHelper.HTML_CODE);

                String offlineData = c.getString(htmlIndex);
                String bdId = c.getString(idIndex);
                if (query.equals(bdId)) {
                    doc = Jsoup.parse(offlineData);
                    break;
                } else c.moveToNext();
            }
        }
        _myDb.close();

        return doc;
    }

    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
}
