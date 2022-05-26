package com.example.safecarrier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class ViewTestActivity extends AppCompatActivity {

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        text=findViewById(R.id.textView);


        //이 activity 에 접속하면 바로 dynamicLink 를 처리
        handleDynamicLink();

    }



    private void handleDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        if (pendingDynamicLinkData == null) { //어플리케이션에서 바로 해당 activity 로 접속했을 경우, 즉 "링크를 통해 접속한게 아닐 경우" -> link 처리 안함
                            System.out.println("어플로 바로 접속");
                            return;
                        }
                        //링크를 통해서 해당 페이지로 접속했을 경우
                        Uri link = pendingDynamicLinkData.getLink();

                        String lid = link.getQueryParameter("lid"); //이 lid 가 API 문서에 적힌 lid 와 동일한 값
                        Intent intent = new Intent(getApplicationContext(), EnterPassword.class);
                        intent.putExtra("lid", lid);
                        startActivity(intent);
                        text.setText(lid); //제대로 값이 가져와졌는지 확인용
                        //1. GET /data/{lid} 로 암호화된 데이터 조회
                        //2. 복호화 -> 복호화 성공시 ""반드시"" GET /data/read/{lid} 호출해서 잔여 조회횟수를 받아와야함
                        //3. 복호화된 데이터, 잔여 조회수로 화면에 적절한 데이터 표시

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
