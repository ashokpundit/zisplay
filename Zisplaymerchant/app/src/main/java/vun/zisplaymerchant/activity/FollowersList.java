    package vun.zisplaymerchant.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import java.util.ArrayList;
import java.util.HashMap;

import it.sephiroth.android.library.picasso.Picasso;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.models.AppUser;
import vun.zisplaymerchant.network.OMGServices;
import vun.zisplaymerchant.network.RestClient;
import vun.zisplaymerchant.utils.AppConstants;
import vun.zisplaymerchant.utils.ImageDownloader;


public class FollowersList extends ZisplayBaseActivity implements FacebookDialog.Callback{

    private UiLifecycleHelper uiHelper;
    final static int PICK_CONTACT=1;
    private static final String SMS_SENT = "vun.zisplaymerchant";
    private static final String SMS_DELIVERED = "vun.zisplaymerchant";

    AlertDialog alert;
    AppUserListAdapter adapter;
    ListView list_Users;
    Toolbar toolbar;
    AppUserListAdapter cla;
    ProgressDialog pgdlg;

    AnalyticsManager analyticsManager=AnalyticsManager.getInstance();
    LocalData localData=null;
    OMGServices omgService= RestClient.getInstance().getApiService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Followers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title=(TextView)findViewById(R.id.headerTitle);
        title.setText(AppConstants.merchantObj.getNoOfFollowers()+" FOLLOWERS");
        analytics(this.getClass().getName());
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        list_Users = (ListView) findViewById(R.id.lst_followersList);
        adapter = new AppUserListAdapter(this,new AppUserList());
        list_Users.setAdapter(adapter);
        cla = adapter;

        localData= LocalData.getInstance();
        loadFollowers();
    }

    public void goBack(View V)
    {
        super.onBackPressed();
    }
    void loadFollowers()
    {
        String merchantId=localData.getData("merchantId");


        if(merchantId==null)
            return;
        pgdlg = ProgressDialog.show(this, "Please Wait", "Loading Followers...", true);
        omgService.getFollowers(merchantId, new Callback<ArrayList<AppUser>>() {
            @Override
            public void success(ArrayList<AppUser> productResponse, Response response) {
                System.out.println("product response " + productResponse);
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
                AppUserList pl=new AppUserList();//productResponse);
                pl.setAppUsers(productResponse);
                cla.gcl=pl;
                cla.notifyDataSetChanged();
            }
            @Override
            public void failure(RetrofitError error) {
                System.out.println("error " + error.getMessage());
                if(pgdlg.isShowing())
                    pgdlg.dismiss();
            }

        });
    }

    public void showInviteDialog() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.diaog_invite_follower, null);

        alert = new AlertDialog.Builder(this).create();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        alert.setCancelable(true);
        alert.setView(promptsView);
        alert.show();

    }

    public void hideDialog(View V)
    {
        alert.cancel();
    }


    public void dashboard(View v)
    {
        Intent intent = new Intent(this, Dashboard2.class);
        startActivity(intent);
    }
    public void inviteWhatsApp(View v)
    {

        Intent sendIntent = new Intent();
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void inviteSMS(View v)
    {
        startActivityForResult(new Intent(this,InviteBySMS.class),PICK_CONTACT);
    }
    public void inviteFacebook(View v)
    {

        publishFeedDialog();
    }
    public void inviteGMail(View v)
    {
        Log.i("Send email", "");


        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Follow "+localData.getData("handle")+" on Zisplay.");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>\"");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

            Log.i("Finished s", "Sending mail");
        } catch (android.content.ActivityNotFoundException ex) {
            AnalyticsManager.getInstance().raiseException("InviteFollowers",ex);
            Toast.makeText(this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void publishFeedDialog() {

        if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            // Publish the post using the Share Dialog
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this)
                    .setLink("http://zisplay.com/f/" + localData.getData("handle"))
                    .setCaption("Follow " + localData.getData("handle"))
                    .setDescription("Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>")
                    .setPicture("https://launchrock-assets.s3.amazonaws.com/logo-files/W4ZLI452_1422871716987.png?_=0")
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());

        } else {
            // Fallback. For example, publish the post using the Feed Dialog

            Bundle params = new Bundle();
            params.putString("name", "Zisplay");
            params.putString("caption", "Follow " + localData.getData("handle"));
            params.putString("description", "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>");
            params.putString("link", "http://zisplay.com/f/" + localData.getData("handle"));
            params.putString("picture", "https://launchrock-assets.s3.amazonaws.com/logo-files/W4ZLI452_1422871716987.png?_=0");

//        Session.openActiveSessionFromCache(this.getApplicationContext());//.openActiveSessionFromCache();

            WebDialog feedDialog = (

                    new WebDialog.FeedDialogBuilder(FollowersList.this,
                            AppConstants.FACEBOOK_ID,
                            params))
                    .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                        @Override
                        public void onComplete(Bundle values,
                                               FacebookException error) {
                            if (error == null) {
                                // When the story is posted, echo the success
                                // and the post Id.
                                final String postId = values.getString("post_id");
                                if (postId != null) {
                                    Toast.makeText(FollowersList.this,
                                            "Posted story, id: " + postId,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // User clicked the Cancel button
                                    Toast.makeText(FollowersList.this.getApplicationContext(),
                                            "Publish cancelled",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (error instanceof FacebookOperationCanceledException) {
                                // User clicked the "x" button
                                Toast.makeText(FollowersList.this.getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Generic, ex: network error
                                Toast.makeText(FollowersList.this.getApplicationContext(),
                                        "Error posting story",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                    })
                    .build();
            feedDialog.show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_CONTACT)
        {
            if(resultCode==RESULT_OK && !data.getExtras().isEmpty() && data.getExtras().containsKey("selectedContacts")) {

                Object[] objArray = (Object[]) data.getExtras().getSerializable("selectedContacts");
                String selectedContacts[][] = null;
                if (objArray != null) {
                    StringBuilder uri = new StringBuilder("");//"sms:");
                    selectedContacts = new String[objArray.length][];
                    for (int i = 0; i < objArray.length; i++) {
                        selectedContacts[i] = (String[]) objArray[i];
                        uri.append(selectedContacts[i][2]);
                        uri.append(", ");
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setData(Uri.parse("smsto:"));  // This ensures only SMS apps respond
                        intent.putExtra("sms_body", "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>");
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }

                    uri.deleteCharAt(uri.length()-1);

                    PendingIntent piSend = PendingIntent.getBroadcast(this, 0, new Intent(SMS_SENT), 0);
                    PendingIntent piDelivered = PendingIntent.getBroadcast(this, 0, new Intent(SMS_DELIVERED), 0);

                    BroadcastReceiver receiver = new BroadcastReceiver() {

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            String message = null;

                            switch (getResultCode()) {
                                case Activity.RESULT_OK:
                                    message = "Message sent!";
                                    break;
                                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                                    message = "Error. Message not sent.";
                                    break;
                                case SmsManager.RESULT_ERROR_NO_SERVICE:
                                    message = "Error: No service.";
                                    break;
                                case SmsManager.RESULT_ERROR_NULL_PDU:
                                    message = "Error: Null PDU.";
                                    break;
                                case SmsManager.RESULT_ERROR_RADIO_OFF:
                                    message = "Error: Radio off.";
                                    break;
                            }

                            Toast.makeText(FollowersList.this, message,Toast.LENGTH_LONG)
                                    .show();
                        }
                    };
                    registerReceiver(receiver, new IntentFilter(SMS_SENT));
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(uri.toString(),null,"Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>",piSend,piDelivered);


                }
            }

        }
        else {
            uiHelper.onActivityResult(requestCode, resultCode, data, this);
        }
    }


    @Override
    public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
        Log.e("Activity", String.format("Error: %s", error.toString()));
    }

    @Override
    public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
        Log.i("Activity", "Success!");
    }
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
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


    public void inviteFollowers(View v)
    {

        showInviteDialog();
//        Intent chatViewIntent = new Intent(FollowersList.this, InviteFollowers.class);
//        startActivity(chatViewIntent);
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_followers_list, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    class AppUserList
    {

        private ArrayList<AppUser> products = new ArrayList<AppUser>();

        public int getCount()
        {
            return this.products.size();
        }
        public void addAppUser(AppUser c)
        {
            this.products.add(c);
        }
        public void removeAppUser(AppUser c)
        {
            this.products.remove(c);
        }
        public void removeAppUser(int id)
        {
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    this.products.remove(this.products.get(i));
                }
            }
        }
        public AppUser getAppUser(int id)
        {
            AppUser tmp=null;
            for(int i=0;i<this.getCount();i++)
            {
                if(id==Integer.parseInt(this.products.get(i).getId()))
                {
                    return this.products.get(i);
                }
            }
            return tmp;
        }
        public ArrayList<AppUser> getAppUsers()
        {
            return products;
        }
        public void setAppUsers(ArrayList<AppUser> c)
        {
            this.products=c;
        }

    }

    class AppUserListAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {
        Context context;
        AppUserList gcl;
        AppUserList selectedContacts;
        public AppUserListAdapter(Context context,AppUserList gcl)
        {
            super();
            this.context = context;
            this.gcl=gcl;
            selectedContacts = new AppUserList();

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            final AppUser user=gcl.getAppUsers().get(position);
            LayoutInflater inflater = (LayoutInflater)    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view_row = inflater.inflate(R.layout.followerthumb, parent,false);
            ImageView img_product = (ImageView) view_row.findViewById(R.id._followerImage);
            TextView chk_contact = (TextView) view_row.findViewById(R.id._followerName);
            if(!user.getName().isEmpty()) {
                chk_contact.setText(user.getName());
            }
//            view_row.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String id = user.getId();
//                    Intent chatViewIntent = new Intent(FollowersList.this, UserProfile.class);
//                    chatViewIntent.putExtra("userId", id);
//                    startActivity(chatViewIntent);
//                }
//            });
            String imageIds=user.getImageId();
            String imagePath="";
            if(imageIds==null||imageIds.isEmpty()) {
                imageIds = user.getFacebookId();
                imagePath=AppConstants.CLOUDINARY_FACEBOOK_URL+"w_40,h_40,g_face,r_max,c_thumb/"+imageIds+".jpg";
            }

            if(imageIds!=null ) {
                imagePath= AppConstants.CLOUDINARY_URL+"w_40,h_40,g_face,r_max,c_thumb/"+imageIds+".jpg";

//                ImageDownloader imgDownloader= new ImageDownloader();
//                imgDownloader.download(imagePath,img_product);
            }
            if(!imagePath.isEmpty()) {
                Picasso.with(FollowersList.this).load(imagePath).into(img_product);
            }
            return view_row;
        }
        @Override
        public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
            AppUser t = gcl.getAppUser(arg0.getId());
            if(t!=null && arg1)
            {
                if(!alreadySelected(t))
                    selectedContacts.addAppUser(t);
            }
            else if(!arg1 && t!=null)
            {
                selectedContacts.removeAppUser(arg0.getId());
            }
        }
        public boolean alreadySelected(AppUser t)
        {
            boolean ret = false;
            if(selectedContacts.getAppUser(Integer.parseInt(t.getId()))!=null)
                ret=true;
            return ret;
        }
        @Override
        public int getCount() {
            return gcl.getCount();
        }
        @Override
        public AppUser getItem(int arg0) {
            return gcl.getAppUsers().get(arg0);
        }
        @Override
        public long getItemId(int arg0) {
            return  arg0;
        }
    }
}
