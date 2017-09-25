package vun.zisplay.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import com.strongloop.android.loopback.RestAdapter;
//import com.strongloop.android.loopback.callbacks.VoidCallback;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import vun.zisplay.R;
import vun.zisplay.Zisplay;
import vun.zisplay.activity.LoginActivity;
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.GA;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.AppUser;
import vun.zisplay.models.Merchant;
import vun.zisplay.models.MobileVerification;
//import vun.zisplay.models.MobileVerificationRepository;
//import vun.zisplay.models.Settings;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;


public class SMSConfirmationActivity extends ZisplayBaseActivity {

    OMGServices omgService= RestClient.getInstance().getApiService();
    private BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics(this.getClass().getName());
        setContentView(R.layout.activity_smsconfirmation);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle pudsBundle = intent.getExtras();
                Object[] pdus = (Object[]) pudsBundle.get("pdus");
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
                if (messages.getMessageBody().startsWith("Verification Code:")) {
                    final EditText confirmationText = (EditText) findViewById(R.id.confirmationCode);
                    confirmationText.getText().clear();
                    confirmationText.getText().append(messages.getMessageBody());
                    abortBroadcast();
                }
            }
        };
        registerReceiver(receiver, filter);
    }


    public void verify(View v) {
                final EditText confirmationText = (EditText) findViewById(R.id.confirmationCode);
                final String confirmationCode=confirmationText.getText().toString();
                System.out.println(confirmationCode);
                final String mobileNo=this.getIntent().getStringExtra("mobileNo");//Settings.getInstance().getMobileNo();
                final String storeTitle=this.getIntent().getStringExtra("storeTitle");
                omgService.getMobileVerificationCode(mobileNo, confirmationCode, new Callback<MobileVerification>() {
                    @Override
                    public void success(MobileVerification mobileVerificationResponse, Response response) {
                        if (mobileVerificationResponse != null) {
                          MerchantRegisterCallback merchantCallback=new MerchantRegisterCallback();
                            omgService.registerMerchant(mobileNo, confirmationCode,storeTitle,merchantCallback);

                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("error " + error.getMessage());
                    }
                });

    }

    class SMSInterceptor  extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle pudsBundle = intent.getExtras();
            Object[] pdus = (Object[]) pudsBundle.get("pdus");
            SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
            if (messages.getMessageBody().startsWith("Verification Code:")) {

                abortBroadcast();
            }
        }
    }

    class MerchantRegisterCallback implements Callback<AppUser>
    {

        @Override
        public void success(AppUser user, Response response) {
            System.out.println("Merchant received");
            LocalData localDataObj=LocalData.getInstance();
            localDataObj.saveData("userId",user.getId());
            localDataObj.saveData("merchantId",user.getMerchantId());
            localDataObj.saveData("mobileNo",user.getMobileNo());
            localDataObj.saveData("storeTitle",user.getTitle());
            localDataObj.saveData("numberConfirmed",true);

            HashMap event=new HashMap<String,String>();
            event.put("screenName","SMS_Confirmation");
            event.put("label","Merchant_Registration");
            event.put("action","SMS Verification");
            event.put("Category","Signup");
            event.put("mobileNo",user.getMobileNo());
            event.put("userId",user.getId());
            AnalyticsManager.getInstance().sendEvent(event);
            AnalyticsManager.getInstance().setUser(user);

            Intent intent = new Intent(SMSConfirmationActivity.this, MerchantRegistrationActivity.class);
            startActivity(intent);

        }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(SMSConfirmationActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("Merchant error" +error.getStackTrace());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smsconfirmation, menu);
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
