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

import Utils.Constants;
import model.Group.ClassGroup;
import model.Group.WeekGroup;

public class ScheduleGroupAdapter extends BaseAdapter {
    private Context ctx;
    private LayoutInflater lInflater;
    private WeekGroup _weekGroup;
    private int indexTab;
    private int indexWeek;

    public ScheduleGroupAdapter(Context context, WeekGroup weekGroup) {
        ctx = context;
        _weekGroup = weekGroup;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return _weekGroup.getWeek().get(indexTab).get_classesBotWeek().size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        switch (indexWeek){
            case 0:
                return _weekGroup.getWeek().get(indexTab).get_classesBotWeek().get(position);
            case 1:
                return _weekGroup.getWeek().get(indexTab).get_classesTopWeek().get(position);
            default:
                return _weekGroup.getWeek().get(indexTab).get_classesBotWeek().get(position);
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ClassGroup getClass(int position) {
        return ((ClassGroup) getItem(position));
    }

    public void refreshData(int idTab, int idWeek){

        //наполняем измененными данными
        indexTab= idTab;
        indexWeek = idWeek;
        //передергиваем адаптер
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ClassGroup p = getClass(position);

        convertView = lInflater.inflate(R.layout.schedule_list_group_item, parent, false);

        ((TextView) convertView.findViewById(R.id.id_pair)).setText(Integer.toString(position + 1));
        ((TextView) convertView.findViewById(R.id.time)).setText(p.get_time());

        LinearLayout.LayoutParams lParams1 = (LinearLayout.LayoutParams) ((LinearLayout) convertView.findViewById(R.id.pair2)).getLayoutParams();
        ((LinearLayout) convertView.findViewById(R.id.pair2)).setLayoutParams(new LinearLayout.LayoutParams(0, 0));

        for (int iCnt = 0; iCnt < p.get_subject().size(); ++iCnt) {
            Integer i = iCnt + 1;
            String cnt = i.toString();
            String packageName = ctx.getPackageName();

            if (iCnt > 0) {
                ((LinearLayout) convertView.findViewById((ctx.getResources().getIdentifier("pair" + cnt, "id", packageName)))).setLayoutParams(lParams1);
            }

            if (p.get_subject().get(iCnt).equals(Constants.FREE)) {
                ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("subject" + cnt, "id", packageName)))).setText(Constants.FREE_TIME);
            } else {
                ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("subject" + cnt, "id", packageName)))).setText(p.get_subject().get(iCnt));
                ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("teacher" + cnt, "id", packageName)))).setText(p.get_teacher().get(iCnt));
                ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("type" + cnt, "id", packageName)))).setText(p.get_type().get(iCnt));

                if (!(p.get_subgroup().get(iCnt).equals(Constants.WITHOUT_SUBGROUB))) {
                    ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("subgroup" + cnt, "id", packageName)))).setText(p.get_subgroup().get(iCnt));
                }

                ((TextView) convertView.findViewById((ctx.getResources().getIdentifier("classroom" + cnt, "id", packageName)))).setText(p.get_classroom().get(iCnt));
            }
        }

        return convertView;
    }

}