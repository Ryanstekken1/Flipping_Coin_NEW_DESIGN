package com.example.android.oli.cointossfinal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class Cheating_Activity extends AppCompatActivity {

    private static final String COIN_SIDE = "coin side";

    private ImageView coinImage;
    private Button headsButton;
    private Button tailsButton;
    private boolean clicked = false;
    //private Random r;
    //private int coinSide;
    private MediaPlayer mp;
    private int curSide = R.drawable.heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Dieser Aufruf führt dazu, dass die Titelleiste (Akku, Zeit, usw.) ausgeblendet wird.
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //r = new Random();
        coinImage = (ImageView) findViewById(R.id.coin);
        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);

        // Restore all values and images after rotate

        if (savedInstanceState != null) {

            coinImage.setImageResource(Integer.parseInt(savedInstanceState.getCharSequence(COIN_SIDE).toString()));

        }

    }
    public void showAlertDialogQuit(MenuItem menuItem){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Beenden / Quit");
        alert.setMessage("Möchten Sie App wirklich beenden?");

        alert.setNegativeButton("Ja", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                finishAffinity();

            }
        });

        alert.setPositiveButton("Nein", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alert.create().show();

    }
    public void showAlertDialogHelp(MenuItem menuItem){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Hilfe / Anleitung");
        alert.setMessage("Linken Bildschirmbereich berühren für Kopf. Rechten Bildschirmbereich berühren für Zahl.");
        alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alert.create().show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cheating_mode_menu, menu);

        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(COIN_SIDE, String.valueOf(curSide));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.settings) {
            Toast.makeText(this, "You clicked SETTINGS", Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId()== R.id.help) {
            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();

        }

        else if(item.getItemId() == R.id.regular){
            Intent myintent = new Intent(Cheating_Activity.this, MainActivity.class);
            this.startActivity(myintent);

            return false;


            }

        else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }



    private void setButtonsEnabled(boolean enabled) {
        headsButton.setEnabled(enabled);
        tailsButton.setEnabled(enabled);
    }

    private long animateCoin(boolean stayTheSame) {

        Rotate3dAnimation animation;

        if (curSide == R.drawable.coinhead) {
            animation = new Rotate3dAnimation(coinImage, R.drawable.coinhead, R.drawable.cointail, 0, 180, 0, 0, 0, 0);

        } else {
            animation = new Rotate3dAnimation(coinImage, R.drawable.cointail, R.drawable.coinhead, 0, 180, 0, 0, 0, 0);
        }
        if (stayTheSame) {
            animation.setRepeatCount(9); // must be odd (5+1 = 6 flips so the side will stay the same)
        } else {
            animation.setRepeatCount(10); // must be even (6+1 = 7 flips so the side will not stay the same)
        }

        animation.setDuration(150);
        animation.setInterpolator(new LinearInterpolator());



        coinImage.startAnimation(animation);


        setButtonsEnabled(false);

        return animation.getDuration() * (animation.getRepeatCount() + 1);
    }

    public void flipCoin(View v) {


        //final int buttonId = ((Button) v).getId();




        stopPlaying();
        mp = MediaPlayer.create(this, R.raw.coin_flip);
        mp.start();

        switch (v.getId()) {

            case R.id.heads: {

                boolean stayTheSame = (curSide == R.drawable.coinhead);
                long timeOfAnimation = animateCoin(stayTheSame);
                curSide = R.drawable.coinhead;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        setButtonsEnabled(true);

                    }

                }, timeOfAnimation + 150);

                break;
            }
            case R.id.tails: {

                boolean stayTheSame = (curSide == R.drawable.cointail);
                long timeOfAnimation = animateCoin(stayTheSame);
                curSide = R.drawable.cointail;

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        setButtonsEnabled(true);

                    }


                }, timeOfAnimation + 150);

                break;

            }
        }





    }

    private void stopPlaying() {
        if (mp != null) {
            mp.stop();
            mp.release();
            mp = null;
        }
    }
}






