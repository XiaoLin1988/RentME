package com.android.emerald.rentme.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.emerald.rentme.R;

/**
 * Created by emerald on 5/30/2017.
 */
public class WorkTimePicker extends LinearLayout {
    private int minHour = 0;
    private int maxHour = 24;

    private int minMinute = 0;
    private int maxMinute = 60;

    private Button hourPlus;
    private EditText hourDisplay;
    private Button hourMinus;

    private Button minutePlus;
    private EditText minuteDisplay;
    private Button minuteMinus;

    private View mView;

    public WorkTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflator.inflate(R.layout.view_timepicker, null);
        this.addView(mView);

        initViewVariables();
    }

    private void initViewVariables() {
        hourPlus = (Button)mView.findViewById(R.id.picker_hour_plus);
        hourDisplay = (EditText)mView.findViewById(R.id.picker_hour_display);
        hourMinus = (Button)mView.findViewById(R.id.picker_hour_plus);

        minutePlus = (Button)mView.findViewById(R.id.picker_minute_plus);
        minuteDisplay = (EditText)mView.findViewById(R.id.picker_minute_display);
        minuteMinus = (Button)mView.findViewById(R.id.picker_minute_minus);


    }


}
