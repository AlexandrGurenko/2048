package com.alexandr.gurenko.a2048.onesignal;

import android.util.Log;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

    private static final String TAG = MyNotificationReceivedHandler.class.getSimpleName();

    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        String customKey;

        if (data != null) {
            customKey = data.optString("customkey");
            Log.i(TAG, "customkey set with value: " + customKey);

        }
    }
}
