package data;

import android.os.AsyncTask;

import com.example.mrnobody43.shedule_application.ExamsSchedule;
import com.example.mrnobody43.shedule_application.MainSchedule;

import java.util.ArrayList;
import java.util.List;

import Utils.Constants;
import Utils.Pair;
import Utils.Utilities;
import data.DataBase.DataBaseMapper;
import data.Parsers.AbstractParser;
import data.Parsers.ClassroomParser;
import data.Parsers.ExamParser;
import data.Parsers.GroupParser;
import data.Parsers.TeacherParser;
import model.ClassRoom.ClassClassRoom;
import model.ClassRoom.DayClassRoom;
import model.ClassRoom.WeekClassRoom;
import model.Exams.AllExams;
import model.Exams.ClassExam;
import model.Exams.DayExam;
import model.Group.ClassGroup;
import model.Group.DayGroup;
import model.Group.WeekGroup;
import model.Teacher.ClassTeacher;
import model.Teacher.DayTeacher;
import model.Teacher.WeekTeacher;

public class Scheduler extends AsyncTask<MainSchedule, Void, Void> {

    public Scheduler(MainSchedule mainSchedule, String query) {
        super();
        _query = query;
        _mainActivity = mainSchedule;
        _dataBaseMapper = new DataBaseMapper(mainSchedule);
    }

    public Scheduler(ExamsSchedule examsSchedule, String query) {
        super();
        _query = query;
        _examsActivity = examsSchedule;
        _dataBaseMapper = new DataBaseMapper(examsSchedule);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        _CURRENT_STATE = Utilities.SetState(_query);

        if(_examsActivity != null)  _parser = new AbstractParser(_examsActivity);
        else _parser = new AbstractParser(_mainActivity);

        ArrayList<String> params = new ArrayList<String>();
        params = _dataBaseMapper.getParseOptions();

        _parser.execute(_query, params.get(0), params.get(1));
    }

    protected Void doInBackground(MainSchedule... params) {

        if(_examsActivity != null) {
            _times = _parser.get_times();
            _examsSchedule = _parser.getScheduleExams();
            MakeExamSchedule();
        }
        else  if (_parser.getScheduleMain() != null && !(_parser.getScheduleMain().isEmpty())) {
            _times = _parser.get_times();
            _weekSchedule = _parser.getScheduleMain();

            switch (_CURRENT_STATE) {
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
        GroupParser groupParser = new GroupParser(_mainActivity);
        WeekGroup weekGroup = new WeekGroup();
        ArrayList<DayGroup> dayGroups = new ArrayList<DayGroup>();

        for (int i = 0; i < 7; ++i) {
            DayGroup curDayGroup = new DayGroup();

            curDayGroup.set_day_of_the_week(_weekSchedule.get(i).get(0).getFirst());

            ArrayList<ClassGroup> classesTop = new ArrayList<ClassGroup>();
            ArrayList<ClassGroup> classesBot = new ArrayList<ClassGroup>();

            for (int j = 1; j < 8; ++j) {
                ClassGroup _classGroupTop = new ClassGroup();
                ClassGroup _classGroupBot = new ClassGroup();

                _classGroupTop = groupParser.parseClass(_weekSchedule.get(i).get(j).getFirst().split(" "));
                _classGroupBot = groupParser.parseClass(_weekSchedule.get(i).get(j).getSecond().split(" "));

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
        _mainActivity.set_currentSchedule(weekGroup);

    }

    private void MakeTeacherSchedule() {

        TeacherParser teacherParser = new TeacherParser(_mainActivity);
        WeekTeacher weekTeacher = new WeekTeacher();
        ArrayList<DayTeacher> dayTeachers = new ArrayList<DayTeacher>();

        for (int i = 0; i < 7; ++i) {
            DayTeacher curDayTeacher = new DayTeacher();

            curDayTeacher.set_day_of_the_week(_weekSchedule.get(i).get(0).getFirst());

            ArrayList<ClassTeacher> classesTop = new ArrayList<ClassTeacher>();
            ArrayList<ClassTeacher> classesBot = new ArrayList<ClassTeacher>();

            for (int j = 1; j < 8; ++j) {
                ClassTeacher _classTeacherTop = new ClassTeacher();
                ClassTeacher _classTeacherBot = new ClassTeacher();

                _classTeacherTop = teacherParser.parseClass(_weekSchedule.get(i).get(j).getFirst().split(" "));
                _classTeacherBot = teacherParser.parseClass(_weekSchedule.get(i).get(j).getSecond().split(" "));

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
        _mainActivity.set_currentSchedule(weekTeacher);

    }

    private void MakeClassRoomSchedule(){
        ClassroomParser classroomParser = new ClassroomParser(_mainActivity);

        WeekClassRoom weekClassRoom = new WeekClassRoom();
        ArrayList<DayClassRoom> dayClassRoom = new ArrayList<DayClassRoom>();

        for (int i = 0; i < 7; ++i) {
            DayClassRoom curDayClassRoom = new DayClassRoom();

            curDayClassRoom.set_day_of_the_week(_weekSchedule.get(i).get(0).getFirst());

            ArrayList<ClassClassRoom> classesTop = new ArrayList<ClassClassRoom>();
            ArrayList<ClassClassRoom> classesBot = new ArrayList<ClassClassRoom>();

            for (int j = 1; j < 8; ++j) {
                ClassClassRoom _classClassRoomTop = new ClassClassRoom();
                ClassClassRoom _classClassRoomBot = new ClassClassRoom();

                _classClassRoomTop = classroomParser.parseClass(_weekSchedule.get(i).get(j).getFirst().split(" "));
                _classClassRoomBot = classroomParser.parseClass(_weekSchedule.get(i).get(j).getSecond().split(" "));

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
        _mainActivity.set_currentSchedule(weekClassRoom);
    }

    private void MakeExamSchedule(){
        ExamParser examParser = new ExamParser(_examsActivity);

        AllExams allExams = new AllExams();
        ArrayList<DayExam> dayExams = new ArrayList<DayExam>();

        for (int i = 0; i < (_examsSchedule == null ? 0 : _examsSchedule.size()); ++i) {
            DayExam curDayExam = new DayExam();

            curDayExam.set_day(_examsSchedule.get(i).get(0).getFirst());

            ArrayList<ClassExam> classes = new ArrayList<ClassExam>();

            for (int j = 1; j <   _examsSchedule.get(i).size(); ++j) {
                ClassExam classExam = new ClassExam();

                classExam = examParser.parseClass(_examsSchedule.get(i).get(j).getFirst().split(" "));

                classExam.set_time(_times.get(j - 1));

                classes.add(classExam);
            }

            curDayExam.set_classes(classes);

            dayExams.add(curDayExam);
        }

        allExams.setAll(dayExams);
        _examsActivity.set_currentScheduleExams(allExams);
    }

    private String _query;
    private AbstractParser _parser;
    private MainSchedule _mainActivity;
    private ExamsSchedule _examsActivity;
    private Integer _CURRENT_STATE;
    private List<List<Pair<String, String>>> _weekSchedule;
    private List<List<Pair<String, String>>> _examsSchedule;
    private ArrayList<String> _times;
    private DataBaseMapper _dataBaseMapper;
}
