package model.Exams;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class AllExams {

    private ArrayList<DayExam> _allDays;

    public boolean isEmpty()
    {
        return this._allDays == null || _allDays.size() == 0;
    }

    public ArrayList<DayExam> getAll() {
        return _allDays;
    }

    public void setAll(ArrayList<DayExam> all) {
        this._allDays = all;
    }
}
