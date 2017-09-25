package vun.zisplaymerchant.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.models.Merchant;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;

public class SocialChannels extends ZisplayBaseActivity {

    JSONObject jsonObject=null;
    JSONObject[] pages=null;

    private LoginButton loginBtn;
    private TextView username;
    private UiLifecycleHelper uiHelper;
    OMGServices omgService= RestClient.getInstance().getApiService();
    public final static String EXTRA_MESSAGE = "vun.zisplay.mobileNo";
    LocalData localData=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_channels);
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        analytics(this.getClass().getName());
        localData= LocalData.getInstance();
        username = (TextView) findViewById(R.id.username);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setPublishPermissions(Arrays.asList("email", "public_profile","manage_pages","publish_actions"));
        loginBtn.setUserInfoChangedCallback(new facebookListener());

    }

    public void connectFb(View V)
    {
        loginBtn.setPublishPermissions(Arrays.asList("email", "public_profile","manage_pages","publish_actions"));
        loginBtn.setUserInfoChangedCallback(new facebookListener());
//        loginBtn.performClick();
    }
    private void getFBPages(String facebookId)
    {
        final String fbId=facebookId;
         /* make the API call */
        new Request(
                Session.getActiveSession(),
                "/" + facebookId + "/accounts",
                null,
                HttpMethod.GET,
                new Request.Callback() {
                    public void onCompleted(com.facebook.Response response) {
                        System.out.println("Page response");
                        System.out.println("response" + response.getRawResponse());

                        try {
                            jsonObject=new JSONObject(response.getRawResponse());
                            if(jsonObject!=null)
                            {
                                JSONArray pagesAr= jsonObject.getJSONArray("data");
                                pages=new JSONObject[pagesAr.length()];
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(
                                        SocialChannels.this);
                                builderSingle.setIcon(R.drawable.ic_launcher);
                                builderSingle.setTitle("Select One Name:-");
                                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                        SocialChannels.this,
                                        android.R.layout.select_dialog_singlechoice);

                                for (int i=0;i<pagesAr.length();i++)
                                {
                                    pages[i]= pagesAr.getJSONObject(i);
                                    arrayAdapter.add(pages[i].getString("name"));

                                }
                                builderSingle.setNegativeButton("cancel",
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });

                                builderSingle.setAdapter(arrayAdapter,
                                        new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String strName = arrayAdapter.getItem(which);
                                                try{
                                                    Log.d("facebook page",strName+pages[which].getString("access_token"));
                                                    saveFBPage(AppConstants.merchantId, fbId,Session.getActiveSession().getAccessToken(),pages[which].getString("id"),pages[which].getString("access_token"));
                                                }catch(Exception e){e.getStackTrace(); return;}

                                                AlertDialog.Builder builderInner = new AlertDialog.Builder(
                                                        SocialChannels.this);
                                                try {
                                                    builderInner.setMessage(strName + pages[which].getString("access_token"));
                                                }catch(Exception e){e.getStackTrace();}
                                                builderInner.setTitle("Your Selected Item is");
                                                builderInner.setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {

                                                            @Override
                                                            public void onClick(
                                                                    DialogInterface dialog,
                                                                    int which) {
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                builderInner.show();
                                            }
                                        });
                                builderSingle.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


            /* handle the result */
                    }
                }
        ).executeAsync();
    }
    private void saveFBPage(String merchantId,String facebookId, String userAccessToken,String pageId,String pageToken)
    {
        omgService.connectFBPage(merchantId,facebookId,userAccessToken,pageId,pageToken, new Callback<String>() {
            @Override
            public void success(String result, Response response) {

                if(result.equalsIgnoreCase("success"))
                {
                    Toast.makeText(SocialChannels.this,"Page connected successfully",Toast.LENGTH_LONG);
                }
                if(result.equalsIgnoreCase("success"))
                {
                    Toast.makeText(SocialChannels.this,"Error "+result,Toast.LENGTH_LONG);
                }
                };


            @Override
            public void failure(RetrofitError error) {
                Log.d("error ", error.getMessage());
                vun.zisplaymerchant.models.RetrofitError err = (vun.zisplaymerchant.models.RetrofitError) error.getBodyAs(vun.zisplaymerchant.models.RetrofitError.class);

            }
        });
    }

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("MainActivity", "Facebook session opened.");
            } else if (state.isClosed()) {
                Log.d("MainActivity", "Facebook session closed.");
            }
        }
    };

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
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

   class facebookListener implements LoginButton.UserInfoChangedCallback {
        @Override
        public void onUserInfoFetched(GraphUser user) {
            if (user != null) {

                username.setText("You are currently logged in as " + user.getUsername());
                final String fbId = user.getId();//
                final String fbuserName = user.getId();//.getUsername();
                String name = user.getName();
                String email = (String) user.getProperty("email");
                String age_range = (String) user.getProperty("age-range");
                final String accessToken = Session.getActiveSession().getAccessToken();
                Log.d("facebook", "fbid=" + fbId + " token=" + accessToken);
                 getFBPages(fbId);
            } else {
                username.setText("You are not logged in.");
            }
        }
    }
}
