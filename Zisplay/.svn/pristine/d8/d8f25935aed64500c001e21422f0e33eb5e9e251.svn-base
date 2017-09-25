package vun.zisplay;

import android.app.Application;
import android.support.multidex.MultiDexApplication;


import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.mixpanel.android.mpmetrics.MixpanelAPI;


import java.util.HashMap;

import vun.zisplay.managers.LocalData;
import vun.zisplay.utils.AppConstants;

/**
 * Created by ashok on 2/7/15.
 */
public class Zisplay extends MultiDexApplication {


    LocalData localData=null;


    Tracker primaryTracker;
    private static Zisplay zisplay;
    public static Zisplay getInstance()
    {
        return zisplay;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        zisplay=this;

        FlurryAgent.setLogEnabled(false);
        FlurryAgent.init(this, AppConstants.FLURRY_KEY);
        localData = LocalData.getInstance();



    }




}
