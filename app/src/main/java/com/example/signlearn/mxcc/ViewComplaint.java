package com.example.signlearn.mxcc;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.signlearn.mxcc.Retrofit.NodeJS;
import com.example.signlearn.mxcc.Retrofit.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ViewComplaint extends Fragment {
    ArrayList<String> dateArr;
    ArrayList<String> rollno;
    ArrayList<String> systemNo;
    ArrayList<String> complaint;
    ArrayList<String> status;




//    public  String dateArr[],rollno[],systemNo[],complaint[],status[];
    CompositeDisposable cd = new CompositeDisposable();
    NodeJS node;
    SharedPreferences sp;
    SharedPreferences.Editor spedit;
    RecyclerView recyclerView;



     //ArrayList<JSONObject> cp = new ArrayList<JSONObject>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.viewcomplaint, container, false);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        dateArr = new ArrayList<>();
        rollno = new ArrayList<>();
        systemNo = new ArrayList<>();
        complaint = new ArrayList<>();
        status = new ArrayList<>();


        sp=getContext().getSharedPreferences("SharedData", Context.MODE_PRIVATE);
        spedit = sp.edit();
        final  String username = sp.getString("Username", null).toUpperCase();

        recyclerView=(RecyclerView)view.findViewById(R.id.studRecycle);

        //backendAPI
        Retrofit retrofitClient = RetrofitClient.getInstance();
        node = retrofitClient.create(NodeJS.class);


        cd.add(node.viewComplaintStud(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                       // Toast.makeText(getContext(), "Complaints are "+response, Toast.LENGTH_LONG).show();
                                            JSONArray _resObj = new JSONArray(response);


                        if(_resObj.length()!=0) {
                            for (int i = 0; i < _resObj.length(); i++)
                            {
                                JSONObject data = _resObj.getJSONObject(i);

                                System.out.println("Complaint Date"+i+" :"+data.getString("date"));

                                //String dateStr= data.getString("date");

                                dateArr.add(i,data.getString("date"));
                                rollno.add(i,data.getString("rollno"));
                                systemNo.add(i,data.getString("systemNo"));
                                complaint.add(i,data.getString("complaint"));
                                status.add(i,data.getString("status"));
                                System.out.println("Date Array "+dateArr.size());
                                //cp.add(data);
                            }
                            MyAdapter myAdapter = new MyAdapter(getContext(),dateArr,rollno,systemNo,complaint,status);
                            recyclerView.setAdapter(myAdapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                          //  Toast.makeText(getContext(), "You Have Made "+_resObj.length()+" Complaints Till Now", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "You Have Not Added Any Complaints Yet.", Toast.LENGTH_SHORT).show();
                        }
                    }

                }));




    }

}
