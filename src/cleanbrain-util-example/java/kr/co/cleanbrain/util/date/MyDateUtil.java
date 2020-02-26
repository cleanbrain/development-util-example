package kr.co.cleanbrain.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Company: NANDSOFT
 * User: 노상현
 * Date: 2020-02-26
 * Time: 오후 1:10
 */
public class MyDateUtil {

    /**
     * @see ' 기준일을 기준으로 영업일을 고려하여 원하는 날짜 범위를 구한다.
     * <p>역순인 경우는 기준일에 작업해야 할 날짜 범위를, 정순인 경우에는 기준일에서 영업일 이후 작업해야 할 날짜를 반환한다.</p>
     * <p>역순과 정순 모두 날짜 범위로 반환하지만 역순은 여러 날짜 범위를 가질 수 있고 정순은 날짜 한 개의 범위만을 반환한다.</p>
     * @param baseDate 기준일
     * @param businessDay 영업일
     * @param isAscend 날짜 정순, 역순
     * @param isIncludeBaseDay 기준일 포함 여부
     * @return String[0] ~ String[1] 날짜 범위
     */
    public static String[] getTargetDateRangeWithBusinessDay(Date baseDate, int businessDay, boolean isAscend, boolean isIncludeBaseDay) throws ParseException {
        int businessDaySumCount = 0;
        Date minDate = null;
        Date maxDate = null;
        boolean isFirstBusinessDay = true;
        int addDays = isAscend ? 1 : -1;
        int isCorrectionNum = isIncludeBaseDay ? 1 : isAscend ? 1 : -1;

        while (businessDaySumCount < businessDay + 1) {
            Date indexDate = addDays(baseDate, addDays);
            addDays += isAscend ? 1 : -1;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(indexDate);

            if (Calendar.SATURDAY == calendar.get(Calendar.DAY_OF_WEEK) ||
                    Calendar.SUNDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
                minDate = indexDate;
                continue;
            } else {
                businessDaySumCount++;

                if (businessDaySumCount == businessDay) {
                    if (isFirstBusinessDay) {
                        maxDate = indexDate;
                        isFirstBusinessDay = false;
                    }

                    minDate = indexDate;
                }
            }
        }

        if (isAscend) {
            return new String[] {format("yyyy-MM-dd", addDays(minDate, isCorrectionNum)),
                    format("yyyy-MM-dd", addDays(minDate, isCorrectionNum))};
        } else {
            return new String[] {format("yyyy-MM-dd", addDays(minDate, isCorrectionNum)),
                    format("yyyy-MM-dd", addDays(maxDate, isCorrectionNum))};
        }
    }

    /**
     * Date 객체 사이의 날짜 차이를 반환한다.
     * @param d1
     * @param d2
     * @return
     */
    public static int getDiffDay(Date d1, Date d2) {
        return (int) ((d1.getTime() - d2.getTime()) / (24 * 60 * 60 * 1000));
    }

    /**
     * @see ' Date 객체를 인자로 입력받아 요일을 반환한다.
     * @param date
     * @return
     * @throws ParseException
     */
    public static int getDayOfWeek(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int dayOfWeek = -1;
        switch(calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1 :
                dayOfWeek = Calendar.SUNDAY;
                break;
            case 2 :
                dayOfWeek = Calendar.MONDAY;
                break;
            case 3 :
                dayOfWeek = Calendar.TUESDAY;
                break;
            case 4 :
                dayOfWeek = Calendar.WEDNESDAY;
                break;
            case 5 :
                dayOfWeek = Calendar.THURSDAY;
                break;
            case 6 :
                dayOfWeek = Calendar.FRIDAY;
                break;
            case 7 :
                dayOfWeek = Calendar.SATURDAY;
                break;
        }

        return dayOfWeek;
    }

    public static Date addDays(Date date, int amount) {
        return addDate(date, 5, amount);
    }

    public static Date addDate(Date date, int calendarField, int amount) {
        return getCalendar(date, calendarField, amount).getTime();
    }

    private static Calendar getCalendar(Date time, int calendarField, int dayOffset) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        if (dayOffset != 0) {
            cal.add(calendarField, dayOffset);
        }

        return cal;
    }

    private static String format(String format, Date date) {
        return date != null ? (new SimpleDateFormat(format)).format(date) : "";
    }

    public static Date parse(String value) throws ParseException {
        if (isNumber(value)) {
            return new Date(Long.parseLong(value));
        } else {
            String[] formats = new String[]{"yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd HH:mm:ss.SS", "yyyy-MM-dd HH:mm:ss.S", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
            int len = value.length();
            String[] var3 = formats;
            int var4 = formats.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String format = var3[var5];
                if (len == format.length()) {
                    return parse(format, value);
                }
            }

            throw new ParseException("Unsupported format: " + value, -1);
        }
    }

    public static Date parse(String format, String value) throws ParseException {
        return isNotEmpty(value) ? (new SimpleDateFormat(format)).parse(value) : null;
    }

    private static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

    private static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

}
