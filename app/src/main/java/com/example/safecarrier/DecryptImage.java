package com.example.safecarrier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safecarrier.dto.DetailResponse;

public class DecryptImage extends AppCompatActivity {
    EncryptCode encryptCode = new EncryptCode();
    //byte[] decImageByte;
    ImageView imageView;
    TextView tempText;
    private RetrofitClient retrofit;
    String encryptedString;
    String fileName;
    String dataType;
    String password;
    String decString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        retrofit = RetrofitClient.getInstance(this).createApi();

        Intent intent = getIntent();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지
        imageView=(ImageView) findViewById(R.id.imageView);

        String lid=intent.getStringExtra("lid");

        password=intent.getStringExtra("password");

        retrofit.getEncryptedData(lid,new RetrofitCallback(){
            @Override
            public void onResponseSuccess(int code, Object receivedData) throws Exception {

                if(code==200) { //조회된 암호화된 데이터 + 그 데이터의 원본 파일명
                    DetailResponse encryptedData = (DetailResponse) receivedData;
                    encryptedString = encryptedData.getEncryptedData(); //암호화된 string
                    fileName = encryptedData.getFileName(); //원본 파일명
                    dataType = encryptedData.getDataType(); //데이터 타입

                    decString=encryptCode.decByKey(password,encryptedString);
                    Log.e("decCheck",decString);
                    byte[] encodeByte = Base64.decode(decString, Base64.DEFAULT);
                    Toast.makeText(getApplicationContext(),"three", Toast.LENGTH_LONG).show();
                    Bitmap bitmapp = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    imageView.setImageBitmap(bitmapp);


                } else if(code==204) { //잘못된 lid 요청 or 삭제된 데이터에 대한 조회 요청
                    Toast.makeText(getApplicationContext(), "잘못된 비밀번호이거나 삭제된 데이터입니다.", Toast.LENGTH_LONG).show();
                }
            }

        });



    }
}
