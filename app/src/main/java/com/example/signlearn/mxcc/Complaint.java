package com.example.signlearn.mxcc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Complaint extends Fragment {

    EditText croll,csysno,ccomplaint;
    TextView dateOfComplaint;
    Button acomplaint;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("hh:mm aa" );
    Date date = new Date();
    String formatedDate = dateFormat.format(date);
    String formagedTime = timeFormat.format(date);
    CompositeDisposable cd = new CompositeDisposable();



    NodeJS node;
    SharedPreferences sp;
    SharedPreferences.Editor spedit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.complaint, container, false);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dateOfComplaint=(TextView)view.findViewById(R.id.complaintDate);
        csysno=(EditText)view.findViewById(R.id.systemNoEdit);
        ccomplaint=(EditText)view.findViewById(R.id.complaintTextEdit);
        acomplaint=(Button)view.findViewById(R.id.btnComplaint);

        dateOfComplaint.setText(formatedDate);

        sp=getContext().getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        spedit = sp.edit();
        final  String username = sp.getString("Username", null).toUpperCase();


        //backendAPI
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);





            acomplaint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String sysno = csysno.getText().toString();
                    final String complaint=ccomplaint.getText().toString();


                    if (sysno.isEmpty()) {
                        csysno.setError("Please Fill in this Field");
                        csysno.requestFocus();
                    } else if (complaint.isEmpty()) {
                        ccomplaint.setError("Please Fill in this Field");
                        ccomplaint.requestFocus();
                    } else {

                        cd.add(node.complaint(formatedDate, username, sysno, complaint, "Pending")
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<String>() {
                                    @Override
                                    public void accept(String response) throws Exception {

                                        Toast.makeText(getContext(), "Complaint Added Successfully", Toast.LENGTH_LONG).show();
                                        csysno.setText("");
                                        ccomplaint.setText("");
                                    }

                                }));

                    }
                }
            });





    }
}
