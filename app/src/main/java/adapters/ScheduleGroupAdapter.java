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
import data.DataBase.DataBaseMapper;
import model.Group.ClassGroup;
import model.Group.WeekGroup;

public class ScheduleGroupAdapter extends BaseAdapter {

    public ScheduleGroupAdapter(Context context, WeekGroup weekGroup, int day, int week) {
        _ctx = context;
        _weekGroup = weekGroup;
        int ret = (new DataBaseMapper(_ctx).getHideWeeksOption());

        _curWeek = Integer.parseInt(new DataBaseMapper(_ctx).getCurrentWeek());

        if(ret == 1)  _isHideWeeks = true;
        else _isHideWeeks = false;

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

            for (int iCnt = 0, pairId = 0; iCnt < Math.min(Constants.MAX_PAIR_COUNT,  p.get_subject().size()); ++iCnt, ++pairId) {

                Integer beginWeek = getBeginWeek(p.get_weeks().get(iCnt));
                Integer endWeek = getEndWeek(p.get_weeks().get(iCnt));

                if(!(checkWeek(beginWeek, endWeek, _curWeek)) && _isHideWeeks)
                {
                    if(iCnt == p.get_subject().size() - 1 && pairId == 0) {
                        viewHolder.subjects.get(pairId).setText(Constants.FREE_TIME);
                        viewHolder.teachers.get(pairId).setText(Constants.EMPTY_STRING);
                        viewHolder.otherInformations.get(pairId).setText(Constants.EMPTY_STRING);
                        viewHolder.classrooms.get(pairId).setText(Constants.EMPTY_STRING);
                    }

                    pairId--;
                    continue;
                }

                if(pairId > 0)  viewHolder.pairs.get(pairId).setLayoutParams(lParams1);

                if (p.get_subject().get(iCnt).equals(Constants.FREE)) {
                    viewHolder.subjects.get(pairId).setText(Constants.FREE_TIME);
                    viewHolder.teachers.get(pairId).setText(Constants.EMPTY_STRING);
                    viewHolder.otherInformations.get(pairId).setText(Constants.EMPTY_STRING);
                    viewHolder.classrooms.get(pairId).setText(Constants.EMPTY_STRING);
                } else {

                    viewHolder.teachers.get(pairId).setText(p.get_teacher().get(iCnt));

                    String weeks;
                    if (p.get_weeks().get(iCnt).equals(Constants.ALL_WEEKS)) weeks = "";
                    else weeks = p.get_weeks().get(iCnt);

                    if (!(p.get_subgroup().get(iCnt).equals(Constants.WITHOUT_SUBGROUB))) {
                        viewHolder.subjects.get(pairId).setText(p.get_subject().get(iCnt) + " - " + p.get_subgroup().get(iCnt));
                    } else {
                        viewHolder.subjects.get(pairId).setText(p.get_subject().get(iCnt));
                    }

                    String other_information = p.get_type().get(iCnt) + " " + weeks;

                    viewHolder.otherInformations.get(pairId).setText(other_information);
                    viewHolder.classrooms.get(pairId).setText(p.get_classroom().get(iCnt));
                }
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    private Integer getBeginWeek(String s) {
        if(s.equals(Constants.ALL_WEEKS)) return 0;
        String begin = s.substring(1, s.indexOf('-'));

        return Integer.parseInt(begin);
    }

    private Integer getEndWeek(String s) {
        if(s.equals(Constants.ALL_WEEKS)) return 228;
        String begin = s.substring(s.indexOf('-') + 1, s.length() - 1);

        return Integer.parseInt(begin);
    }

    private boolean checkWeek(Integer left, Integer right, Integer check) {
        return check >= left && check <= right;
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

            for(Integer i = 1; i <= Constants.MAX_PAIR_COUNT; ++i){
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
    private boolean _isHideWeeks;
    private int _indexTab;
    private int _curWeek;
    private int _indexWeek;
}