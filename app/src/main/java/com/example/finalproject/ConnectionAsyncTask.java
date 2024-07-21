package com.example.finalproject;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
public class ConnectionAsyncTask extends AsyncTask<String, String, String> {
    Activity activity;
    public ConnectionAsyncTask(Activity activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        ((MainActivity) activity).setButtonText("connecting");
        super.onPreExecute();
        ((MainActivity) activity).setProgress(true);
    }
    @Override
    protected String doInBackground(String... params) {
        String data = HttpManager.getData(params[0]);
        return data;
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        ((MainActivity) activity).setProgress(false);
        if(s == null){
            ((MainActivity) activity).ConnectionStatus(false);
            ((MainActivity) activity).setButtonText("Try again");
        }
        else {
            ((MainActivity) activity).ConnectionStatus(true);
            ((MainActivity) activity).setButtonText("connected");
            ArrayList<String> types = JsonParser.getObjectFromJson(s);
            ((MainActivity) activity).setTypes(types);
            ((MainActivity) activity).nextActivity();
        }
    }
}
