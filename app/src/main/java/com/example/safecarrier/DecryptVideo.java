package com.example.safecarrier;

import static com.example.safecarrier.EncryptCode.MakeKey;
import static com.example.safecarrier.EncryptCode.byteArrayToHexaString;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.safecarrier.dto.DetailResponse;

import javax.crypto.BadPaddingException;

public class DecryptVideo extends AppCompatActivity {
    EncryptCode encryptCode = new EncryptCode();
    private RetrofitClient retrofit;
    TextView leftRead;
    String encryptedString;
    String fileName;
    String dataType;
    String password;
    String decString;
    byte[] makekey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        retrofit = RetrofitClient.getInstance(this).createApi();
        VideoView view = findViewById(R.id.videoView);
        leftRead=(TextView) findViewById(R.id.leftReadVideo);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지

        /**
         * 아래의 url 은 복호화 성공 후, 받아오는 videoUrl 값을 넣어주면 됨
         */
        Intent intent = getIntent();
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
                    makekey = MakeKey(password);
                    Log.v("test", "makekeyde: " + byteArrayToHexaString(makekey));
                    try{
                        decString=encryptCode.decByKey(makekey,encryptedString);
                    }catch (BadPaddingException e){
                        Intent enterIntent=new Intent(getApplicationContext(),EnterPassword.class);
                        enterIntent.putExtra("lid",lid);
                        enterIntent.putExtra("status","fail");
                        startActivity(enterIntent);
                    }

                    if(decString.contains("success")){
                        decString = decString.substring(0, decString.length() - 7);

                        //여기서부터 조회횟수--
                        retrofit.alertDecryptSuccessAndGetLeftReadcount(lid, new RetrofitCallback() {
                            @Override
                            public void onResponseSuccess(int code, Object receivedData) {
                                //마찬가지로 호출 성공 시 이 부분 실행

                                Integer rc=(Integer) receivedData;
//                            String videoUrl=rc.getVideoUrl();
//                            Log.e("VIDEO URL : ",videoUrl);
                                String videoUrl=decString;
                                Log.e("VIDEO URL : ",videoUrl);
                                Uri uri = Uri.parse(videoUrl);
                                view.setVideoURI(uri);

                                view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                    @Override
                                    public void onPrepared(MediaPlayer mediaPlayer) {
                                        mediaPlayer.start();
                                    }
                                });
                                int leftReadCount = rc; //잔여 조회 횟수를 받아올 수 있음
                                leftRead.setText("   남은 조회 횟수 : "+String.valueOf(leftReadCount));

                                if(code==200)
                                    System.out.println("잔여 조회 횟수 받아옴! 0이면 이번 조회 이후로 즉시 삭제 (더 이상 조회 불가), 아니면 남은 잔여횟수 표시");
                            }
                        });
                    }
                    else{
                        Intent enterIntent=new Intent(getApplicationContext(),EnterPassword.class);
                        enterIntent.putExtra("lid",lid);
                        enterIntent.putExtra("status","fail");
                        startActivity(enterIntent);
//                        failDialog();
                    }

                } else if(code==204) { //잘못된 lid 요청 or 삭제된 데이터에 대한 조회 요청
                    Toast.makeText(getApplicationContext(), "잘못된 비밀번호이거나 삭제된 데이터입니다.", Toast.LENGTH_LONG).show();
                }
            }

        });
/*
        String url="https://ittasekki-images.s3.ap-northeast-2.amazonaws.com/20220522_124825_1653672802210.mp4";
        Uri uri = Uri.parse(url);
        view.setVideoURI(uri);

        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
*/


    }

}
