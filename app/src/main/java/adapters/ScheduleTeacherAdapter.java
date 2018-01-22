package adapters;

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
import model.Teacher.ClassTeacher;

/**
 * Created by Mr.Nobody43 on 20.01.2018.
 */

public class ScheduleTeacherAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ClassTeacher> objects;

    public ScheduleTeacherAdapter(Context context, ArrayList<ClassTeacher> classTeacher) {
        ctx = context;
        objects = classTeacher;
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

    ClassTeacher getClass(int position) {
        return ((ClassTeacher) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;

        ClassTeacher p = getClass(position);

        view = lInflater.inflate(R.layout.schedule_list_teacher_item, parent, false);

        String cnt = Integer.toString(position + 1);
        ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) view.findViewById(R.id.time)).setText(p.get_time());

        for (Integer iCnt = 1; iCnt <= p.get_subject().size(); ++iCnt)
        {

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("classroom" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);

            } else {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_subject().get(iCnt - 1));

                String groups = "";

                for (String cur: p.get_groups().get(iCnt - 1) ){

                    cur = cur.replace("??", "пг");

                    groups += cur + "\n";
                }

                ((TextView) view.findViewById((ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", ctx.getPackageName())))).setText(groups);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_type().get(iCnt - 1));
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("classroom" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_classroom().get(iCnt - 1));
            }
        }

        for (Integer iCnt = p.get_classroom().size() + 1; iCnt <= 11; ++iCnt)
        {
            ((LinearLayout) view.findViewById((ctx.getResources().getIdentifier("pair" + iCnt.toString(), "id", ctx.getPackageName())))).setLayoutParams(new LinearLayout.LayoutParams(1, 1));

        }

        return view;
    }
}
