package vun.zisplaymerchant.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.facebook.AppEventsLogger;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.parse.ParseAnalytics;
//import com.strongloop.android.loopback.AccessToken;
//import com.strongloop.android.loopback.RestAdapter;
//import com.strongloop.android.loopback.UserRepository;
//import com.strongloop.android.loopback.callbacks.VoidCallback;

import org.json.JSONException;
import org.json.JSONObject;

import io.fabric.sdk.android.Fabric;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.Zisplay;

import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.managers.GA;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.managers.MixPanel;
import vun.zisplaymerchant.models.Merchant;
import vun.zisplaymerchant.models.MobileVerification;
//import vun.zisplaymerchant.models.MobileVerificationRepository;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;


public class MainActivity extends ZisplayBaseActivity {
    OMGServices omgService= RestClient.getInstance().getApiService();
    public final static String EXTRA_MESSAGE = "vun.zisplaymerchant.mobileNo";
    LocalData localData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        analytics(this.getClass().getName());
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        localData= LocalData.getInstance();
        if(localData.getData("userAccessToken")!=null && !localData.getData("userAccessToken").isEmpty()) {


          /*  localData.saveData("merchantId", "54f441df78fa5d1c13f379cd");
            localData.saveData("userId", "54f441df78fa5d1c13f379ce");
            localData.saveData("userAccessToken", "iJ5CaODCkcuAJ4GXvk5SJJulfTka9LWKBaDG4WBnu1JC88hzTDQg88AE6LEhbDL3");*/
            if (localData.getData("merchantId") != null) {
                AppConstants.merchantId = localData.getData("merchantId");
                AppConstants.userId = localData.getData("userId");
                AppConstants.userAccessToken = localData.getData("userAccessToken");


                String merchantData = localData.getData("merchantObj");
                if (merchantData != null && !merchantData.isEmpty()) {
                    Gson gson = new Gson();
                    Merchant merchantObj = gson.fromJson(merchantData, Merchant.class);
                    AppConstants.merchantObj = merchantObj;
                }
                Intent intent = new Intent(MainActivity.this, Dashboard2.class);
                startActivity(intent);
            }
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, Registration2.class);
            startActivity(intent);
        }
    }

    public void nextStep(View v)
    {
        
    }
            public void sendMessage(View v) {
                final EditText phoneText = (EditText) findViewById(R.id.editText);
                final EditText storeTitleText = (EditText) findViewById(R.id.storeTitleText);
                final String mobileNo=phoneText.getText().toString();
                final String storeTitle=storeTitleText.getText().toString();
                System.out.println(mobileNo);
                if(mobileNo.isEmpty() || storeTitle.isEmpty())
                {
                    Toast.makeText(MainActivity.this,"Mobile no and store title can't be blank.",Toast.LENGTH_LONG);
                    return;
                }
                JSONObject jsonPara=new JSONObject();
                try {
                    jsonPara.put("mobileNo", mobileNo);
                    jsonPara.put("storeTitle", storeTitle);
                }catch(Exception e){
                    AnalyticsManager.getInstance().raiseException("MainActivity", e);

                }

                MixPanel.getInstance().sendEvent("signupWithMobile",jsonPara);
               omgService.postMobileVerificationCode(mobileNo,new Callback<MobileVerification>() {
                    @Override
                    public void success(MobileVerification mobileVerificationResponse, Response response) {
                        System.out.println("success");
                        Intent intent = new Intent(MainActivity.this, Registration2.class);
                        intent.putExtra("mobileNo", mobileNo);
                        intent.putExtra("storeTitle", storeTitle);
                        startActivity(intent);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("error "+error.getMessage());
                    }
                });

            }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
