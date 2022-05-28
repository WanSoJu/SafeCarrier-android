package com.example.safecarrier;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class MyItemView extends LinearLayout {
    TextView textView, textView2;

    public MyItemView(Context context) {
        super(context);
        init(context);
    }

    public MyItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item, this, true);

        textView = findViewById(R.id.filename);
        textView2 = findViewById(R.id.last);
    }

    public void setFile(String file){
        textView.setText(file);
    }
    public void setTime(String time){
        textView2.setText(time);
    }

}
