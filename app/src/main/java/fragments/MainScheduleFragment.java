package fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mrnobody43.shedule_application.MainSchedule;
import com.example.mrnobody43.shedule_application.R;

import Utils.Constants;
import adapters.ScheduleClassRoomAdapter;
import adapters.ScheduleEmptyAdapter;
import adapters.ScheduleGroupAdapter;
import adapters.ScheduleTeacherAdapter;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

/**
 * Created by Mr.Nobody43 on 13.02.2018.
 */

public class MainScheduleFragment extends Fragment {

    static public MainScheduleFragment newInstance(MainSchedule ctx, WeekGroup _currentScheduleGroup, int day, int CURRENT_STATE, int week) {
        MainScheduleFragment pageFragment = new MainScheduleFragment();

        pageFragment.set_CURRENT_STATE(CURRENT_STATE);
        pageFragment.set_currentScheduleGroup(_currentScheduleGroup);
        pageFragment.set_day(day);
        pageFragment.set_week(week);

        pageFragment._scheduleGroupAdapter = new ScheduleGroupAdapter(ctx, _currentScheduleGroup);

        return pageFragment;
    }

    static public MainScheduleFragment newInstance(MainSchedule ctx, WeekTeacher _currentScheduleTeacher, int day, int CURRENT_STATE, int week) {
        MainScheduleFragment pageFragment = new MainScheduleFragment();

        pageFragment.set_CURRENT_STATE(CURRENT_STATE);
        pageFragment.set_currentScheduleTeacher(_currentScheduleTeacher);
        pageFragment.set_day(day);
        pageFragment.set_week(week);

        pageFragment._scheduleTeacherAdapter = new ScheduleTeacherAdapter(ctx, _currentScheduleTeacher);

        return pageFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.main_schedule_fragment, null);

        listView1 = (ListView) view.findViewById(R.id.list1);


        switch (_CURRENT_STATE)
        {
            case Constants.GROUP: {
                if (_currentScheduleGroup != null) {
                    _scheduleGroupAdapter.refreshData(_day, _week % 2);
                    listView1.setAdapter(_scheduleGroupAdapter);
                } else {
                    listView1.setAdapter(new ScheduleEmptyAdapter(getContext()));
                }
                break;
            }
            case Constants.TEACHER: {
                if (_currentScheduleTeacher != null) {
                    _scheduleTeacherAdapter.refreshData(_day, _week % 2);
                    listView1.setAdapter(_scheduleTeacherAdapter);
                } else {
                    listView1.setAdapter(new ScheduleEmptyAdapter(getContext()));
                }
                break;
            }
            case Constants.CLASSROOM: {

                break;
            }
            default: {
                break;
            }
        }

        return view;
    }


    public void set_CURRENT_STATE(Integer _CURRENT_STATE) {
        this._CURRENT_STATE = _CURRENT_STATE;
    }

    public void set_week(int _week) {
        this._week = _week;
    }

    public void set_day(int _day) {
        this._day = _day;
    }

    public void set_currentScheduleGroup(WeekGroup _currentScheduleGroup) {
        this._currentScheduleGroup = _currentScheduleGroup;
    }

    public void set_currentScheduleTeacher(WeekTeacher _currentScheduleTeacher) {
        this._currentScheduleTeacher = _currentScheduleTeacher;
    }

    public void set_currentScheduleClassRoom(WeekClassRoom _currentScheduleClassRoom) {
        this._currentScheduleClassRoom = _currentScheduleClassRoom;
    }

    private Integer _CURRENT_STATE;
    private ListView listView1;
    private int _week;
    private int _day;
    private ScheduleClassRoomAdapter _scheduleClassRoomAdapter;
    private ScheduleTeacherAdapter _scheduleTeacherAdapter;
    private ScheduleGroupAdapter _scheduleGroupAdapter;
    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private WeekClassRoom _currentScheduleClassRoom;
}
