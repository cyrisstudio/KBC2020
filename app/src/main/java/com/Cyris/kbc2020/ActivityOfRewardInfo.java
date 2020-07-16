package com.Cyris.kbc2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.Cyris.kbc2020.AdapterClass.RewardAdapter;
import com.Cyris.kbc2020.AdapterClass.RewardPrices;

public class ActivityOfRewardInfo extends AppCompatActivity {
    RecyclerView rewardInfoRecyclerView;
    LinearLayoutManager rewardLayoutmanager;
    static RewardAdapter rewardAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_of_reward_info);
        Intent intent = getIntent();
        rewardInfoRecyclerView = findViewById(R.id.reward_info_recycler_view);
        rewardLayoutmanager = new LinearLayoutManager(this);
        rewardInfoRecyclerView.setLayoutManager(rewardLayoutmanager);
        rewardInfoRecyclerView.setNestedScrollingEnabled(false);
        rewardAdapter = new RewardAdapter(this, RewardPrices.getPrice());
        rewardInfoRecyclerView.setAdapter(rewardAdapter);
    }

    public static void  Info()
    {
        rewardAdapter.notifyDataSetChanged();
    }


}
