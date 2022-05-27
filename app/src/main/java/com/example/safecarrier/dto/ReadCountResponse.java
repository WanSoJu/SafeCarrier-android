package com.example.safecarrier.dto;

public class ReadCountResponse {
    private Integer leftReadCount;
    private String videoUrl;

    public Integer getLeftReadCount(){
        return this.leftReadCount;
    }

    public String getVideoUrl(){
        return this.videoUrl;
    }

    public void setVideoUrl(String url){
        this.videoUrl=url;
    }

    public void setLeftReadCount(Integer count){
        this.leftReadCount=count;
    }
}
