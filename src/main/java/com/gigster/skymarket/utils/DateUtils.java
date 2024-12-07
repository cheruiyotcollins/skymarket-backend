package com.gigster.skymarket.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    public static Date dateNow(){
        ZoneId zoneId = ZoneId.of("Africa/Nairobi");
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
        return new Date(System.currentTimeMillis());
    }
    public static String dateNowString(){
        ZoneId zoneId = ZoneId.of("Africa/Nairobi");
        TimeZone.setDefault(TimeZone.getTimeZone(zoneId));
        return ""+ new Date(System.currentTimeMillis());
    }

    private static String[] dayTodayInArray(){
        LocalDateTime localDateTime = dateToday();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return dateTimeFormatter.format(localDateTime).split("-");
    }
    private static LocalDateTime dateToday(){
        return  LocalDateTime.now();
    }

    public static int day(){
        return Integer.parseInt(dayTodayInArray()[0]);
    }

    public static int month(){
        return Integer.parseInt(dayTodayInArray()[1]);
    }

    public static int year(){
        return Integer.parseInt(dayTodayInArray()[2]);
    }

    public static boolean dayBeforeToday(String dateGiven){
        //date to be in the format of yyyy-MM--dd
        String[] splitDate = dateGiven.split("-");
        LocalDate givenDate = LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

        // Get today's date
        LocalDate today = LocalDate.now();

        // Check if the given date is after today's date
        return givenDate.isBefore(today);
    }

    public static boolean dayAfterToday(String dateGiven) {
        String[] splitDate = dateGiven.split("-");
        LocalDate givenDate = LocalDate.of(Integer.parseInt(splitDate[0]), Integer.parseInt(splitDate[1]), Integer.parseInt(splitDate[2]));

        // Get today's date
        LocalDate today = LocalDate.now();

        // Check if the given date is after today's date
        return givenDate.isAfter(today);
    }

}