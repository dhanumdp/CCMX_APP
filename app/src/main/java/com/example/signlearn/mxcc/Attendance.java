package com.example.signlearn.mxcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.namespace.QName;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class Attendance<Callbacks> extends Fragment  {

    TextView checkin, checkout, dateOf;
    EditText reason;
    Button make;
    CompositeDisposable cd = new CompositeDisposable();
    NodeJS node;
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    DateFormat timeFormat = new SimpleDateFormat("HH:mm aa" );
    Date date = new Date();
    String formatedDate = dateFormat.format(date);
    String formagedTime = timeFormat.format(date);

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    SharedPreferences sp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendance, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
         checkin = (TextView) view.findViewById(R.id.checkinText);
         checkout = (TextView)view.findViewById(R.id.checkoutText);
         dateOf = (TextView)view.findViewById(R.id.attendanceDate);
       reason = (EditText)view.findViewById(R.id.reason);
         dateOf.setText(formatedDate);
         make = (Button)view.findViewById(R.id.btnAttendance);
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);
        sp=getContext().getSharedPreferences("SharedData", Context.MODE_PRIVATE);

        loadAttendance();


        make.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                putAttendance();
            }
        });
    }

    public void loadAttendance()
    {
        final  String username = sp.getString("Username", null).toUpperCase();
                 cd.add(node.getAttendance(formatedDate,username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        JSONArray _resObj = new JSONArray(response);
                        if(_resObj.length() != 0)
                        {
                            JSONObject data = _resObj.getJSONObject(0);
                            checkin.setText(data.getString("checkIn"));
                            checkout.setText(data.getString("checkOut"));
                            reason.setText(data.getString("reason"));
                            reason.setEnabled(false);
                        }
                        else
                        {
                            checkin.setText("");
                            checkout.setText("");
                            reason.setEnabled(true);
                        }

                    }
                }));

    }


   public void putAttendance()
    {
        final  String username = sp.getString("Username", null).toUpperCase();
        final String reasonText = reason.getText().toString();
        if(reasonText.isEmpty())
        {
            reason.setError("Please Enter the Reason");
            reason.requestFocus();
        }
        else
        {
            cd.add(node.putAttendance(formatedDate,username,reasonText,formagedTime)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(String response) throws Exception {
                            if(checkin.getText()=="" && checkout.getText()=="")
                            {
                                checkin.setText(formagedTime);
                                reason.setText(reasonText);
                                reason.setEnabled(false);


                            }
                            else
                            {
                                reason.setEnabled(false);
                                checkout.setText(formagedTime);


                            }
                        }
                    }));
            loadAttendance();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }




}