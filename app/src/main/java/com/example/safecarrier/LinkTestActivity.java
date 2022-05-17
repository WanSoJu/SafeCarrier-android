package com.example.safecarrier;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class LinkTestActivity extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_test);

        btn=findViewById(R.id.generateLink);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDynamicLink();
            }
        });


    }

    private String generateRandomLid(){
        //link 에 들어갈 랜덤한 문자열을 생성
        //여기서 생성된 값이 lid, POST /data 때 같이 백으로 보내주어야함
        return "47dsfdfs";
    }

    private Uri generateUrl(){
        return Uri.parse("https://safecarrier.page.link/invite?lid="+generateRandomLid());
    }

    private void createDynamicLink() {
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(generateUrl())
                .setDomainUriPrefix("https://safecarrier.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(getPackageName()).build())
                .buildDynamicLink();
        Uri longUri = dynamicLink.getUri();   //긴 URI


        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(longUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            System.out.println("shortLink = " + shortLink);    //짧은 URI -> 이걸 사용자가 공유하도록 함! + POST /data 때 백으로 전송
                            Toast.makeText(getApplicationContext(), (CharSequence) shortLink,Toast.LENGTH_LONG).show();
                        } else {
//                            Log.w(TAG, task.toString());
                        }
                    }
                });
    }
}
