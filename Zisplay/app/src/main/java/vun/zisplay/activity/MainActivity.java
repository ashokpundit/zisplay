package vun.zisplay.activity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

//import com.appsee.Appsee;
//import com.crashlytics.android.Crashlytics;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
//import com.strongloop.android.loopback.AccessToken;
//import com.strongloop.android.loopback.RestAdapter;
//import com.strongloop.android.loopback.UserRepository;
//import com.strongloop.android.loopback.callbacks.VoidCallback;

//import io.fabric.sdk.android.Fabric;
import java.util.Arrays;
import java.util.Random;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplay.R;
import vun.zisplay.managers.LocalData;
import vun.zisplay.models.AppUser;
//import vun.zisplay.models.MobileVerificationRepository;
import vun.zisplay.network.OMGServices;
import vun.zisplay.network.RestClient;
import vun.zisplay.utils.AppConstants;


public class MainActivity extends ZisplayBaseActivity {
    private LoginButton loginBtn;
    private TextView username;
    private UiLifecycleHelper uiHelper;
    OMGServices omgService= RestClient.getInstance().getApiService();
    public final static String EXTRA_MESSAGE = "vun.zisplay.mobileNo";
    LocalData localData=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        analytics(this.getClass().getName());
        localData= LocalData.getInstance();
        String userAccessToken=localData.getData("accessToken");
        if(userAccessToken!=null &&!userAccessToken.isEmpty())
        {
            Intent intent = new Intent(MainActivity.this, Dashboard.class);
            startActivity(intent);
            return;
        }
//        LoginButton authButton = (LoginButton) this.findViewById(R.id.authButton);
//        authButton.setReadPermissions(Arrays.asList("public_profile"));



        username = (TextView) findViewById(R.id.username);
        loginBtn = (LoginButton) findViewById(R.id.fb_login_button);
        loginBtn.setReadPermissions(Arrays.asList("email","public_profile"));
        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {

                    username.setText("You are currently logged in as " + user.getUsername());
                    final String fbId=user.getId();//
                    final String fbuserName=user.getId();//.getUsername();
                    String name=user.getName();
                    String email=(String)user.getProperty("email");
                    String age_range=(String)user.getProperty("age-range");
                   Random r=new Random();
                    r.setSeed(343546);
                    int password=r.nextInt();
                    final String pass="pass"+password;
                   final String accessToken=Session.getActiveSession().getAccessToken();

                    Log.d("facebook","fbid="+fbId+" token="+accessToken);
                    omgService.loginFBUser(fbId,accessToken, new Callback<AppUser>() {
                        @Override
                        public void success(AppUser appUser, Response response) {
                            if (appUser != null) {
                                AppConstants.userId=appUser.getId();
                                localData.saveData("fbId",fbId);
                                localData.saveData("fbtoken",accessToken);
                                localData.saveData("userId",appUser.getId());
                                localData.saveData("name",appUser.getName());
                                localData.saveData("registered",true);
                                localData.saveData("accessToken",appUser.getAccessToken());
                                AppConstants.userAccessToken=appUser.getAccessToken();
                                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.d("error " , error.getMessage());
                            vun.zisplay.models.RetrofitError err = (vun.zisplay.models.RetrofitError)error.getBodyAs(vun.zisplay.models.RetrofitError.class);

                        }
                    });


                } else {
                    username.setText("You are not logged in.");
                }
            }
        });
//        if(localData.getData("merchantId")!=null)
//        {
//            Intent intent = new Intent(MainActivity.this, Dashboard.class);
//            startActivity(intent);
//        }
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
}
