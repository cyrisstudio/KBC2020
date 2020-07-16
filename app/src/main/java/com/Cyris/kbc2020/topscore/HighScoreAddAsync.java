package com.Cyris.kbc2020.topscore;

import android.content.Context;
import android.os.AsyncTask;

import com.Cyris.kbc2020.MainActivity;

public class HighScoreAddAsync extends AsyncTask<TopEntity,Void,Void> {

    Context context;
    public HighScoreAddAsync(Context ctx)
    {
        context = ctx;
    }

    @Override
    protected Void doInBackground(TopEntity... topEntities) {
        TopDatabase.getInstance(context).daoInterface().Insert(topEntities[0]);
        return null;
    }
}
