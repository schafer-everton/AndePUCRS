package com.pucrs.andepucrs.api;

/**
 * Created by ScHaFeR on 25/08/2015.
 */
public class Constants {
    private static final String serverURL = "http://192.168.0.104:8080";
    private static final String myPreferenceFile = "MyPrefsFile";
    private static final String firstTime = "myFirstTime";
    private static final String userId = "userID";
    private static final String session = "session";
    private static final String userDataPreference = "userDataPreference";
    private static final String markerLatitude = "markerLatitude";
    private static final String markerLongitude = "markerLongitude";
    private static final String userData = "userData";
    private static final String redirect = "redirectLogin";
    private static final String appName = "AndePUCRS";
    private static final String serachLatitude = "serachLatitude";
    private static final String serachLongitude = "serachLongitude";

    public Constants() {
    }

    public static String getSerachLongitude() {
        return serachLongitude;
    }

    public static String getSerachLatitude() {

        return serachLatitude;
    }

    public static String getRedirect() {
        return redirect;
    }

    public static String getAppName() {
        return appName;
    }

    public static String getUserData() {
        return userData;
    }

    public static String getSession() {
        return session;
    }

    public static String getUserDataPreference() {
        return userDataPreference;
    }

    public static String getServerURL() {
        return serverURL;
    }

    public static String getMyPreferenceFile() {
        return myPreferenceFile;
    }

    public static String getFirstTime() {
        return firstTime;
    }

    public static String getUserId() {
        return userId;
    }

    public static String getMarkerLatitude() {
        return markerLatitude;
    }

    public static String getMarkerLongitude() {
        return markerLongitude;
    }
}
