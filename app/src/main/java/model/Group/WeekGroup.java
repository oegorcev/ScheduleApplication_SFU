package model.Group;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 28.10.2017.
 */

public class WeekGroup {

    private ArrayList<DayGroup> _week;

    public boolean isEmpty()
    {
        return this._week == null;
    }

    public ArrayList<DayGroup> get_week() {
        return _week;
    }

    public void set_week(ArrayList<DayGroup> _week) {
        this._week = _week;
    }
}
