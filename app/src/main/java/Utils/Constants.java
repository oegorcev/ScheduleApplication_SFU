package Utils;

import java.util.Calendar;
import java.util.Stack;

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
   public static final String WEEK_URL = "http://asu.tti.sfedu.ru/Raspisanie/Default.aspx?isPotok=";
   public static final String WEEK_INFO = "ctl00_Main_lbCurrentWeek";
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
   public static final String CONSULTATION = "(конс.)";
   public static final String CREDIT = "(зач.)";
   public static final String UST_LECTION = "(уст.";
   public static final String KURS_WORK = "(курс.";
   public static final String EXAM = "(экз.)";

   public static final String FIRST_SUB_BAD = "1??.";
   public static final String SECOND_SUB_BAD = "2??.";
   public static final String THIRD_SUB_BAD = "3??.";
   public static final String FIRST_SUB = "1пг";
   public static final String SECOND_SUB = "2пг";
   public static final String THIRD_SUB = "3пг";

   public static final String FREE = "free";
   public static final String ALL_WEEKS = "all";
   public static final String WITHOUT_SUBGROUB = "nothing";

   public static final String FREE_TIME = "Свободное время!";
   public static final String EMPTY_SCHEDULE = "Расписание отсутствует.";
   public static final String EMPTY_EXAMS = "До экзаменов ещё далеко!";
   public static final String BOTTOM_WEEK_SCHEDULE = "Показывается нижняя неделя.";
   public static final String TOP_WEEK_SCHEDULE = "Показывается верхняя неделя.";
   public static final String WEEK = " неделя";
   public static final String EXAMS = "Экзамены";

   public static final int DAYS_ON_WEEK = 7;
   public static final int DATE_INDEX = 1;
   public static final int BEGIN_TIME = 1;
   public static final int DEFAULT_VALUE_CNT_PARSER = 0;
   public static final Integer FIRST_POTOK = 121;
   public static final int MAX_PAIR_COUNT = 6;

   public static final int GROUP = 0;
   public static final int TEACHER = 1;
   public static final int CLASSROOM = 2;

   public static final String BAD_CASE_IN = "Вакансия%20ВМК%20....";
   public static final String BAD_CASE_OUT = "Вакансия%20%20ВМК%20....";

   public static final String SEMESTR_DB_ID = "semestr";
   public static final String YEARS_DB_ID = "year";
   public static final String WEEK_DB_ID = "week";
   public static final String CUR_QUERY_DB_ID = "query";
   public static final String HIDE_WEEKS_DB_ID = "hideweeks";

   public static final String SEPARATOR = " ";
   public static final String SLASH_SEPARATOP = " / ";
   public static final String EMPTY_STRING = "";
   public static final char DOT = '.';
   public static final char SPACE = ' ';

   private static Stack<String> stack;
   public static Integer lastPage;

   public static void addQuery(String string) {
      if(stack == null) {
         stack = new Stack<String>();
      }

      stack.add(string);
   }

   public static String getLastQuery() {
      if(stack == null || stack.empty()){return EMPTY_STRING;}

      String top = stack.lastElement();
      stack.pop();

      return  top;
   }
}
