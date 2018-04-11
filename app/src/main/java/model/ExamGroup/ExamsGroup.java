package model.ExamGroup;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class ExamsGroup {

    private ArrayList<DayExamGroup> _allDays;

    public boolean isEmpty()
    {
        return this._allDays == null || _allDays.size() == 0;
    }

    public ArrayList<DayExamGroup> getAll() {
        return _allDays;
    }

    public void setAll(ArrayList<DayExamGroup> all) {
        this._allDays = all;
    }
}
