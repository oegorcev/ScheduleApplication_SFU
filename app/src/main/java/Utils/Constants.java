package Utils;

import java.util.Calendar;

/**
 * Created by Mr.Nobody43 on 25.10.2017.
 */

public class Constants {

   static Calendar calendar = Calendar.getInstance();

   public static final String MONDAY = "Понедельник";
   public static final String TUESDAY = "Вторник";
   public static final String WEDNESDAY = "Среда";
   public static final String THURSDAY = "Четверг";
   public static final String FRIDAY = "Пятница";
   public static final String SATURDAY = "Суббота";
   public static final String SUNDAY = "Воскресенье";

   public static final String WEEK_SCHEDULE = "tblRaspis";
   public static final String EXAMS_SCHEDULE  = "tblRaspisZaoch";

   public static final String URL = "http://asu.tti.sfedu.ru/Raspisanie/ShowRaspisanie.aspx?Substance=";
   public static final String START_TIME = "start-time";
   public static final String END_TIME = "end-time";
   public static final String PARSE_TAG_DAYS = "tr";
   public static final String PARSE_TAG_ELEMENTS = "td";
   public static final String BOTH = "row_rowspan";
   public static final String TOP_WEEK = "row top-week";
   public static final String BOTTOM_WEEK = "row bottom-week";
   public static final String CLASS = "class";
   public static final String DAY = "th_row_day";
   public static final String RESERVED = "reserved";
   public static final String POTOK = "&isPotok=";
   public static final String SEMESTR = "&Semestr=";

   public static final String LECTION = "(лек.)";
   public static final String PRACTIC = "(прак.)";
   public static final String LAB = "(лаб.)";

   public static final String FIRST_SUB = "1пг";
   public static final String SECOND_SUB = "2пг";

   public static final String FREE = "free";
   public static final String ALL_WEEKS = "all";
   public static final String WITHOUT_SUBGROUB = "nothing";
   public static final String FREE_TIME = "Свободное время!";

   public static final int DAYS_ON_WEEK = 7;
   public static final int DATE_INDEX = 1;
   public static final int BEGIN_TIME = 1;
   public static final int DEFAULT_VALUE_CNT_PARSER = 0;
   public static final int FIRST_YEAR = 2017;
   public static final int FIRST_POTOK = 121;
   public static final int START_NEXT_SEMESTR = 7;

   public static final String SEPARATOR = " ";
   public static final String EMPTY_STRING = "";
   public static final char DOT = '.';

   public static String getCurPot()
   {
      int curYear = calendar.get(Calendar.YEAR);
      int curMonth = calendar.get(Calendar.MONTH);

      int pot = FIRST_POTOK + (curYear - FIRST_YEAR);

      if(curYear != FIRST_YEAR)
      {
         if(curMonth < START_NEXT_SEMESTR) --pot;
      }

      return  Integer.toString(pot);
   }
}
