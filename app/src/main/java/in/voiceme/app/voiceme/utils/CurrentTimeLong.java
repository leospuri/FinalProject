package in.voiceme.app.voiceme.utils;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

/**
 * Created by harishpc on 5/7/2017.
 */
public class CurrentTimeLong {

    public static String getCurrentTime(String currentTimeInSeconds, Context context) {
        String currentTimeForPost;
        DateTime now = DateTime.now();
        int currentTime = (int) (System.currentTimeMillis() - Long.parseLong(currentTimeInSeconds));

        if (currentTime < 60000){
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusSeconds(currentTime));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 3600000){
            int currentTimeInMinutes = currentTime/60000;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusMinutes(currentTimeInMinutes));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 86400000){
            int currentTimeInHours = currentTime/3600000;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusHours(currentTimeInHours));
            currentTimeForPost = relativeTime;
        } else if (currentTime < 604800000){
            int currentTimeInDays = currentTime/86400000;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusDays(currentTimeInDays));
            currentTimeForPost = relativeTime;
        } else{
            int currentTimeInMonths = currentTime/604800000;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusMonths(currentTimeInMonths));
            currentTimeForPost = relativeTime;
        }

        return currentTimeForPost;
    }
}
