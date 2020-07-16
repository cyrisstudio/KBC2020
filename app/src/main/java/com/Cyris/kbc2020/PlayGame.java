package com.Cyris.kbc2020;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Cyris.kbc2020.AdapterClass.RewardAdapter;
import com.Cyris.kbc2020.AdapterClass.RewardPrices;
import com.Cyris.kbc2020.Questions.HindiQuestionSet1;
import com.Cyris.kbc2020.Questions.HindiQuestionSet2;
import com.Cyris.kbc2020.Questions.HindiQuestionSet3;
import com.Cyris.kbc2020.Questions.QuestionAnswerObjectClass;
import com.Cyris.kbc2020.Questions.QuestionSet1;
import com.Cyris.kbc2020.Questions.QuestionSet2;
import com.Cyris.kbc2020.Questions.QuestionSet3;
import com.Cyris.kbc2020.topscore.FirebaseVariable;
import com.Cyris.kbc2020.topscore.HighScoreAddAsync;
import com.Cyris.kbc2020.topscore.TopEntity;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.RewardedVideoAd;
import com.facebook.ads.RewardedVideoAdExtendedListener;
import com.facebook.ads.RewardedVideoAdListener;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;

public class PlayGame extends AppCompatActivity implements RewardedVideoAdListener {
    TextView question, option1, option2, option3, option4;
    ArrayList<QuestionAnswerObjectClass> data = new ArrayList<>();
    CountDownTimer countDownTimer;
    ArrayList<TextView> opt;
    ArrayList<String> priceData;
    ArrayList<String> priceInfo;
    QuestionAnswerObjectClass quacObject;
    int getQuacCount = 0, priceCount = 13, questionCount = 0, randomSelection = 0;//0,13
    Dialog dialog, dlg, pofDialog, wantToQuitDialog,wonDialog;
    RecyclerView rewardInfoRecyclerView;
    LinearLayoutManager rewardLayoutmanager;
    RewardAdapter rewardAdapter;
    Random random;
    LinearLayout layout1, layout2, layout3, layout4;
    Handler handler = new Handler();
    Animation animation;
    Boolean isHandlerActive = false,isTimerContinue=true;
    String language = "";
    MediaPlayer mediaPlayer;
    Boolean soundOnOff;
    //private InterstitialAd mInterstitialAd;
    Button quitDialogYes,exitOnWrongAnswer;
    EditText nameText;
    TextView showRewardAll;
    TextView countDownText;
    long timerCount;
   // private RewardedVideoAd mRewardedVideoMain,mRewardedVideoPhone;
    String AdSelection;
    int watchAdCount=0;
    boolean internetToLoadAd = false;
    Dialog timesUpDialog;
    private InterstitialAd onLostInterstitialAdF;
    private RewardedVideoAd rewardedVideoMainFacebook;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private Date myDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Intent intent = getIntent();
        language = intent.getStringExtra(String.valueOf(R.string.select_language));
        countDownText = findViewById(R.id.countdownTimer);
        showRewardAll = findViewById(R.id.show_reward_all);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("TopScoreUpdate");

        soundOnOff = intent.getBooleanExtra(String.valueOf(R.string.sound_on_off), true);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        random = new Random();
        priceData = RewardPrices.getPrice();
        // pie= AnyChart.pie();

        animation = new AnimationUtils().loadAnimation(this, R.anim.price_dialog_in);
        if (language.equals(String.valueOf(R.string.language_english)))
            data = QuestionSet1.getAllDataFromSet1();
        else
            data = HindiQuestionSet1.getAllDataFromHindiSet1();
        opt = new ArrayList<>();
        FindViews();
        FirstQuesPlayer();

        onLostInterstitialAdF = new InterstitialAd(this, getString(R.string.onLostInterstitialFacebook));
        onLostInterstitialAdF.loadAd();


        rewardedVideoMainFacebook = new RewardedVideoAd(this,getString(R.string.playAgainRewardedFacebook));
        rewardedVideoMainFacebook.setAdListener(this);
        rewardedVideoMainFacebook.loadAd();
                /*mInterstitialAd = new InterstitialAd(PlayGame.this);
                mInterstitialAd.setAdUnitId(getString(R.string.onLostInterestial));
                mInterstitialAd.loadAd(new AdRequest.Builder().build());*/

       /* mRewardedVideoMain = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoMain.setRewardedVideoAdListener(this);

        mRewardedVideoPhone = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoPhone.setRewardedVideoAdListener(this); */

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                SetData();
                countDownTimer = new CountDownTimer(45000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                            countDownText.setText(String.valueOf(millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        if(!PlayGame.this.isFinishing())
                            TimesUpDialog();
                    }
                };
                countDownTimer.start();
                isHandlerActive = true;
            }
        }, soundOnOff ? 5000 : 1000);



    }

    private void FindViews() {
        question = findViewById(R.id.question_textView);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);
        layout1 = findViewById(R.id.layout_option1);
        layout2 = findViewById(R.id.layout_option2);
        layout3 = findViewById(R.id.layout_option3);
        layout4 = findViewById(R.id.layout_option4);

        opt.clear();
        opt.add(option1);
        opt.add(option2);
        opt.add(option3);
        opt.add(option4);
    }

    public void ClickAnswer(View view) {
        // TextView tv = (TextView) view;
        // tv.setTextColor(Color.YELLOW);

        if (isHandlerActive) {
            if(countDownTimer!=null)
            {
                countDownTimer.cancel();
            }


            isHandlerActive=false;
            layout1.setClickable(false);
            layout2.setClickable(false);
            layout3.setClickable(false);
            layout4.setClickable(false);
            LockQuesPlayer();
            final LinearLayout layout = (LinearLayout) view;
            final TextView tv2 = (TextView) layout.getChildAt(0);
            layout.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.click_on_answer));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (tv2.getTag().equals(quacObject.getAnswer())) {

                        layout.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.correct_answer_button));
                        tv2.setTextColor(ContextCompat.getColor(PlayGame.this, R.color.white));
                        if (getQuacCount == 14) {
                            CallWonGameActivity();
                            return;
                        }
                        if(getQuacCount==7)
                            ThreeLakh20Player();
                        else
                            RightAnsPlayer();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                CallRewardActivity(layout);
                            }
                        }, soundOnOff ? 2000 : 0);


                    } else {
                        tv2.setTextColor(ContextCompat.getColor(PlayGame.this, R.color.white));
                        layout.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.wrong_answer_button));
                        WrongAnswerResponse();

                    }

                }
            }, 2000);
        }

    }

    public void SetData() {

        layout1.setClickable(true);
        layout2.setClickable(true);
        layout3.setClickable(true);
        layout4.setClickable(true);
        if (getQuacCount == 3) {
            if (language.equals(String.valueOf(R.string.language_english)))
                data = QuestionSet2.getAllDataFromSet2();
            else
                data = HindiQuestionSet2.getAllDataFromHindiSet2();
        }
        if (getQuacCount == 8) {
            if (language.equals(String.valueOf(R.string.language_english)))
                data = QuestionSet3.getAllDataFromHindiSet3();
            else
                data = HindiQuestionSet3.getAllDataFromHindiSet3();
        }
        randomSelection = random.nextInt(data.size());
        while (data.get(randomSelection).getQuestionAsked()) {
            randomSelection = random.nextInt(data.size());
        }
        getQuacCount++;
        quacObject = data.get(randomSelection);
        quacObject.setQuestionAsked(true);
        Log.i("dataSet", quacObject.getQuestion());
        question.setText(quacObject.getQuestion());
        option1.setText("A. " + quacObject.getOption1());
        option2.setText("B. " + quacObject.getOption2());
        option3.setText("C. " + quacObject.getOption3());
        option4.setText("D. " + quacObject.getOption4());
        option1.setTag(quacObject.getOption1());
        option2.setTag(quacObject.getOption2());
        option3.setTag(quacObject.getOption3());
        option4.setTag(quacObject.getOption4());
        isHandlerActive=true;

    }


    public void WrongAnswerResponse() {
        if (option1.getTag().equals(quacObject.getAnswer()))
            layout1.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.correct_answer_button));
        else if (option2.getTag().equals(quacObject.getAnswer()))
            layout2.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.correct_answer_button));
        else if (option3.getTag().equals(quacObject.getAnswer()))
            layout3.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.correct_answer_button));
        else
            layout4.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.correct_answer_button));

        WrongAnsMenuOption();

    }

    private void WrongAnsMenuOption() {
        TextView cashWon;
        isHandlerActive=false;
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_wrong_answer);
        cashWon = dialog.getWindow().findViewById(R.id.cash_prize_won);
        exitOnWrongAnswer = dialog.findViewById(R.id.exit_from_wrong);
        Button okButton = dialog.findViewById(R.id.okButtonInWrongAnswer);
        Button watchAd = dialog.findViewById(R.id.watch_ad);
        TextView wrongAnsText = dialog.findViewById(R.id.text_wrong_answer);
        final TopEntity entity = new TopEntity();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        nameText = dialog.getWindow().findViewById(R.id.name_on_cheque_wrong_ans);

        if (priceCount >= 12) {
            {
                cashWon.setText("You won: ₹ 0");
                entity.price = "₹ 0";
                entity.rank =15;
            }
        } else if (priceCount >= 7) {
            {
                cashWon.setText("You won: ₹ 10,000");
                entity.price = "₹ 10,000";
                entity.rank =13;
            }
        } else if (priceCount >= 0) {
            {
                cashWon.setText("You won: ₹ 3,20,000");
                entity.price = "₹ 3,20,000";
                entity.rank =8;
            }
        } else
        {
            cashWon.setText("You won: ₹ 7 Crores");
            entity.price = "₹ 7 Crores";
            entity.rank =1;
        }

        if(getQuacCount<8&&watchAdCount==0)
        {
            watchAd.setVisibility(View.VISIBLE);
            watchAdCount++;
            wrongAnsText.setText(getString(R.string.wrongAnsAd));
        }
        else
        {
            watchAd.setVisibility(View.GONE);
            wrongAnsText.setText(getString(R.string.wrongAns));
        }

        watchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdSelection = "Main";
               if(rewardedVideoMainFacebook!=null &&rewardedVideoMainFacebook.isAdLoaded())
               {
                   rewardedVideoMainFacebook.show();
                    internetToLoadAd = true;
                }

            if(!isNetworkAvailable()) {
                Toast.makeText(PlayGame.this,"No Internet Connection!!!",Toast.LENGTH_SHORT).show();
            }
            else if(isNetworkAvailable()&&internetToLoadAd)
            {
                internetToLoadAd = false;
            }
            }
        });



        WrongAnsPlayer();

        exitOnWrongAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = nameText.getText().toString();
                if(!text.matches("")) {
                    entity.name = text;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    AdOnLostMethodInterstitial();
                    PlayGame.this.finish();
                }
                else {
                    AdOnLostMethodInterstitial();
                    PlayGame.this.finish();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = nameText.getText().toString();
                if(!text.matches("")) {
                    entity.name = text;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    SendDataToDatabase(entity);
                    AdOnLostMethodInterstitial();
                    PlayGame.this.finish();
                }
                else {
                    Toast.makeText(PlayGame.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.setCanceledOnTouchOutside(false);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(dialog!=null)
                    dialog.show();
            }
        }, soundOnOff ? 1500 : 0);
    }

    private void AdOnLostMethodInterstitial() {
        if(onLostInterstitialAdF!=null&&onLostInterstitialAdF.isAdLoaded())
        {
            onLostInterstitialAdF.show();
        }
                   /* if(mInterstitialAd.isLoaded())
                        mInterstitialAd.show(); */
    }


    public void SetRecyclerViewRewardActivity() {
        dlg = new Dialog(this);
        dlg.setContentView(R.layout.activity_of_reward_info);
        Window window = dlg.getWindow();
        window.setGravity(Gravity.RIGHT);
        rewardInfoRecyclerView = dlg.findViewById(R.id.reward_info_recycler_view);
        rewardLayoutmanager = new LinearLayoutManager(dlg.getContext());
        rewardInfoRecyclerView.setLayoutManager(rewardLayoutmanager);
        rewardInfoRecyclerView.setNestedScrollingEnabled(false);
        rewardAdapter = new RewardAdapter(this, RewardPrices.getPrice());
        rewardInfoRecyclerView.setAdapter(rewardAdapter);
    }

    public void CallRewardActivity(final LinearLayout layout) {

     /*   if (getQuacCount == 1 || priceCount == 15) {
            SetRecyclerViewRewardActivity();

        } */
     showRewardAll.setVisibility(View.VISIBLE);
     showRewardAll.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(dlg.isShowing())
                 dlg.dismiss();
             else
                 dlg.show();
         }
     });


     SetRecyclerViewRewardActivity();
        if(priceCount>0)
        rewardAdapter.ChangeColorOfPrice(priceData.get(priceCount--));

        Log.i("PriceCount",String.valueOf(priceCount));
        rewardAdapter.notifyDataSetChanged();
        dlg.setCanceledOnTouchOutside(false);
        dlg.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dlg.getWindow().getAttributes().windowAnimations = R.style.PriceDialogAnimation;

        if(dlg!=null&&!isFinishing())
            dlg.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HideTextQuestionAnswer();
                if(dlg!=null&&dlg.isShowing()&&!isFinishing())
                {
                    try{
                        dlg.dismiss();
                    }
                    catch (Exception e)
                    {
                        Log.i("dismissDialogError",e+"\t Dismiss Dialog Error");
                    }

                }

                layout.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.main_button_gradient));
                    NextQuesPlayer();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SetData();
                        if(getQuacCount<8) {
                            CountDownTimerCall(45000);
                        }
                        else
                            countDownText.setVisibility(View.INVISIBLE);
                    }
                }, soundOnOff ? 3000 : 0);

            }
        }, 2000);


    }

    private void CountDownTimerCall(long value) {
        countDownTimer = new CountDownTimer(value,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownText.setText(String.valueOf(millisUntilFinished/1000));
                timerCount = (millisUntilFinished/1000);
                Log.i("timerCount2",String.valueOf(timerCount));
            }

            @Override
            public void onFinish() {
                    TimesUpDialog();
            }
        };
        countDownTimer.start();
    }

    public void CallWonGameActivity() {
        wonDialog = new Dialog(this);
        wonDialog.setContentView(R.layout.you_won);
        wonDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        wonDialog.setCancelable(false);
        wonDialog.setCanceledOnTouchOutside(false);
        Button okButtonInYouWon = wonDialog.findViewById(R.id.okButtonInYouWon);
        final EditText name = wonDialog.findViewById(R.id.name_on_cheque_wrong_ans_in_you_won);
        Button youWOnButton = wonDialog.findViewById(R.id.main_menu_in_you_won);
        youWOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = name.getText().toString();
                if(!text.matches("")) {
                    TopEntity entity = new TopEntity();
                    entity.name =text;
                    entity.price = "₹ 7 Crore";
                    entity.rank = 1;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    SendDataToDatabase(entity);
                    wonDialog.dismiss();
                    PlayGame.this.finish();
                }
                else
                {
                    wonDialog.dismiss();
                    PlayGame.this.finish();
                }
            }
        });

        okButtonInYouWon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = name.getText().toString();
                if(!text.matches("")) {
                    TopEntity entity = new TopEntity();
                    entity.name =text;
                    entity.price = "₹ 7 Crore";
                    entity.rank = 1;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    SendDataToDatabase(entity);
                    wonDialog.dismiss();
                    PlayGame.this.finish();
                }
                else
                {
                    Toast.makeText(PlayGame.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                }
            }
        });


        YouWonPlayer();
        wonDialog.show();
    }


    @Override
    public void finish() {

        super.finish();
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }



   public void FiftyFiftyFunctionality(View view)
    {
        if(isHandlerActive)
        {

            view.setClickable(false);
            view.setBackground(ContextCompat.getDrawable(this,R.drawable.wrong_answer_button));

            if(soundOnOff)
                LifeLinePlayer();

            int count=0,previous=-1;
            randomSelection = random.nextInt(4);
            while(!opt.get(randomSelection).getTag().equals(quacObject.getAnswer()))
                randomSelection = random.nextInt(4);;
            randomSelection = (randomSelection+1)%4;
            opt.get(randomSelection).setText("");
            randomSelection = (randomSelection+1)%4;
            opt.get(randomSelection).setText("");



        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void AudiencePollFunctionality(View view)
    {

        if(isHandlerActive) {
            final long timeValue = Long.valueOf(countDownText.getText().toString());
        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }
            if(soundOnOff)
                LifeLinePlayer();
            AudiencePollPlayer();
            view.setClickable(false);
            view.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_button));
            int num = 100, randomO, count = 0,i=0;
            int[] previous = {-1,-1};
            final Dialog apfDialog = new Dialog(this);
            apfDialog.setContentView(R.layout.audience_poll);
            apfDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            Button okButton = apfDialog.findViewById(R.id.okInAudienePoll);
            apfDialog.setCancelable(false);
            TextView apf1, apf2, apf3, apf4;
            float val1=0,val2=0,val3=0,val4=0;
            ArrayList<getAudiencePoll> apf = new ArrayList<>();
            ArrayList<String> optionsName = new ArrayList<>();
            optionsName.add("Option A: ");
            optionsName.add("Option B: ");
            optionsName.add("Option C: ");
            optionsName.add("Option D: ");


            apf1 = apfDialog.findViewById(R.id.audience_poll_op1);
            apf2 = apfDialog.findViewById(R.id.audience_poll_op2);
            apf3 = apfDialog.findViewById(R.id.audience_poll_op3);
            apf4 = apfDialog.findViewById(R.id.audience_poll_op4);

            getAudiencePoll a1=new getAudiencePoll(),a2=new getAudiencePoll(),a3=new getAudiencePoll(),a4 = new getAudiencePoll();
            apf.add(a1);apf.add(a2);apf.add(a3);apf.add(a4);

           /* apf.add(apf1);
            apf.add(apf2);
            apf.add(apf3);
            apf.add(apf4); */

            randomSelection = random.nextInt(4);
            randomO = 40 + random.nextInt(20);
            num -= randomO;
            while (!opt.get(randomSelection).getTag().equals(quacObject.getAnswer()))
                randomSelection = random.nextInt(4);
            //apf.get(randomSelection).setText(String.valueOf(random));
            apf.get(randomSelection).setVal(randomO);
            Log.i("randomO", String.valueOf(randomO));

            randomSelection = random.nextInt(4);
            while (count < 3) {
                if (opt.get(randomSelection).getTag().equals(quacObject.getAnswer())) {
                    randomSelection = random.nextInt(4);
                    continue;
                }
                if (count == 2) {
                    if(randomSelection!=previous[0]&&randomSelection!=previous[1])
                    {
                       // apf.get(randomSelection).setText(optionsName.get(randomSelection)+String.valueOf(num)+"%");
                        apf.get(randomSelection).setVal(num);
                        Log.i("randomO", String.valueOf(num));
                        break;
                    }
                    randomSelection = random.nextInt(4);
                    continue;
                }
                if (previous[0] == randomSelection) {
                    randomSelection = random.nextInt(4);
                    continue;
                }

                previous[i++] = randomSelection;
                randomO = random.nextInt(num / 2);
                num -= randomO;
                //apf.get(randomSelection).setText(optionsName.get(randomSelection)+String.valueOf(randomO)+"%");
                apf.get(randomSelection).setVal(randomO);
                Log.i("randomO", String.valueOf(randomO));

                count++;
                randomSelection = random.nextInt(4);

            }

            BarChart chart = apfDialog.findViewById(R.id.barchart);
            ArrayList NoOfEmp = new ArrayList();

            NoOfEmp.add(new BarEntry(apf.get(0).getVal(), 0));
            NoOfEmp.add(new BarEntry(apf.get(1).getVal(), 1));
            NoOfEmp.add(new BarEntry(apf.get(2).getVal(), 2));
            NoOfEmp.add(new BarEntry(apf.get(3).getVal(), 3));


            ArrayList year = new ArrayList();

            year.add("A");
            year.add("B");
            year.add("C");
            year.add("D");

            BarDataSet bardataset = new BarDataSet(NoOfEmp, "Options");
            chart.animateY(5000);
            BarData data = new BarData(year, bardataset);
            bardataset.setColor(getColor(R.color.white));
            bardataset.setHighLightColor(getColor(R.color.white));
            bardataset.setBarShadowColor(getColor(R.color.white));
            bardataset.setValueTextColor(getColor(R.color.white));
            bardataset.setValueTextSize(15);
            bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
            chart.setData(data);
            chart.setDescription("");
            chart.setBorderColor(getColor(R.color.golden));
            chart.setDrawGridBackground(false);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int qCount = getQuacCount;

                    if(qCount<=7) {
                        TikTikSoundPlayer();
                        CountDownTimerCall(timeValue*1000);
                    }
                    else
                        SuspencePlayer();
                    apfDialog.dismiss();
                }
            });

            apfDialog.show();
        }

    }

    public void QuitMatch(View view)
    {
        if(isHandlerActive) {
            WantToQuitDialog();
        }
    }

    public void FlipQuestionFunctionality(View view)
   {
       if(isHandlerActive)
       {
           isTimerContinue = false;
           if(countDownTimer!=null)
                countDownTimer.cancel();
           if(soundOnOff)
           {
               if(getQuacCount<8)
                   TikTikSoundPlayer();
               else
                   LifeLinePlayer();
           }

           view.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_button));
           view.setClickable(false);
           CountDownTimerCall(45000);
           getQuacCount--;
           SetData();
       }
   }

   public void PhoneOfFriendFunctionality(View view)
   {
       if(isHandlerActive) {

           final long value = Long.valueOf(countDownText.getText().toString());
           if(countDownTimer!=null)
                countDownTimer.cancel();
           if(soundOnOff)
               LifeLinePlayer();
           view.setClickable(false);
           view.setBackground(ContextCompat.getDrawable(this, R.drawable.wrong_answer_button));
           pofDialog = new Dialog(this);
           pofDialog.setContentView(R.layout.phone_of_friend);
           pofDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
           //TextView pof = pofDialog.findViewById(R.id.phone_of_friend_tv);
           TextView pof = pofDialog.findViewById(R.id.phone_of_friend_tv);
           pofDialog.setCancelable(false);
           ImageView telephone = pofDialog.getWindow().findViewById(R.id.telephone_in_pof);
           Button okButton = pofDialog.findViewById(R.id.pofOkButton);
                pof.setText(quacObject.getAnswer());



            telephone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        AdSelection = "PhoneAd";


                    if(!isNetworkAvailable()) {
                        Toast.makeText(PlayGame.this,"No Internet Connection!!!",Toast.LENGTH_SHORT).show();
                    }
                    else if(isNetworkAvailable()&&internetToLoadAd)
                    {
                        internetToLoadAd = false;
                    }


                }
            });

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qCount = getQuacCount;
                    Log.i("countTime",String.valueOf(value));

                    if(qCount<=7) {
                        TikTikSoundPlayer();
                        CountDownTimerCall(value*1000);
                    }
                    else
                        SuspencePlayer();
                    pofDialog.dismiss();
                }
            });

           pofDialog.show();
       }
   }







    public void WantToQuitDialog()
    {
        wantToQuitDialog = new Dialog(this);
        wantToQuitDialog.setContentView(R.layout.want_to_quit);
        quitDialogYes = wantToQuitDialog.findViewById(R.id.quitDialogYes);
        if(!PlayGame.this.isFinishing()) {
         try {
             if(wantToQuitDialog!=null&&!wantToQuitDialog.isShowing())
             wantToQuitDialog.show();
         }
         catch (Exception e)
         {
             Log.i("Error","Dialog Already hidden");
         }
        }
        WantToQuitDialogYes();

    }

    public void WantToQuitDialogYes()
    {

        quitDialogYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(countDownTimer!=null)
                {
                    countDownTimer.cancel();
                    countDownTimer = null;

                }
                ReleaseMediaPlayer();
               final Dialog exitDialog1 = new Dialog(PlayGame.this);
                    exitDialog1.setContentView(R.layout.activity_wrong_answer);
                    exitDialog1.getWindow().setLayout(1000,LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView oops = exitDialog1.getWindow().findViewById(R.id.oops_in_wrong_answer);
                    oops.setText("Quit!!");
                    Button okButton = exitDialog1.findViewById(R.id.okButtonInWrongAnswer);
                    TextView youQuit = exitDialog1.getWindow().findViewById(R.id.text_wrong_answer);
                    youQuit.setText("Add your Name in High Score");
                final TopEntity entity = new TopEntity();
                priceInfo = RewardPrices.getPriceInfo();
                TextView cashWon = exitDialog1.getWindow().findViewById(R.id.cash_prize_won);
                int rank =priceInfo.size()-getQuacCount+1;
                Log.i("valuOfCash",String.valueOf(rank));
                int a=55;
                String cash = (getQuacCount>1)?priceInfo.get(rank):"₹"+String.valueOf(0);
                cashWon.setText("You Won"+cash);
                entity.price = cash;
                exitDialog1.setCancelable(false);
                final EditText nameText = exitDialog1.getWindow().findViewById(R.id.name_on_cheque_wrong_ans);
                    Button exit = exitDialog1.getWindow().findViewById(R.id.exit_from_wrong);
                    if(getQuacCount==2)
                        entity.rank = rank+1;
                    else
                        entity.rank = rank;
                    exitDialog1.setCanceledOnTouchOutside(false);
            exit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String name = nameText.getText().toString();
                            if(!name.matches(""))
                            {
                                entity.name = name;
                                HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                                score.execute(entity);
                                SendDataToDatabase(entity);
                                wantToQuitDialog.dismiss();
                                AdOnLostMethodInterstitial();
                            }
                            else {
                                exitDialog1.dismiss();
                            }
                            PlayGame.this.finish();
                        }
                    });

            okButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String name = nameText.getText().toString();
                            if(!name.matches(""))
                            {
                                entity.name = name;
                                HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                                score.execute(entity);
                                SendDataToDatabase(entity);
                                wantToQuitDialog.dismiss();
                                AdOnLostMethodInterstitial();
                            }
                            else {
                                Toast.makeText(PlayGame.this,"Enter Your Name",Toast.LENGTH_SHORT);
                            }
                            PlayGame.this.finish();
                        }
                    });
            exitDialog1.show();
            }
        });


    }


 public void TimesUpDialog()
    {

                timesUpDialog = new Dialog(PlayGame.this);
                    timesUpDialog.setContentView(R.layout.times_up_dialog);
                    timesUpDialog.getWindow().setLayout(1000,LinearLayout.LayoutParams.WRAP_CONTENT);
                    TextView oops = timesUpDialog.getWindow().findViewById(R.id.oops_in_times_up);
                    Button okButton = timesUpDialog.findViewById(R.id.okButtonInTimesUpDialog);
                    oops.setText("Times Up!!");
                    timesUpDialog.setCancelable(false);
                    final Button timesUpMainMenu = timesUpDialog.findViewById(R.id.exit_from_wrong_in_times_up);
                    TextView cashWon = timesUpDialog.findViewById(R.id.cash_prize_won_in_times_up);
                    TextView youQuit = timesUpDialog.getWindow().findViewById(R.id.text_wrong_answer_in_times_up);
                    youQuit.setText("");
        final TopEntity entity = new TopEntity();

        nameText = timesUpDialog.getWindow().findViewById(R.id.name_on_cheque_wrong_ans_in_times_up);

        if (priceCount >= 12) {
            {
                cashWon.setText("You won: ₹ 0");
                entity.price = "₹ 0";
                entity.rank =15;
            }
        } else if (priceCount >= 7) {
            {
                cashWon.setText("You won: ₹ 10,000");
                entity.price = "₹ 10,000";
                entity.rank =13;
            }
        } else if (priceCount >= 0) {
            {
                cashWon.setText("You won: ₹ 3,20,000");
                entity.price = "₹ 3,20,000";
                entity.rank =8;
            }
        } else
        {
            cashWon.setText("You won: ₹ 7 Crores");
            entity.price = "₹ 7 Crores";
            entity.rank =1;
        }


        if(!isFinishing())
        {
            TimesUpPlayer();
            if(timesUpDialog!=null&&!timesUpDialog.isShowing())
            {
                try {
                    timesUpDialog.show();
                }
                catch (Exception e)
                {
                    Log.i("TimesUpError",e+ "\t this error we got");
                }
            }

        }

        timesUpMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = nameText.getText().toString();
                if(!text.matches("")) {
                    entity.name = text;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    SendDataToDatabase(entity);
                    AdOnLostMethodInterstitial();

                    if(timesUpDialog!=null&&timesUpDialog.isShowing())
                        timesUpDialog.dismiss();
                    PlayGame.this.finish();
                }
                else {

                    AdOnLostMethodInterstitial();

                    if(timesUpDialog!=null&&timesUpDialog.isShowing())
                        timesUpDialog.dismiss();
                    finish();
                }
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = nameText.getText().toString();
                if(!text.matches("")) {
                    entity.name = text;
                    HighScoreAddAsync score = new HighScoreAddAsync(PlayGame.this);
                    score.execute(entity);
                    AdOnLostMethodInterstitial();
                    SendDataToDatabase(entity);

                    if(timesUpDialog!=null&&timesUpDialog.isShowing())
                        timesUpDialog.dismiss();
                    PlayGame.this.finish();
                }
                else {

                    Toast.makeText(PlayGame.this,"Enter Your Name",Toast.LENGTH_SHORT).show();
                }
            }
        });




    }

    private void SendDataToDatabase(TopEntity entity) {

        FirebaseVariable variable=new FirebaseVariable();
        variable.setEntity(entity);
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy");
        //myDate = new Date();
        Date date = new Date();
        variable.setDate(date);
        //String date = timeStampFormat.format(myDate);
        try {


            databaseReference.child(String.valueOf(entity.rank)).child(entity.name).setValue(variable).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.i("TaskState", "SuccessFul");
                    } else {
                        Log.i("TaskState", "Failed");
                    }

                }
            });
        }catch (Exception e)
        {
            Log.i("LOG",e.getMessage());
        }
    }

    public void WantToQuitDialogNo(View view)
    {
        wantToQuitDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        WantToQuitDialog();
    }

    public void HideTextQuestionAnswer()
    {
        question.setText("");
        option1.setText("");
        option2.setText("");
        option3.setText("");
        option4.setText("");
    }















    public void FirstQuesPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.first_question);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    TikTikSoundPlayer();
                }
            });
        }
    }

    public void LockQuesPlayer()
    {
        if(soundOnOff) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.lock_sound);
            mediaPlayer.start();
        }
    }

    public void NextQuesPlayer()
    {
        if(soundOnOff) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            final int qCount = getQuacCount;
            mediaPlayer = MediaPlayer.create(this, R.raw.next_question);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(qCount<7)
                        TikTikSoundPlayer();
                    else
                        SuspencePlayer();
                }
            });
        }
    }

    public void SuspencePlayer()
    {
        if(soundOnOff) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.suspence);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void AudiencePollPlayer()
    {
        if(soundOnOff) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.audience_poll);
            mediaPlayer.start();
        }
    }

    public void WrongAnsPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.wrong_answer);
            mediaPlayer.start();

        }
    }

    public void LifeLinePlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.lifeline);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SuspencePlayer();
                }
            });
        }
    }

    public void RightAnsPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.right_answer);
            mediaPlayer.start();

        }
    }

    public void TikTikSoundPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.tik_tik_sound);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

        }
    }

    public void ThreeLakh20Player()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.three_lakh_20thousan);
            mediaPlayer.start();

        }
    }


    public void TimesUpPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.times_up);
            mediaPlayer.start();

        }
    }

    public void WelcomeThemePlayer()
    {
        if(soundOnOff)
        {


            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this, R.raw.you_won);
            mediaPlayer.start();
        }
    }

    public void YouWonPlayer()
    {
        if(soundOnOff)
        {


            if(mediaPlayer!=null)
            {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            mediaPlayer = MediaPlayer.create(this,R.raw.you_won);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    WelcomeThemePlayer();
                }
            });
        }
    }

    public void ReleaseMediaPlayer()
    {
        if(soundOnOff)
        {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }


    @Override
    protected void onPause() {
        ReleaseMediaPlayer();

        super.onPause();
    }



    @Override
    protected void onStop() {
        if(dialog!=null&&dialog.isShowing())
            dialog.dismiss();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(rewardedVideoMainFacebook!=null)
            rewardedVideoMainFacebook.destroy();
        if(onLostInterstitialAdF!=null)
            onLostInterstitialAdF.destroy();
        if(countDownTimer!=null)
            countDownTimer.cancel();
    }









/*
    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    @Override
    public void onRewarded(RewardItem rewardItem) {



    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }
*/

    @Override
    public void onRewardedVideoCompleted() {
        Log.i("AdSelection",AdSelection);
        if(AdSelection.equals("Main"))
        {
            Log.i("AdSelection",AdSelection+" working 1");
            LinearLayout layout1,layout2,layout3,layout4;
            layout1 = findViewById(R.id.layout_option1);
            layout2 = findViewById(R.id.layout_option2);
            layout3 = findViewById(R.id.layout_option3);
            layout4 = findViewById(R.id.layout_option4);

            layout1.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.main_button_gradient));
            layout2.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.main_button_gradient));
            layout3.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.main_button_gradient));
            layout4.setBackground(ContextCompat.getDrawable(PlayGame.this, R.drawable.main_button_gradient));
            Log.i("AdSelection",AdSelection+" working 1");
            isTimerContinue = false;
            if(countDownTimer!=null)
                countDownTimer.cancel();
            if(soundOnOff)
            {
                if(getQuacCount<8)
                    TikTikSoundPlayer();
                else
                    LifeLinePlayer();
            }

            CountDownTimerCall(45000);
            getQuacCount--;
            SetData();
            if(dialog.isShowing())
                dialog.dismiss();

        }
        else
        {
            if(pofDialog.isShowing())
            {
                TextView pof = pofDialog.findViewById(R.id.phone_of_friend_tv);
                pof.setText(quacObject.getAnswer());
                TextView accuracyText = pofDialog.findViewById(R.id.pofAccuracy);
                accuracyText.setText(getString(R.string.accuracy100));
            }
        }
    }

    @Override
    public void onError(Ad ad, AdError adError) {

    }

    @Override
    public void onAdLoaded(Ad ad) {

    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    @Override
    public void onRewardedVideoClosed() {

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
