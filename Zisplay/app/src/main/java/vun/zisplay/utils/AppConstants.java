package vun.zisplay.utils;

/**
 * Created by ashok on 2/25/15.
 */
public class AppConstants {
    public  static String userAccessToken=null;
    public  static String userId=null;
    public static String GOOGLE_PROJECT_ID="1009290474710";
    public static String FLURRY_KEY="FYZSDT96JZFBWTGWXRZR";
    public static final String APSEE_KEY = "08274ceacb234e4ead31c1ed48b3f33b";
    public final static String GA_PROPERTY_ID="UA-59573846-1";
    public static final String MIXPANEL_TOKEN = "ff000273225df724ba51c5d4a89144a3";
    public final static String CLOUDINARY_URL="http://res.cloudinary.com/zisplay/image/upload/";
    public final static String CLOUDINARY_URL_FACEBOOK="http://res.cloudinary.com/zisplay/image/facebook/";
    public final static String ADOBE_CLIENT_KEY="7ab09768cc314382a5604475b8fa3207";
    public final static String ADOBE_CLIENT_SECRET="4acfbc79-ef8b-418e-b6ce-bcbdc4440240";
        public final static String SERVER_BASE_URL="http://api.zisplay.com:3000";//"http://54.191.158.155:3000/api";//192.168.1.3
    public final static String API_BASE_URL=SERVER_BASE_URL+"/api";
    private static AppConstants ourInstance = new AppConstants();

    public static AppConstants getInstance() {
        return ourInstance;
    }

    private AppConstants() {
    }
}
