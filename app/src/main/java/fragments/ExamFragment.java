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

import adapters.ExamAdapter;
import adapters.ScheduleEmptyAdapter;
import model.Exams.AllExams;

/**
 * Created by Mr.Nobody43 on 09.03.2018.
 */

public class ExamFragment  extends Fragment {

    static public ExamFragment newInstance(ExamsSchedule ctx, AllExams allExams, int day) {
        ExamFragment pageFragment = new ExamFragment();

        pageFragment._adapter = new ExamAdapter(ctx, allExams, day);
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

        if (_adapter != null &&  !_adapter.isNull()) {
            listView.setAdapter(_adapter);
        } else {
            listView.setAdapter(new ScheduleEmptyAdapter(getContext()));
        }

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ExamsSchedule _ctx;
    private ExamAdapter _adapter;
}
