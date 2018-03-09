package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrnobody43.shedule_application.R;

import Utils.Constants;
import model.Exams.AllExams;
import model.Exams.ClassExam;

/**
 * Created by Mr.Nobody43 on 09.03.2018.
 */

public class ExamAdapter extends BaseAdapter {
    public ExamAdapter(Context context, AllExams allExams, int day) {
        _allExams = allExams;
        indexTab= day;
        _lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean isNull()
    {
        return _allExams == null;
    }

    // кол-во элементов
    @Override
    public int getCount() {return _allExams.getAll().get(indexTab).get_classes().size();}

    // элемент по позиции
    @Override
    public Object getItem(int position) {return _allExams.getAll().get(indexTab).get_classes().get(position);}

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassExam getClass(int position) {
        return ((ClassExam) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassExam p = getClass(position);

        if(convertView == null || parent != convertView.getParent()) {
            convertView = _lInflater.inflate(R.layout.schedule_list_exams_item, parent, false);

            String cnt = Integer.toString(position + 1);
            ((TextView) convertView.findViewById(R.id.id_pair)).setText(cnt);
            ((TextView) convertView.findViewById(R.id.time)).setText(p.get_time());

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) convertView.findViewById(R.id.subject)).setText(Constants.FREE_TIME);
            }
            else {
                ((TextView) convertView.findViewById(R.id.subject)).setText(p.get_subject().get(0));

                String teachers = "";

                for (int iCnt = 0; iCnt < p.get_teacher().size(); ++iCnt) {
                    teachers += p.get_teacher().get(iCnt) + " ";
                }

                ((TextView) convertView.findViewById(R.id.teacher)).setText(teachers);
                ((TextView) convertView.findViewById(R.id.type)).setText(p.get_type().get(0));
                ((TextView) convertView.findViewById(R.id.classroom)).setText(p.get_classroom().get(0));
            }
        }

        return convertView;
    }

    private LayoutInflater _lInflater;
    private AllExams _allExams;
    private int indexTab;
}
