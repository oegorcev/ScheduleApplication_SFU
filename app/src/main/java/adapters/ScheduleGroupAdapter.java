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
import model.Group.WeekGroup;

public class ScheduleGroupAdapter extends BaseAdapter {

    public ScheduleGroupAdapter(Context context, WeekGroup weekGroup, int day, int week) {
        _ctx = context;
        _weekGroup = weekGroup;
        _indexTab = day;
        _indexWeek = week;
        _lInflater = (LayoutInflater) _ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public boolean isNull()
    {
        return _weekGroup == null || _weekGroup.isEmpty();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return _weekGroup.get_week().get(_indexTab).get_classesBotWeek().size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        switch (_indexWeek % 2){
            case 0:
                return _weekGroup.get_week().get(_indexTab).get_classesBotWeek().get(position);
            case 1:
                return _weekGroup.get_week().get(_indexTab).get_classesTopWeek().get(position);
            default:
                return _weekGroup.get_week().get(_indexTab).get_classesBotWeek().get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassGroup getClass(int position) {
        return ((ClassGroup) getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassGroup p = getClass(position);

        ViewHolder viewHolder;

        if(convertView == null || parent != convertView.getParent()) {
            convertView = _lInflater.inflate(R.layout.schedule_list_group_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

            viewHolder.idPair.setText(Integer.toString(position + 1));
            viewHolder.time.setText(p.get_time());

            LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) ((LinearLayout) convertView.findViewById(R.id.pair2)).getLayoutParams();
            viewHolder.pairs.get(1).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

            //max = 6???
            for (int iCnt = 0; iCnt < p.get_subject().size(); ++iCnt) {

                if (iCnt > 0) {
                    viewHolder.pairs.get(iCnt).setLayoutParams(lParams1);
                }

                if (p.get_subject().get(iCnt).equals(Constants.FREE)) {
                    viewHolder.subjects.get(iCnt).setText(Constants.FREE_TIME);
                } else {
                    viewHolder.subjects.get(iCnt).setText(p.get_subject().get(iCnt));
                    viewHolder.teachers.get(iCnt).setText(p.get_teacher().get(iCnt));

                    String other_information = p.get_type().get(iCnt);

                    if (!(p.get_subgroup().get(iCnt).equals(Constants.WITHOUT_SUBGROUB))) {
                        other_information += " " + p.get_subgroup().get(iCnt);
                    }

                    viewHolder.otherInformations.get(iCnt).setText(other_information);
                    viewHolder.classrooms.get(iCnt).setText(p.get_classroom().get(iCnt));
                }
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private class ViewHolder {
        final TextView idPair, time;
        final ArrayList<TextView> subjects, teachers, otherInformations, classrooms;
        final ArrayList<LinearLayout> pairs;
        ViewHolder(View view){
            String packageName = _ctx.getPackageName();

            idPair = (TextView) view.findViewById(R.id.id_pair);
            time = (TextView) view.findViewById(R.id.time);

            subjects = new ArrayList<TextView>();
            teachers = new ArrayList<TextView>();
            otherInformations = new ArrayList<TextView>();
            classrooms = new ArrayList<TextView>();
            pairs = new ArrayList<LinearLayout>();

            for(Integer i = 1; i <= 6; ++i){
                String cnt = i.toString();

                pairs.add(((LinearLayout) view.findViewById((_ctx.getResources().getIdentifier("pair" + cnt, "id", packageName)))));
                subjects.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("subject" + cnt, "id", packageName)))));
                teachers.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("teacher" + cnt, "id", packageName)))));
                otherInformations.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("other_information" + cnt, "id", packageName)))));
                classrooms.add(((TextView) view.findViewById((_ctx.getResources().getIdentifier("classroom" + cnt, "id", packageName)))));
            }

        }
    }

    private Context _ctx;
    private LayoutInflater _lInflater;
    private WeekGroup _weekGroup;
    private int _indexTab;
    private int _indexWeek;
}