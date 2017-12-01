package com.android.emerald.rentme.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.emerald.rentme.AppController;
import com.android.emerald.rentme.ChattingActivity;
import com.android.emerald.rentme.Models.MessageModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Task.APIStringRequester;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Transaction;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 6/16/2017.
 */
public class FirebaseTracker extends Service implements Response.Listener<String>, Response.ErrorListener {
    private Firebase f = new Firebase(Constants.FIREBASE_ROOT);
    private ChildEventListener firebaseHandler;
    private int counter = 0;
    private boolean isInitial;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        isInitial = false;

        firebaseHandler = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!isInitial)
                    return;
                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                postNotif(message);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        f.addChildEventListener(firebaseHandler);

        f.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isInitial = true;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void onStart(Intent intent, int startId) {

    }

    private void postNotif(MessageModel message) {
        if (Utils.checkActivityRunning(getApplicationContext(), "Chatting"))  {
            Log.e("Firebase Tracker", "Chatting activity running");
            return;
        }
        Log.e("Firebase Tracker", "Chatting activity not running");

        UserModel curUser = Utils.retrieveUserInfo(getApplicationContext());
        if (curUser.getId() == message.getReceiver()) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int icon = R.drawable.app_logo;
            Intent notificationIntent = new Intent(getApplicationContext(), ChattingActivity.class);
            Gson gson = new Gson();
            String json = gson.toJson(message);
            notificationIntent.putExtra("message", json);
            notificationIntent.putExtra(Constants.EXTRA_PROJECT_DETAIL, message.getProjectId());
            Log.e("FIREBASE", json);
            PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), Constants.MESSAGE_REQUEST, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            Notification notification = builder.setContentIntent(contentIntent)
                    .setSmallIcon(icon)
                    .setTicker("Firebase" + Math.random())
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle("New message arrived")
                    .setContentText(message.getMessage()).build();

            mNotificationManager.notify(counter++, notification);
            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(800);
        }
    }

    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }
}
