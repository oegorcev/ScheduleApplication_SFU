package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrnobody43.shedule_application.R;

import java.util.HashSet;

import Utils.Constants;
import model.ExamClassroom.ClassExamClassroom;
import model.ExamClassroom.ExamsClassroom;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class ExamClassroomAdapter extends BaseAdapter {
    public ExamClassroomAdapter(Context context, ExamsClassroom examsClassroom, int day) {
        _ExamsClassrom = examsClassroom;
        _indexTab = day;
        _lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean isNull()
    {
        return _ExamsClassrom == null || _ExamsClassrom.isEmpty();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return _ExamsClassrom.getAll().get(_indexTab).get_classes().size();}

    // элемент по позиции
    @Override
    public Object getItem(int position) {return _ExamsClassrom.getAll().get(_indexTab).get_classes().get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassExamClassroom getClass(int position) {
        return ((ClassExamClassroom) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassExamClassroom p = getClass(position);

        if(convertView == null || parent != convertView.getParent()) {
            convertView = _lInflater.inflate(R.layout.schedule_list_exam_classroom_item, parent, false);

            String cnt = Integer.toString(position + 1);
            ((TextView) convertView.findViewById(R.id.id_pair)).setText(cnt);
            ((TextView) convertView.findViewById(R.id.time)).setText(p.get_time());

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) convertView.findViewById(R.id.subject)).setText(Constants.FREE_TIME);
            }
            else {
                ((TextView) convertView.findViewById(R.id.subject)).setText(p.get_subject().get(0) + " " + p.get_type().get(0));

                String groups = "";

                HashSet<String> groupsHashSet = new HashSet<>();

                for (int iCnt = 0; iCnt < p.get_groups().get(0).size(); ++iCnt) {groupsHashSet.add(p.get_groups().get(0).get(iCnt));}

                String[] groupsArray = {};
                groupsArray = groupsHashSet.toArray(new String[groupsHashSet.size()]);

                for (int iCnt = 0; iCnt < groupsArray.length; ++iCnt) {
                    groups += groupsArray[iCnt];
                    if(iCnt <  p.get_groups().get(0).size() - 1) groups += "\n";
                }

                String teachers = "";

                for (int iCnt = 0; iCnt < p.get_teacher().size(); ++iCnt) {
                    teachers += p.get_teacher().get(iCnt);
                    if(iCnt < p.get_teacher().size() - 1) teachers += "\n";
                }

                ((TextView) convertView.findViewById(R.id.groups)).setText(groups);

                ((TextView) convertView.findViewById(R.id.teachers)).setText(teachers);
            }
        }

        return convertView;
    }


    private LayoutInflater _lInflater;
    private ExamsClassroom _ExamsClassrom;
    private int _indexTab;
}
