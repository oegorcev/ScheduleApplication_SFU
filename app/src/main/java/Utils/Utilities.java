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

    public static boolean CheckType(String s)
    {
        return s.equals(Constants.LECTION) || s.equals(Constants.PRACTIC) || s.equals(Constants.LAB);
    }

    public static boolean IsGroup (String s)
    {
        //Если перед тире стоит буква, то это точно не группа или группы нет
        return (s.indexOf('-') != -1) && ((s.equals('-')) || !Character.isLetter(s.charAt(s.indexOf('-') - 1)));
    }

    public static boolean IsSubgroup (String s)
    {
        return s.equals(Constants.FIRST_SUB_BAD) || s.equals(Constants.SECOND_SUB_BAD) || s.equals(Constants.THIRD_SUB_BAD);
    }
}
