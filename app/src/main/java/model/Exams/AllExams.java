package model.Exams;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class AllExams {

    private ArrayList<DayExam> _allDays;

    public ArrayList<DayExam> getWeek() {
        return _allDays;
    }

    public void setWeek(ArrayList<DayExam> week) {
        this._allDays = week;
    }

}
