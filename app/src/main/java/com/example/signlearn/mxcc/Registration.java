package com.example.signlearn.mxcc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Registration extends AppCompatActivity {
    EditText name,rollno,password,email,confirm;
    Button register;
    CompositeDisposable cd = new CompositeDisposable();
    NodeJS node;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //initialize nodejs service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);


        name= (EditText)findViewById(R.id.Name);
        rollno = (EditText)findViewById(R.id.RollNo);
        email = (EditText)findViewById(R.id.Email);
        password = (EditText)findViewById(R.id.password);
        confirm = (EditText)findViewById(R.id.confirm);
        register =(Button)findViewById(R.id.register);

        listener();
    }
    public void listener()
    {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname,srollno,semail,spassword,sconfirm;
                sname=name.getText().toString();
                srollno=rollno.getText().toString();
                semail = email.getText().toString().toLowerCase();
                spassword=password.getText().toString();
                sconfirm=confirm.getText().toString();

                if(sname.isEmpty())
                {
                    name.setError("Please Enter Your Name !!");
                    name.requestFocus();
                }
                else if(srollno.isEmpty()) {
                    rollno.setError("Please Enter Your Roll No !!");
                    rollno.requestFocus();
                }
                else if(semail.isEmpty()) {
                    email.setError("Please Enter Your Email-Id!!");
                    email.requestFocus();
                }
                else if (spassword.isEmpty())
                {
                    password.setError("Please Enter Your Password !!");
                    password.requestFocus();
                }
                else if(sconfirm.isEmpty())
                {
                    confirm.setError("Please Re-Enter Your Password !!");
                    confirm.requestFocus();
                }
                else if(!spassword.equals(sconfirm))
                {
                    confirm.setError("Passwords Won't Matched");
                    confirm.requestFocus();
                }
                else
                {

                    cd.add(node.studentRegister(sname,srollno,semail,sconfirm)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String response) throws Exception {
                                    Toast.makeText(Registration.this,""+response,Toast.LENGTH_LONG).show();
                                }
                            }));
                    startActivity(new Intent(Registration.this,MainActivity.class));




                }



            }
        });
    }
}
