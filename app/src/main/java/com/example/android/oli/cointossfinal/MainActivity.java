package com.example.android.oli.cointossfinal;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String COIN_SIDE = "coin side";

    private ImageView coinImage;
    private Button headsButton;
    private Button tailsButton;
    private Random r;
    private int coinSide;
    private MediaPlayer mp;
    private int curSide = R.drawable.heads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        r = new Random();
        coinImage = (ImageView) findViewById(R.id.coin);
        headsButton = (Button) findViewById(R.id.heads);
        tailsButton = (Button) findViewById(R.id.tails);

        // Restore all values and images after rotate

        if (savedInstanceState != null) {

            coinImage.setImageResource(Integer.parseInt(savedInstanceState.getCharSequence(COIN_SIDE).toString()));

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(COIN_SIDE, String.valueOf(curSide));

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


        final int buttonId = ((Button) v).getId();
        coinSide = r.nextInt(2);

        stopPlaying();
        mp = MediaPlayer.create(this, R.raw.coin_flip);
        mp.start();

        if (coinSide == 0) {  // We have Tails

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


        } else {  // We have Heads

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
