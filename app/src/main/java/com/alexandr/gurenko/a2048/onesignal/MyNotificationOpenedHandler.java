package com.alexandr.gurenko.a2048.onesignal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alexandr.gurenko.a2048.MainActivity;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

import org.json.JSONObject;

public class MyNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    private static final String TAG = MyNotificationOpenedHandler.class.getSimpleName();

    private Context context;

    public MyNotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        OSNotificationAction.ActionType actionType = result.action.type;
        JSONObject data = result.notification.payload.additionalData;
        String customKey;

        Log.i(TAG, "result.notification.payload.toJSONObject().toString(): " + result.notification.payload.toJSONObject().toString());
        if (data != null) {
            customKey = data.optString("customkey");
            Log.i(TAG, "customkey set with value: " + customKey);
        }

        if (actionType == OSNotificationAction.ActionType.ActionTaken) {
            Log.i(TAG, "Button pressed with id: " + result.action.actionID);

            Intent intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
