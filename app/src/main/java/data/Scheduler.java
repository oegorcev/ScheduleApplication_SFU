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
import data.Parsers.ExamClassroomParser;
import data.Parsers.ExamGroupParser;
import data.Parsers.ExamTeacherParser;
import data.Parsers.GroupParser;
import data.Parsers.TeacherParser;
import model.ClassRoom.ClassClassRoom;
import model.ClassRoom.DayClassRoom;
import model.ClassRoom.WeekClassRoom;
import model.ExamClassroom.ClassExamClassroom;
import model.ExamClassroom.DayExamClassroom;
import model.ExamClassroom.ExamsClassroom;
import model.ExamGroup.ClassExamGroup;
import model.ExamGroup.DayExamGroup;
import model.ExamGroup.ExamsGroup;
import model.ExamTeacher.ClassExamTeacher;
import model.ExamTeacher.DayExamTeacher;
import model.ExamTeacher.ExamsTeacher;
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
        _CURRENT_STATE = Utilities.GetState(_query);

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


            switch (_CURRENT_STATE) {
                case Constants.GROUP: {
                    MakeExamGroupSchedule();
                    break;
                }
                case Constants.TEACHER: {
                    MakeExamTeacherSchedule();
                    break;
                }
                case Constants.CLASSROOM: {
                    MakeExamClassroomSchedule();
                    break;
                }
                default: {
                    break;
                }
            }
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

        isEnd = true;

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

        weekGroup.set_week(dayGroups);
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

        weekClassRoom.set_week(dayClassRoom);
        _mainActivity.set_currentSchedule(weekClassRoom);
    }

    private void MakeExamGroupSchedule(){
        ExamGroupParser examGroupParser = new ExamGroupParser(_examsActivity);

        ExamsGroup examsGroup = new ExamsGroup();
        ArrayList<DayExamGroup> dayExamGroups = new ArrayList<DayExamGroup>();

        for (int i = 0; i < (_examsSchedule == null ? 0 : _examsSchedule.size()); ++i) {
            DayExamGroup curDayExamGroup = new DayExamGroup();

            curDayExamGroup.set_day(_examsSchedule.get(i).get(0).getFirst());

            ArrayList<ClassExamGroup> classes = new ArrayList<ClassExamGroup>();

            for (int j = 1; j <   _examsSchedule.get(i).size(); ++j) {
                ClassExamGroup classExamGroup = new ClassExamGroup();

                classExamGroup = examGroupParser.parseClass(_examsSchedule.get(i).get(j).getFirst().split(" "));

                classExamGroup.set_time(_times.get(j - 1));

                classes.add(classExamGroup);
            }

            curDayExamGroup.set_classes(classes);

            dayExamGroups.add(curDayExamGroup);
        }

        examsGroup.setAll(dayExamGroups);
        _examsActivity.set_currentScheduleExamsGroup(examsGroup);
    }

    private void MakeExamTeacherSchedule(){
        ExamTeacherParser examTeacherParser = new ExamTeacherParser(_examsActivity);

        ExamsTeacher examsTeacher = new ExamsTeacher();
        ArrayList<DayExamTeacher> dayExamTeacher = new ArrayList<DayExamTeacher>();

        for (int i = 0; i < (_examsSchedule == null ? 0 : _examsSchedule.size()); ++i) {
            DayExamTeacher curDayExamTeacher = new DayExamTeacher();

            curDayExamTeacher.set_day(_examsSchedule.get(i).get(0).getFirst());

            ArrayList<ClassExamTeacher> classes = new ArrayList<ClassExamTeacher>();

            for (int j = 1; j <   _examsSchedule.get(i).size(); ++j) {
                ClassExamTeacher classExamTeacher = new ClassExamTeacher();

                classExamTeacher = examTeacherParser.parseClass(_examsSchedule.get(i).get(j).getFirst().split(" "));

                classExamTeacher.set_time(_times.get(j - 1));

                classes.add(classExamTeacher);
            }

            curDayExamTeacher.set_classes(classes);

            dayExamTeacher.add(curDayExamTeacher);
        }

        examsTeacher.setAll(dayExamTeacher);
        _examsActivity.set_currentScheduleExamsTeacher(examsTeacher);
    }

    private void MakeExamClassroomSchedule(){
        ExamClassroomParser examClassroomParser = new ExamClassroomParser(_examsActivity);

        ExamsClassroom examsClassroom = new ExamsClassroom();
        ArrayList<DayExamClassroom> dayExamClassroom = new ArrayList<DayExamClassroom>();

        for (int i = 0; i < (_examsSchedule == null ? 0 : _examsSchedule.size()); ++i) {
            DayExamClassroom curDayExamClassroom = new DayExamClassroom();

            curDayExamClassroom.set_day(_examsSchedule.get(i).get(0).getFirst());

            ArrayList<ClassExamClassroom> classes = new ArrayList<ClassExamClassroom>();

            for (int j = 1; j <   _examsSchedule.get(i).size(); ++j) {
                ClassExamClassroom classExamClassroom = new ClassExamClassroom();

                classExamClassroom = examClassroomParser.parseClass(_examsSchedule.get(i).get(j).getFirst().split(" "));

                classExamClassroom.set_time(_times.get(j - 1));

                classes.add(classExamClassroom);
            }

            curDayExamClassroom.set_classes(classes);

            dayExamClassroom.add(curDayExamClassroom);
        }

        examsClassroom.setAll(dayExamClassroom);
        _examsActivity.set_currentScheduleExamsClassroom(examsClassroom);
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
    public boolean isEnd;
}
