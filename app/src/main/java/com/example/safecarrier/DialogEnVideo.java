package com.example.safecarrier;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.Random;

public class DialogEnVideo extends AppCompatActivity {
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;
    private RetrofitClient retrofit;
    public String name_Str = "first";
    public int number;
    public String type;
    static public String numberPassword;
    public Uri uri;
    public byte[] encText;
    static public String link;
    public String lid; //랜덤문자열
    Bitmap bitmap;
    byte[] byteArray;
    public Long linkId; //1,2,3 이런 값 (기본키)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_en);
        Button testBtn = (Button)findViewById(R.id.button2);
        testBtn.setOnClickListener(t);
        retrofit = RetrofitClient.getInstance(this).createApi();

    }
    Button.OnClickListener t = new Button.OnClickListener() { //Button.OnclickLisener의 객체생성
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            startActivityForResult(intent, 0);




        }
    };
    //********여기에 Uri 있다아아아아
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FILECHOOSER_NORMAL_REQ_CODE:
                //fileChooser 로 파일 선택 후onActivityResult 에서 결과를 받아 처리함
                if(resultCode == RESULT_OK) {
                    //파일 선택 완료 했을 경우
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Log.v("test","part2"+data.getDataString());
                        name_Str = getImageNameToUri(data.getData());
                        type = name_Str.substring(name_Str.length()-3, name_Str.length());
                        uri=data.getData();
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
            EncryptCode encryptCode = new EncryptCode();
            number = Integer.parseInt(times.getText().toString());
            try {
                numberPassword =  password.getText().toString();
                Log.v("test", "part1 " + numberPassword);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //사진일 경우


            createDynamicLink(new LinkCallback() {
                @Override
                public void onLinkSuccess(String shortLink) {

                    link=shortLink;

                    DataDto dataDto = new DataDto(new String(encText), "VIDEO", number, shortLink, lid, name_Str);
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
                                link = shortLink;
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