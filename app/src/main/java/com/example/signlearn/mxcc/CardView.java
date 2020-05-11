package com.example.signlearn.mxcc;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CardView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private Timer timer;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm aa" );
    Date date = new Date();
    String formatedDate = dateFormat.format(date);
    String formagedTime = timeFormat.format(date);
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    View header;
    LoadingDiolog loadingDiolog = new LoadingDiolog(CardView.this);
        DrawerLayout drawerLayout;
        ActionBarDrawerToggle actionBarDrawerToggle;
        Toolbar toolbar;
        NavigationView navigationView;
        FragmentManager fragmentManager;
        FragmentTransaction fragmentTransaction;
        TextView USERNAME, userRollNo;

        //backendAPI
                 CompositeDisposable cd = new CompositeDisposable();
                 NodeJS node;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("HOME");

        navigationView= findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        actionBarDrawerToggle= new ActionBarDrawerToggle(this , drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        fragmentManager= getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();



        header=navigationView.getHeaderView(0);


        USERNAME=(TextView)header.findViewById(R.id.username);
        userRollNo=(TextView)header.findViewById(R.id.userRollNo);

        sp=getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
        spedit = sp.edit();

        //backendAPI
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);


        loadUser();
        //checkConnectivity();


    }

    @Override
    public void onBackPressed()
    {

        this.moveTaskToBack(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //checkConnectivity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       // checkConnectivity();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.home)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager= getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new MainFragment());
            fragmentTransaction.commit();
            setTitle(item.getTitle().toString().toUpperCase());
        }

        if(item.getItemId() == R.id.attendance)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager= getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new Attendance());
            fragmentTransaction.commit();
            final LoadingDiolog loadingDiolog = new LoadingDiolog(CardView.this);
            loadingDiolog.startLoadingDiolog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDiolog.dismissDiolog();
                }
            },1000);
            setTitle(item.getTitle().toString().toUpperCase());
        }
        if(item.getItemId()== R.id.addComplaint)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager= getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new Complaint());
            fragmentTransaction.commit();
            setTitle(item.getTitle().toString().toUpperCase());
        }

        if(item.getItemId()== R.id.viewComplaint)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager= getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new ViewComplaint());
            fragmentTransaction.commit();
            final LoadingDiolog loadingDiolog = new LoadingDiolog(CardView.this);
            loadingDiolog.startLoadingDiolog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDiolog.dismissDiolog();
                }
            },1000);
            setTitle(item.getTitle().toString().toUpperCase());
        }


        if(item.getItemId()==R.id.logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Are you sure want to Logout ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    spedit.clear();
                    spedit.commit();
                    startActivity(new Intent(CardView.this, MainActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                }
            });
            builder.show();

        }


        return true;
    }

    public  void loadUser()
    {
        final  String username = sp.getString("Username", null).toUpperCase();
        userRollNo.setText(username);

        cd.add(node.getStudentDetails(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        String res= response;

                        USERNAME.setText(res);

                    }
                }));
    }

//    public void checkConnectivity()
//    {
//        if(Settings.Secure.getInt(getApplicationContext().getContentResolver(),"mobile_data",0) == 1)
//        {
//            Toast.makeText(CardView.this, "Connected", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//
//            Toast.makeText(CardView.this, "Turn On Your Mobile Data", Toast.LENGTH_SHORT).show();
//            loadingDiolog.startLoadingDiolog();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//                @Override
//                public void run() {
//                    loadingDiolog.dismissDiolog();
//                    LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
//                    logoutTimeTask.run();
//                }
//            },2000);
//        }

//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        if(wifiManager.isWifiEnabled())
//        {
//            WifiInfo info = wifiManager.getConnectionInfo();
//            String ssid = info.getSSID();
//            if(ssid.equals("Dhanu"))
//            {
//                rollno.setEnabled(false);
//                password.setEnabled(false);
//                login.setEnabled(false);
//                Toast.makeText(MainActivity.this, "You are Connected to our Dept Wifi. Please Connect it and Restart the App", Toast.LENGTH_LONG).show();
//            }
//            else
//            {
//                rollno.setEnabled(true);
//                password.setEnabled(true);
//                login.setEnabled(true);
//                Toast.makeText(MainActivity.this, "Connected to "+ssid, Toast.LENGTH_SHORT).show();
//            }
//
//        }
//        else
//        {
//            Toast.makeText(MainActivity.this, "Turn On your Wifi", Toast.LENGTH_SHORT).show();
//        }//  }


    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        Log.i("Main", "Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask,3600000);
    }
    private class LogOutTimerTask extends TimerTask {

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void run() {
            spedit.clear();
            spedit.commit();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory( Intent.CATEGORY_HOME );
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        }
    }
}
