package com.example.hamchetemp;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String token) {
        Log.e(TAG, "here ! sendRegistrationToServer! token is " + token);
    }

    public void onTokenRefresh() {
    }

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID
     token
     * is initially generated so this is where you would retrieve the token. */
}