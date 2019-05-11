package com.sg.hzy.idea.UI;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;

import com.sg.hzy.idea.R;

/**
 * Created by 胡泽宇 on 2018/12/5.
 */

public class MyRadioButton extends AppCompatRadioButton {
    private static final String TAG = "MyCheckBox";
    private int unCheckedResId;
    private int checkedResId;

    public MyRadioButton(Context context) {
        this(context, null);
    }

    public MyRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyRadioButton, defStyleAttr, 0);
        unCheckedResId = ta.getResourceId(R.styleable.MyRadioButton_unCheckedResId, 0);
        checkedResId = ta.getResourceId(R.styleable.MyRadioButton_checkedResId, 0);
        updateView();
        this.setClickable(true);
        this.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.e(TAG, "onCheckedChanged: " + isChecked);
                updateView();
            }
        });

    }

    private void updateView() {
        if (isChecked()) {
            this.setBackgroundResource(checkedResId);
        } else {
            this.setBackgroundResource(unCheckedResId);
        }
    }

    public void setIconResId(int unCheckedResId, int checkedResId) {
        this.unCheckedResId = unCheckedResId;
        this.checkedResId = checkedResId;
        updateView();
    }
}