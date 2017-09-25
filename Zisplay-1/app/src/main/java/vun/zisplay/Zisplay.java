package vun.zisplay;


import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import io.fabric.sdk.android.Fabric;
import vun.zisplay.managers.LocalData;
/**
 * Created by ashok on 2/7/15.
 */
public class Zisplay extends android.support.multidex.MultiDexApplication {




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
        Fabric.with(this, new Crashlytics());
        zisplay=this;
//        FlurryAgent.setLogEnabled(false);
//        FlurryAgent.init(this, AppConstants.FLURRY_KEY);
        localData = LocalData.getInstance();
        String merchantId=localData.getData("merchantId");
        if(merchantId==null)
        {
            localData.saveData("merchantId","54eade97dcc01cdb218149a3");
        }


    }




}
