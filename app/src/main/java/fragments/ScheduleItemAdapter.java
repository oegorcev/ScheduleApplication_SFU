package fragments;

/**
 * Created by Mr.Nobody43 on 02.01.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.mrnobody43.shedule_application.R;

import java.util.ArrayList;

import model.Day;

public class ScheduleItemAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Day> objects;

    ScheduleItemAdapter(Context context, ArrayList<Day> days) {
        ctx = context;
        objects = days;
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

    Day getDay(int position) {
        return ((Day) getItem(position));
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.schedule_list_item, parent, false);
        }

        Day p = getDay(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
       // ((TextView) view.findViewById(R.id.tvDescr)).setText(p.name);
       // ((TextView) view.findViewById(R.id.tvPrice)).setText(p.price + "");


        return view;
    }

}