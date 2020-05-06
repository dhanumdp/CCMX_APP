package com.example.signlearn.mxcc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.w3c.dom.Node;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    TextView register;
    EditText rollno,password;
    Button login;
    CompositeDisposable cd = new CompositeDisposable();
    NodeJS node;

    //for session handling, here i use shared preferences

    SharedPreferences sp;
    SharedPreferences.Editor spedit;




    @Override
    protected void onStop()
    {
        cd.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();


        //initialize nodejs service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);



        rollno=(EditText)findViewById(R.id.rollno);
        password=(EditText)findViewById(R.id.password);
        register = (TextView)findViewById(R.id.forRegistration);
        login=(Button)findViewById(R.id.login);


        sp=getApplicationContext().getSharedPreferences("SharedData", MODE_PRIVATE);
        spedit = sp.edit();

//        if(!sp.contains("Username") && ! sp.contains("Password"))
//        {
//            Toast.makeText(MainActivity.this, "New User", Toast.LENGTH_SHORT).show();
//        }
         if(sp.contains("Username") &&  sp.contains("Password"))
        {
            startActivity(new Intent(MainActivity.this, CardView.class));
        }

        listener();
    }

    @Override
    public void onBackPressed()
    {
        //  Toast.makeText(CardView.this, "Press Logout", Toast.LENGTH_LONG).show();
        this.moveTaskToBack(true);
    }

    public void listener()
    {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String roll = rollno.getText().toString();
                final  String pass = password.getText().toString();
                if(roll.isEmpty())
                {
                    rollno.setError("Please Enter Your Roll No !!");
                    rollno.requestFocus();
                }
                else if( pass.isEmpty())
                {
                    password.setError("Please Enter Your Password !!");
                    password.requestFocus();
                }
                else
                {
                   cd.add(node.studentLogin(roll,pass)
                   .subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe(new Consumer<String>() {
                       @Override
                       public void accept(String response) throws Exception {
                          String res= response;
                          if(res.equals("Success"))
                          {
                                spedit.putString("Username", roll);
                                spedit.putString("Password", pass);

                                spedit.commit();

                              startActivity(new Intent(MainActivity.this, CardView.class));
                          }
                          else
                              Toast.makeText(MainActivity.this,""+response,Toast.LENGTH_LONG).show();
                       }
                   }));

                }

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Registration.class));
                rollno.setText("");
                password.setText("");
            }
        });


    }


}
