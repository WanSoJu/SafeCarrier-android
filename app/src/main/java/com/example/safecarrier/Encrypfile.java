package com.example.safecarrier;


import static com.example.safecarrier.DialogEn.link;
import static com.example.safecarrier.DialogEn.numberPassword;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Encrypfile extends AppCompatActivity {
    private RetrofitClient retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("넘어옴");
        setContentView(R.layout.activity_show_link);
        TextView showLink=findViewById(R.id.genlinktextview2);
        TextView showPassword=findViewById(R.id.genlinktextview3);
        System.out.println("link" + link);
        showLink.append(link);
        showPassword.append(numberPassword);
        //showLink.setText(link);
    }
}
