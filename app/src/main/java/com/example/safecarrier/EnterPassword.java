package com.example.safecarrier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterPassword extends AppCompatActivity {

    DecryptText decryptText;
    EditText enterPassword;
    Button submitPwBtn;
    boolean checkPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);

        try {
            decryptText=new DecryptText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        enterPassword = (EditText) findViewById(R.id.enterPassword);
        submitPwBtn = (Button) findViewById(R.id.submitPw);

        submitPwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(enterPassword.getText().toString().length()!=0) {
                    checkPw = checkPassword();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                }

                if(checkPw==true) { //파일확인창으로 이동
                    Intent intent2 = new Intent(getApplicationContext(), DecryptText.class);
                    startActivity(intent2);
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다", Toast.LENGTH_LONG).show();
                }
                //Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                //startActivity(intent);
            }
        });


    }

    private boolean checkPassword() {
        if(enterPassword.getText().toString().equals("password")) { //password에 디비에서 받아온 패스워드 넣기
            return true;
        } else {
            return false;
        }
    }
}
