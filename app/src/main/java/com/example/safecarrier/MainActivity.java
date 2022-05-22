package com.example.safecarrier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //파일명을 위한 변수
    public String name_Str = "first";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 권한ID를 가져옵니다
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        // 권한이 열려있는지 확인
        if (permission == PackageManager.PERMISSION_DENIED || permission2 == PackageManager.PERMISSION_DENIED) {
            // 마쉬멜로우 이상버전부터 권한을 물어본다
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 권한 체크(READ_PHONE_STATE의 requestCode를 1000으로 세팅
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000); } return; } }
    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true; // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }
            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {

            } else {
                finish();
            }
        }
    }




//dialog_en 코드
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;
    public void onClick(View view) {

        CustomDialog customDialog = new CustomDialog(MainActivity.this,new CustomDialog.DialogListener(){
            @Override
            public void clickBtn(Intent data) {
                startActivityForResult(data, 0);
            }
        });
        customDialog.show();
        customDialog.FileName=name_Str;
        }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FILECHOOSER_NORMAL_REQ_CODE:
                //fileChooser 로 파일 선택 후 onActivityResult 에서 결과를 받아 처리함
                if(resultCode == RESULT_OK) {
                    //파일 선택 완료 했을 경우
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.d("test", "part1");
                      //  name_Str = getImageNameToUri(data.getData());
                        Log.v("test", "part1" + name_Str);
                    }else{
                    }
                } else {
                    //cancel 했을 경우
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   //public String getImageNameToUri(Uri data) {
     //   String[] proj = { MediaStore.Images.Media.DATA };
       // Cursor cursor = managedQuery(data, proj, null, null, null);
        //int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
       // cursor.moveToFirst();
      //  String imgPath = cursor.getString(column_index);
       // String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);
       // Log.d("test", "part2");
       // return imgName; }

    }






