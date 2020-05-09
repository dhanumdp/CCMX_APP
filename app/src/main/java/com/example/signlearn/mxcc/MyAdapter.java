package com.example.signlearn.mxcc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

   // String dateArr[],rollno[],systemNo[],complaint[],status[];

    ArrayList<String> dateArr = new ArrayList<String>();
    ArrayList<String> rollno = new ArrayList<String>();
    ArrayList<String> systemNo = new ArrayList<String>();
    ArrayList<String> complaint = new ArrayList<String>();
    ArrayList<String> status = new ArrayList<String>();

    Context context;

    public  MyAdapter(Context ct, ArrayList s1, ArrayList s2, ArrayList s3, ArrayList s4, ArrayList s5){

            context=ct;
          dateArr=s1;
          rollno=s2;
          systemNo=s3;
          complaint=s4;
          status=s5;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.dateText.setText(dateArr.get(position));
        holder.rollnoText.setText(rollno.get(position));
        holder.sysNoText.setText(systemNo.get(position));
        holder.complaintText.setText(complaint.get(position));
        holder.statusText.setText(status.get(position));
    }

    @Override
    public int getItemCount() {
        return status.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{


        TextView dateText, rollnoText, sysNoText, complaintText, statusText;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateOfComplaint);
            rollnoText = itemView.findViewById(R.id.studRollnoTxt);
            sysNoText = itemView.findViewById(R.id.systemNoTxt);
            complaintText = itemView.findViewById(R.id.complaintTexttxt);
            statusText = itemView.findViewById(R.id.complaintStatustxt);
        }
    }
}
