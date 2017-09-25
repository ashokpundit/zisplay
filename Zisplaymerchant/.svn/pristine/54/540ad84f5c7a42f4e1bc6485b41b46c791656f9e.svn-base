package vun.zisplaymerchant.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Field;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.managers.MixPanel;
import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.models.Category;
import vun.zisplaymerchant.models.MobileVerification;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

public class Registration2 extends ZisplayBaseActivity implements AdapterView.OnItemClickListener{
    private LayoutInflater mInflater = null;
    private BroadcastReceiver receiver;
    EditText handle;
    boolean handleAvailabiity=false;
    boolean existingHandle=false;
    OMGServices omgService= RestClient.getInstance().getApiService();
    String mobileNo;
    String verificationCode;
    EditText smsCodeText;

    Geocoder geoCoder;
    AlertDialog alert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
        analytics(this.getLocalClassName());
        handle = (EditText) findViewById(R.id.handle);
        handle.setOnFocusChangeListener(new handleChangeListner());
        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.address);
        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this.getBaseContext(),R.layout.address_auto_suggest_layout));

        autoCompView.setOnItemClickListener(this);
        mInflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setSMSReciver();


    }

    public void setSMSReciver()
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle pudsBundle = intent.getExtras();
                Object[] pdus = (Object[]) pudsBundle.get("pdus");
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
                String messageCode=messages.getMessageBody();
                if (messageCode.startsWith("zisplay verification code :")) {
                    messageCode= messageCode.replace("zisplay verification code : ","");
//                    final EditText confirmationText = (EditText) findViewById(R.id.confirmationCode);
                    smsCodeText.getText().clear();
                    smsCodeText.getText().append(messageCode);

                    verificationCode=messageCode;
                    abortBroadcast();
                    verify();
                }
            }
        };
        registerReceiver(receiver, filter);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    public void showConfirmDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.confirm_mobileno_dialog, null);

            alert = new AlertDialog.Builder(this).create();

//            alert.setTitle("Confirm");
//            alert.setMessage("Enter verification code");
            smsCodeText = new EditText(this);
            smsCodeText.setInputType(InputType.TYPE_CLASS_NUMBER);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            smsCodeText.setLayoutParams(lp);
        alert.setCancelable(true);
//            alert.setView(smsCodeText);
//            alert.setView(R.layout.confirm_mobileno_dialog);
             alert.setView(promptsView);
//            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    if (smsCodeText.getText() != null) {
//                        verificationCode = smsCodeText.getText().toString();
//                        if (!verificationCode.isEmpty())
//                            verify();
//
//                    }
//                }
//            });

//            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//                    // Canceled.
//
//                }
//            });

        alert.show();
    }
    public void sendMessage(View v) {
        final EditText phoneText = (EditText) findViewById(R.id.mobileNo);
        mobileNo=phoneText.getText().toString();
        System.out.println(mobileNo);

        if(mobileNo.isEmpty() || mobileNo.length()!=10 )
        {
            Toast.makeText(Registration2.this,"Please enter valid mobile no.",Toast.LENGTH_LONG);
            return;
        }
        JSONObject jsonPara=new JSONObject();
        try {
            jsonPara.put("mobileNo", "91"+mobileNo);

        }catch(Exception e){
            AnalyticsManager.getInstance().raiseException("MainActivity", e);

        }



        MixPanel.getInstance().sendEvent("signupWithMobile",jsonPara);
        omgService.postMobileVerificationCode(mobileNo,new Callback<MobileVerification>() {
            @Override
            public void success(MobileVerification mobileVerificationResponse, Response response) {

                System.out.println("success");
                showConfirmDialog();

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Registration2.this,error.getMessage(),Toast.LENGTH_LONG);
                System.out.println("error " + error.getMessage());
            }
        });

    }
    public void verify() {

        final String confirmationCode=verificationCode;//smsCodeText.getText().toString();
        System.out.println(confirmationCode);
        omgService.verifyCode(mobileNo, confirmationCode, new Callback<JsonObject>() {
            @Override
            public void success(JsonObject mobileVerificationResponse, Response response) {
                alert.cancel();

                RelativeLayout registerationBlock=(RelativeLayout)findViewById(R.id.registerationBlock);
                registerationBlock.setVisibility(View.GONE);
                RelativeLayout createStore=(RelativeLayout)findViewById(R.id.createStore);
                createStore.setVisibility(View.VISIBLE);

                if (mobileVerificationResponse != null) {


                    if(mobileVerificationResponse.has("name")) {
                        MerchantRegisterCallback mbObj=new MerchantRegisterCallback();
                        Type collectionType = new TypeToken<AppUser>(){}.getType();
                        Gson gson = new Gson();
                        AppUser user = gson.fromJson(mobileVerificationResponse, collectionType);

                        mbObj.success(user,response);
                        Intent intent = new Intent(Registration2.this, Dashboard2.class);
                        startActivity(intent);
                        return;
                    }
                    Button nextButton=(Button)findViewById(R.id.next);
                    EditText mobileNoText=(EditText)findViewById(R.id.mobileNo);
                    Button signupButton=(Button)findViewById(R.id.signup);
                    EditText handleText=(EditText)findViewById(R.id.handle);
                    EditText addressText=(EditText)findViewById(R.id.address);
                    EditText titleText=(EditText)findViewById(R.id.title);
                    nextButton.setVisibility(View.INVISIBLE);
                    mobileNoText.setVisibility(View.INVISIBLE);
                    signupButton.setVisibility(View.VISIBLE);
                    handleText.setVisibility(View.VISIBLE);
                    addressText.setVisibility(View.VISIBLE);
                    titleText.setVisibility(View.VISIBLE);

                    if(mobileVerificationResponse.has("name"))
                    {

                        handleText.setText(mobileVerificationResponse.get("name").getAsString());
                        handleText.setEnabled(false);
                        handleText.setFocusable(false);
                        existingHandle=true;
                        handleAvailabiity=true;
                        addressText.setText(mobileVerificationResponse.get("address").getAsString());
                        titleText.setText(mobileVerificationResponse.get("title").getAsString());
                    }


                }
                else
                {
                    Toast.makeText(Registration2.this,"Invalid code",Toast.LENGTH_LONG);
                }
            }
            @Override
            public void failure(RetrofitError error) {
                alert.cancel();
                Toast.makeText(Registration2.this,error.getMessage(),Toast.LENGTH_LONG);
                System.out.println("error " + error.getMessage());
            }
        });
        if(geoCoder==null)
        geoCoder=new Geocoder(this);
    }

    public void register(View v)
    {

        String mobileNoText=((EditText)findViewById(R.id.mobileNo)).getText().toString();
        String handleText=((EditText)findViewById(R.id.handle)).getText().toString();
        String addressText=((EditText)findViewById(R.id.address)).getText().toString();
        String titleText=((EditText)findViewById(R.id.title)).getText().toString();

        String address=((EditText)findViewById(R.id.address)).getText().toString();
        String confirmationCode=smsCodeText.getText().toString();
        if(!existingHandle && !handleAvailabiity)
        {
            Log.d("register","Handle not avaialble");
            Toast.makeText(Registration2.this,"Handle not avaialble",Toast.LENGTH_LONG);
            return;
        }
        if(mobileNoText.isEmpty()||handleText.isEmpty()||addressText.isEmpty()||titleText.isEmpty()||confirmationCode.isEmpty()) {
            Toast.makeText(Registration2.this,"Invalid value",Toast.LENGTH_LONG);
            return;
        }
        String latitude="0";
        String longitutde="0";
        try {
            List<Address> locations = geoCoder.getFromLocationName(address, 1);
            if((locations!=null) && locations.size()>0) {
                latitude = locations.get(0).getLatitude() + "";
                longitutde = locations.get(0).getLongitude() + "";
            }
        }catch(Exception e){
            e.printStackTrace();
        }
            MerchantRegisterCallback merchantCallback = new MerchantRegisterCallback();
           omgService.registerMerchant2(mobileNo,confirmationCode,handleText,titleText,address,latitude,longitutde,merchantCallback);



    }
    class MerchantRegisterCallback implements Callback<AppUser>
    {

        @Override
        public void success(AppUser user, Response response) {
            System.out.println("Merchant received");
            LocalData localDataObj=LocalData.getInstance();
            Gson gson = new Gson();
            String jsonRepresentation = gson.toJson(user);
            localDataObj.saveData("userObj",jsonRepresentation);
            localDataObj.saveData("userId",user.getId());
            localDataObj.saveData("merchantId",user.getMerchantId());
            localDataObj.saveData("mobileNo",user.getMobileNo());
            localDataObj.saveData("storeTitle",user.getTitle());
            localDataObj.saveData("userAccessToken",user.getAccessToken());
            localDataObj.saveData("numberConfirmed",true);
            AppConstants.merchantId = user.getMerchantId();
            AppConstants.userId = user.getId();
            AppConstants.userAccessToken = user.getAccessToken();
            HashMap event=new HashMap<String,String>();
            event.put("screenName","SMS_Confirmation");
            event.put("label","Merchant_Registration");
            event.put("action","SMS Verification");
            event.put("Category","Signup");
            event.put("mobileNo",user.getMobileNo());
            event.put("userId",user.getId());
            AnalyticsManager.getInstance().sendEvent(event);
            AnalyticsManager.getInstance().setUser(user);
            Intent intent = new Intent(Registration2.this, Dashboard2.class);
            startActivity(intent);
    }

        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(Registration2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            System.out.println("Merchant error" +error.getStackTrace());
        }
    }

    class handleChangeListner implements View.OnFocusChangeListener
    {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                validateHandle();
            }
        }
    }

    private void validateHandle()
    {
        String handleText=handle.getText().toString();
        omgService.checkHandleAvailability(handleText,new Callback<JsonObject>() {


            @Override
            public void success(JsonObject jsonObject, Response response) {
                System.out.println(jsonObject);
                try {
                    if (jsonObject.get("code").getAsInt()==0) {
                        handleAvailabiity=true;
                        Toast.makeText(Registration2.this, "Handle is available", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        handleAvailabiity=false;
                        Toast.makeText(Registration2.this, jsonObject.get("result").getAsString(), Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    handleAvailabiity=false;
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(Registration2.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        String str = (String) adapterView.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();

        try {
            predsJsonArray.getJSONObject(position).getString("description");

        }catch(Exception e){}
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration2, menu);
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


    private static final String LOG_TAG = "Zisplay";

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = AppConstants.GOOGLE_API_KEY;
    JSONArray predsJsonArray;

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }


        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected Filter.FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }
}
