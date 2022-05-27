package com.example.safecarrier.dto;

public class DataDto {
    private String encryptedData;
    private String dataType;
    private int readLimit;
    private String appLink;
    private String lid;
    private String fileName;

    public DataDto(){}

    public DataDto(String encryptedData, String dataType, int readLimit, String appLink, String lid, String fileName) {
        this.encryptedData = encryptedData;
        this.dataType = dataType;
        this.readLimit = readLimit;
        this.appLink = appLink;
        this.lid = lid;
        this.fileName = fileName;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getReadLimit() {
        return readLimit;
    }

    public void setReadLimit(int readLimit) {
        this.readLimit = readLimit;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
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
}
