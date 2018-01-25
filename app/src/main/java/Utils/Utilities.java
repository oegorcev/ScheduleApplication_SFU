package Utils;

/**
 * Created by Mr.Nobody43 on 25.10.2017.
 */

public class Utilities {

    public static boolean isEven(int value)
    {
        return ((value & 1) == 0);
    }

    public static boolean isEven(long value)
    {
        return ((value & 1) == 0);
    }

    public static boolean CheckType(String s) {
        return s.equals(Constants.LECTION) || s.equals(Constants.PRACTIC) || s.equals(Constants.LAB);
    }

    public static Integer SetState(String query)
    {
        String[] mas = query.split(" ");

        if(Utilities.IsGroup(query)){
            return Constants.GROUP;
        } else if(Utilities.IsClassRoom(query)){
            return Constants.CLASSROOM;
        } else if(mas.length > 1){
            return Constants.TEACHER;
        }

        return  Constants.GROUP;
    }




    public static boolean IsGroup (String s) {
        //Если перед тире стоит буква, то это точно не группа или группы нет

        boolean someCases = s.equals("ЦДП") || s.equals("----") || s.equals("МРЦПК")
                || s.equals("шк.26") || s.equals("-") ||  s.equals("10") || s.equals("11");
        if(someCases) {
            return true;
        } else {
            if ((s.indexOf('-') != -1) && !s.equals("---") && s.indexOf("Вакансия") == -1) {
                return !Character.isLetter(s.charAt(s.indexOf('-') - 1)) && (s.indexOf('(') == -1);
            } else {
                return false;
            }
        }
    }

    public static boolean IsClassRoom (String s) {
        //Если перед тире стоит буква, то это аудитория

        boolean someCases = s.equals("---") || s.equals("ЛИЦ.") || s.equals("ЛИЦ. 4") || s.equals("ЛИЦ.") || s.equals("4") || s.equals("СПОРТЗАЛ") || s.equals("ТИР");
        if(someCases) {
            return true;
        }

        if((s.indexOf('-') != -1) && !s.equals("-")) {
            return  ((s.indexOf('-') - 1 != -1) && Character.isLetter(s.charAt(s.indexOf('-') - 1)));
        } else {
            return  false;
        }
    }

    public static boolean IsSubgroup (String s) {
        return s.equals(Constants.FIRST_SUB_BAD) || s.equals(Constants.SECOND_SUB_BAD) || s.equals(Constants.THIRD_SUB_BAD) || s.equals("А") || s.equals("В")|| s.equals("Б");
    }

    public  static  boolean IsWeek (String s){
        //Если предпоследни  символ не точка, то это не типа а точно недяля обычная или слитая
        return (s.charAt(0) == '(') && (s.charAt(s.length() - 2) != '.');
    }
}
