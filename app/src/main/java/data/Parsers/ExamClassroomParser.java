package data.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.ExamClassroom.ClassExamClassroom;

/**
 * Created by Mr.Nobody43 on 11.04.2018.
 */

public class ExamClassroomParser extends AbstractParser {

    public ExamClassroomParser(Context mContext)
    {
        super(mContext);
    }

    public ClassExamClassroom parseClass(String[] data){
        ClassExamClassroom aClassExamClassroom = new ClassExamClassroom();
        _counter = 0;

        ArrayList<ArrayList<String>> groups  = new ArrayList<ArrayList<String>>();
        groups.add(new ArrayList<String>());
        ArrayList<String> teachers  = new ArrayList<String>();
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();

        if(data.length == 1) {
            groups.get(_counter).add(Constants.FREE);
            teachers.add(Constants.FREE);
            subjects.add(Constants.FREE);
            types.add(Constants.FREE);
        }
        else {
            int index = 0;

            while(index < data.length) {
                index = parseData(data, groups, subjects, types, teachers, index);
            }
        }

        aClassExamClassroom.set_teacher(teachers);
        aClassExamClassroom.set_subject(subjects);
        aClassExamClassroom.set_groups(groups);
        aClassExamClassroom.set_type(types);

        return aClassExamClassroom;
    }

    private int parseData( String[] data,
                           ArrayList<ArrayList<String>> groups,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> teachers,

                           int index){

        teachers.add(data[index++] + " " + data[index++]);

        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");
        }
        subjects.add(subject);

        if(data[index].equals(Constants.KURS_WORK) ||(data[index].equals(Constants.UST_LECTION))) {
            types.add(data[index] + data[index + 1]);
            index += 2;
        } else {
            types.add(data[index++]);
        }

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

        if(index != data.length){
            if(Utilities.IsWeek(data[index])) {
                index++;
            }
        }

        return index;
    }

    private int _counter;

}
