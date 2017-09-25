package vun.zisplay.managers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by ashok on 2/7/15.
 */
public class SMSInterceptor  extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Bundle pudsBundle = intent.getExtras();
        Object[] pdus = (Object[]) pudsBundle.get("pdus");
        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[0]);
//        Log.i(TAG, messages.getMessageBody());
        if (messages.getMessageBody().startsWith("Verification Code:")) {

            abortBroadcast();
        }
    }
}