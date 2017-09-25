package vun.zisplaymerchant.utils;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

import vun.zisplaymerchant.models.Merchant;

/**
 * Created by ashok on 2/25/15.
 */
public class AppConstants {
//    public static Context appContext;
    public static Merchant merchantObj;
    public static String userId=null;
    public static String merchantId=null;
    public static String userAccessToken=null;
    public static String GOOGLE_PROJECT_ID="1009290474710";
    public static String GOOGLE_API_KEY="AIzaSyCfuh7IqdzxsFF3AbWtX-be4fG3lozaoI8";
    public static String TWITTER_KEY="ACi3GxHr0b7MylFZ3jNvxdihC";
    public static String TWITTER_SECRET="x3CArVBR83RmF4mkzAgc3HYsomQAvH8UxdyQ1utR8xGcT3RcF2";
    public static String FACEBOOK_ID="1409405566028830";

    public static String FLURRY_KEY="RKHVDF7HBH7T3YT9TWPW";
    public static final String APSEE_KEY = "08274ceacb234e4ead31c1ed48b3f33b";
    public final static String GA_PROPERTY_ID="UA-59573846-1";
    public static final String MIXPANEL_TOKEN = "d049a14dfd938b2ea39945a69e4709c2";
    public final static String CLOUDINARY_URL="http://res.cloudinary.com/zisplay/image/upload/";
    public final static String CLOUDINARY_FACEBOOK_URL="http://res.cloudinary.com/zisplay/image/facebook/";
    public final static String ADOBE_CLIENT_KEY="7ab09768cc314382a5604475b8fa3207";
    public final static String ADOBE_CLIENT_SECRET="4acfbc79-ef8b-418e-b6ce-bcbdc4440240";
    public final static String SERVER_BASE_URL ="http://api.zisplay.com:3000";//"http://192.168.1.102:3000";//
    public final static String API_BASE_URL =SERVER_BASE_URL+"/api";//"http://54.191.158.155:3000/api";192.168.1.101
    private static AppConstants ourInstance = new AppConstants();


    // Number of columns of Grid View
    public static final int NUM_OF_COLUMNS = 3;

    // Gridview image padding
    public static final int GRID_PADDING = 8; // in dp

    // SD card image directory
    public static final String PHOTO_ALBUM = "zisplaymerchant";

    // supported file formats
    public static final List<String> FILE_EXTN = Arrays.asList("jpg", "jpeg",
            "png");

    public static AppConstants getInstance() {
        return ourInstance;
    }

    private AppConstants() {
    }
}



