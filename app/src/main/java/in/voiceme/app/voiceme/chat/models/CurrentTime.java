package in.voiceme.app.voiceme.chat.models;

import android.content.Context;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;

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
            int currentTimeInMonths = currentTime/604800;
            String relativeTime = (String) DateUtils.getRelativeTimeSpanString(context, now.minusMonths(currentTimeInMonths));
            currentTimeForPost = relativeTime;
        }

        return currentTimeForPost;
    }
}
