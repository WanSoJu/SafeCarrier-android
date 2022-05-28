package com.example.safecarrier;

import static com.example.safecarrier.EncryptCode.MakeKey;

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

import java.io.File;
import java.util.Objects;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DialogEnVideo extends AppCompatActivity {
    private final static int FILECHOOSER_NORMAL_REQ_CODE = 0;
    private RetrofitClient retrofit;
    public String name_Str = "first";
    public int number;
    public String type;
    static public String numberPassword3 = "video";
    public Uri uri;
    public String encText;
    static public String link3 = "video";
    public String lid; //랜덤문자열
    Bitmap bitmap;
    byte[] byteArray;
    public Long linkId; //1,2,3 이런 값 (기본키)
    byte[] makekey;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_en);
        Button testBtn = (Button)findViewById(R.id.button2);
        testBtn.setOnClickListener(t);
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
                numberPassword3 =  password.getText().toString();
                Log.v("test", "part1 " + numberPassword3);
                makekey=MakeKey(numberPassword3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //영상일 경우
            if (Objects.equals(type,"mp4")) {
                String needToEncrypt=lid+"success";
                try {
                    encText = encryptCode.encByKey(makekey, needToEncrypt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //업로드 된 mp4 파일 영상파일로 반환 -> 서버에 업로드
               String vidPath = getFilePath(uri);
                System.out.println("vidPath = " + vidPath);
                File file = new File(vidPath);
                RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);
                MultipartBody.Part body=MultipartBody.Part.createFormData("file",name_Str,requestBody);
                retrofit.uploadFile(body, new RetrofitCallback() {
                    @Override
                    public void onResponseSuccess(int code, Object receivedData) {
//                        System.out.println("code = " + code);

                        if(code==400){
                            System.out.println("Upload Video File Response 400 BAD_REQUEST");
                        }

                        if(code==200){
                            createDynamicLink(new LinkCallback() {
                                @Override
                                public void onLinkSuccess(String shortLink) {

                                    link3=shortLink;
                                    /**
                                     * 위의 needToEncrypt 변수를 암호화해서 DataDto 에 넣어서 전송!
                                     * 복호화 시, 뒤에 success 가 나오면 복호화 성공
                                     */



//                                    DataDto dataDto = new DataDto(new String(encText), "VIDEO", number, shortLink, lid, name_Str);
                                    DataDto dataDto = new DataDto(encText, "VIDEO", number, shortLink, lid, name_Str);

                                    retrofit.postData(dataDto, new RetrofitCallback() {
                                        @Override
                                        public void onResponseSuccess(int code, Object receivedData) {

                                            linkId = (Long) receivedData;
                                            Log.v("test", "code : "+code);
                                            if (code == 200) {
                                                Log.v("test", "part6 ");
                                                System.out.println("등록 성공");
                                                System.out.println("이번에 등록된 링크의 PK (Primary key), 즉 linkId == " + linkId);
                                                link3 = shortLink;

                                                /**
                                                 * 아래는 등록 후 videoUrl 과 잔여 조회회수가 잘 넘어오는지 확인하기 위한 테스트 코드
                                                 * (원래는 복호화 성공 후 호출하는 부분)
                                                 */
//                                                retrofit.alertDecryptSuccessAndGetLeftReadcount(lid, new RetrofitCallback() {
//                                                    @Override
//                                                    public void onResponseSuccess(int code, Object receivedData) throws Exception {
//                                                        if(code==200){
//                                                            ReadCountResponse receivedData2 = (ReadCountResponse) receivedData;
//                                                            Integer leftReadCount = receivedData2.getLeftReadCount();
//                                                            System.out.println("leftReadCount = " + leftReadCount);
//                                                            String videoUrl = receivedData2.getVideoUrl();
//                                                            System.out.println("videoUrl = " + videoUrl);
//                                                        }
//                                                    }
//                                                });


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
                        }
                    }
                });
            }

        }
    };

    //파일 원본 경로 가져옴
    private String getFilePath(Uri data){
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

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
