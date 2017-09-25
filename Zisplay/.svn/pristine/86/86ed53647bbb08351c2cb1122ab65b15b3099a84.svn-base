package vun.zisplay.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.flurry.android.FlurryAgent;

import vun.zisplay.R;
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.GA;
import vun.zisplay.managers.MixPanel;
import vun.zisplay.utils.AppConstants;

public abstract class ZisplayBaseActivity extends Activity {


     void analytics(String screenName) {
         FlurryAgent.onStartSession(this, AppConstants.FLURRY_KEY);
         AnalyticsManager.getInstance().sendScreenView(screenName);
    }



    @Override
    protected void onStop()
    {
        super.onStop();
        FlurryAgent.onEndSession(this);
    }
}
