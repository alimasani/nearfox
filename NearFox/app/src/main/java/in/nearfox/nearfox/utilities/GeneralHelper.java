package in.nearfox.nearfox.utilities;

import android.util.Log;
import android.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GeneralHelper {

    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateFormat dateFormat2 = new SimpleDateFormat("yyyy-mm-dd");
    static String TAG = "GeneralHelper";

    public static String getBeautifulDate(String dateTime) {

        Date currentDateTime = getCurrentDateTime();
        Date givenDateTime = null;
        try {
            givenDateTime = dateFormat.parse(dateTime);
        } catch (ParseException e) {
            Log.d(TAG, e.toString());
        }

        DifferenceInDateTime differenceInDateTime = getDifferenceInDateTime(currentDateTime, givenDateTime);

        return getBeautifulDateFromDifference(differenceInDateTime);
    }


    public static Pair getRealTime(String date, String start_time){

        String shortDate = getShortDate(date);

        if (shortDate.equals("today"))
            return new Pair<String, String>("today", getRealTime(start_time));

        else
            return new Pair<String, String>(shortDate, start_time);
    }


    public static String getRealTime(String start_time){
        Date currentTime = getCurrentDateTime();

        Calendar c = Calendar.getInstance();
        c.setTime(currentTime);

        int currentHour = c.get(Calendar.HOUR);
        int currentMinute = c.get(Calendar.MINUTE);

        String[] splittedStart_time = start_time.split(" ")[0].split(":");
        int startHour = Integer.parseInt(splittedStart_time[0]);
        int startMinute = Integer.parseInt(splittedStart_time[1]);

        if (startHour >= currentHour){

            if (startHour - currentHour >= 2){
                return start_time;
            }else{

                return "Ongoing";
            }
        }else{
            return "Ongoing";
        }
    }

    public static String getShortDate(String date){

        Date givenDate = null;
        try {
            givenDate = dateFormat2.parse(date);
        } catch (ParseException e) {
            Log.d(TAG, e.toString());
        }

        Date currentDateTime = getCurrentDateTime();
        DifferenceInDateTime differenceInDateTime = getDifferenceInDateTime(givenDate, currentDateTime);

        if (differenceInDateTime.day == 0)
            return "today";

        else {

            Calendar c = Calendar.getInstance();
            c.setTime(givenDate);

            int dayOfWeek = (c.get(Calendar.DAY_OF_WEEK_IN_MONTH)) % 7 + 1;
            int monthOfYear = c.get(Calendar.MONTH);
            Log.d(TAG, "month: " + monthOfYear);
//            int year = c.get(Calendar.YEAR);
            int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);


            String day = intToDay(dayOfWeek);
            String month = shortMonth(Integer.parseInt(date.split("-")[1]));
            String dateOfMonth = beautifiedDate(dayOfMonth);

            return day + ", " + dateOfMonth + " " + month;
        }
    }

    public static String getBeautifulDateFromDifference(DifferenceInDateTime differenceInDateTime) {

        if (differenceInDateTime.week >= 1) {
            if (differenceInDateTime.week == 1) {
                return "1 week ago";
            } else {
                return String.valueOf(differenceInDateTime.week) + " weeks ago";
            }
        } else if (differenceInDateTime.day >= 1) {
            if (differenceInDateTime.day == 1) {
                return "1 day ago";
            } else {
                return String.valueOf(differenceInDateTime.day) + " days ago";
            }
        } else if (differenceInDateTime.hour >= 1) {
            if (differenceInDateTime.hour == 1) {
                return "1 hour ago";
            } else {
                return String.valueOf(differenceInDateTime.hour) + " hours ago";
            }
        } else if (differenceInDateTime.min >= 1) {
            if (differenceInDateTime.min == 1) {
                return "1 minute ago";
            } else {
                return String.valueOf(differenceInDateTime.min) + " minutes ago";
            }
        }

        return "just now";
    }

    public static Date getCurrentDateTime() {
        return new Date();
    }

    public static DifferenceInDateTime getDifferenceInDateTime(Date currentDateTime, Date givenDateTime) {

        long currentDateInInSeconds = currentDateTime.getTime();
        long givenDateTimeInSeconds = givenDateTime.getTime();

        long difference = currentDateInInSeconds - givenDateTimeInSeconds;

        long diffInSecs = (difference / 1000) % 60;
        long diffInMinutes = (difference / (60 * 1000)) % 60;
        long diffeInHours = (difference / (60 * 60 * 1000)) % 24;
        long diffInDays = (difference / (24 * 60 * 60 * 1000)) % 7;
        long diffInWeeks = (difference / (7 * 24 * 60 * 60 * 1000));

        return new DifferenceInDateTime(diffInSecs, diffInMinutes, diffeInHours, diffInDays, diffInWeeks);
    }

    static class DifferenceInDateTime {
        int sec;
        int min;
        int hour;
        int day;
        int week;

        public DifferenceInDateTime(long sec, long min, long hour, long day, long week) {
            this.sec = (int) sec;
            this.min = (int) min;
            this.hour = (int) hour;
            this.day = (int) day;
            this.week = (int) week;
        }
    }

    private static String shortMonth(int month) {

        switch (month) {
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return String.valueOf(month);
        }
    }

    public static String wordMonthToNumber(String month) {
        month = month.toLowerCase();
        switch (month) {
            case "jan": {
                return "0";
            }
            case "feb": {
                return "1";
            }
            case "mar": {
                return "2";
            }
            case "apr": {
                return "3";
            }
            case "may": {
                return "4";
            }
            case "jun": {
                return "5";
            }
            case "jul": {
                return "6";
            }
            case "aug": {
                return "7";
            }
            case "sep": {
                return "8";
            }
            case "oct": {
                return "9";
            }
            case "nov": {
                return "10";
            }
            case "dec": {
                return "12";
            }
            default: {
                return null;
            }
        }
    }

    public static String intToDay(int day){

        switch(day){
            case 1: return "Sun";
            case 2: return "Mon";
            case 3: return "Tue";
            case 4: return "Wed";
            case 5: return "Thu";
            case 6: return "Fri";
            case 7: return "Sat";
            default: return "Invalid Day";
        }
    }

    public static String beautifiedDate(int date){

        int remainder = date % 10;

        if (remainder == 1)
            return String.valueOf(date) + "st";

        else if (remainder == 2)
            return String.valueOf(date) + "nd";

        else if (remainder == 3)
            return String.valueOf(date) + "rd";

        else
            return String.valueOf(date) + "th";
    }
}
