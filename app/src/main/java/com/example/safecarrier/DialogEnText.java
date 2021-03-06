package com.example.safecarrier;

import static com.example.safecarrier.DialogEn.sharedPreference;
import static com.example.safecarrier.EncryptCode.MakeKey;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.safecarrier.dto.DataDto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.IOException;
import java.util.Random;

public class DialogEnText extends AppCompatActivity {
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;
    private RetrofitClient retrofit;
    public String name_Str = "TEXT";
    public int number;
    static public String numberPassword2 = "text";
    public String encText;
    static public String link2 = "text";
    public String lid; //랜덤문자열
    public Long linkId; //1,2,3 이런 값 (기본키)
    byte[] makekey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_text);
        retrofit = RetrofitClient.getInstance(this).createApi();
        EditText password = (EditText) findViewById(R.id.editTextNumberPassword);
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox) ;
        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    Random rand = new Random();
                    String randNum = "";
                    for(int i=0;i<8;i++) {
                        String ran = Integer.toString(rand.nextInt(10));
                        randNum += ran;
                    }
                    password.setText(randNum);

                }
                else{
                    password.setText("");
                }
            }
        }) ;


    }

    @Override
    public void onResume(){
        super.onResume();
        Button createBtn = (Button)findViewById(R.id.button);
        createBtn.setOnClickListener(t2);


    }
    Button.OnClickListener t2 = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        @RequiresApi(api = Build.VERSION_CODES.P)
        public void onClick(View v) {
            TextView textView= (TextView)findViewById(R.id.editTextTextPersonName);
            EditText times = (EditText) findViewById(R.id.editTextNumber);
            EditText password = (EditText) findViewById(R.id.editTextNumberPassword);
            EncryptCode encryptCode = new EncryptCode();
            number = Integer.parseInt(times.getText().toString());
            String temp =  textView.getText().toString();
            Log.v("test", "text: " + temp);
            try {
               numberPassword2 =  password.getText().toString();
                Log.v("test", "password: " + numberPassword2);
                makekey=MakeKey(numberPassword2);
            } catch (Exception e) {
                e.printStackTrace();
            }
                try {
                 encText = encryptCode.encByKey(makekey, temp+"success");

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            createDynamicLink(new LinkCallback() {
                @Override
                public void onLinkSuccess(String shortLink) {

                    link2=shortLink;

                    DataDto dataDto = new DataDto(new String(encText), "TEXT", number, shortLink, lid, name_Str);
                    System.out.println("link not null, sending data to server");

                    retrofit.postData(dataDto, new RetrofitCallback() {
                        @Override
                        public void onResponseSuccess(int code, Object receivedData) {
                            //암호화된 데이터 조회: DetailResponse 로 캐스팅,  전체 데이터 조회: List<AllResponse> 로 캐스팅,  복호화 성공 알림 후 잔여 조회수 조회: int 로 캐스팅
                            linkId = (Long) receivedData;
                            Log.v("test", "code : "+code);
                            if (code == 200) {
                                Log.v("test", "part6 ");
                                System.out.println("등록 성공");
                                System.out.println("이번에 등록된 링크의 PK (Primary key), 즉 linkId == " + linkId);
                                sharedPreference += linkId.toString()+",";
                                System.out.println("sharedPreference "+sharedPreference);
                                link2 = shortLink;
                                Intent intent = new Intent(getApplicationContext(), Encrypfile.class);
                                startActivity(intent);
                            } else if (code == 409) {
                                System.out.println("**********"+code+"*****************");
                                //이 때의 linkId 는 null
                            } else if (code == 400) {
                                //잘못된 요청
                                System.out.println("*************"+code+"**********");

                            }

                        }

                    });

                    //전역변수 linkId 꺼내가서 백으로 요청

                }
            });

            //sendData
//            link
            //Intent intent = new Intent(getApplicationContext(), Encrypfile.class);
            //startActivity(intent);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String generateRandomLid() {
        //link 에 들어갈 랜덤한 문자열을 생성
        Random random = new Random();
        String generatedString = random.ints(97, 123)
                .limit(12)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        //여기서 생성된 값이 lid, POST /data 때 같이 백으로 보내주어야함
        //String lid="sdfgfdhdfdgs";
        lid = generatedString;
        return generatedString;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Uri generateUrl() {
        return Uri.parse("https://safecarrier.page.link/invite?lid=" + generateRandomLid());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createDynamicLink(LinkCallback callback) {
//        final String[] linkSemi = new String[1];
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(generateUrl())
                .setDomainUriPrefix("https://safecarrier.page.link")
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder(getPackageName()).build())
                .buildDynamicLink();
        Uri longUri = dynamicLink.getUri();   //긴 URI

        System.out.println("long link: "+longUri.toString());
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(longUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            System.out.println("shortLink = " + shortLink);    //짧은 URI -> 이걸 사용자가 공유하도록 함! + POST /data 때 백으로 전송
                            callback.onLinkSuccess(shortLink.toString());
//                            linkSemi[0] = shortLink.toString();
//                            Toast.makeText(getApplicationContext(), (CharSequence) shortLink,Toast.LENGTH_LONG).show();
                        } else {
                            System.out.println("short link fail");
                            System.out.println(task.toString());
                        }
                    }
                });

//        return linkSemi[0];
    }



}
