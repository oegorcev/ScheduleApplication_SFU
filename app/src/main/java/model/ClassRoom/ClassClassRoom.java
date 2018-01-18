package model.ClassRoom;

import java.util.ArrayList;

/**
 * Created by Mr.Nobody43 on 18.01.2018.
 */

public class ClassClassRoom {

    private String _time;
    private ArrayList<String> _teacher;
    private ArrayList<String> _subject;
    private ArrayList<String> _type;
    private ArrayList<ArrayList<String>> _groups;
    private ArrayList<String> _subgroup;
    private ArrayList<String> _weeks;

    public String get_time() {
        return _time;
    }

    public void set_time(String _time) {
        this._time = _time;
    }

    public ArrayList<String> get_teacher() {
        return _teacher;
    }

    public void set_teacher(ArrayList<String> _teacher) {
        this._teacher = _teacher;
    }

    public ArrayList<String> get_subject() {
        return _subject;
    }

    public void set_subject(ArrayList<String> _subject) {
        this._subject = _subject;
    }

    public ArrayList<String> get_type() {
        return _type;
    }

    public void set_type(ArrayList<String> _type) {
        this._type = _type;
    }

    public ArrayList<ArrayList<String>> get_groups() {
        return _groups;
    }

    public void set_groups(ArrayList<ArrayList<String>> _groups) {
        this._groups = _groups;
    }

    public ArrayList<String> get_subgroup() {
        return _subgroup;
    }

    public void set_subgroup(ArrayList<String> _subgroup) {
        this._subgroup = _subgroup;
    }

    public ArrayList<String> get_weeks() {
        return _weeks;
    }

    public void set_weeks(ArrayList<String> _weeks) {
        this._weeks = _weeks;
    }
}
