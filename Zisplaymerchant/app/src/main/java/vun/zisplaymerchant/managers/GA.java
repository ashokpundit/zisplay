package vun.zisplaymerchant.managers;

import android.app.Application;
import android.content.Context;


import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import vun.zisplaymerchant.R;
import vun.zisplaymerchant.Zisplay;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 2/7/15.
 */
public class GA {
    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        ECOMMERCE_TRACKER
    }
    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    Tracker appTracker = getTracker(TrackerName.APP_TRACKER);
    static GA googleAnalytics;
   private GA()
    {
        Tracker appTracker = getTracker(TrackerName.APP_TRACKER);


    }

    public static GA getInstance()
    {
        if (googleAnalytics==null)
        {
            googleAnalytics=new GA();
        }
        return googleAnalytics;
    }

    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            Context context= Zisplay.getInstance().getApplicationContext();
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(AppConstants.GA_PROPERTY_ID)
//                    : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.ecommerce_tracker)
                    : analytics.newTracker(R.xml.global_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }

    public void setUserId(String userId)
    {
        appTracker.set("&uid", userId);
    }
    public void sendSocialAction(String network,String action, String target)
    {
        appTracker.send(new HitBuilders.SocialBuilder()
                .setNetwork(network)
                .setAction(action)
                .setTarget(target)
                .build());
    }
    public void sendScreenView(String screenName)
    {
        appTracker.setScreenName(screenName);
        appTracker.send(new HitBuilders.AppViewBuilder().build());
    }

    public void sendEvent(String screenName,String category,String action,String label) {
        if (!screenName.isEmpty()) {
            appTracker.setScreenName(screenName);
        }
        appTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());
    }

    public void sendException(Exception e,String screen)
    {
        appTracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(screen+" : "+e.getMessage() + ":" + e.getStackTrace())
                .build());
    }
}
