package com.Cyris.kbc2020.topscore;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.Cyris.kbc2020.R;

import java.util.ArrayList;
import java.util.List;

public class TopScoreAdapter extends RecyclerView.Adapter<TopScoreAdapter.TopScoreViewHolder> {

    Context context;
    List<TopEntity> topList;
    Dialog dialog;
    LinearLayout noRecord;

    public TopScoreAdapter(Context ctx, List<TopEntity> list,LinearLayout layout)
    {
        context = ctx;
        topList = list;
        noRecord = layout;
    }

    @NonNull
    @Override
    public TopScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_score_view_holder,parent,false);
        return new TopScoreViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull TopScoreViewHolder holder, int position) {
        if(position%2==0)
            holder.highScoreParent.setBackgroundColor(context.getColor(R.color.golden));
        else
            holder.highScoreParent.setBackgroundColor(context.getColor(R.color.silver));
        /*if(position==1)
            holder.highScoreParent.setBackgroundColor(context.getColor(R.color.silver));*/
       /* if(position==2)
            holder.highScoreParent.setBackgroundColor(context.getColor(R.color.bronze));*/
        holder.highTopCount.setText(String.valueOf(position+1)+".");
        holder.highScore.setText(topList.get(position).name);
        holder.highScoreCash.setText(topList.get(position).price);
    }

    @Override
    public int getItemCount() {

        if(isNetworkAvailable()) {
            try {
                if (topList.size() == 0 && noRecord != null) {
                    noRecord.setVisibility(View.VISIBLE);
                    Log.i("TopListSize", String.valueOf(topList.size()));
                } else {
                    if (noRecord != null)
                        noRecord.setVisibility(View.GONE);
                }
            }
            catch(Exception e)
            {
                Log.i("TopScoreError","Error in top Score");
            }
        }

        return topList.size();
    }

    class TopScoreViewHolder extends RecyclerView.ViewHolder {

        TextView highScore,highScoreCash,highTopCount;
        LinearLayout highScoreParent;
        public TopScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            highTopCount = itemView.findViewById(R.id.count_high_score_holder);
            highScore = itemView.findViewById(R.id.high_score_view_holder);
            highScoreParent = itemView.findViewById(R.id.top_score_parent_view);
            highScoreCash = itemView.findViewById(R.id.high_score_view_holder_cash);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
