package com.example.safecarrier;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safecarrier.dto.DetailResponse;

public class EnterPassword extends AppCompatActivity {

    DecryptText decryptText;
    EditText enterPassword;
    Button submitPwBtn;
    String lid;
    private RetrofitClient retrofit;
    boolean checkPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        Intent intent = getIntent();
        lid=intent.getStringExtra("lid");
        retrofit = RetrofitClient.getInstance(this).createApi();
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
                retrofit.getEncryptedData(lid,new RetrofitCallback(){
                    @Override
                    public void onResponseSuccess(int code, Object receivedData) {
                        Toast.makeText(getApplicationContext(), "Hello", Toast.LENGTH_LONG).show();
                        if(code==200) { //조회된 암호화된 데이터 + 그 데이터의 원본 파일명
                            DetailResponse encryptedData = (DetailResponse) receivedData;
                            byte[] encryptedByte = encryptedData.getEncryptedData(); //암호화된 바이트 -> 이걸 복호화해서 복호화 성공여부 확인해아함
                            String fileName = encryptedData.getFileName(); //원본 파일명

                            Intent intent2 = new Intent(getApplicationContext(), DecryptImage.class); //일단 다 이미지로 가게 처리
                            intent2.putExtra("encDataByte",encryptedByte);
                            intent2.putExtra("fileName",fileName);
                            startActivity(intent2);


                        } else if(code==204) { //잘못된 lid 요청 or 삭제된 데이터에 대한 조회 요청
                            Toast.makeText(getApplicationContext(), "잘못된 비밀번호이거나 삭제된 데이터입니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                });
                /*
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

                 */
            }
        });
    }
}
