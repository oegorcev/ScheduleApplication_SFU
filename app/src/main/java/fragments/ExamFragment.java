package fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mrnobody43.shedule_application.ExamsSchedule;
import com.example.mrnobody43.shedule_application.R;

import Utils.Constants;
import adapters.ExamClassroomAdapter;
import adapters.ExamGroupAdapter;
import adapters.ExamTeacherAdapter;
import adapters.ScheduleEmptyAdapter;
import model.ExamClassroom.ExamsClassroom;
import model.ExamGroup.ExamsGroup;
import model.ExamTeacher.ExamsTeacher;

/**
 * Created by Mr.Nobody43 on 09.03.2018.
 */

public class ExamFragment  extends Fragment {

    static public ExamFragment newInstance(ExamsSchedule ctx, ExamsGroup examsGroup, int day, int CURRENT_STATE) {
        ExamFragment pageFragment = new ExamFragment();

        pageFragment.set_CURRENT_STATE(CURRENT_STATE);
        pageFragment._groupAdapter = new ExamGroupAdapter(ctx, examsGroup, day);
        pageFragment._ctx = ctx;

        return pageFragment;
    }

    static public ExamFragment newInstance(ExamsSchedule ctx, ExamsClassroom examsClassroom, int day, int CURRENT_STATE) {
        ExamFragment pageFragment = new ExamFragment();

        pageFragment.set_CURRENT_STATE(CURRENT_STATE);
        pageFragment._classroomAdapter = new ExamClassroomAdapter(ctx, examsClassroom, day);
        pageFragment._ctx = ctx;

        return pageFragment;
    }

    static public ExamFragment newInstance(ExamsSchedule ctx, ExamsTeacher examsTeacher, int day, int CURRENT_STATE) {
        ExamFragment pageFragment = new ExamFragment();

        pageFragment.set_CURRENT_STATE(CURRENT_STATE);
        pageFragment._teacherAdapter = new ExamTeacherAdapter(ctx, examsTeacher, day);
        pageFragment._ctx = ctx;

        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_schedule, container, false);

        ListView listView = (ListView) view.findViewById(R.id.list1);

        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()  {
                _ctx.renderScheduleData();
            }
        });

        if(_CURRENT_STATE != null) {
            switch (_CURRENT_STATE) {
                case Constants.GROUP: {
                    if (_groupAdapter != null &&  !_groupAdapter.isNull()) {
                        listView.setAdapter(_groupAdapter);
                    } else {
                        listView.setAdapter(new ScheduleEmptyAdapter(getContext()));
                    }
                    break;
                }
                case Constants.TEACHER: {
                    if (_teacherAdapter != null &&  !_teacherAdapter.isNull()) {
                        listView.setAdapter(_teacherAdapter);
                    } else {
                        listView.setAdapter(new ScheduleEmptyAdapter(getContext()));
                    }
                    break;
                }
                case Constants.CLASSROOM: {
                    if (_classroomAdapter != null &&  !_classroomAdapter.isNull()) {
                        listView.setAdapter(_classroomAdapter);
                    } else {
                        listView.setAdapter(new ScheduleEmptyAdapter(getContext()));
                    }
                    break;
                }
                default: {
                    break;
                }
            }
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void set_CURRENT_STATE(Integer _CURRENT_STATE) {this._CURRENT_STATE = _CURRENT_STATE;}

    private ExamsSchedule _ctx;
    private Integer _CURRENT_STATE;
    private ExamGroupAdapter _groupAdapter;
    private ExamClassroomAdapter _classroomAdapter;
    private ExamTeacherAdapter _teacherAdapter;
}
