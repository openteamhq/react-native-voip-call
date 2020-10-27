
package com.ajith.voipcall;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;


public class RNVoipCallModule extends ReactContextBaseJavaModule implements ActivityEventListener {

  private final ReactApplicationContext reactContext;
  private static ReactApplicationContext reactContextStatic;

  private  RNVoipNotificationHelper rnVoipNotificationHelper;
  private RNVoipSendData sendjsData;
  private static RNVoipSendData sendjsDataStatic;
  public static final String LogTag = "RNVoipCall";

  @Override
  public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
    Log.i("RNVoipCallModule", "onActivityResult" );

    sendjsData.sentEventToJsModule(intent);
  }

  @Override
  public void onNewIntent(Intent intent){
    Log.i("RNVoipCallModule", "onNewIntent" );

    sendjsData.sentEventToJsModule(intent);
  }

  public static void sendEventFromBroadcast(Intent intent) {
    Log.i("RNVoipCallModule", "sendEventFromBroadcast");

    sendjsDataStatic.sentEventToJsModule(intent);
 }

  RNVoipCallModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    this.reactContextStatic = reactContext;
    reactContext.addActivityEventListener(this);
    Application applicationContext = (Application) reactContext.getApplicationContext();
    rnVoipNotificationHelper = new RNVoipNotificationHelper(applicationContext);
    sendjsData = new RNVoipSendData(reactContext);
    sendjsDataStatic = sendjsData;

    Log.i("RNVoipCallModule", "Initilising RNVoipCallModule");

  }

  @Override
  public String getName() {
    return "RNVoipCall";
  }

  @ReactMethod
  public void goToBackground(){
    Activity activity = getCurrentActivity();
    activity.moveTaskToBack(true);
  }

  @ReactMethod
  public void displayIncomingCall(ReadableMap jsonObject){
    ReadableMap data = RNVoipConfig.callNotificationConfig(jsonObject);
    rnVoipNotificationHelper.sendCallNotification(data);
  }

  @ReactMethod
  public void clearNotificationById(int id){
    rnVoipNotificationHelper.clearNotification(id);
  }

  @ReactMethod
  public void clearAllNotifications(){
    rnVoipNotificationHelper.clearAllNotifications();
  }

  @ReactMethod
  public void getInitialNotificationActions(Promise promise) {
    Activity activity = getCurrentActivity();

    if(activity != null) {
        Intent intent = activity.getIntent();
        sendjsData.sendInitialData(promise,intent);
    }
  }


  @ReactMethod
  public void playRingtune(String fileName, Boolean isLooping){
    RNVoipRingtunePlayer.getInstance(reactContext).playRingtune(fileName, isLooping);
  }

  @ReactMethod
  public void  stopRingtune(){
    RNVoipRingtunePlayer.getInstance(reactContext).stopMusic();
  }

  @ReactMethod
  public  void  showMissedCallNotification(String title, String body, String callerId){
    rnVoipNotificationHelper.showMissCallNotification(title,body, callerId);
  }
}
