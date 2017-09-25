package vun.zisplaymerchant.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;

import org.apache.commons.lang3.text.WordUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.managers.MixPanel;
import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.models.Merchant;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

public class Dashboard2 extends ZisplayBaseActivity {
    OMGServices omgService= RestClient.getInstance().getApiService();
    LocalData localData= LocalData.getInstance();
    Merchant merchant;
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = AppConstants.GOOGLE_PROJECT_ID;
    static final String TAG = "GCM Demo";
    GoogleCloudMessaging gcm;
    AtomicInteger msgId = new AtomicInteger();
    Context context;
    String regid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard2);
        analytics(this.getClass().getName());
        TextView heading=(TextView) findViewById(R.id.heading);
        String title=localData.getData("storeTitle");
        if(title.length()==0)
            title="Eclectiques Boutique";
        heading.setText(title);
        merchant=AppConstants.merchantObj;
        updateScreen(merchant);
        loadMerchant();


    }
    private void updateScreen(Merchant merchant)
    {
        if(merchant!=null)
        {

            TextView merchantUrl = (TextView) findViewById(R.id.merchantUrl);
            merchantUrl.setText("zisplay.com/m-"+merchant.getName());
            TextView heading = (TextView) findViewById(R.id.heading);

            if(merchant.getTitle()!=null && !merchant.getTitle().isEmpty())
            heading.setText(WordUtils.capitalizeFully(merchant.getTitle()));

            TextView catalogText = (TextView) findViewById(R.id.catalogText);
            catalogText.setText(merchant.getNoOfProducts()+" Products");
            TextView noOfProductText = (TextView) findViewById(R.id.noOfProductText);
            noOfProductText.setText(merchant.getProductViews()+" views "+merchant.getProductLikes()+" likes");

            TextView Unreadcount = (TextView) findViewById(R.id.Unreadcount);
            Unreadcount.setText(merchant.getUnreadMessage()+" Unread");
            TextView followersCount = (TextView) findViewById(R.id.followersText);
            followersCount.setText(merchant.getNoOfFollowers()+" Followers");
            TextView paymentPending = (TextView) findViewById(R.id.paymentPending);
            paymentPending.setText(merchant.getPendingPayments()+" pending");

        }
    }

    private void loadMerchant()
    {
        omgService.getMerchant(AppConstants.userAccessToken,AppConstants.merchantId, new Callback<Merchant>() {
            @Override
            public void success(Merchant merchantObj, Response response) {
                if (merchantObj != null) {
                    Gson gson = new Gson();
                    String jsonRepresentation = gson.toJson(merchantObj);
                    localData.saveData("merchantObj",jsonRepresentation);
                    updateScreen(merchantObj);
                    AppConstants.merchantObj=merchantObj;
                    AppConstants.merchantId=merchantObj.getId();

                    if (checkPlayServices()) {
                        gcm = GoogleCloudMessaging.getInstance(Dashboard2.this);
                        regid = getRegistrationId(context);

                        if (regid.isEmpty()) {
                            registerInBackground();
                        }
                    } else {
                        Log.i(TAG, "No valid Google Play Services APK found.");
                    }

                }
            }
            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
            }
        });
    }

    public void uploadProduct(View v)
    {
        Intent intent = new Intent(Dashboard2.this, NewProductActivity.class);
        startActivity(intent);
    }

    public void showSocialSettings(View V)
    {
        Intent intent = new Intent(Dashboard2.this, SocialChannels.class);
        startActivity(intent);
    }
    public void showSettings(View v)
    {
        Intent intent = new Intent(Dashboard2.this, SettingsActivity.class);

        startActivity(intent);

    }

    public void showCatalog(View v)
    {
        Intent intent = new Intent(Dashboard2.this, Catalog.class);
        startActivity(intent);
    }

    public void showChat(View v)
    {
        Intent intent = new Intent(Dashboard2.this, ConversastionList.class);
        startActivity(intent);
    }
    public void showProfile(View v)
    {
        Intent intent = new Intent(Dashboard2.this, Profile.class);
        startActivity(intent);
    }
    public void showPayments(View v)
    {
        Intent intent = new Intent(Dashboard2.this, PaymentList.class);
        startActivity(intent);
    }
    public void showFollowers(View v)
    {
        Intent intent = new Intent(Dashboard2.this, FollowersList.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_dashboard2, menu);
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




    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {

        localData.saveData("GCMDeviceId",regId);

    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        return localData.getData("GCMDeviceId");

        }


    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        regid=localData.getData("prereg-gcmid");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    if(regid==null || regid.isEmpty()) {
                        regid = gcm.register(SENDER_ID);
                        localData.saveData("prereg-gcmid",regid);
                    }
                    msg = "Device registered, registration ID=" + regid;
                    sendRegistrationIdToBackend();
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            }
        }.execute(null, null, null);
    }
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        return getSharedPreferences("zisplaymerchantgcm",
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {

        HashMap merchantMap=new HashMap<String,String>();

        merchantMap.put("gcmId",regid);

        omgService.updateAppUser(AppConstants.userAccessToken, AppConstants.userId, merchantMap, new Callback<AppUser>() {
            @Override
            public void success(AppUser merchantObj, Response response) {
                if (merchantObj != null) {
                    storeRegistrationId(context, regid);

                }
            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
            }
        });

    }
}
