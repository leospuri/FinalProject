package in.voiceme.app.voiceme.utils;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;

/**
 * Created by harish on 2/4/2017.
 */

public class CurrentTime {

    public static String getCurrentTime(String currentTimeInSeconds, Context context) {
        String currentTimeForPost;
        DateTime now = DateTime.now();
        int currentTime = (int) (System.currentTimeMillis()/1000 - Integer.parseInt(currentTimeInSeconds));

        if (currentTime < 60){
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusSeconds(currentTime));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 3600){
            int currentTimeInMinutes = currentTime/60;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusMinutes(currentTimeInMinutes));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 86400){
            int currentTimeInHours = currentTime/3600;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusHours(currentTimeInHours));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 604800){
            int currentTimeInDays = currentTime/86400;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusDays(currentTimeInDays));
            currentTimeForPost = relativeTime;
        } else{
            long itemLong = Long.parseLong(currentTimeInSeconds);
            java.util.Date d = new java.util.Date(itemLong*1000L);
            String itemDateStr = new SimpleDateFormat("dd-MMM HH:mm").format(d);
       //     holder.time.setText(itemDateStr);

       //     int currentTimeInMonths = currentTime/604800;
       //     String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusMonths(currentTimeInMonths));
            currentTimeForPost = itemDateStr;
        }

        return currentTimeForPost;
    }
}
