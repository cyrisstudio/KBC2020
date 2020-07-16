package com.Cyris.kbc2020.HighScoreTop;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.Cyris.kbc2020.R;
import com.Cyris.kbc2020.topscore.FirebaseVariable;
import com.Cyris.kbc2020.topscore.TopEntity;
import com.Cyris.kbc2020.topscore.TopScoreAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Entity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyTop#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyTop extends Fragment {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    List<TopEntity> topList;
    TopScoreAdapter adapter;
    LinearLayout progressLoadingLayout;
    LinearLayout networkConnection,noRecord;

    public DailyTop() {
        // Required empty public constructor
    }


    public static DailyTop newInstance(String param1, String param2) {
        DailyTop fragment = new DailyTop();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        topList = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_daily_top, container, false);
        networkConnection = view.findViewById(R.id.no_internet_connection);
        noRecord = view.findViewById(R.id.no_item);
        progressLoadingLayout = view.findViewById(R.id.progress_loading_layout);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("TopScoreUpdate");
        recyclerView = view.findViewById(R.id.recyclerview_in_daily_top);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TopScoreAdapter(getContext(),topList,noRecord);
        recyclerView.setAdapter(adapter);
        return view;
    }

    Date date,todayDate;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onStart() {
        super.onStart();
        final SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy");
        todayDate = new Date();
        if(!isNetworkAvailable())
            networkConnection.setVisibility(View.VISIBLE);
        databaseReference.orderByChild("rank").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("entityCheck", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot snapshot1:dataSnapshot.getChildren()) {

                for (DataSnapshot snapshot:snapshot1.getChildren()) {

                    FirebaseVariable firebaseVariable;
                    firebaseVariable = snapshot.getValue(FirebaseVariable.class);
                    try {
                        Log.i("entityCheck", firebaseVariable.getEntity().name);
                        Log.i("entityCheck", firebaseVariable.getEntity().price);
                        Log.i("entityCheck", String.valueOf(firebaseVariable.getEntity().price));
                        date = firebaseVariable.getDate();
                        String date1 = timeStampFormat.format(date);
                        String date2 = timeStampFormat.format(todayDate);
                        Log.i("rankCheck",String.valueOf(firebaseVariable.getEntity().rank));
                        Log.i("dateCheck",date1+"||"+date2);
                        if(date1.equals(date2)&&firebaseVariable.getEntity().rank!=15)
                            topList.add(firebaseVariable.getEntity());
                    } catch (Exception e)
                    {
                        Log.i("entityCheck", e+" error");
                    }

                }
                }

                progressLoadingLayout.setVisibility(View.GONE);
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean isNetworkAvailable() {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}