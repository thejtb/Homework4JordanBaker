package com.example.codecaboose.homework4jordanbaker;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.codecaboose.homework4jordanbaker.playClass.MusicBinder;
import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    int downloadFlag;
    int playing;


    Intent downloadIntent;
    MediaPlayer player;
    BroadcastReceiver receiver;
    //Use a reciever to check for incoming data from download service
    //then switch out vairables and whatnot and the album icon


    Intent playIntent;

    @Override
    protected void onStart(){
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("com.example.codecaboose.homework4jordanbaker.downloadService.RESQUEST_PROCESSED"));

        /*
        Intent mIntent = new Intent(this,playClass.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        */
        if(playIntent==null){
            playIntent = new Intent(this, playClass.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);

        }

    }




    boolean musicBound = false;
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            pClass = binder.getServiceInstance();
            //pass list

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };



    @Override
    protected void onStop(){
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);

        super.onStop();
        if(mBounded){

            unbindService(mConnection);
            mBounded = false;
        }

    }


    Uri songList;


    @Override
    public void onNewIntent(Intent intent)
    {
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            if(extras.containsKey("NotM"))
            {
                setContentView(R.layout.activity_main);
                downloadFlag = 1;
                playing = 0;
                songList = Uri.parse(extras.getString("NotM"));
                ImageView imgy = (ImageView) findViewById(R.id.imageView);
                imgy.setImageResource(R.drawable.musicx);
                TextView texty = (TextView) findViewById(R.id.textView);
                texty.setText("Smooth Tunes\nBy Smooth Carl");


            }

        }


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        downloadFlag = 0;
        playing = 0;


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                String str = intent.getStringExtra("com.example.codecaboose.homework4jordanbaker.downloadService.DS_MSG");
                Log.d("33",str);
                downloadFlag = 1;
                Uri music = Uri.parse(str);
               // startService(new Intent(MainActivity.this,playClass.class));

                songList = music;
                //pClass.playSong(music);
                ImageView imgy = (ImageView) findViewById(R.id.imageView);
                imgy.setImageResource(R.drawable.musicx);
                TextView texty = (TextView) findViewById(R.id.textView);
                texty.setText("Smooth Tunes\nSmooth Carl");

                pClass.setSong(music);

            }


        };



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.download:
                startService(new Intent(this,downloadService.class));
                return true;
            case R.id.exit:
                pClass.kill();
                System.exit(1);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    boolean mBounded;
    playClass pClass;

    ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceDisconnected(ComponentName name) {

            mBounded = false;
            pClass = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {

            mBounded = true;
            MusicBinder mBind = (MusicBinder)service;
            pClass = mBind.getServiceInstance();
        }
    };

    int playFlag = 0;

    public void onClick(View v) {

        if(v.getId() == R.id.button && downloadFlag == 1 && playFlag == 0)
        {
            pClass.playSong();
            playFlag = 1;
        }
        else if(v.getId() == R.id.button2 && downloadFlag == 1 && playFlag == 1)
        {
            pClass.stopSong();
            playFlag = 0;
        }
        else if(v.getId() == R.id.button3 && downloadFlag == 1 && playFlag == 1)
        {
            pClass.pauseSong();
            playFlag = 0;
        }




    }





    /*

*/
    /*
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.layout.options, menu);
        return true;
    }
    */

}
