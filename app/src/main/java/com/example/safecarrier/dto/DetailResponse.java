package com.example.safecarrier.dto;

public class DetailResponse {
    private String fileName;
    private String encryptedData;
    private String dataType;

    public DetailResponse(String dataType) {
        this.dataType = dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
    public String getDataType() { return dataType; }

    public DetailResponse(){}
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public DetailResponse(String fileName, String encryptedData,String dataType) {
        this.fileName = fileName;
        this.encryptedData = encryptedData;
        this.dataType=dataType;
    }
}
