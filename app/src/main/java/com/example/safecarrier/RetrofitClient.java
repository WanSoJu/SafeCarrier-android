package com.example.safecarrier;

import android.content.Context;

import com.example.safecarrier.dto.AllResponse;
import com.example.safecarrier.dto.DataDto;
import com.example.safecarrier.dto.DetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private RestApi restApi;
    public String baseUrl="http://52.79.132.248:8080/";
    private static Retrofit retrofit;
    private static Context mContext;

//    public RetrofitClient(){
//        retrofit = new Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(baseUrl)
//            .build();
//    }

    private static class SingletonHolder{
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }

    public static RetrofitClient getInstance(Context context){
        if(context!=null){
            mContext=context;
        }
        return SingletonHolder.INSTANCE;
    }

    private RetrofitClient(Context context){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    public RetrofitClient createApi(){
     restApi= retrofit.create(RestApi.class);
     return this;
    }


    //데이터 업로드 -> DataDto를 만들어서 파라미터로 넘겨주어야함
    public void postData(DataDto dataDto,RetrofitCallback callback){

        restApi.postData(dataDto).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                    System.out.println("success");
                try {
                    callback.onResponseSuccess(response.code(), response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                System.out.println("postData fail");
            }
        });
    }

    //lid 로 암호화된 데이터 수신
    public void getEncryptedData(String lid, RetrofitCallback callback){
        restApi.getDataByLid(lid).enqueue(new Callback<DetailResponse>() {
            @Override
            public void onResponse(Call<DetailResponse> call, Response<DetailResponse> response) {
                try {
                    callback.onResponseSuccess(response.code(), response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<DetailResponse> call, Throwable t) {
                t.printStackTrace();
                System.out.println("get Detail Data By Lid Fail");
            }
        });
    }

    //송신자의 linkId 리스트 (ex. String ld="1,3,5" ) 로 이 송신자가 보낸 모든 데이터 목록 수신
    public void getAllDataBySender(String id, RetrofitCallback callback){
        restApi.getAllData(id).enqueue(new Callback<List<AllResponse>>() {
            @Override
            public void onResponse(Call<List<AllResponse>> call, Response<List<AllResponse>> response) {
                try {
                    callback.onResponseSuccess(response.code(), response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<List<AllResponse>> call, Throwable t) {
                System.out.println("get All Response By Sender Fail");
            }
        });
    }

    //복호화 성공 시, 복호화 성공 여부 알리고 "이번 조회 이후" 잔여 조회횟수 반환
    public void alertDecryptSuccessAndGetLeftReadcount(String lid,  RetrofitCallback callback){
        restApi.getLeftReadCount(lid).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                try {
                    callback.onResponseSuccess(response.code(), response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                System.out.println("Alert Decryption Success and Get Left Read Count Fail");
            }
        });

    }

    public void getLinkByLinkIdPK(Long linkId,  RetrofitCallback callback){
        restApi.getLinkByLinkId(linkId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    callback.onResponseSuccess(response.code(),response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("get Link BY linkId fail");

            }
        });

    }


}
