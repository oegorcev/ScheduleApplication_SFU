package Utils.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.Teacher.ClassTeacher;

/**
 * Created by Mr.Nobody43 on 16.01.2018.
 */

public class TeacherParser extends AbstractParser {

    public TeacherParser(Context mContext)
    {
        super(mContext);
    }
    private int _counter;

    public ClassTeacher parseClass(String[] data){
        ClassTeacher aClassTeacher = new ClassTeacher();
        _counter = 0;

        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<ArrayList<String>> groups  = new ArrayList<ArrayList<String>>();
        groups.add(new ArrayList<String>());
        ArrayList<String> subgroups = new ArrayList<String>();
        ArrayList<String> classrooms = new ArrayList<String>();
        ArrayList<String> weeks = new ArrayList<String>();


        if(data.length == 1) {
            groups.get(_counter).add(Constants.FREE);
            subjects.add(Constants.FREE);
            types.add(Constants.FREE);
            classrooms.add(Constants.FREE);
            subgroups.add(Constants.FREE);
            weeks.add(Constants.ALL_WEEKS);
        }
        else {
            int index = 0;

            while(index < data.length) {
                if(_counter != 0) groups.add(new ArrayList<String>());
                index = parseData(data, groups, subjects, types, classrooms, subgroups, weeks, index);
                _counter++;
            }
        }

        aClassTeacher.set_classroom(classrooms);
        aClassTeacher.set_subgroup(subgroups);
        aClassTeacher.set_subject(subjects);
        aClassTeacher.set_groups(groups);
        aClassTeacher.set_weeks(weeks);
        aClassTeacher.set_type(types);

        return aClassTeacher;
    }

    private int parseData( String[] data,
                           ArrayList<ArrayList<String>> groups,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> classrooms,
                           ArrayList<String> subgroups,
                           ArrayList<String> weeks, int index){


        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        String type = data[index];

        types.add(data[index++]);

        for (; Utilities.IsGroup(data[index]) ; ++index) {
            groups.get(_counter).add(data[index]);
        }

        if (types.get(types.size()- 1).equals(Constants.LAB) && Utilities.IsSubgroup(data[index])){
            subgroups.add(data[index++]);
        } else {
            subgroups.add(Constants.WITHOUT_SUBGROUB);
        }

        classrooms.add(data[index++]);

        //Обработка случаев составления расписания

        //Случай один - простейшее расписание нет неделей и групп
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
        //Случай три, неделя последняя, но ещё второй предмет по другой неделе ((10-18)РиОВ,ПП)
        else if(data[index].charAt(0) == '(' && data[index].charAt(data[index].length() - 1) != ')'){
            weeks.add(data[index].substring(0, data[index].indexOf(')') + 1));
            data[index] = data[index].substring(data[index].indexOf(')') + 1, data[index].length());
        }
        //Случай четыре-, нет недели, расписание корректно, несколько предметов
        else {
            weeks.add(Constants.ALL_WEEKS);
            subgroups.add(Constants.WITHOUT_SUBGROUB);
        }

        return index;
    }


}
