package com.pucrs.andepucrs.api;

public class Constants {
    private static final String serverURL = "http://192.168.25.5:8080";
    private static final String myPreferenceFile = "MyPrefsFile";
    private static final String firstTime = "myFirstTime";
    private static final String userId = "userID";
    private static final String session = "session";
    private static final String userDataPreference = "userDataPreference";
    private static final String userDataPreferenceReDO = "userDataPreferenceReDO";
    private static final String markerLatitude = "markerLatitude";
    private static final String markerLongitude = "markerLongitude";
    private static final String userData = "userData";
    private static final String redirect = "redirectLogin";
    private static final String appName = "AndePUCRS";
    private static final String serachPoint = "serachPoint";
    private static final String allPoints = "allPoints";
    private static final String favorite = "favorite";
    private static final String establishments = "establishments";
    private static final String myCurrentLocation = "myCurrentLocation";
    private static final String user = "user";
    private static final String turnByTurn = "turnByTurn";
    private static final String favoriteDetails = "favoriteDetails";
    private static final int xMapSize = 810;
    private static final int yMapSize = 712;
    private static final String resultAStar = "resultAStar";
    private static final String map = "map";
    private static final String obstacleMap = "obstacleMap";
    private static final String destX = "destinoX";
    private static final String destY = "destinoY";
    public Constants() {
    }

    public static String getUserDataPreferenceReDO() {
        return userDataPreferenceReDO;
    }

    public static String getDestY() {
        return destY;
    }

    public static String getDestX() {

        return destX;
    }

    public static String getObstacleMap() {
        return obstacleMap;
    }

    public static String getMap() {
        return map;
    }

    public static String getResultAStar() {
        return resultAStar;
    }

    public static int getxMapSize() {
        return xMapSize;
    }

    public static int getyMapSize() {
        return yMapSize;
    }

    public static String getFavoriteDetails() {
        return favoriteDetails;
    }

    public static String getTurnByTurn() {
        return turnByTurn;
    }

    public static String getUser() {
        return user;
    }

    public static String getMyCurrentLocation() {
        return myCurrentLocation;
    }

    public static String getEstablishments() {
        return establishments;
    }

    public static String getSerachPoint() {
        return serachPoint;
    }

    public static String getFavorite() {
        return favorite;
    }

    public static String getAllPoints() {
        return allPoints;
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
