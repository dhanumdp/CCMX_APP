package com.example.signlearn.mxcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ChangePassword extends Fragment {

    CompositeDisposable cd = new CompositeDisposable();
    NodeJS node;
    SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.changepassword, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);
        sp=getContext().getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        final  String username = sp.getString("Username", null).toUpperCase();
        final EditText newPass=(EditText)view.findViewById(R.id.newPassword);

        Button changePass=(Button)view.findViewById(R.id.changePasswordBtn);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (newPass.getText().toString().isEmpty())
                {
                    newPass.setError("This Field is Required");
                    newPass.requestFocus();
                }
                else {

                    String pass = newPass.getText().toString();
                cd.add(node.newPassword(username,pass)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                String res= response;
                                if(res.equals("Success"))
                                {
                                    Toast.makeText(getContext(), "Password Changed", Toast.LENGTH_SHORT).show();
                                    newPass.setText("");
                                }
                                else
                                {
                                    Toast.makeText(getContext(), "Password not Changed", Toast.LENGTH_SHORT).show();
                                }


                            }
                        }));
                }

            }
        });

    }
}


