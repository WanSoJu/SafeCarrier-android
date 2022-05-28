package com.example.safecarrier;


import static com.example.safecarrier.DialogEn.link;
import static com.example.safecarrier.DialogEn.numberPassword;
import static com.example.safecarrier.DialogEnText.link2;
import static com.example.safecarrier.DialogEnText.numberPassword2;
import static com.example.safecarrier.DialogEnVideo.link3;
import static com.example.safecarrier.DialogEnVideo.numberPassword3;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        if(link == "setting" && link3 == "video"){
            System.out.println("link" + link2);
            showLink.append(link2);
        }
        else if(link == "setting" && link2 == "text"){
            System.out.println("link" + link3);
            showLink.append(link3);
        }
        else{
            System.out.println("link" + link);
            showLink.append(link);
        }

        if(numberPassword == "setting" && numberPassword3 == "video"){
            showPassword.append(numberPassword2);
        }
        else if(numberPassword == "setting" && numberPassword2 == "text"){
            showPassword.append(numberPassword3);
        }
        else {
            showPassword.append(numberPassword);
        }
        Button copyLink = (Button)findViewById(R.id.button3);
        copyLink.setOnClickListener(t);
        Button copyPassword = (Button)findViewById(R.id.button4);
        copyPassword.setOnClickListener(t2);
    }

    Button.OnClickListener t = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        public void onClick(View v) {
            TextView textView= (TextView)findViewById(R.id.genlinktextview2);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("CODE", textView.getText().toString().trim()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), "코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();

        }
    };
    Button.OnClickListener t2 = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        public void onClick(View v) {
            TextView textView= (TextView)findViewById(R.id.genlinktextview3);
            ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("CODE", textView.getText().toString().trim()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(getApplicationContext(), "코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();

        }
    };
}
