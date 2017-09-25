package vun.zisplay.managers;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import vun.zisplay.R;
import vun.zisplay.Zisplay;
import vun.zisplay.models.AppUser;
import vun.zisplay.utils.AppConstants;

/**
 * Created by ashok on 2/7/15.
 */
public class MixPanel {

    MixpanelAPI mixpanelApi ;

    static MixPanel mixPanel;
   private MixPanel()
    {
        mixpanelApi =  MixpanelAPI.getInstance(Zisplay.getInstance().getApplicationContext(), AppConstants.MIXPANEL_TOKEN);
    }

    public static MixPanel getInstance()
    {
        if (mixPanel==null)
        {
            mixPanel=new MixPanel();
        }
        return mixPanel;
    }



    public void setUser(AppUser appUser)
    {

        MixpanelAPI.People people=mixpanelApi.getPeople();
        people.identify(appUser.getId());
        people.initPushHandling(AppConstants.GOOGLE_PROJECT_ID);
        people.set("$phone",appUser.getMobileNo());
        people.set("createdOn", new Date());
        people.set("city", appUser.getCity());
        people.set("state",appUser.getState());
        people.set("country",appUser.getCountry());
        people.set("platform","android");

    }

    public void sendSocialAction(String network,String action, String target)
    {

    }
    public void sendScreenView(String screenName)
    {
        mixpanelApi.track(screenName,null);
        mixpanelApi.flush();
    }

    public void sendEvent(String eventName,JSONObject props) {
        mixpanelApi.track(eventName, props);
        mixpanelApi.flush();
    }

    public void sendException(Exception e,String screen)
    {

    }

    public void setProfileAttribute(String userId,String key, String value)
    {
        mixpanelApi.getPeople().identify(userId);
        mixpanelApi.getPeople().set(key, value);
    }
}
