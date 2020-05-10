package com.example.signlearn.mxcc;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PasswordForgot extends AppCompatActivity {


    EditText rollno,mail,code,password,confirm;
    Button submit,getCode,verify;
    String receivedCode="";


    CompositeDisposable cd = new CompositeDisposable();
    LoadingDiolog loadingDiolog = new LoadingDiolog(PasswordForgot.this);
    NodeJS node;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize nodejs service
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);


        setContentView(R.layout.activity_forgot_password);

        rollno = (EditText)findViewById(R.id.RollNoForgot);
        mail = (EditText)findViewById(R.id.EmailForgot);
        code = (EditText)findViewById(R.id.CodeForgot);
        password=(EditText)findViewById(R.id.passwordForgot);
        confirm=(EditText)findViewById(R.id.confirmForgot);
        submit=(Button) findViewById(R.id.submitPassword);
        getCode=(Button)findViewById(R.id.getCode);
        verify=(Button)findViewById(R.id.verifyCode);

        code.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        verify.setVisibility(View.INVISIBLE);

        listener();

    }

    public void listener()
    {



        getCode.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String roll = rollno.getText().toString().toUpperCase();
                String email = mail.getText().toString().toLowerCase();

                if(roll.isEmpty())
                {
                    rollno.setError("This Field is Required");
                    rollno.requestFocus();
                }
                else if(email.isEmpty())
                {
                    mail.setError("This Field is Required");
                    mail.requestFocus();
                }
                else {



                    loadingDiolog.startLoadingDiolog();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDiolog.dismissDiolog();

                        }
                    }, 2000);

                    cd.add(node.getCode(roll, email)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<String>() {
                                @Override
                                public void accept(String response) throws Exception {


                                    JSONObject data = new JSONObject(response);



                                    if (data.getString("success") == "true") {
                                        rollno.setEnabled(false);
                                        mail.setEnabled(false);
                                        getCode.setVisibility(View.INVISIBLE);
                                        code.setVisibility(View.VISIBLE);
                                        verify.setVisibility(View.VISIBLE);
                                        receivedCode = data.getString("code");
                                        String Codemessage = data.getString("message");
                                        Toast.makeText(PasswordForgot.this, "" + Codemessage, Toast.LENGTH_LONG).show();
                                    } else {

                                        String ErrorMessage = data.getString("message");
                                        Toast.makeText(PasswordForgot.this, "" + ErrorMessage, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }));
                }


            }
        });


        verify.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                String typedCode=code.getText().toString();

                if (typedCode.isEmpty()) {
                    code.setError("This Field is Required");
                    code.requestFocus();
                } else {



                    if (receivedCode.equals(typedCode)) {
                        loadingDiolog.startLoadingDiolog();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                loadingDiolog.dismissDiolog();

                            }
                        }, 1000);


                        code.setEnabled(false);

                        Toast.makeText(PasswordForgot.this, "Code Matched. Enter Your New Password", Toast.LENGTH_LONG).show();

                        verify.setVisibility(View.INVISIBLE);
                        password.setVisibility(View.VISIBLE);
                        confirm.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(PasswordForgot.this, "Code Mismatched", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {




            @Override
            public void onClick(View v) {

               String pwd=password.getText().toString();
                String cfm=confirm.getText().toString();

                if(pwd.isEmpty())
                {
                    password.setError("This Field is Required");
                    password.requestFocus();
                }
                else if (cfm.isEmpty())
                {
                    confirm.setError("This Field is Required");
                    confirm.requestFocus();
                }
                else
                {
                    if(pwd.equals(cfm))
                    {
                        password.setEnabled(false);
                        confirm.setEnabled(false);
                            cd.add(node.changePassword(rollno.getText().toString(),mail.getText().toString(),cfm)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String response) throws Exception {

                                        JSONObject data = new JSONObject(response);
                                        if(data.getString("success")=="true")
                                        {

                                           startActivity(new Intent(PasswordForgot.this, MainActivity.class));
                                            Toast.makeText(PasswordForgot.this,""+data.getString("message"),Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {


                                            Toast.makeText(PasswordForgot.this,""+data.getString("message"),Toast.LENGTH_LONG).show();
                                        }



                                    }
                                }));

                    }
                    else
                    {
                        Toast.makeText(PasswordForgot.this, "Passwords Mismatched", Toast.LENGTH_LONG).show();
                    }
                }




            }
        });

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        rollno.setText("");
        password.setText("");
        getCode.setVisibility(View.VISIBLE);
        code.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.INVISIBLE);
        submit.setVisibility(View.INVISIBLE);
        verify.setVisibility(View.INVISIBLE);

    }
}
