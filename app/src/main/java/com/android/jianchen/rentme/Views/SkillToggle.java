package com.android.jianchen.rentme.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

import com.android.jianchen.rentme.R;

/**
 * Created by emerald on 6/2/2017.
 */
public class SkillToggle extends ToggleButton {
    public SkillToggle(Context context) {
        super(context);
        init();
    }

    public SkillToggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setBackgroundResource(R.drawable.skill_color);
        setHeight(50);
        setWidth(FlowLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setSkillTitle(String title) {
        setText(title);
        setTextOff(title);
        setTextOn(title);
    }
}
