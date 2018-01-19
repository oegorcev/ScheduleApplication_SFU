package data.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.ClassRoom.ClassClassRoom;

/**
 * Created by Mr.Nobody43 on 16.01.2018.
 */

public class ClassroomParser extends AbstractParser {

    public ClassroomParser(Context mContext) {super(mContext);}
    private int _counter;

    public ClassClassRoom parseClass(String[] data){
        ClassClassRoom aClassClassRoom = new ClassClassRoom();

        _counter = 0;

        ArrayList<String> teachers = new ArrayList<String>();
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<ArrayList<String>> groups  = new ArrayList<ArrayList<String>>();
        groups.add(new ArrayList<String>());
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> weeks = new  ArrayList<String>();

        if(data.length == 1) {
            teachers.add(Constants.FREE);
            subjects.add(Constants.FREE);
            groups.get(_counter).add(Constants.FREE);
            types.add(Constants.FREE);
            weeks.add(Constants.ALL_WEEKS);
        }
        else {
            int index = 0;

            while(index < data.length) {
                if(_counter != 0) groups.add(new ArrayList<String>());
                index = parseData(data, teachers, subjects, types, weeks, groups, index);
                _counter++;
            }
        }

        aClassClassRoom.set_subject(subjects);
        aClassClassRoom.set_groups(groups);
        aClassClassRoom.set_teacher(teachers);
        aClassClassRoom.set_weeks(weeks);
        aClassClassRoom.set_type(types);

        return aClassClassRoom;
    }

    private int parseData( String[] data,
                           ArrayList<String> teachers,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> weeks,
                           ArrayList<ArrayList<String>> groups,
                           int index){

        teachers.add(data[index++] + " " + data[index++]);
        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        String type = data[index];

        types.add(data[index++]);

        String gr = "";
        int cntGroups = 0;

        for (; ; ++index) {

            if(index == data.length || (!(Utilities.IsGroup(data[index])) && !(Utilities.IsSubgroup(data[index])))){
                groups.get(_counter).add(gr);
                break;
            }

            if(Utilities.IsGroup(data[index]) && cntGroups > 0){
                groups.get(_counter).add(gr);
                gr = "";
            }
            else if(Utilities.IsGroup(data[index]) && cntGroups == 0) {
                cntGroups++;
            }

            if(!gr.equals(Constants.EMPTY_STRING)){
                gr += " ";
            }
            gr += data[index];
        }

        //Обработка случаев составления расписания

        //Случай один - простейшее расписание
        if(index == data.length)
        {
            weeks.add(Constants.ALL_WEEKS);
            return index;
        }

        //Случай два, если расписание заканчивается на указании недели пар
        else if(data[index].charAt(data[index].length() - 1) == ')' && data[index].charAt(0) == '(' && index == (data.length - 1))
        {
            weeks.add(data[index++]);
        }
        //Случай три, неделя последняя, но ещё второй предмет по другой неделе ((1-9)Пилипушко)
        else if(data[index].charAt(0) == '(' && data[index].charAt(data[index].length() - 1) != ')'){
            weeks.add(data[index].substring(0, data[index].indexOf(')') + 1));
            data[index] = data[index].substring(data[index].indexOf(')') + 1, data[index].length());
        }
        //Случай восемь, нет недели, расписание корректно, несколько предметов
        else {
            weeks.add(Constants.ALL_WEEKS);
        }

        return index;
    }

}
