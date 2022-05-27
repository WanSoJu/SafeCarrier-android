package com.example.safecarrier;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class DecryptVideo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        VideoView view = findViewById(R.id.videoView);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지

        /**
         * 아래의 url 은 복호화 성공 후, 받아오는 videoUrl 값을 넣어주면 됨
         */

        String url="https://ittasekki-images.s3.ap-northeast-2.amazonaws.com/20220522_124825_1653672802210.mp4";
        Uri uri = Uri.parse(url);
        view.setVideoURI(uri);

        view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });



    }
}
