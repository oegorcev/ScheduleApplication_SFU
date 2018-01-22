package adapters;

/**
 * Created by Mr.Nobody43 on 02.01.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
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

        view = lInflater.inflate(R.layout.schedule_list_group_item, parent, false);

        String cnt = Integer.toString(position + 1);
        ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) view.findViewById(R.id.time)).setText(p.get_time());

        for (Integer iCnt = 1; iCnt <= p.get_subject().size(); ++iCnt)
        {

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subgroup" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("classroom" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
            } else {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_subject().get(iCnt - 1));
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_teacher().get(iCnt - 1));
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_type().get(iCnt - 1));

                if (p.get_subgroup().get(iCnt - 1).equals(Constants.WITHOUT_SUBGROUB)) {
                    ((TextView) view.findViewById((ctx.getResources().getIdentifier("subgroup" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                } else {
                    ((TextView) view.findViewById((ctx.getResources().getIdentifier("subgroup" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_subgroup().get(iCnt - 1));
                }


                ((TextView) view.findViewById((ctx.getResources().getIdentifier("classroom" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_classroom().get(iCnt - 1));
            }
        }

        for (Integer iCnt = p.get_classroom().size() + 1; iCnt <= 4; ++iCnt)
        {
            ((LinearLayout) view.findViewById((ctx.getResources().getIdentifier("pair" + iCnt.toString(), "id", ctx.getPackageName())))).setLayoutParams(new LinearLayout.LayoutParams(1, 1));

        }


        return view;
    }

}