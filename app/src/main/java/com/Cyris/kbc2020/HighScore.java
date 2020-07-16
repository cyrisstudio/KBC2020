package com.Cyris.kbc2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.Cyris.kbc2020.HighScoreTop.DailyTop;
import com.Cyris.kbc2020.HighScoreTop.HomeHighScore;
import com.Cyris.kbc2020.HighScoreTop.WeeklyTop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HighScore extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment selectedFragment = null;
    HomeHighScore homeHighScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);
        getSupportActionBar().hide();
        bottomNavigationView = findViewById(R.id.navigation_view);
        selectedFragment = new HomeHighScore();
        getSupportFragmentManager().beginTransaction().replace(R.id.high_score_fragment_place,selectedFragment).commit();

       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {

               switch (item.getItemId())
               {
                   case R.id.home_top_score:
                       selectedFragment  = new HomeHighScore();
                       break;
                    case R.id.daily_top:
                        selectedFragment = new DailyTop();
                        break;
                    case R.id.weekly_top:
                        selectedFragment = new WeeklyTop();
                        break;

               }
               getSupportFragmentManager().beginTransaction().replace(R.id.high_score_fragment_place,selectedFragment).commit();

               return true;
           }
       });
    }
}