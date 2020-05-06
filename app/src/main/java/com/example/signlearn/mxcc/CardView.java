package com.example.signlearn.mxcc;

import androidx.annotation.NonNull;
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
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class CardView extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {



    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm aa" );
    Date date = new Date();
    String formatedDate = dateFormat.format(date);
    String formagedTime = timeFormat.format(date);
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    View header;
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


    }

    @Override
    public void onBackPressed()
    {
      //  Toast.makeText(CardView.this, "Press Logout", Toast.LENGTH_LONG).show();
        this.moveTaskToBack(true);
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
            setTitle(item.getTitle().toString().toUpperCase());
        }
        if(item.getItemId()== R.id.complaint)
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            fragmentManager= getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new Complaint());
            fragmentTransaction.commit();
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
            builder.setNegativeButton("No", null);
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

//    public void listner()
//{
//    logout.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            startActivity(new Intent(CardView.this, MainActivity.class));
//        }
//    });
//
//
//
//    print.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//
//            String username = sp.getString("Username", null);
//            String pass = sp.getString("Password", null);
//
//            Toast.makeText(CardView.this,"Username :"+username+" Password : "+pass+". Date : "+formatedDate+" Time : "+formagedTime+".", Toast.LENGTH_LONG).show();
//
//
//
//        }
//    });
//}
}
