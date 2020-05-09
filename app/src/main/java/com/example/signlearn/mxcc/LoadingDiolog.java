package com.example.signlearn.mxcc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

public class LoadingDiolog {


   Activity activity;
   Context context;

    AlertDialog diolog;

    LoadingDiolog(Activity ct)
    {
        activity=ct;
    }


    void startLoadingDiolog()
    {
        AlertDialog.Builder builder= new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loadingdiolog, null));
        builder.setCancelable(true);
        diolog=builder.create();
        diolog.show();
    }

    void dismissDiolog()
    {
        diolog.dismiss();
    }


}
