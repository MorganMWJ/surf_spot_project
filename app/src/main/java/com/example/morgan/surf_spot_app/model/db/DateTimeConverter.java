package com.example.morgan.surf_spot_app.model.db;

import android.provider.SyncStateContract;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import androidx.room.TypeConverter;

public class DateTimeConverter {
    private static final String DATE_ERROR_TAG = "DATE FORMAT ERROR";

    @TypeConverter
    public static Date toDate(Long timestamp) {
        Date result = timestamp == null ? null : new Date(timestamp);
        return result;
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        Long result = date == null ? null : date.getTime();
        return result;
    }


    public static Date fromStringToDate(String stringDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = format.parse(stringDate);
            return date;
        } catch (ParseException e) {
            Log.w(DATE_ERROR_TAG, e.toString());
        }
        return null;
    }

    public static String currentDateAsString(){

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        return df.format(new Date());
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public static String previousDateAsString(int numDays) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -numDays); // Subtract numDays from current date
        return df.format(cal.getTime());
    }
}

