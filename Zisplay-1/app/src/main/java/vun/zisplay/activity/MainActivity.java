package vun.zisplay.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.appsee.Appsee;
import com.crashlytics.android.Crashlytics;
import com.flurry.android.FlurryAgent;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
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
import vun.zisplay.R;
import vun.zisplay.Zisplay;
import vun.zisplay.activity.SMSConfirmationActivity;
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.GA;
import vun.zisplay.managers.LocalData;
import vun.zisplay.managers.MixPanel;
import vun.zisplay.models.MobileVerification;
//import vun.zisplay.models.MobileVerificationRepository;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;


public class MainActivity extends ZisplayBaseActivity {
    OMGServices omgService= RestClient.getInstance().getApiService();
    public final static String EXTRA_MESSAGE = "vun.zisplay.mobileNo";
    LocalData localData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        analytics(this.getClass().getName());
        localData= LocalData.getInstance();
//        if(localData.getData("merchantId")!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, Dashboard.class);
//            startActivity(intent);
//        }
    }

            public void sendMessage(View v) {
                final EditText phoneText = (EditText) findViewById(R.id.editText);
                final EditText storeTitleText = (EditText) findViewById(R.id.storeTitleText);
                final String mobileNo=phoneText.getText().toString();
                final String storeTitle=storeTitleText.getText().toString();


                if(mobileNo.isEmpty()|| storeTitle.isEmpty()) {
//                    final TextView errorMessageText = (TextView) findViewById(R.id.errormessage);
//                    errorMessageText.setText("Please fill all fields");
                    return;
                }
                System.out.println(mobileNo);
                if(mobileNo.isEmpty() || storeTitle.isEmpty())
                {
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
                        Intent intent = new Intent(MainActivity.this, SMSConfirmationActivity.class);
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
}
