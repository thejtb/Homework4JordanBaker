package com.example.codecaboose.homework4jordanbaker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by Jordan on 10/9/2016.
 */
public class playClass extends Service {
    MediaPlayer player;


    @Override
    public IBinder onBind(Intent intent) {

        return pBind;
    }


    @Override
            public boolean onUnbind(Intent intent){

        player.stop();
        player.release();
        return false;
    }

    IBinder pBind = new MusicBinder();

    @Override
    public void onCreate(){

        super.onCreate();
        player = new MediaPlayer();




    }


    public class MusicBinder extends Binder {
       public playClass getServiceInstance() {
            return playClass.this;
        }
    }





        Intent playIntent;





/*

    playClass mServ;
    boolean musicBind;
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            mServ = binder.getService();

            musicBind = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBind = false;
        }
    };

*/
    Uri MUX;

    public void setSong(Uri Music)
    {
        player = MediaPlayer.create(playClass.this,Music);
        MUX = Music;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;

    }

    public void playSong() {



        //player.prepareAsync();
        player.start();


        Intent Intent = new Intent(this, MainActivity.class);
        Intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent.putExtra("NotM",MUX.toString());
        PendingIntent pendInt = PendingIntent.getActivity(this, 0,
                Intent, PendingIntent.FLAG_UPDATE_CURRENT);



        Notification.Builder builder = new Notification.Builder(this);

        builder.setContentIntent(pendInt)
                .setSmallIcon(R.drawable.testimage)

                .setTicker("Song")
                .setOngoing(true)
                .setContentTitle("Playing")
        .setContentText("Smooth Tunes");
        Notification not = builder.build();

        startForeground(1, not);

    }

    public void pauseSong(){

        player.pause();

    }

    public void kill(){

        player.stop();
    }

    public void stopSong(){

        player.pause();
        player.seekTo(0);

    }

    /*
    @Override
    public void onPrepared(MediaPlayer mp) {
        //start playback
        mp.start();

    }
    */
}
