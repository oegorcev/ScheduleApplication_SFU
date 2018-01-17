package Utils;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Group.ClassGroup;

public class Parser extends AsyncTask<String, Void, Void> {

    private ArrayList<String> _times;
    private List<List<Pair<String,String>>> _schedule;

    @Override
    protected Void doInBackground(String... params) {

        Document doc = null;
        try {
            doc = Jsoup.connect(Constants.URL + params[0] + Constants.POTOK + Constants.getCurPot() + Constants.SEMESTR + params[1]).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Element table = doc.getElementById(Constants.WEEK_SCHEDULE);

        Elements trs = table.getElementsByTag("tr");

        _times = new ArrayList<String>();

        _schedule = new ArrayList<>();
        for (int cnt = Constants.DEFAULT_VALUE_CNT_PARSER; cnt < Constants.DAYS_ON_WEEK; ++cnt) _schedule.add(new ArrayList<Pair<String,String>>());

        int cntI = Constants.DEFAULT_VALUE_CNT_PARSER;
        int curDay = Constants.DEFAULT_VALUE_CNT_PARSER;

        for (Element curTr: trs) {
            Elements tds = curTr.getElementsByTag("td");
            int cntJ = Constants.DEFAULT_VALUE_CNT_PARSER;

            for (Element curTd: tds) {
                if (cntI == Constants.DATE_INDEX) {
                    if(cntJ >= Constants.BEGIN_TIME) {
                        Elements strtTime = curTd.getElementsByClass(Constants.START_TIME);
                        Elements endTime = curTd.getElementsByClass(Constants.END_TIME);

                        String s = strtTime.get(0).html().concat(Constants.SEPARATOR).concat(endTime.get(0).html());

                        _times.add(s);
                    }
                }
                else if(cntI > Constants.DATE_INDEX) {
                    if(Utilities.isEven(cntI)) {
                        Pair<String,String> curPair = new Pair<String,String>(Constants.SEPARATOR, Constants.SEPARATOR);

                        if (curTd.attr(Constants.CLASS).equals(Constants.TOP_WEEK)) {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(Constants.RESERVED);
                        }
                        else {
                            curPair.setFirst(curTd.html());
                            curPair.setSecond(curTd.html());
                        }

                        _schedule.get(curDay).add(curPair);
                    }
                    else {
                        int cntPair = Constants.DEFAULT_VALUE_CNT_PARSER;

                        for (Pair<String, String> curP: _schedule.get(curDay)) {
                            if (curP.getSecond().equals(Constants.RESERVED)) {
                                _schedule.get(curDay).get(cntPair).setSecond(curTd.html());
                                break;
                            }

                            cntPair++;
                        }
                    }
                }

                cntJ++;
            }

            if(cntI > Constants.DATE_INDEX && !Utilities.isEven(cntI)) curDay++;

            cntI++;
        }

        return null;
    }

    public ArrayList<String> get_times() {
        return _times;
    }

    public void set_times(ArrayList<String> _times) {
        this._times = _times;
    }

    public List<List<Pair<String, String>>> get_schedule() {
        return _schedule;
    }

    public void set_schedule(List<List<Pair<String, String>>> _schedule) {
        this._schedule = _schedule;
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

        teachers.add(data[index++] + " " + data[index++]);
        String subject = "";
        for (; !Utilities.CheckType(data[index]) ; ++index) {
            subject = subject.concat(data[index] + " ");

        }
        subjects.add(subject);

        String type = data[index];

        types.add(data[index++]);
        classrooms.add(data[index++]);

        //Обработка случаев составления расписания

        //Случай один - простейшее расписание
        if(index == data.length)
        {
            weeks.add(Constants.ALL_WEEKS);
            subgroups.add(Constants.WITHOUT_SUBGROUB);
            return index;
        }

        //Случай два, если расписание закнчивается на указании недели пар
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

        return index;
    }
}
