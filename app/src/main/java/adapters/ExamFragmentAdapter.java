package adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.mrnobody43.shedule_application.ExamsSchedule;

import java.util.ArrayList;

import fragments.ExamFragment;
import model.Exams.AllExams;

/**
 * Created by Mr.Nobody43 on 09.03.2018.
 */

public class ExamFragmentAdapter extends FragmentStatePagerAdapter {

    public ExamFragmentAdapter(FragmentManager fm, ExamsSchedule ctx, ArrayList<String> days) {
        super(fm);

        _ctx = ctx;

        _fragments = new ArrayList<Fragment>();

        _day = days;
    }


    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Fragment getItem(int position) {return _fragments.get(position);}

    @Override
    public CharSequence getPageTitle(int position) {return _day.get(position);}

    @Override
    public int getCount() {return  _day.size();}

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void set_allExams(AllExams _allExams) {
        _fragments.clear();

        for(int cnt = 0; cnt < _day.size(); ++cnt) {
            _fragments.add(ExamFragment.newInstance(_ctx,  _allExams, cnt));
        }
    }

    private ArrayList<String>_day;
    private ExamsSchedule _ctx;
    private ArrayList<Fragment> _fragments;
}
