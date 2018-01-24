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
        } else if(mas.length > 1){
            return Constants.TEACHER;
        } else if(Utilities.IsClassRoom(query)){
            return Constants.CLASSROOM;
        }

        return  Constants.GROUP;
    }

    public static String CheckSpecialsCases(String s)
    {
        if(s.indexOf("Вакансия") != -1 && s.indexOf("....") == -1)
        {
            return s + " ....";
        }
        else return s;
    }


    public static boolean IsGroup (String s) {
        //Если перед тире стоит буква, то это точно не группа или группы нет
        return s.equals("----") || (s.indexOf('-') != -1) && ((s.equals("-")) || !Character.isLetter(s.charAt(s.indexOf('-') - 1))) && (s.indexOf('(') == -1);
    }

    public static boolean IsClassRoom (String s) {
        //Если перед тире стоит буква, то это аудитория
        return s.equals("---") || (!s.equals("-") && (s.indexOf('-') != -1) &&  (s.indexOf('-') - 1 != -1) && Character.isLetter(s.charAt(s.indexOf('-') - 1)));
    }

    public static boolean IsSubgroup (String s) {
        return s.equals(Constants.FIRST_SUB_BAD) || s.equals(Constants.SECOND_SUB_BAD) || s.equals(Constants.THIRD_SUB_BAD);
    }

    public  static  boolean IsWeek (String s){
        //Если предпоследни  символ не точка, то это не типа а точно недяля обычная или слитая
        return (s.charAt(0) == '(') && (s.charAt(s.length() - 2) != '.');
    }
}
