package com.example.sohel.rushinalarm.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.sohel.rushinalarm.Listener.ItemClickListener;
import com.example.sohel.rushinalarm.Model.Sound;
import com.example.sohel.rushinalarm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sohel on 15-09-17.
 */

public class SoundAdapter extends RecyclerView.Adapter<SoundAdapter.SoundHolder>{
    private Context context;
    private List<Sound> soundList;
    private LayoutInflater inflater;
    private ItemClickListener listener;
    private Sound selectedSound;

    public SoundAdapter(Context context,Sound selectedSound) {
        this.context = context;
        this.soundList = initSound();
        this.selectedSound = selectedSound;
        this.inflater = LayoutInflater.from(context);
    }

    private List<Sound> initSound() {
        List<Sound> sounds = new ArrayList<>();
        sounds.add(new Sound("Alarm",R.raw.alarm));
        sounds.add(new Sound("Sweet Alarm",R.raw.sweet_alarm));
        sounds.add(new Sound("Nice Alarm",R.raw.nice_alarm));
        sounds.add(new Sound("Awesome Alarm",R.raw.awesome_alarm));
        sounds.add(new Sound("Rythmic Alarm",R.raw.rythmic_alarm));

        return sounds;
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

        if(selectedSound.getSound_id()==sound.getSound_id()){
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

            selectedSound = soundList.get(getAdapterPosition());
            notifyDataSetChanged();

            if(listener!=null){
                listener.onItemClick(selectedSound);
            }

        }
    }



}
