package com.example.safecarrier;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
public class Encrypfile extends AppCompatActivity {
    private Uri uri;
    private int number;
    private String filename;
    private String password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_link);



    }
}
