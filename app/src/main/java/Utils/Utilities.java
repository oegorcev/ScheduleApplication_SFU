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
}
