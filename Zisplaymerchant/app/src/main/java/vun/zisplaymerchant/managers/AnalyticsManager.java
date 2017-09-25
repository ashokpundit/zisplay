package vun.zisplaymerchant.managers;

import com.flurry.android.FlurryAgent;
import com.parse.ParseAnalytics;

import org.json.JSONObject;

import java.util.HashMap;

import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.utils.AppConstants;

/**
 * Created by ashok on 3/2/15.
 */
public class AnalyticsManager {
    private static AnalyticsManager ourInstance = new AnalyticsManager();

    public static AnalyticsManager getInstance() {
        return ourInstance;
    }

    private AnalyticsManager() {
    }

    public void sendScreenView(String screenName)
    {
        GA.getInstance().sendScreenView(screenName);
        MixPanel.getInstance().sendScreenView(screenName);
    }
    public void sendEvent(HashMap<String,String> event)
    {
        GA.getInstance().sendEvent(event.get("screenName"),event.get("category"),event.get("action"),event.get("label"));
        JSONObject props=new JSONObject(event);
        MixPanel.getInstance().sendEvent(event.get("label"),props);
        FlurryAgent.logEvent(event.get("label"),event);
        ParseAnalytics.trackEventInBackground(event.get("label"), event);
    }
    public void setUser(AppUser appUser)
    {
        GA.getInstance().setUserId(appUser.getId());
        FlurryAgent.setUserId(appUser.getId());
        MixPanel.getInstance().setUser(appUser);


    }

    public void raiseException(String location, Exception e)
    {
        GA.getInstance().sendException(e,location);
        MixPanel.getInstance().sendException(e,location);
        FlurryAgent.onError(location,"",e);
    }
}
