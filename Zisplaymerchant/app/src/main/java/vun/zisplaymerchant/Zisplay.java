package vun.zisplaymerchant;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.aviary.android.feather.sdk.IAviaryClientCredentials;
import com.crashlytics.android.Crashlytics;
import com.digits.sdk.android.Digits;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.GoogleMap;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import com.parse.Parse;
import com.parse.ParseCrashReporting;                                                                               
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import vun.zisplaymerchant.managers.CategoryManager;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 2/7/15.
 */
public class Zisplay extends MultiDexApplication implements IAviaryClientCredentials {

    @Override
    public String getBillingKey() {
        return "";
    }

    @Override
    public String getClientID() {
        return AppConstants.ADOBE_CLIENT_KEY;
    }

    @Override
    public String getClientSecret() {
        return AppConstants.ADOBE_CLIENT_SECRET;
    }
    LocalData localData=null;
    Tracker primaryTracker;
    public static Zisplay zisplay;
    public static Zisplay getInstance()
    {
        return zisplay;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        TwitterAuthConfig authConfig =
                new TwitterAuthConfig(AppConstants.TWITTER_KEY, AppConstants.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());
        zisplay=this;
        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, AppConstants.FLURRY_KEY);
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "PiqaQmw2d5tHKrCfu9b7S4zqn7VkjLKQG8Eq8fKq", "2bB1fDrkT5RKHTJ3Xpo5yUCcp9YkAKUlLTshqvF3");
        localData = LocalData.getInstance();
        CategoryManager.getInstance().updateCategories();
//        String merchantId=localData.getData("merchantId");
//        if(merchantId==null)
//        {
//            localData.saveData("merchantId","54eade97dcc01cdb218149a3");
//        }


    }

}
