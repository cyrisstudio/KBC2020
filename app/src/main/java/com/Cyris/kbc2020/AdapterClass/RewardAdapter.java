package com.Cyris.kbc2020.AdapterClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.Cyris.kbc2020.R;

import java.util.ArrayList;

public class RewardAdapter extends RecyclerView.Adapter<RewardAdapter.RewardViewModel> {

    Context context;
    ArrayList<String> rewardPriceData;
   // int changeColorNumber =-1,changeColorCount=15;
    String colorChangePrice;

    public RewardAdapter(Context ctx, ArrayList<String> str)
    {
        this.context = ctx;
        this.rewardPriceData = str;
    }

    public void ChangeColorOfPrice(String str)
    {
        this.colorChangePrice = str;
       // this.changeColorNumber = num1;
    //    changeColorCount--;
    }



    @NonNull
    @Override
    public RewardViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.content_in_reward_layout,parent,false);
        RewardViewModel model = new RewardViewModel(view);
        return model;

    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewModel holder, int position) {
        holder.Bind(position);
    }

    @Override
    public int getItemCount() {
        return rewardPriceData.size();
    }


    public class RewardViewModel extends RecyclerView.ViewHolder
    {
        TextView content_of_recyclerView;
        LinearLayout layout;
        public RewardViewModel(@NonNull View itemView) {
            super(itemView);
            content_of_recyclerView = itemView.findViewById(R.id.data_for_recyclerView);
            layout = itemView.findViewById(R.id.layout_of_reward_content);


        }


        public void Bind(int pos)
        {
           /* if(pos == 4||pos == 9||pos == 15)
            {
                if(changeColorCount == 4 ||changeColorCount == 9 ||changeColorCount == 15  )
                {
                    content_of_recyclerView.setTextColor(Color.BLACK);
                }
                else
                content_of_recyclerView.setTextColor(Color.YELLOW);
            } */
            content_of_recyclerView.setText(rewardPriceData.get(pos));
            content_of_recyclerView.setTag(rewardPriceData.get(pos));
            content_of_recyclerView.setTextColor(Color.WHITE);
            layout.setMinimumWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            layout.setBackgroundColor(Color.TRANSPARENT);
            if(pos == 12||pos==7||pos==0)
            {
                content_of_recyclerView.setTextColor(ContextCompat.getColor(context,R.color.golden));
            }
            if(colorChangePrice.equals(content_of_recyclerView.getTag()))
            {
                content_of_recyclerView.setTextColor(Color.WHITE);
                layout.setBackgroundColor(ContextCompat.getColor(context,R.color.golden));
            }


        }
    }
}
