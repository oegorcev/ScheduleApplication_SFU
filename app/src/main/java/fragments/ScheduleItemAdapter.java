package fragments;

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

import model.Class;

public class ScheduleItemAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Class> objects;

    public ScheduleItemAdapter(Context context, ArrayList<Class> classes) {
        ctx = context;
        objects = classes;
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

    Class getClass(int position) {
        return ((Class) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.schedule_list_item, parent, false);
        }

        Class p = getClass(position);

        String cnt = Integer.toString(position + 1);

        ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) view.findViewById(R.id.time)).setText(p.get_time());
        ((TextView) view.findViewById(R.id.subject)).setText(p.get_subject().get(0));
        ((TextView) view.findViewById(R.id.teacher)).setText(p.get_teacher().get(0));
        ((TextView) view.findViewById(R.id.type)).setText(p.get_type().get(0));
        ((TextView) view.findViewById(R.id.subgroup)).setText(p.get_subgroup().get(0));
        ((TextView) view.findViewById(R.id.classroom)).setText(p.get_classroom().get(0));

        return view;
    }

}