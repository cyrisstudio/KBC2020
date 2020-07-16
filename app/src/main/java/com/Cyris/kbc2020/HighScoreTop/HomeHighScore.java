package com.Cyris.kbc2020.HighScoreTop;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.Cyris.kbc2020.MainActivity;
import com.Cyris.kbc2020.R;
import com.Cyris.kbc2020.topscore.TopDatabase;
import com.Cyris.kbc2020.topscore.TopEntity;
import com.Cyris.kbc2020.topscore.TopScoreAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeHighScore#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeHighScore extends Fragment {



    private ArrayList<TopEntity> topList;
    LinearLayout progressLoadingLayout;
    LinearLayout networkConnection,noRecord;

    public HomeHighScore() {
        // Required empty public constructor
    }


    public static HomeHighScore newInstance(String param1, String param2) {
        HomeHighScore fragment = new HomeHighScore();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    RecyclerView recyclerView;
    TopScoreAdapter topScoreAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_high_score, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_in_home_high_score);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        noRecord = view.findViewById(R.id.no_item);
        topList = new ArrayList<>();


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new ShowTop().execute();


    }

    public class ShowTop extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            TopDatabase database = TopDatabase.getInstance(getContext());
            topList.clear();
            for (int i = 0; i < database.daoInterface().size(); i++) {
                if(database.daoInterface().getData().get(i).rank!=15)
                topList.add(database.daoInterface().getData().get(i));

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            TopScoreAdapter topScoreAdapter = new TopScoreAdapter(getContext(), topList,noRecord);
            recyclerView.setAdapter(topScoreAdapter);
        }
    }
}