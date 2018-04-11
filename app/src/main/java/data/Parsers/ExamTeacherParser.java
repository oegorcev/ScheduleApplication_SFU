package data.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.ExamTeacher.ClassExamTeacher;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class ExamTeacherParser extends AbstractParser {
    public ExamTeacherParser(Context mContext)
    {
        super(mContext);
    }

    public ClassExamTeacher parseClass(String[] data){
        ClassExamTeacher aClassExamTeacher = new ClassExamTeacher();
        _counter = 0;

        ArrayList<ArrayList<String>> groups  = new ArrayList<ArrayList<String>>();
        groups.add(new ArrayList<String>());
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> classrooms = new ArrayList<String>();

        if(data.length == 1) {
            groups.get(_counter).add(Constants.FREE);
            subjects.add(Constants.FREE);
            types.add(Constants.FREE);
            classrooms.add(Constants.FREE);
        }
        else {
            int index = 0;

            while(index < data.length) {
                index = parseData(data, groups, subjects, types, classrooms, index);
            }
        }

        aClassExamTeacher.set_classroom(classrooms);
        aClassExamTeacher.set_subject(subjects);
        aClassExamTeacher.set_groups(groups);
        aClassExamTeacher.set_type(types);

        return aClassExamTeacher;
    }

    private int parseData( String[] data,
                           ArrayList<ArrayList<String>> groups,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> classrooms,

                           int index){

        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        types.add(data[index++]);

        String gr = "";
        int cntGroups = 0;

        for (; ; ++index) {
            if(Utilities.IsClassRoom(data[index])){
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

        classrooms.add(data[index++]);

        return index;
    }

    private int _counter;
}
