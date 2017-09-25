package vun.zisplay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;
import vun.zisplay.R;
import vun.zisplay.managers.AnalyticsManager;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.Location;
import vun.zisplay.models.Merchant;
import vun.zisplay.models.Message;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;

public class MerchantProfile extends ZisplayBaseActivity {


    ProgressDialog pgdlg;

    AnalyticsManager analyticsManager=AnalyticsManager.getInstance();
    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();

    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient=null;

    Merchant merchant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_profile);
        analytics(this.getClass().getName());
        Intent intent = getIntent();
       String merchantId  = intent.getStringExtra("merchantId");
        localData=LocalData.getInstance();
        loadMerchant(merchantId);
    }


    public void startChat(View v)
    {

        final String message="Hi";

        final String userId=localData.getData("userId");
        final String username=localData.getData("name");
        omgService.sendMessage(new TypedString(merchant.getId()), new TypedString(userId), new TypedString(merchant.getName()), new TypedString(username), new TypedString(message), null,null, new Callback<Message>() {
            @Override
            public void success(Message message, Response response) {

                System.out.println("image created ");
                String chatId=message.getChatId();

                String[] users={userId,merchant.getId()};
                String[] userNames={username,merchant.getName()};
                Intent chatViewIntent=new Intent(MerchantProfile.this, ChatHistory.class);
                chatViewIntent.putExtra("chatId",chatId);
                chatViewIntent.putExtra("users",users);
                chatViewIntent.putExtra("userNames",userNames);
                startActivity(chatViewIntent);

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
            }
        });
    }

    public void loadMerchant(String merchantId)
    {


        if(merchantId==null)
            return;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Merchant...", true);
        omgService.getMerchant(merchantId, new Callback<Merchant>() {
            @Override
            public void success(Merchant merchantObj, Response response) {
                System.out.println("following response " + merchantObj);
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
                if(merchantObj==null) {

                }
                else
                {
                    merchant = merchantObj;
                    loadMerchant(merchant);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                if (pgdlg.isShowing())
                    pgdlg.dismiss();
            }

        });
    }

    private void loadMerchant(Merchant merchantObj)
    {

        TextView titleText = (TextView) findViewById(R.id.title);
        titleText.setText(merchantObj.getTitle());
        TextView zisplayHandleText = (TextView) findViewById(R.id.zisplayHandle);
        zisplayHandleText.setText(merchantObj.getName() +" "+merchantObj.getMobileNo());
        TextView merchantAddressText = (TextView) findViewById(R.id.address);
        merchantAddressText.setText(merchantObj.getAddress()+", "+merchantObj.getLocality()+" ,"+merchantObj.getCity());
//        Location location=merchantObj.getLocation();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_merchant_profile, menu);
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
