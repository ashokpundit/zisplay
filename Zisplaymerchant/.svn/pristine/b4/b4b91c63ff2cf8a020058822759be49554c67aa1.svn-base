package vun.zisplaymerchant.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


import vun.zisplaymerchant.R;
import vun.zisplaymerchant.managers.AnalyticsManager;
import vun.zisplaymerchant.managers.LocalData;
import vun.zisplaymerchant.utils.AppConstants;

public class InviteFollowers extends ZisplayBaseActivity implements FacebookDialog.Callback{

    private UiLifecycleHelper uiHelper;
    final static int PICK_CONTACT=1;
    private static final String SMS_SENT = "vun.zisplaymerchant";
    private static final String SMS_DELIVERED = "vun.zisplaymerchant";
    LocalData localData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics(this.getClass().getName());
        setContentView(R.layout.activity_invite_followers);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);
        localData=LocalData.getInstance();
        if(localData.getData("handle").isEmpty())
            localData.saveData("handle", "ashokg");


    }


    public void dashboard(View v)
    {
        Intent intent = new Intent(InviteFollowers.this, Dashboard2.class);
        startActivity(intent);
    }
    public void inviteWhatsApp(View v)
    {

        Intent sendIntent = new Intent();
        sendIntent.setPackage("com.whatsapp");
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,"Zisplay is free and easy mobile app to see our new collection. Click on the link <a href='http://zisplay.com/f/"+AppConstants.merchantObj.getName()+"'> Follow</a> to download the app and follow us on '"+AppConstants.merchantObj.getName()+"'");
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void inviteSMS(View v)
    {
        startActivityForResult(new Intent(InviteFollowers.this,InviteBySMS.class),PICK_CONTACT);
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
            Toast.makeText(InviteFollowers.this,
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

                    new WebDialog.FeedDialogBuilder(InviteFollowers.this,
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
                                    Toast.makeText(InviteFollowers.this,
                                            "Posted story, id: " + postId,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    // User clicked the Cancel button
                                    Toast.makeText(InviteFollowers.this.getApplicationContext(),
                                            "Publish cancelled",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else if (error instanceof FacebookOperationCanceledException) {
                                // User clicked the "x" button
                                Toast.makeText(InviteFollowers.this.getApplicationContext(),
                                        "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // Generic, ex: network error
                                Toast.makeText(InviteFollowers.this.getApplicationContext(),
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
                        intent.putExtra("sms_body", "Zisplay is free and easy mobile app to see our new collection. Click on the link  'http://zisplay.com/f/abcdf' to download the app and follow us on 'abcdf'");
//                        intent.putExtra("sms_body", "Hey! Follow my new designs on Zisplay. <a href='http://zisplay.com/f/abcdf'> Follow</a>");
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

                            Toast.makeText(InviteFollowers.this, message,Toast.LENGTH_LONG)
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_followers, menu);
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
