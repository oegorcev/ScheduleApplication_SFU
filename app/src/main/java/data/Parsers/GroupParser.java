package data.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.Group.ClassGroup;

/**
 * Created by Mr.Nobody43 on 15.01.2018.
 */

public class GroupParser extends AbstractParser {

    public GroupParser(Context mContext)
    {
        super(mContext);
    }

    public ClassGroup parseClass(String[] data){
        ClassGroup aClassGroup = new ClassGroup();

        ArrayList<String> teachers = new ArrayList<String>();
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> classrooms = new ArrayList<String>();
        ArrayList<String> subgroups = new ArrayList<String>();
        ArrayList<String> weeks = new  ArrayList<String>();

        if(data.length == 1) {
            teachers.add(Constants.FREE);
            subjects.add(Constants.FREE);
            types.add(Constants.FREE);
            classrooms.add(Constants.FREE);
            subgroups.add(Constants.FREE);
            weeks.add(Constants.ALL_WEEKS);
        }
        else {
            int index = 0;

            while(index < data.length) {
                index = parseData(data, teachers, subjects, types, classrooms, subgroups, weeks, index);
            }
        }

        aClassGroup.set_classroom(classrooms);
        aClassGroup.set_subgroup(subgroups);
        aClassGroup.set_subject(subjects);
        aClassGroup.set_teacher(teachers);
        aClassGroup.set_weeks(weeks);
        aClassGroup.set_type(types);

        return aClassGroup;
    }

    private int parseData( String[] data,
                           ArrayList<String> teachers,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> classrooms,
                           ArrayList<String> subgroups,
                           ArrayList<String> weeks, int index){

        teachers.add(data[index++] + " " + data[index++] + (data[index].equals("....") ? " "  + data[index++] : (data[index].equals("-.-.")) ? " "  + data[index++]  :""));
        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        String type = data[index];

        types.add(data[index++]);

        String cr = "";

        for (;index < data.length && Utilities.IsClassRoom(data[index]); ++index) {
            if(!cr.equals(Constants.EMPTY_STRING)){
                cr += " ";
            }
            cr += data[index];
        }
        classrooms.add(cr);

        //Обработка случаев составления расписания

        //Случай один - простейшее расписание
        if(index == data.length)
        {
            weeks.add(Constants.ALL_WEEKS);
            subgroups.add(Constants.WITHOUT_SUBGROUB);
            return index;
        }

        //Случай два, если расписание заканчивается на указании недели пар
        else if(data[index].charAt(data[index].length() - 1) == ')' && data[index].charAt(0) == '(' && index == (data.length - 1))
        {
            weeks.add(data[index++]);
            subgroups.add(Constants.WITHOUT_SUBGROUB);
        }
        //Случай три, неделя последняя, но ещё второй предмет по другой неделе ((1-9)Пилипушко)
        else if(data[index].charAt(0) == '(' && data[index].charAt(data[index].length() - 1) != ')'){
            weeks.add(data[index].substring(0, data[index].indexOf(')') + 1));
            subgroups.add(Constants.WITHOUT_SUBGROUB);
            data[index] = data[index].substring(data[index].indexOf(')') + 1, data[index].length());
        }
        //Случай четыре, корректное расписание подргуппа и неделя
        else if(data[index].charAt(data[index].length() - 1) == ')' && data[index].charAt(0) == '(' && index == (data.length - 2)) {
            weeks.add(data[index++]);

            if (data[index++].charAt(0) == '1') {
                subgroups.add(Constants.FIRST_SUB);
            } else {
                subgroups.add(Constants.SECOND_SUB);
            }
        }
        //Случай пять, некорректное расписание, имеется подгруппа и неделя и номер подргуппы слит со следующим расписанием (1??.Пилипушко)
        else if(data[index].charAt(0) == '(' && data[index].charAt(data[index].length() - 1) == ')' && data[index + 1].charAt(3) == Constants.DOT && data[index + 1].length() > 4){
            weeks.add(data[index++]);

            if (data[index].charAt(0) == '1') {
                subgroups.add(Constants.FIRST_SUB);
            } else {
                subgroups.add(Constants.SECOND_SUB);
            }

            //*пг. + 1 = 4
            data[index] = data[index].substring(4, data[index].length());
        }
        //Случай шесть, нет недели, подгруппа последняя в расписании
        else if ((data[index].charAt(0) == '1' || data[index].charAt(0) == '2') && index == data.length - 1){
            weeks.add(Constants.ALL_WEEKS);
            if (data[index++].charAt(0) == '1') {
                subgroups.add(Constants.FIRST_SUB);
            } else {
                subgroups.add(Constants.SECOND_SUB);
            }
        }
        //Случай семь, нет недели, есть подргуппа, раписание некорректно (1??.Пилипушко)
        else if(data[index].charAt(0) == '1' || data[index].charAt(0) == '2'){
            weeks.add(Constants.ALL_WEEKS);
            if (data[index].charAt(0) == '1') {
                subgroups.add(Constants.FIRST_SUB);
            } else {
                subgroups.add(Constants.SECOND_SUB);
            }

            data[index] = data[index].substring(4, data[index].length());
        }

        //Случай восемь, нет недели, расписание корректно, несколько предметов
        else {
            weeks.add(Constants.ALL_WEEKS);
            subgroups.add(Constants.WITHOUT_SUBGROUB);
        }

        return index;
    }
}

