package in.voiceme.app.voiceme.infrastructure;

import android.content.SharedPreferences;

/**
 * Created by harish on 1/2/2017.
 */

public class MySharedPreferences {

    private static final String AMAZON_USER_ID = "AMAZON_USER_ID";

    public static void wipe(SharedPreferences sharedPreferences) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, AMAZON_USER_ID, null);
    }

    protected static void storeValueInSharedPreferences(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected static String getValueFromSharedPreferences(SharedPreferences sharedPreferences, String key) {
        return sharedPreferences.getString(key, null);
    }

    public static void registerUserId(SharedPreferences sharedPreferences, String userId) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, AMAZON_USER_ID, userId);
    }

    public static void registerEmail(SharedPreferences sharedPreferences, String email) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.EMAIL, email);
    }

    public static void registerSocialID(SharedPreferences sharedPreferences, String socialID) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.SOCIAL_ID, socialID);
    }

    public static void registerImageUrl(SharedPreferences sharedPreferences, String imgurl) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.IMAGE_URL, imgurl);
    }

    public static void registerUsername(SharedPreferences sharedPreferences, String username) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.USERNAME, username);
    }

    public static String getUsername(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.USERNAME);
    }

    public static String getImageUrl(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.IMAGE_URL);
    }

    public static String getUserId(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, AMAZON_USER_ID);
    }

    public static String getEmail(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.EMAIL);
    }

    public static String getSocialID(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.SOCIAL_ID);
    }

    public static void checkContactSent(SharedPreferences sharedPreferences, String checked) {
        MySharedPreferences.storeValueInSharedPreferences(sharedPreferences, Constants.SENT_CONTACT, checked);
    }

    public static String getContactSent(SharedPreferences sharedPreferences) {
        return MySharedPreferences.getValueFromSharedPreferences(sharedPreferences, Constants.SENT_CONTACT);
    }


}
