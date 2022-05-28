package com.example.safecarrier;

import static com.example.safecarrier.DialogEn.sharedPreference;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.safecarrier.dto.AllResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //파일명을 위한 변수
    private RetrofitClient retrofit;
    List file_name = new ArrayList();
    List file_count = new ArrayList();
    String test;

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
                // 권한 체크(READ_PHONE_STATE의requestCode를1000으로 세팅
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},1000); }
            return; }


    }
    // 권한 체크 이후로직
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {
        // READ_PHONE_STATE의 권한 체크 결과를 불러온다
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        if (requestCode == 1000) {
            boolean check_result = true;
            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;break;}}
            // 권한 체크에 동의를 하지 않으면 안드로이드 종료
            if (check_result == true) {} else {finish();}}}
    @Override
    public void onResume() {
        /*파일목록 띄우기*/
        super.onResume();
        retrofit = RetrofitClient.getInstance(this).createApi();
        if(sharedPreference==""){

        }
       else{
            String id = sharedPreference.substring(0, sharedPreference.length() - 1);
            System.out.println("id shared: "+id);
            retrofit.getAllDataBySender(id, new RetrofitCallback() {
                @Override
                public void onResponseSuccess(int code, Object receivedData) {
                //이 사용자가 보낸 전체 데이터를 조회하므로, 노션 API 문서 대로 List 로 응답이 옴
                    List<AllResponse> allResponses = (List<AllResponse>) receivedData;
                    for (AllResponse response : allResponses) {
                        String fileName = response.getFileName();
                        file_name.add(fileName);
                        String lid1 = response.getLid();
                        Integer leftReadCount = response.getLeftCount();
                        file_count.add(String.valueOf(leftReadCount));
                        System.out.println("file_count: "+file_count.get(0));
                        Long linkId = response.getLinkId();
                    }
                    if (code == 200){
                        System.out.println("잔여 횟수가 남은 데이터가 없으면: 빈 값이 옴 (null 또는 empty list) / 그 외에는 List<AllResponse> 가 옴");
                       ListView listView = (ListView) findViewById(R.id.listView);
                        MyAdapter adapter = new MyAdapter();
                        for(int i=0; i<file_name.size();i++){
                            adapter.addItem(new MyItem((String)file_name.get(i),(String)file_count.get(i) ));
                        }
                        file_name.clear();
                        file_count.clear();
                        listView.setAdapter(adapter);
                     }}
                 });
        }
    }

    class MyAdapter extends BaseAdapter {

        private ArrayList<MyItem> items = new ArrayList<>();

        public void addItem(MyItem item){
            items.add(item);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public MyItem getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            MyItemView view = new MyItemView(getApplicationContext());

            MyItem item = items.get(position);
            view.setFile(item.getFile());
            view.setTime(item.getTime());

            return view;
        }
    }





//DialogEn 화면 출력
    public void onClick(View view) {
        final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
        getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_menu1){
                    Intent intent = new Intent(getApplicationContext(), DialogEn.class);
                    startActivity(intent);
                }else if (menuItem.getItemId() == R.id.action_menu2){
                    Intent intent2 = new Intent(getApplicationContext(), DialogEnVideo.class);
                    startActivity(intent2);
                }else {
                    Intent intent3 = new Intent(getApplicationContext(), DialogEnText.class);
                    startActivity(intent3);
                }

                return false;
            }
        });
        popupMenu.show();
       // Intent intent = new Intent(getApplicationContext(), DialogEn.class);
       // startActivity(intent);
    }




}






