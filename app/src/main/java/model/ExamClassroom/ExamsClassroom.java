package model.ExamClassroom;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class ExamsClassroom {
    private ArrayList<DayExamClassroom> _allDays;

    public boolean isEmpty()
    {
        return this._allDays == null || _allDays.size() == 0;
    }

    public ArrayList<DayExamClassroom> getAll() {
        return _allDays;
    }

    public void setAll(ArrayList<DayExamClassroom> all) {
        this._allDays = all;
    }
}
