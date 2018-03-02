package adapters;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mrnobody43.shedule_application.MainSchedule;

import java.util.ArrayList;

import Utils.Constants;
import fragments.MainScheduleFragment;
import model.ClassRoom.WeekClassRoom;
import model.Group.WeekGroup;
import model.Teacher.WeekTeacher;

/**
 * Created by Mr.Nobody43 on 13.02.2018.
 */

public class MainScheduleFragmentAdapter extends FragmentStatePagerAdapter {

    public MainScheduleFragmentAdapter(FragmentManager fm, MainSchedule ctx, int CURRENT_STATE) {
        super(fm);

        _ctx = ctx;
        _CURRENT_STATE = CURRENT_STATE;

        _fragments = new ArrayList<Fragment>();

        _day_of_a_weak = new ArrayList<String>();
        _day_of_a_weak.add(Constants.MONDAY);
        _day_of_a_weak.add(Constants.TUESDAY);
        _day_of_a_weak.add(Constants.WEDNESDAY);
        _day_of_a_weak.add(Constants.THURSDAY);
        _day_of_a_weak.add(Constants.FRIDAY);
        _day_of_a_weak.add(Constants.SATURDAY);
        _day_of_a_weak.add(Constants.SUNDAY);
    }

    @Override
    public Fragment getItem(int position) {

        switch (_CURRENT_STATE)
        {
            case Constants.GROUP: {
                return _fragments.get(position);
            }
            case Constants.TEACHER: {
                return MainScheduleFragment.newInstance(_ctx,  _currentScheduleTeacher, position, _CURRENT_STATE, 1);
            }
            case Constants.CLASSROOM: {
                return MainScheduleFragment.newInstance(_ctx,  _currentScheduleGroup, position, _CURRENT_STATE, 1);/////////////
            }
            default: {
                break;
            }
        }
        return MainScheduleFragment.newInstance(_ctx,  _currentScheduleGroup, position, _CURRENT_STATE, 1);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _day_of_a_weak.get(position);
    }

    public void set_CURRENT_STATE(Integer _CURRENT_STATE) {
        this._CURRENT_STATE = _CURRENT_STATE;
    }

    @Override
    public int getCount() {
        return Constants.DAYS_ON_WEEK;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void set_currentSchedule(WeekGroup _currentSchedule) {
        this._currentScheduleGroup = _currentSchedule;

        _fragments.clear();

        for(int cnt = 0; cnt < Constants.DAYS_ON_WEEK; ++cnt) {
            _fragments.add(MainScheduleFragment.newInstance(_ctx,  _currentScheduleGroup, cnt, _CURRENT_STATE, 1));
        }
    }

    public void set_currentSchedule(WeekTeacher _currentSchedule) {
        this._currentScheduleTeacher = _currentSchedule;
    }

    public void set_currentSchedule(WeekClassRoom _currentSchedule) {
        this._currentScheduleClassRoom = _currentSchedule;
    }

    private class LoadFragment extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            return null;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


    private WeekGroup _currentScheduleGroup;
    private WeekTeacher _currentScheduleTeacher;
    private WeekClassRoom _currentScheduleClassRoom;
    private ArrayList<String>_day_of_a_weak;
    private Integer _CURRENT_STATE;
    private MainSchedule _ctx;
    private ArrayList<Fragment> _fragments;
}
