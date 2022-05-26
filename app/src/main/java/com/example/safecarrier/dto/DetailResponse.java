package com.example.safecarrier.dto;

public class DetailResponse {
    private String fileName;
    private byte[] encryptedData;
    private String dataType;

    public DetailResponse(String dataType) {
        this.dataType = dataType;
    }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public DetailResponse(){}
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public DetailResponse(String fileName, byte[] encryptedData,String dataType) {
        this.fileName = fileName;
        this.encryptedData = encryptedData;
        this.dataType=dataType;
    }
}
