package com.example.safecarrier;

import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class DecryptImage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지

    }
}
