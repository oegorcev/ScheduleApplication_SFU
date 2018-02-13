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
import model.ClassRoom.ClassClassRoom;

/**
 * Created by Mr.Nobody43 on 20.01.2018.
 */

public class ScheduleClassRoomAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ClassClassRoom> objects;

    public ScheduleClassRoomAdapter(Context context, ArrayList<ClassClassRoom> classClassRooms) {
        ctx = context;
        objects = classClassRooms;
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

    ClassClassRoom getClass(int position) {
        return ((ClassClassRoom) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;

        ClassClassRoom p = getClass(position);

        if (view == null){
            view = lInflater.inflate(R.layout.schedule_list_classroom_item, parent, false);
        }

        String cnt = Integer.toString(position + 1);
        ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) view.findViewById(R.id.time)).setText(p.get_time());

        for (Integer iCnt = 1; iCnt <= Math.min(20, p.get_subject().size()); ++iCnt)
        {

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", ctx.getPackageName())))).setText(Constants.EMPTY_STRING);

            } else {
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_subject().get(iCnt - 1));

                String groups = "";

                for (String cur: p.get_groups().get(iCnt - 1) ){

                    cur = cur.replace("??", "пг");

                    groups += cur + "\n";
                }

                ((TextView) view.findViewById((ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_teacher().get(iCnt - 1));
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", ctx.getPackageName())))).setText(groups);
                ((TextView) view.findViewById((ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", ctx.getPackageName())))).setText(p.get_type().get(iCnt - 1));
            }
        }

        for (Integer iCnt = p.get_subject().size() + 1; iCnt <= Constants.LINEAR_LAYOUT_COUNT; ++iCnt)
        {
            ((LinearLayout) view.findViewById((ctx.getResources().getIdentifier("pair" + iCnt.toString(), "id", ctx.getPackageName())))).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        }


        return view;
    }
}
