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
import model.ClassRoom.WeekClassRoom;

/**
 * Created by Mr.Nobody43 on 20.01.2018.
 */

public class ScheduleClassRoomAdapter extends BaseAdapter {


    public ScheduleClassRoomAdapter(Context context, WeekClassRoom weekClassRoom, int day, int week) {
        _ctx = context;
        _weekClassRoom = weekClassRoom;
        _indexTab = day;
        _indexWeek = week;
        _lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean isNull()
    {
        return _weekClassRoom == null;
    }

    // кол-во элементов
    @Override
    public int getCount() {return _weekClassRoom.getWeek().get(_indexTab).get_classesBotWeek().size();}

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        switch (_indexWeek % 2){
            case 0:
                return _weekClassRoom.getWeek().get(_indexTab).get_classesBotWeek().get(position);
            case 1:
                return _weekClassRoom.getWeek().get(_indexTab).get_classesTopWeek().get(position);
            default:
                return _weekClassRoom.getWeek().get(_indexTab).get_classesBotWeek().get(position);
        }
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassClassRoom getClass(int position) {
        return ((ClassClassRoom) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ClassClassRoom p = getClass(position);

        if (view == null){
            view = _lInflater.inflate(R.layout.schedule_list_classroom_item, parent, false);
        }

        String cnt = Integer.toString(position + 1);
        ((TextView) view.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) view.findViewById(R.id.time)).setText(p.get_time());

        for (Integer iCnt = 1; iCnt <= Math.min(20, p.get_subject().size()); ++iCnt)
        {

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(Constants.FREE_TIME);
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(Constants.EMPTY_STRING);
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(Constants.EMPTY_STRING);

            } else {
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("subject" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(p.get_subject().get(iCnt - 1));

                String groups = "";

                for (String cur: p.get_groups().get(iCnt - 1) ){

                    cur = cur.replace("??", "пг");

                    groups += cur + "\n";
                }

                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("teacher" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(p.get_teacher().get(iCnt - 1));
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("groups" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(groups);
                ((TextView) view.findViewById((_ctx.getResources().getIdentifier("type" + iCnt.toString(), "id", _ctx.getPackageName())))).setText(p.get_type().get(iCnt - 1));
            }
        }

        for (Integer iCnt = p.get_subject().size() + 1; iCnt <= Constants.LINEAR_LAYOUT_COUNT; ++iCnt)
        {
            ((LinearLayout) view.findViewById((_ctx.getResources().getIdentifier("pair" + iCnt.toString(), "id", _ctx.getPackageName())))).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        }


        return view;
    }

    private class ViewHolder {
        final TextView idPair, time;
        final ArrayList<TextView> subjects, types, groups, teachers;
        final ArrayList<LinearLayout> pairs;
        ViewHolder(View view){
            String packageName = _ctx.getPackageName();

            idPair = (TextView) view.findViewById(R.id.id_pair);
            time = (TextView) view.findViewById(R.id.time);

            subjects = new ArrayList<TextView>();
            teachers = new ArrayList<TextView>();
            types = new ArrayList<TextView>();
            groups = new ArrayList<TextView>();
            pairs = new ArrayList<LinearLayout>();

            for(Integer i = 1; i <= 20; ++i){
                String cnt = i.toString();

                pairs.add(((LinearLayout) view.findViewById((_ctx.getResources().getIdentifier("pair" + cnt, "id", packageName)))));
                subjects.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("subject" + cnt, "id", packageName)))));
                teachers.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("teacher" + cnt, "id", packageName)))));
                types.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("other_information" + cnt, "id", packageName)))));
                groups.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("classroom" + cnt, "id", packageName)))));
            }
        }
    }

    private Context _ctx;
    private LayoutInflater _lInflater;
    private WeekClassRoom _weekClassRoom;
    private int _indexTab;
    private int _indexWeek;
}
