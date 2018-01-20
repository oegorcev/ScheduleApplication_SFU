package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

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

        return view;
    }
}
