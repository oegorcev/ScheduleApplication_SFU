package data.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import Utils.Constants;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class DataBaseMapper {

    public DataBaseMapper(Context context)
    {
        _myDb = new DataBaseHelper(context);
    }

    public String getCurruntQuery()
    {
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

    private DataBaseHelper _myDb;
    private SQLiteDatabase _db;
}
