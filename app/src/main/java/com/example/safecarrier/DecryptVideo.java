package com.example.safecarrier;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class DecryptVideo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지

    }
}
