package com.example.sohel.rushinalarm.DialogFragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TimePicker;

import com.example.sohel.rushinalarm.Listener.TimeListener;
import com.example.sohel.rushinalarm.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClockFragment extends DialogFragment implements View.OnClickListener {

    private TimePicker timePicker;
    private Button btnOk,btnCancel;

    private TimeListener listener;




    public ClockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Window window=getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_clock, container, false);

        initView(view);

        return view;
    }

    public void setTimeListener(TimeListener listener){
        this.listener = listener;
    }

    private void initView(View view){
        timePicker = view.findViewById(R.id.time_picker);
        btnOk = view.findViewById(R.id.ok);
        btnCancel = view.findViewById(R.id.cancel);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ok:
                int hour =timePicker.getHour();
                int minute =timePicker.getMinute();

                if(listener!=null){
                    listener.getTime(hour,minute);

                    dismiss();
                }

                break;

            case R.id.cancel:
                dismiss();
                break;
        }

    }
}
