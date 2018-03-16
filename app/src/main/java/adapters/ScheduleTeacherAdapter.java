package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mrnobody43.shedule_application.R;

import Utils.Constants;
import model.Teacher.ClassTeacher;
import model.Teacher.WeekTeacher;

/**
 * Created by Mr.Nobody43 on 20.01.2018.
 */

public class ScheduleTeacherAdapter extends BaseAdapter {

    public ScheduleTeacherAdapter(Context context, WeekTeacher WeekTeacher,int day, int week) {
        _ctx = context;
        _weekTeacher = WeekTeacher;
        _indexTab= day;
        _indexWeek = week;
        _lInflater = (LayoutInflater) _ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean isNull()
    {
        return _weekTeacher == null || _weekTeacher.isEmpty();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return _weekTeacher.getWeek().get(_indexTab).get_classesBotWeek().size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        switch (_indexWeek % 2){
            case 0:
                return _weekTeacher.getWeek().get(_indexTab).get_classesBotWeek().get(position);
            case 1:
                return _weekTeacher.getWeek().get(_indexTab).get_classesTopWeek().get(position);
            default:
                return _weekTeacher.getWeek().get(_indexTab).get_classesBotWeek().get(position);
        }
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassTeacher getClass(int position) {
        return ((ClassTeacher) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view

        ClassTeacher p = getClass(position);

        if(convertView == null || convertView.getParent() != parent) {
            convertView = _lInflater.inflate(R.layout.schedule_list_teacher_item, parent, false);
        }

        String cnt = Integer.toString(position + 1);
        ((TextView) convertView.findViewById(R.id.id_pair)).setText(cnt);
        ((TextView) convertView.findViewById(R.id.time)).setText(p.get_time());

        LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) ((LinearLayout) convertView.findViewById((_ctx.getResources().getIdentifier("pair2", "id", _ctx.getPackageName())))).getLayoutParams();
        ((LinearLayout) convertView.findViewById((_ctx.getResources().getIdentifier("pair2", "id", _ctx.getPackageName())))).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        for (Integer iCnt = 1; iCnt <= p.get_subject().size(); ++iCnt)
        {

            String sCnt = iCnt.toString();
            String packageName = _ctx.getPackageName();

            if(iCnt > 1){
                ((LinearLayout) convertView.findViewById((_ctx.getResources().getIdentifier("pair" + iCnt.toString(), "id", _ctx.getPackageName())))).setLayoutParams(lParams1);
            }

            if (p.get_subject().get(0).equals(Constants.FREE)) {
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("subject" + sCnt, "id", packageName)))).setText(Constants.FREE_TIME);
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("type" + sCnt, "id", packageName)))).setText(Constants.EMPTY_STRING);
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("groups" + sCnt, "id", packageName)))).setText(Constants.EMPTY_STRING);
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("classroom" + sCnt, "id", packageName)))).setText(Constants.EMPTY_STRING);

            } else {
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("subject" + sCnt, "id", packageName)))).setText(p.get_subject().get(iCnt - 1));

                String groups = "";

                for (String cur: p.get_groups().get(iCnt - 1) ){

                    cur = cur.replace("??", "пг");

                    groups += cur + "\n";
                }

                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("groups" + sCnt, "id", packageName)))).setText(groups);
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("type" + sCnt, "id", packageName)))).setText(p.get_type().get(iCnt - 1));
                ((TextView) convertView.findViewById((_ctx.getResources().getIdentifier("classroom" + sCnt, "id", packageName)))).setText(p.get_classroom().get(iCnt - 1));
            }
        }


        return convertView;
    }

    private Context _ctx;
    private LayoutInflater _lInflater;
    private WeekTeacher _weekTeacher;
    private int _indexTab;
    private int _indexWeek;
}
