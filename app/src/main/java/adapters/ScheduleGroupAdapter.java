package adapters;

/**
 * Created by Mr.Nobody43 on 02.01.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrnobody43.shedule_application.R;

import java.util.ArrayList;

import Utils.Constants;
import model.Group.ClassGroup;

public class ScheduleGroupAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ClassGroup> objects;

    public ScheduleGroupAdapter(Context context, ArrayList<ClassGroup> classGroups) {
        ctx = context;
        objects = classGroups;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    ClassGroup getClass(int position) {
        return ((ClassGroup) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        ClassGroup p = getClass(position);

        if(p.get_classroom().size() == 1) {
            view = lInflater.inflate(R.layout.schedule_list_item, parent, false);
        }
        else
        {
            view = lInflater.inflate(R.layout.schedule_list_item_double, parent, false);
        }


        if(p.get_classroom().size() == 1) {
            String cnt = Integer.toString(position + 1);

            ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
            ((TextView) view.findViewById(R.id.time)).setText(p.get_time());
            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById(R.id.subject)).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById(R.id.teacher)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.type)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.subgroup)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.classroom)).setText(Constants.EMPTY_STRING);

                return view;
            } else {
                ((TextView) view.findViewById(R.id.subject)).setText(p.get_subject().get(0));
                ((TextView) view.findViewById(R.id.teacher)).setText(p.get_teacher().get(0));
                ((TextView) view.findViewById(R.id.type)).setText(p.get_type().get(0));
            }


            if (p.get_subgroup().get(0).equals(Constants.WITHOUT_SUBGROUB)) {
                ((TextView) view.findViewById(R.id.subgroup)).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById(R.id.subgroup)).setText(p.get_subgroup().get(0));
            }

            ((TextView) view.findViewById(R.id.classroom)).setText(p.get_classroom().get(0));

        } else {
            String cnt = Integer.toString(position + 1);

            /* Первая пара */
            ((TextView) view.findViewById(R.id.id_pair_first)).setText(cnt);
            ((TextView) view.findViewById(R.id.time)).setText(p.get_time());
            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById(R.id.subject_first)).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById(R.id.teacher_first)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.type_first)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.subgroup_first)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.classroom_first)).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById(R.id.subject_first)).setText(p.get_subject().get(0));
                ((TextView) view.findViewById(R.id.teacher_first)).setText(p.get_teacher().get(0));
                ((TextView) view.findViewById(R.id.type_first)).setText(p.get_type().get(0));
            }


            if (p.get_subgroup().get(0).equals(Constants.WITHOUT_SUBGROUB)) {
                ((TextView) view.findViewById(R.id.subgroup_first)).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById(R.id.subgroup_first)).setText(p.get_subgroup().get(0));
            }

            if(!(((TextView) view.findViewById(R.id.classroom_first)).toString().equals(Constants.EMPTY_STRING)))
            {
                ((TextView) view.findViewById(R.id.classroom_first)).setText(p.get_classroom().get(0));
            }
            /* Вторая пара */

            ((TextView) view.findViewById(R.id.time)).setText(p.get_time());
            if (p.get_subject().get(1).equals(Constants.FREE)) {
                ((TextView) view.findViewById(R.id.subject_second)).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById(R.id.teacher_second)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.type_second)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.subgroup_second)).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById(R.id.classroom_second)).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById(R.id.subject_second)).setText(p.get_subject().get(1));
                ((TextView) view.findViewById(R.id.teacher_second)).setText(p.get_teacher().get(1));
                ((TextView) view.findViewById(R.id.type_second)).setText(p.get_type().get(1));
            }

            if (p.get_subgroup().get(1).equals(Constants.WITHOUT_SUBGROUB)) {
                ((TextView) view.findViewById(R.id.subgroup_second)).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById(R.id.subgroup_second)).setText(p.get_subgroup().get(1));
            }

            if(!(((TextView) view.findViewById(R.id.classroom_second)).toString().equals(Constants.EMPTY_STRING)))
            {
                ((TextView) view.findViewById(R.id.classroom_second)).setText(p.get_classroom().get(1));
            }
        }
        return view;
    }

}