package com.Cyris.kbc2020;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.Cyris.kbc2020.topscore.TopDatabase;
import com.Cyris.kbc2020.topscore.TopEntity;
import com.Cyris.kbc2020.topscore.TopScoreAdapter;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Animation animationCircle,animationCircle1,animationUpDown,animationActivity;
    ImageView rotateCircle,rotateCircle1,kbcLogo;
    Dialog exitDialog;
    MediaPlayer mediaPlayer;
    Button playButton,okNoInternet;
    String language="";
    Boolean soundOnOff=true;
   // private InterstitialAd mInterstitialAd,interstitialAd,exitInterstitialAd;
    Button exitFromMain,highScore;
    List<TopEntity> topList;
    int highScoreShowCount=0;
    RatingBar ratingBar;
    ImageView volume,rate_us,share;
    String PREFER = "PREFER";
    LinearLayout noInternetConnection;
    InterstitialAd exitInterstitialAdF,soundInterstitialAdF,languageInterstitialAdF;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        rotateCircle = findViewById(R.id.rotateCircle);
        rotateCircle1 = findViewById(R.id.rotateCircle1);
        playButton = findViewById(R.id.main_play_button);
        exitFromMain = findViewById(R.id.exit_from_main);
        highScore = findViewById(R.id.high_score);
        noInternetConnection = findViewById(R.id.no_internet_connection);
        okNoInternet = findViewById(R.id.ok_no_internet_button);

        topList = new ArrayList<>();
        language = String.valueOf(R.string.language_english);

        mediaPlayer = MediaPlayer.create(this,R.raw.kbc_welcome_long);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        kbcLogo = findViewById(R.id.kbcLogo);
        animationCircle = new AnimationUtils().loadAnimation(this,R.anim.circular_rotate);
        animationCircle1 = new AnimationUtils().loadAnimation(this,R.anim.circular_rotate1);
        animationUpDown = new AnimationUtils().loadAnimation(this,R.anim.up_down);
        animationActivity = new AnimationUtils().loadAnimation(this,R.anim.slide_down);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rotateCircle.startAnimation(animationCircle);
                rotateCircle1.startAnimation(animationCircle1);
                kbcLogo.startAnimation(animationUpDown);
            }
        },500);

        new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("2E634816C5070582A516B48F0983ECFB"));


        playButton.startAnimation(animationUpDown);
        exitInterstitialAdF = new InterstitialAd(this, getString(R.string.exitInterstitialFacebook));
        exitInterstitialAdF.loadAd();
        soundInterstitialAdF = new InterstitialAd(this,getString(R.string.soundInterstitialFacebook));
        soundInterstitialAdF.loadAd();
        languageInterstitialAdF = new InterstitialAd(this,getString(R.string.languageInterstitialFacebook));
        languageInterstitialAdF.loadAd();

     /*   mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.languageInterestial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.soundInterestial));
        interstitialAd.loadAd(new AdRequest.Builder().build());

        exitInterstitialAd = new InterstitialAd(MainActivity.this);
        exitInterstitialAd.setAdUnitId(getString(R.string.exitInterestial));
        exitInterstitialAd.loadAd(new AdRequest.Builder().build()); */


        exitFromMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(exitInterstitialAdF!=null&&exitInterstitialAdF.isAdLoaded())
                {
                    exitInterstitialAdF.show();
                }
                /*if(exitInterstitialAd.isLoaded())
                    exitInterstitialAd.show();*/
                ExitDialog();
            }
        });

        highScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,HighScore.class);
                startActivity(intent);
                //topList.clear();
                //showTopScore();
            }
        });

        VolRateShare();
        NotificationCaller();

        if(!isNetworkAvailable())
            noInternetConnection.setVisibility(View.VISIBLE);
        okNoInternet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetConnection.setVisibility(View.GONE);
            }
        });


    }

    private void VolRateShare() {
        share = findViewById(R.id.share);
        volume = findViewById(R.id.volume);
        volume.setTag(R.string.soundOn);
        rate_us = findViewById(R.id.rate_us);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_TEXT,"Download KBC:-\n https://play.google.com/store/apps/details?id=com.Cyris.kbc2020");
                startActivity(Intent.createChooser(intent,"Share"));
            }
        });
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(soundInterstitialAdF!=null && soundInterstitialAdF.isAdLoaded())
                {
                    soundInterstitialAdF.show();
                }
                /*if(interstitialAd.isLoaded())
                    interstitialAd.show();*/
                if(volume.getTag().equals(R.string.soundOff))
                {
                    soundOnOff = true;
                    Toast.makeText(MainActivity.this,"Sound On",Toast.LENGTH_LONG);
                    CreateMediaPlayer();
                    volume.setTag(R.string.soundOn);
                    volume.setBackgroundResource(R.drawable.main_button_gradient);
                    volume.setImageResource(R.drawable.volume_on);
                }
                else
                {
                    ReleaseMediaPlayer();
                    Toast.makeText(MainActivity.this,"Sound Off",Toast.LENGTH_LONG);
                    soundOnOff = false;
                    volume.setImageResource(R.drawable.volume_off);
                    volume.setBackgroundResource(R.drawable.wrong_answer_button);
                    volume.setTag(R.string.soundOff);

                }
            }
        });
        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingBarFunctionality();
            }
        });


    }

    private void RatingBarFunctionality() {
        final Dialog rateDialog = new Dialog(this);
        rateDialog.setContentView(R.layout.rate_us);
        ratingBar =rateDialog.findViewById(R.id.rating_bar);
        rateDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_red_white));
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating>3)
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.Cyris.kbc2020"));
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/e/1FAIpQLScHqfa7rysPNuxkcQq65h4iOkVeUQA1RLDff3KaUN_WZSq4nA/viewform?usp=sf_link"));
                    try {
                        startActivity(intent);
                        if(rateDialog!=null&&rateDialog.isShowing())
                            rateDialog.dismiss();
                    }
                    catch(Exception e)
                    {
                        Log.i("RatingError","Got Error while rating");
                    }
                }
            }
        });
        rateDialog.show();





    }

   /* private void showTopScore() {
        ShowTop showTop = new ShowTop();
        showTop.execute();
        if(topList!=null) {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.high_score_dialog);
            TopScoreAdapter topScoreAdapter = new TopScoreAdapter(this, topList,dialog);
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_red_white));
            dialog.getWindow().setLayout(800, LinearLayout.LayoutParams.WRAP_CONTENT);
            RecyclerView recyclerView = dialog.findViewById(R.id.recyclerview_in_high_score);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(topScoreAdapter);
            dialog.show();
        }

    }
*/
    public void PlayNow(View view)
    {
        ReleaseMediaPlayer();
        Intent intent = new Intent(this,PlayGame.class);
        intent.putExtra(String.valueOf(R.string.select_language),language);
        intent.putExtra(String.valueOf(R.string.sound_on_off),soundOnOff);
        startActivity(intent);
    }

    public void LanguangeSeletion(View view)
    {
        Button tv = (Button) view;
        if(languageInterstitialAdF!=null&&languageInterstitialAdF.isAdLoaded())
        {
            languageInterstitialAdF.show();
        }
        /*if(mInterstitialAd.isLoaded())
            mInterstitialAd.show(); */
        if(tv.getTag().equals(R.string.language_hindi_button))
        {
            language = String.valueOf(R.string.language_english);
            Toast.makeText(MainActivity.this,"English Selected",Toast.LENGTH_LONG);
            tv.setText(R.string.language_english_button);
            tv.setBackgroundResource(R.drawable.main_button_gradient);
            tv.setTag(R.string.language_english_button);
        }
        else//(tv.getText().equals(String.valueOf(R.string.language_hindi_button)))
        {
            language = String.valueOf(R.string.language_hindi);
            Toast.makeText(MainActivity.this,"Hindi Selected",Toast.LENGTH_LONG);
            tv.setText(R.string.language_hindi_button);
            tv.setTag(R.string.language_hindi_button);
        }
    }

    @Override
    protected void onResume() {
        CreateMediaPlayer();
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(PREFER,0);
        int a = preferences.getInt("dialogShow",0);
        Log.i("preferencCount",String.valueOf(a));

        if(a<5)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("dialogShow",++a);
            editor.commit();
        }
        else if(a==5)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("dialogShow",++a);
            editor.commit();
            RatingBarFunctionality();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        ReleaseMediaPlayer();
        super.onStop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
    }

    public void ExitDialog()
    {
        exitDialog = new Dialog(this);
        exitDialog.setContentView(R.layout.exit_game);
        exitDialog.show();
    }

    public  void ExitDialogYes(View view)
    {
        ReleaseMediaPlayer();
        if(exitDialog.isShowing())
            exitDialog.dismiss();
        this.finish();
    }

    public void ExitDialogNo(View view)
    {
        exitDialog.cancel();
    }

    @Override
    public void onBackPressed() {
        ExitDialog();
    }

    public void ReleaseMediaPlayer()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void CreateMediaPlayer()
    {
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if(soundOnOff) {
            mediaPlayer = MediaPlayer.create(this, R.raw.kbc_welcome_long);
            mediaPlayer.start();
        }
    }

    public void NotificationCaller()
    {
        Intent intent = new Intent(MainActivity.this, NotificationClass.class);
        PendingIntent pending1 = PendingIntent.getBroadcast(getApplicationContext(), 42, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pending2 = PendingIntent.getBroadcast(getApplicationContext(), 43, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pending3 = PendingIntent.getBroadcast(getApplicationContext(), 44, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Schdedule notification
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY,10);
        calendar1.set(Calendar.MINUTE, 00);
        calendar1.set(Calendar.SECOND, 00);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 14);
        calendar2.set(Calendar.MINUTE, 00);
        calendar2.set(Calendar.SECOND, 00);

        Calendar calendar3 = Calendar.getInstance();
        calendar3.set(Calendar.HOUR_OF_DAY, 19);
        calendar3.set(Calendar.MINUTE, 00);
        calendar3.set(Calendar.SECOND, 00);

        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pending1);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pending2);
        manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pending3);


    }



  /*  public class ShowTop extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            TopDatabase database = TopDatabase.getInstance(MainActivity.this);
            for(int i=0;i<database.daoInterface().size();i++)
            {
                topList.add(database.daoInterface().getData().get(i));

            }

            return null;
        }
    } */

    @Override
    protected void onDestroy() {
        if(exitInterstitialAdF!=null)
            exitInterstitialAdF.destroy();
        if(languageInterstitialAdF!=null)
            languageInterstitialAdF.destroy();
        if(soundInterstitialAdF!=null)
            languageInterstitialAdF.destroy();

        super.onDestroy();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
