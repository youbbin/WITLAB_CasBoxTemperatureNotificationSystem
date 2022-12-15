package com.example.hamchetemp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import android.os.Handler;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService { private static final String TAG = "MyFirebaseMessagingService";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) { super.onMessageReceived(remoteMessage);
// 앱이 실행 중일 때 (Foreground 상황) 에서 푸쉬를 받으면 호출됩니다. // 백그라운드 상황에서는 호출되지 않고 그냥 알림목록에 알림이 추가됩니다. Log.d(TAG,"Message Arrived");
        if ( remoteMessage.getData().size() > 0 ) {
            Log.d(TAG, "FCM Data Message : " + remoteMessage.getData()); }
        if ( remoteMessage.getNotification() != null ) {
            final String messageBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "FCM Notification Message Body : " + messageBody);
            Handler handler = new Handler(Looper.getMainLooper()); handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), messageBody, Toast.LENGTH_SHORT).show();
                } });
        } }
}