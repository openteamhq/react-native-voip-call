package com.ajith.voipcall;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class RNVoipBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Application applicationContext = (Application) context.getApplicationContext();

        RNVoipNotificationHelper rnVoipNotificationHelper = new RNVoipNotificationHelper(applicationContext);
        int notificationId = intent.getIntExtra("notificationId",0);
        Log.i("RNVoipBroadcastReceiver.onReceive", intent.getAction());

        switch (intent.getAction()){
            case "callAnswer":
                RNVoipRingtunePlayer.getInstance(context).stopMusic();
                rnVoipNotificationHelper.clearNotification(notificationId);

                RNVoipCallModule.sendEventFromBroadcast(intent);

//              // rnVoipNotificationHelper.showMissCallNotification(intent.getStringExtra("missedCallTitle"), intent.getStringExtra("missedCallBody"), intent.getStringExtra("callerId"));
                break;
            case "callDismiss":
                RNVoipRingtunePlayer.getInstance(context).stopMusic();
                rnVoipNotificationHelper.clearNotification(notificationId);

                RNVoipCallModule.sendEventFromBroadcast(intent);

//                rnVoipNotificationHelper.showMissCallNotification(intent.getStringExtra("missedCallTitle"), intent.getStringExtra("missedCallBody"), intent.getStringExtra("callerId"));
                break;
            case "callTimeOut":
               // rnVoipNotificationHelper.showMissCallNotification(intent.getStringExtra("missedCallTitle"), intent.getStringExtra("missedCallBody"), intent.getStringExtra("callerId"));
                break;
            default:
                break;
        }

    }
}
