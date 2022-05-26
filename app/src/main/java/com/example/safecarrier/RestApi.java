package com.example.safecarrier;

import com.example.safecarrier.dto.AllResponse;
import com.example.safecarrier.dto.DataDto;
import com.example.safecarrier.dto.DetailResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApi {
    @Headers("Cache-control: no-cache")
    @POST("data")
    Call<Long> postData(@Body DataDto dataDto);

    @Headers("Cache-control: no-cache")
    @GET("data/{lid}")
    Call<DetailResponse> getDataByLid(@Path("lid") String lid);

    @Headers("Cache-control: no-cache")
    @GET("data")
    Call<List<AllResponse>> getAllData(@Query("id") String id);

    @Headers("Cache-control: no-cache")
    @GET("data/read/{lid}")
    Call<Integer> getLeftReadCount(@Path("lid") String lid);

    @Headers("Cache-control: no-cache")
    @GET("data/link/{linkId}")
    Call<String> getLinkByLinkId(@Path("linkId") Long linkId);

}
