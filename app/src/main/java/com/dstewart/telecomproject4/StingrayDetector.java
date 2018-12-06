package com.dstewart.telecomproject4;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class StingrayDetector extends Service {

    public static final String ACTION_START_STINGRAY_DETECTOR_SERVICE = "ACTION_START_STINGRAY_DETECTOR_SERVICE";
    private static Handler mHandler;
    public static final String ACTION_STOP_STINGRAY_DETECTOR_SERVICE = "ACTION_STOP_STINGRAY_DETECTOR_SERVICE";




    public StingrayDetector() {
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startID) {
        if(intent != null) {
            String action = intent.getAction();
            switch(action) {
                case ACTION_START_STINGRAY_DETECTOR_SERVICE:
                    startForegroundService();
                    Toast.makeText(getApplicationContext(), "Stingray Detector Started", Toast.LENGTH_LONG).show();
                    break;
                case ACTION_STOP_STINGRAY_DETECTOR_SERVICE:
                    stopForegroundService();
                    Toast.makeText(getApplicationContext(), "Stingray Dectector Stopped", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startID);
    }

    private void startForegroundService() {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelId = "stingray_channel_id";
        CharSequence channelName = "Stingray Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(notificationChannel);

        //Create notification default intent
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_service_icon)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(getText(R.string.notification_message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Notification notification = mBuilder.build();
        startForeground(1, notification);

        final Handler handler = new Handler();
        final int delay = 5000; //15 seconds
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                        //Yut, time to work
                        //Open the whitelist file and read it
                        FileInputStream fIn;
                        int size;
                        byte[] buffer;
                        String json;
                        //read whitelistedCellTowers file
                        try {
                            fIn = openFileInput("whitelistedTowers");
                            size = fIn.available();
                            buffer = new byte[size];
                            fIn.read(buffer);
                            fIn.close();
                            json = new String(buffer, "UTF-8");
                            JSONArray cellTowersJSON = new JSONArray(json);
                            String allCellTowers = "";
                            for(int i = 0; i<cellTowersJSON.length();i++) {
                                JSONObject cellTower = cellTowersJSON.getJSONObject(i);
                                String networkType = cellTower.getString("networkType");
                                if(networkType.compareToIgnoreCase("GSM") == 0) {
                                    //network is GSM
                                    allCellTowers += "\n\nNetwork Type: " + networkType;
                                    allCellTowers += "\nCID: " + cellTower.getString("CID");
                                    allCellTowers += "\nMCC: " + cellTower.getString("MCC");
                                    allCellTowers += "\nMNC: " + cellTower.getString("MNC");
                                    allCellTowers += "\nLAC: " + cellTower.getString("LAC");
                                }
                            }
                            TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                            String mcc = tm.getNetworkOperator().substring(0,3);
                            String mnc = tm.getNetworkOperator().substring(3);
                            Log.d("mcc", mcc);
                            Log.d("mnc", mnc);
                            if(!(allCellTowers.contains(mcc) && allCellTowers.contains(mnc))) {
                                mHandler = new Handler(Looper.getMainLooper());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(StingrayDetector.this.getApplicationContext(), "CONNECTED TO NON-SAFE CELL TOWER!!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        } catch (Exception e) {
                            return;
                        }
                    handler.postDelayed(this, delay);
                    }
                }, delay);
            }

    private void stopForegroundService() {
        stopForeground(true);
        stopSelf();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
