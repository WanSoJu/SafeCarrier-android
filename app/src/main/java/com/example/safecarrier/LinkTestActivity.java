package com.example.safecarrier;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safecarrier.dto.DataDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.HashMap;

public class LinkTestActivity extends AppCompatActivity {

    //Retrofit 가져와서 사용! (여기에 있는 API 호출)
    private RetrofitClient retrofit;

    Button btn;
    Button sendData;
    TextView showLink;
    TextView showResponse;
    HashMap<String, String> link=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_test);

        //onCreate 들어오자마자, 해당 API 사용할 수 있도록 초기화
        retrofit=RetrofitClient.getInstance(this).createApi();

        btn=findViewById(R.id.generateLink);
        sendData=findViewById(R.id.postdatabutton);
        showLink=findViewById(R.id.genlinktextview);
        showResponse=findViewById(R.id.responseshow);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDynamicLink();
            }
        });

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                byte[] test={'a','b','c','d'};
                //retrofit 데이터 전송 API 사용 예시
                DataDto dataDto = new DataDto(test, "TEXT", 3, link.get("link"), link.get("lid"), "test");
                int code;
                //Retrofit 내에 메소드 구성은 몰라도 되고, "어떤 메소드를 써야하는지만 알면 됨! + 어떤 파라미터를 넣어주면 되고 응답값이 무엇인지 (dto 패키지 밑의 ResponseWithCode 참고)
                retrofit.postData(dataDto, new RetrofitCallback() {
                    @Override
                    public void onResponseSuccess(int code, Object receivedData) {
                        //암호화된 데이터 조회: DetailResponse 로 캐스팅,  전체 데이터 조회: List<AllResponse> 로 캐스팅,  복호화 성공 알림 후 잔여 조회수 조회: int 로 캐스팅
                        Long linkId =(Long) receivedData;
                        showResponse.setText("code: "+code+"\nllinkId: "+linkId);

                    }

                });

            }
        });


    }

    private String generateRandomLid(){
        //link 에 들어갈 랜덤한 문자열을 생성
        //여기서 생성된 값이 lid, POST /data 때 같이 백으로 보내주어야함
        String lid="sdfgfdhdfdgs";
        link.put("lid",lid);
        return lid;
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
                            link.put("link",shortLink.toString());
//                            Toast.makeText(getApplicationContext(), (CharSequence) shortLink,Toast.LENGTH_LONG).show();
                            showLink.setText(shortLink.toString());
                        } else {
//                            Log.w(TAG, task.toString());
                        }
                    }
                });
    }
}
