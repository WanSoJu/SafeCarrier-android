package com.example.safecarrier.dto;

public class AllResponse {
    private Integer leftCount;
    private Long linkId;
    private String lid;
    private String fileName;

    public AllResponse(){}

    public Integer getLeftCount() {
        return leftCount;
    }

    public void setLeftCount(Integer leftCount) {
        this.leftCount = leftCount;
    }

    public Long getLinkId() {
        return linkId;
    }

    public void setLinkId(Long linkId) {
        this.linkId = linkId;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public AllResponse(Integer leftCount, Long linkId, String lid, String fileName) {
        this.leftCount = leftCount;
        this.linkId = linkId;
        this.lid = lid;
        this.fileName = fileName;
    }
}
