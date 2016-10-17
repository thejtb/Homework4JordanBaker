package com.example.codecaboose.homework4jordanbaker;

import android.app.DownloadManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jordan on 10/11/2016.
 */
public  class downloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public final static String url =
            "https://upload.wikimedia.org/wikipedia/commons/b/bb/Test_ogg_mp3_48kbps.wav";

    static DownloadManager dm;
    LocalBroadcastManager broadcaster;
    public downloadService(){
        super("downloadService");


    }


    public downloadService(String name) {
        super(name);
    }


    @Override
    public void onCreate(){
            super.onCreate();
        Log.d("2", "OnCreate");

        registerReceiver(receiver, new IntentFilter(dm.ACTION_DOWNLOAD_COMPLETE));

        broadcaster = LocalBroadcastManager.getInstance(this);


    }

public BroadcastReceiver receiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("2","running");
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(downloadService.this)
                        .setSmallIcon(R.drawable.testimage)
                        .setContentTitle("Download Complete!")
                        .setContentText("Done");


        mNotifyMgr.notify(1, mBuilder.build());

    }};


    @Override
    protected void onHandleIntent(Intent intent) {




        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(downloadService.url));
        enqueue = dm.enqueue(request);
        //save que
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "music.mp3");

        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(downloadService.this)
                        .setSmallIcon(R.drawable.testimage)
                        .setContentTitle("Download Started!")
                        .setContentText("Downloading");


        mNotifyMgr.notify(1, mBuilder.build());
        int k = 0;
        //Add in a thread
        for(int i =0; i <= 99999; i++)
            for(int j=0; j <= 9959; j++)
                k += 2;

        sendResult(url);

    }

    private long enqueue;
    public void sendResult(String message){

        Intent intent = new Intent("com.example.codecaboose.homework4jordanbaker.downloadService.RESQUEST_PROCESSED");

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(enqueue);
        Cursor c = dm.query(query);
        c.moveToFirst();
        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        if(message != null)
            intent.putExtra("com.example.codecaboose.homework4jordanbaker.downloadService.DS_MSG",uriString);
        broadcaster.sendBroadcast(intent);


    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(receiver);

    }

}
