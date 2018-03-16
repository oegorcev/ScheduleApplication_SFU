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
        return _weekClassRoom == null || _weekClassRoom.isEmpty();
    }

    // кол-во элементов
    @Override
    public int getCount() {return _weekClassRoom.get_week().get(_indexTab).get_classesBotWeek().size();}

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        switch (_indexWeek % 2){
            case 0:
                return _weekClassRoom.get_week().get(_indexTab).get_classesBotWeek().get(position);
            case 1:
                return _weekClassRoom.get_week().get(_indexTab).get_classesTopWeek().get(position);
            default:
                return _weekClassRoom.get_week().get(_indexTab).get_classesBotWeek().get(position);
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

        ClassClassRoom p = getClass(position);
        ViewHolder viewHolder;

        if(convertView == null || parent != convertView.getParent()) {
            convertView = _lInflater.inflate(R.layout.schedule_list_classroom_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            viewHolder.idPair.setText(Integer.toString(position + 1));
            viewHolder.time.setText(p.get_time());

            LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) ((LinearLayout) convertView.findViewById(R.id.pair2)).getLayoutParams();
            viewHolder.pairs.get(1).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            for (Integer iCnt = 0; iCnt < Math.min(Constants.MAX_PAIR_COUNT, p.get_subject().size()); ++iCnt) {

                if (iCnt > 0) {
                    viewHolder.pairs.get(iCnt).setLayoutParams(lParams1);
                }

                if (p.get_subject().get(iCnt).equals(Constants.FREE)) {
                    viewHolder.subjects.get(iCnt).setText(Constants.FREE_TIME);
                    viewHolder.teachers.get(iCnt).setText(Constants.EMPTY_STRING);
                    viewHolder.types.get(iCnt).setText(Constants.EMPTY_STRING);
                    viewHolder.groups.get(iCnt).setText(Constants.EMPTY_STRING);
                } else {
                    viewHolder.subjects.get(iCnt).setText(p.get_subject().get(iCnt));
                    viewHolder.teachers.get(iCnt).setText(p.get_teacher().get(iCnt));
                    viewHolder.types.get(iCnt).setText(p.get_type().get(iCnt));

                    String groups = "";

                    for (String cur : p.get_groups().get(iCnt)) {

                        cur = cur.replace("??", "пг");

                        groups += cur + "\n";
                    }

                    viewHolder.groups.get(iCnt).setText(groups);
                }
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
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

            for(Integer i = 1; i <= Constants.MAX_PAIR_COUNT; ++i){
                String cnt = i.toString();

                pairs.add(((LinearLayout) view.findViewById((_ctx.getResources().getIdentifier("pair" + cnt, "id", packageName)))));
                subjects.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("subject" + cnt, "id", packageName)))));
                teachers.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("teacher" + cnt, "id", packageName)))));
                types.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("type" + cnt, "id", packageName)))));
                groups.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("groups" + cnt, "id", packageName)))));
            }
        }
    }

    private Context _ctx;
    private LayoutInflater _lInflater;
    private WeekClassRoom _weekClassRoom;
    private int _indexTab;
    private int _indexWeek;
}
