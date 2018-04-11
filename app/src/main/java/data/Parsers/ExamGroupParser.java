package data.Parsers;

import android.content.Context;

import java.util.ArrayList;

import Utils.Constants;
import Utils.Utilities;
import model.ExamGroup.ClassExamGroup;

/**
 * Created by Mr.Nobody43 on 07.03.2018.
 */

public class ExamGroupParser extends AbstractParser {

    public ExamGroupParser(Context mContext)
    {
        super(mContext);
    }

    public ClassExamGroup parseClass(String[] data){
        ClassExamGroup aClassExamGroup = new ClassExamGroup();

        ArrayList<String> teachers = new ArrayList<String>();
        ArrayList<String> subjects = new ArrayList<String>();
        ArrayList<String> types = new ArrayList<String>();
        ArrayList<String> classrooms = new ArrayList<String>();

        if(data.length == 1) {
            teachers.add(Constants.FREE);
            subjects.add(Constants.FREE);
            types.add(Constants.FREE);
            classrooms.add(Constants.FREE);
        }
        else {
            int index = 0;

            while(index < data.length) {
                index = parseData(data, teachers, subjects, types, classrooms, index);
            }
        }

        aClassExamGroup.set_classroom(classrooms);
        aClassExamGroup.set_subject(subjects);
        aClassExamGroup.set_teacher(teachers);
        aClassExamGroup.set_type(types);

        return aClassExamGroup;
    }

    private int parseData( String[] data,
                           ArrayList<String> teachers,
                           ArrayList<String> subjects,
                           ArrayList<String> types,
                           ArrayList<String> classrooms,
                           int index){

        teachers.add(data[index++] + " " + data[index++]);
        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        types.add(data[index++]);

        classrooms.add(data[index++]);

        return index;
    }
}
