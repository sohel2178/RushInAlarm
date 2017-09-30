package com.linearbd.sohel.rushinalarm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.linearbd.sohel.rushinalarm.Listener.ItemClickListener;
import com.linearbd.sohel.rushinalarm.Model.Sound;
import com.linearbd.sohel.rushinalarm.R;
import com.linearbd.sohel.rushinalarm.Utility.SoundHelper;

import java.util.List;

/**
 * Created by sohel on 15-09-17.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder>{
    private Context context;
    private List<Sound> soundList;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    private int selectedSound;

    public SoundAdapter(Context context,int selectedSound) {
        this.context = context;
        SoundHelper soundHelper = new SoundHelper(context);
        this.soundList = soundHelper.getSoundList();
        this.selectedSound = selectedSound;
        this.inflater = LayoutInflater.from(context);
    }


    public void setItemClickListener(ItemClickListener listener){
        this.listener =listener;
    }

    @Override
    public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.sound_single_item,parent,false);

        SoundHolder holder = new SoundHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(SoundHolder holder, int position) {
        Sound sound = soundList.get(position);

        holder.tvName.setText(sound.getName());

        if(selectedSound==sound.getId()){
            holder.radioButton.setChecked(true);
        }else{
            holder.radioButton.setChecked(false);
        }


    }

    @Override
    public int getItemCount() {
        return soundList.size();
    }

    public class SoundHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        RadioButton radioButton;
        TextView tvName;

        public SoundHolder(View itemView) {
            super(itemView);

            radioButton = itemView.findViewById(R.id.radio);
            tvName = itemView.findViewById(R.id.name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            //removeAllCheck();

            //this.radioButton.setChecked(false);

            selectedSound = getAdapterPosition();
            notifyDataSetChanged();

            if(listener!=null){
                listener.onItemClick(selectedSound);
            }

        }
    }



}
