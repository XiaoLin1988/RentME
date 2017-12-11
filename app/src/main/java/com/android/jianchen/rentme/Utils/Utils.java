package com.android.jianchen.rentme.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.android.jianchen.rentme.Models.UserModel;
import com.android.jianchen.rentme.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by emerald on 6/2/2017.
 */
public class Utils {
    public static void saveUserInfo(Context context, UserModel curUser) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(curUser);
        prefsEditor.putString(Constants.PREF_CUR_USER, json);
        prefsEditor.commit();
    }

    public static UserModel retrieveUserInfo(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString(Constants.PREF_CUR_USER, "");
        UserModel curUser = gson.fromJson(json, UserModel.class);

        return curUser;
    }

    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }

    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static boolean checkActivityRunning(Context context, String myActivity) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfo = manager.getRunningTasks(1);
        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
        Log.e("RUNNING", componentInfo.getShortClassName());
        return componentInfo.getShortClassName().contains(myActivity);
    }

    public static boolean checkPermission (Context context, String permission) {
        int res = context.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    public static int getGridSpanCount(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        float screenWidth  = displayMetrics.widthPixels;
        float cellWidth = activity.getResources().getDimension(R.dimen.recycler_item_size);
        return Math.round(screenWidth / cellWidth);
    }

    public static String beautifyDate(Date date, boolean time) {
        SimpleDateFormat formatter = null;
        if (time)
            formatter = new SimpleDateFormat("MMM dd, yyyy h:m a");
        else
            formatter = new SimpleDateFormat("MMM dd, yyyy");

        String res = formatter.format(date);

        return res;
    }

    public static Date stringToDate(String dt) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(dt);
            return date;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
