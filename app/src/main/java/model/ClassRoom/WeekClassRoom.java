package model.ClassRoom;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 18.01.2018.
 */

public class WeekClassRoom {

    private ArrayList<DayClassRoom> _week;

    public boolean isEmpty()
    {
        return this._week == null;
    }

    public ArrayList<DayClassRoom> get_week() {
        return _week;
    }

    public void set_week(ArrayList<DayClassRoom> _week) {
        this._week = _week;
    }
}
