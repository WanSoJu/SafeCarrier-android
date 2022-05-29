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
    String status=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
        Intent intent = getIntent();
        lid=intent.getStringExtra("lid");
        status=intent.getStringExtra("status");

        if(status!=null&&status.equals("fail"))
            Toast.makeText(getApplicationContext(),"잘못된 비밀번호를 입력하셨습니다.",Toast.LENGTH_SHORT).show();

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

                        if(code==200) { //조회된 암호화된 데이터 + 그 데이터의 원본 파일명
                            DetailResponse encryptedData = (DetailResponse) receivedData;
                            //String encryptedString = encryptedData.getEncryptedData(); //암호화된 바이트 -> 이걸 복호화해서 복호화 성공여부 확인해아함
                            //String fileName = encryptedData.getFileName(); //원본 파일명
                            String dataType = encryptedData.getDataType(); //데이터 타입
                            Intent intent2 = new Intent(getApplicationContext(), DecryptImage.class);

                            if(dataType.equals("IMAGE")) {
                                ;
                            } else if(dataType.equals("TEXT")) {
                                intent2 = new Intent(getApplicationContext(), DecryptText.class);
                            } else if(dataType.equals("VIDEO")) {
                                intent2 = new Intent(getApplicationContext(), DecryptVideo.class);
                            } else {

                            }
                             //일단 다 이미지로 가게 처리
                            intent2.putExtra("lid",lid);
                            //intent2.putExtra("encDataString",encryptedString);
                            //intent2.putExtra("fileName",fileName);
                            intent2.putExtra("password",enterPassword.getText().toString());

                            startActivity(intent2);


                        } else if(code==204) { //잘못된 lid 요청 or 삭제된 데이터에 대한 조회 요청
                            Toast.makeText(getApplicationContext(), "잘못된 비밀번호이거나 삭제된 데이터입니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                });

            }
        });
    }


    private long backpressedTime = 0;
    @Override
    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            exitProgram();
        }

    }

    private void exitProgram() {
        finishAffinity();
        System.runFinalization();
        System.exit(0);


    }



}
