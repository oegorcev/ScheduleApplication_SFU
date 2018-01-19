package Utils;

import android.os.AsyncTask;

import com.example.mrnobody43.shedule_application.StartScreen;

import java.util.ArrayList;
import java.util.List;

import data.Parsers.AbstractParser;
import data.Parsers.ClassroomParser;
import data.Parsers.GroupParser;
import data.Parsers.TeacherParser;
import model.ClassRoom.ClassClassRoom;
import model.ClassRoom.DayClassRoom;
import model.ClassRoom.WeekClassRoom;
import model.Group.ClassGroup;
import model.Group.DayGroup;
import model.Group.WeekGroup;
import model.Teacher.ClassTeacher;
import model.Teacher.DayTeacher;
import model.Teacher.WeekTeacher;

public class Scheduler extends AsyncTask<StartScreen, Void, Void> {

    private String _query;
    private AbstractParser _parser;
    private StartScreen _startScreen;
    private Integer CURRENT_STATE;
    private List<List<Pair<String, String>>> _schedule;
    private ArrayList<String> _times;

    public Scheduler(StartScreen startScreen, String query) {
        super();
        _query = query;
        _startScreen = startScreen;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        CURRENT_STATE = SetState();
        _parser = new GroupParser(_startScreen);
        _parser.execute(_query, "1");
    }

    protected Void doInBackground(StartScreen... params) {

        if (_parser.get_schedule_main() != null && !(_parser.get_schedule_main().isEmpty())) {
            _times = _parser.get_times();
            _schedule = _parser.get_schedule_main();

            switch (CURRENT_STATE) {
                case Constants.GROUP: {
                    MakeGroupSchedule();
                    break;
                }
                case Constants.TEACHER: {
                    MakeTeacherSchedule();
                    break;
                }
                case Constants.CLASSROOM: {
                    MakeClassRoomSchedule();
                    break;
                }
                default: {
                    break;
                }
            }
        }

        return null;
    }

    private void MakeGroupSchedule() {
        GroupParser groupParser = new GroupParser(_startScreen);
        WeekGroup weekGroup = new WeekGroup();
        ArrayList<DayGroup> dayGroups = new ArrayList<DayGroup>();

        for (int i = 0; i < 7; ++i) {
            DayGroup curDayGroup = new DayGroup();

            curDayGroup.set_day_of_the_week(_schedule.get(i).get(0).getFirst());

            ArrayList<ClassGroup> classesTop = new ArrayList<ClassGroup>();
            ArrayList<ClassGroup> classesBot = new ArrayList<ClassGroup>();

            for (int j = 1; j < 8; ++j) {
                ClassGroup _classGroupTop = new ClassGroup();
                ClassGroup _classGroupBot = new ClassGroup();

                _classGroupTop = groupParser.parseClass(_schedule.get(i).get(j).getFirst().split(" "));
                _classGroupBot = groupParser.parseClass(_schedule.get(i).get(j).getSecond().split(" "));

                _classGroupTop.set_time(_times.get(j - 1));
                _classGroupBot.set_time(_times.get(j - 1));

                classesTop.add(_classGroupTop);
                classesBot.add(_classGroupBot);
            }

            curDayGroup.set_classesTopWeek(classesTop);
            curDayGroup.set_classesBotWeek(classesBot);

            dayGroups.add(curDayGroup);
        }

        weekGroup.setWeek(dayGroups);
        _startScreen.set_currentSchedule(weekGroup);

        return;
    }

    private void MakeTeacherSchedule() {

        TeacherParser teacherParser = new TeacherParser(_startScreen);
        WeekTeacher weekTeacher = new WeekTeacher();
        ArrayList<DayTeacher> dayTeachers = new ArrayList<DayTeacher>();

        for (int i = 0; i < 7; ++i) {
            DayTeacher curDayTeacher = new DayTeacher();

            curDayTeacher.set_day_of_the_week(_schedule.get(i).get(0).getFirst());

            ArrayList<ClassTeacher> classesTop = new ArrayList<ClassTeacher>();
            ArrayList<ClassTeacher> classesBot = new ArrayList<ClassTeacher>();

            for (int j = 1; j < 8; ++j) {
                ClassTeacher _classTeacherTop = new ClassTeacher();
                ClassTeacher _classTeacherBot = new ClassTeacher();

                _classTeacherTop = teacherParser.parseClass(_schedule.get(i).get(j).getFirst().split(" "));
                _classTeacherBot = teacherParser.parseClass(_schedule.get(i).get(j).getSecond().split(" "));

                _classTeacherTop.set_time(_times.get(j - 1));
                _classTeacherBot.set_time(_times.get(j - 1));

                classesTop.add(_classTeacherTop);
                classesBot.add(_classTeacherBot);
            }

            curDayTeacher.set_classesTopWeek(classesTop);
            curDayTeacher.set_classesBotWeek(classesBot);

            dayTeachers.add(curDayTeacher);
        }

        weekTeacher.setWeek(dayTeachers);
        _startScreen.set_currentSchedule(weekTeacher);

        return;
    }

    private void MakeClassRoomSchedule(){
        ClassroomParser classroomParser = new ClassroomParser(_startScreen);

        WeekClassRoom weekClassRoom = new WeekClassRoom();
        ArrayList<DayClassRoom> dayClassRoom = new ArrayList<DayClassRoom>();

        for (int i = 0; i < 7; ++i) {
            DayClassRoom curDayClassRoom = new DayClassRoom();

            curDayClassRoom.set_day_of_the_week(_schedule.get(i).get(0).getFirst());

            ArrayList<ClassClassRoom> classesTop = new ArrayList<ClassClassRoom>();
            ArrayList<ClassClassRoom> classesBot = new ArrayList<ClassClassRoom>();

            for (int j = 1; j < 8; ++j) {
                ClassClassRoom _classClassRoomTop = new ClassClassRoom();
                ClassClassRoom _classClassRoomBot = new ClassClassRoom();

                _classClassRoomTop = classroomParser.parseClass(_schedule.get(i).get(j).getFirst().split(" "));
                _classClassRoomBot = classroomParser.parseClass(_schedule.get(i).get(j).getSecond().split(" "));

                _classClassRoomTop.set_time(_times.get(j - 1));
                _classClassRoomBot.set_time(_times.get(j - 1));

                classesTop.add(_classClassRoomTop);
                classesBot.add(_classClassRoomBot);
            }

            curDayClassRoom.set_classesTopWeek(classesTop);
            curDayClassRoom.set_classesBotWeek(classesBot);

            dayClassRoom.add(curDayClassRoom);
        }

        weekClassRoom.setWeek(dayClassRoom);
        _startScreen.set_currentSchedule(weekClassRoom);

        return;
    }

    private Integer SetState()
    {
        String[] mas = _query.split(" ");

        if(Utilities.IsGroup(_query)){
            return Constants.GROUP;
        } else if(mas.length > 1){
            return Constants.TEACHER;
        } else if(Utilities.IsClassRoom(_query)){
            return Constants.CLASSROOM;
        }

        return  Constants.GROUP;
    }
}
