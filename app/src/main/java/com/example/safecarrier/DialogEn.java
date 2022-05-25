//원래 dialog로 창을 띄울려고 했으나 정보들이 쉽게 전달이 되지 않아 결국엔 페이지로 만들 수 밖에 없었다는 슬픈 전설
package com.example.safecarrier;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class DialogEn extends AppCompatActivity {
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;
    public String name_Str = "first";
    public int number;
    public String type;
    public String numberPassword;
    public Uri uri;
    Bitmap bitmap;
    byte[] byteArray;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_en);
        Button testBtn = (Button)findViewById(R.id.button2);
        testBtn.setOnClickListener(t);

    }
    Button.OnClickListener t = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, 0);



        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FILECHOOSER_NORMAL_REQ_CODE:
                //fileChooser 로 파일 선택 후onActivityResult 에서 결과를 받아 처리함
                if(resultCode == RESULT_OK) {
                    //파일 선택 완료 했을 경우
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        name_Str = getImageNameToUri(data.getData());
                        type = name_Str.substring(name_Str.length()-3, name_Str.length());
                        uri=data.getData();
                        Log.v("test","part4 "+type);
                        //
                    }
                    else{

                    }} else {
                    //cancel 했을 경우
                }
                break;
            default:
                break;}
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void onResume(){
        super.onResume();
        TextView textView= (TextView)findViewById(R.id.editTextTextPersonName);
        if(name_Str == "first")
            textView.setText("");
        else
            textView.setText(name_Str);
        Button createBtn = (Button)findViewById(R.id.button);
        createBtn.setOnClickListener(t2);


    }
    Button.OnClickListener t2 = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        @RequiresApi(api = Build.VERSION_CODES.P)
        public void onClick(View v) {
            EditText times = (EditText) findViewById(R.id.editTextNumber);
            EditText password = (EditText) findViewById(R.id.editTextNumberPassword);
            number = Integer.parseInt(times.getText().toString());
            numberPassword = password.getText().toString();
            //사진일 경우
            if(Objects.equals(type, "jpg")){
                try {
                    //Uri를 사진 비트맵으로 변환
                    bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(),uri));
                    //비트맵 바이트로 변환
                    byteArray = bitmapToByteArray(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            //텍스트파일이나 동영상일경우 (이게 맞나)
            else if(Objects.equals(type, "txt") || Objects.equals(type, "mp4")){
               //Uri File로 변환
                File file = null;
                FileChannel ch = null;
                file = new File(uri.getPath());


                //File 비트맵으로 변환
                //bitmap = BitmapFactory.decodeFile(file.getPath());
               // Log.v("test","part5 "+bitmap.toString());

                //String result = new String(byteArray);


            }










            //페이지 넘어가기
            //Intent intent = new Intent(getApplicationContext(), Encrypfile.class);
           // startActivity(intent);

        }
    };

    //파일 이름 불러오는 함수
    public String getImageNameToUri(Uri data) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
        return imgName;
    }

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }






}
