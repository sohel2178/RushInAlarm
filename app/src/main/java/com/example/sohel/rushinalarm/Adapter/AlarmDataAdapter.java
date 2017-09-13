package com.example.sohel.rushinalarm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;

import java.util.List;

/**
 * Created by sohel on 11-09-17.
 */

public class AlarmDataAdapter extends RecyclerView.Adapter<AlarmDataAdapter.AlarmDataHolder> {

    private Context context;
    private List<AlarmData> alarmDataList;
    private LayoutInflater inflater;

    public AlarmDataAdapter(Context context, List<AlarmData> alarmDataList) {
        this.context = context;
        this.alarmDataList = alarmDataList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public AlarmDataHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_alarm,parent,false);

        AlarmDataHolder holder = new AlarmDataHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AlarmDataHolder holder, int position) {

        AlarmData alarmData = alarmDataList.get(position);

        //holder.

    }

    @Override
    public int getItemCount() {
        return alarmDataList.size();
    }


    public class AlarmDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

        TextView tvTime,tvDesc;
        Switch swOffOn;

        public AlarmDataHolder(View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.time);
            tvDesc = itemView.findViewById(R.id.desc);
            swOffOn = itemView.findViewById(R.id.off_on);

            tvTime.setOnClickListener(this);
            tvDesc.setOnClickListener(this);
            swOffOn.setOnCheckedChangeListener(this);
        }

        @Override
        public void onClick(View view) {

        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                Log.d("YYY","ON");
            }else{
                Log.d("YYY","OFF");
            }
        }
    }
}
