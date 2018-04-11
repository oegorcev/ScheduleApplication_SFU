package model.ExamTeacher;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class ExamsTeacher {
    private ArrayList<DayExamTeacher> _allDays;

    public boolean isEmpty()
    {
        return this._allDays == null || _allDays.size() == 0;
    }

    public ArrayList<DayExamTeacher> getAll() {
        return _allDays;
    }

    public void setAll(ArrayList<DayExamTeacher> all) {
        this._allDays = all;
    }
}
