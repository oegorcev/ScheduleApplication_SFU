package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.mrnobody43.shedule_application.R;

import Utils.Constants;

/**
 * Created by Mr.Nobody43 on 23.01.2018.
 */

public class ScheduleEmptyAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;

    public ScheduleEmptyAdapter(Context context) {
        ctx = context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return 1;
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return 0;
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return 0;
    }


    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;

        if (view == null){
            view = lInflater.inflate(R.layout.schedule_list_group_item, parent, false);
        }

        ((TextView) view.findViewById(R.id.subject1)).setText(Constants.EMPTY_SCHEDULE);


        return view;
    }
}
