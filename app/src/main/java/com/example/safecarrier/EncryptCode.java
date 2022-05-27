package com.example.safecarrier;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptCode {
    public static byte[] iv = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };

    // 사용자 지정 키로 AES256 암호화
    public static String encByKey(byte[] key, String value) throws Exception {
        return encByKey(key, value.getBytes());
    }

    // 사용자 지정 키로 AES256 복호화
    public static String encByKey(byte[] key, byte[] value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] randomKey = cipher.doFinal(value);
        return Base64.encodeToString(randomKey, 0);
    }

    // 사용자 지정 키로 AES256 복호화
    public static String decByKey(byte[] key, String plainText) throws Exception {
        System.out.println("KEY : "+key);
        return decByKey(key, Base64.decode(plainText, 0));
    }

    // 사용자 지정 키로 AES256 복호화
    public static String decByKey(byte[] key, byte[] encText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] secureKey = cipher.doFinal(encText);
        return new String(secureKey);
    }



    public static byte[] MakeKey(String password)
            throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeySpecException {
        //암호키를 생성하는 팩토리 객체 생성
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        //다이제스트를 이용하여, SHA-512로 단방향 해시 생성 (salt 생성용)
        MessageDigest digest = MessageDigest.getInstance("SHA-512");

        // C# : byte[] keyBytes = System.Text.Encoding.UTF8.GetBytes(password);
        byte[] keyBytes = password.getBytes("UTF-8");
        // C# : byte[] saltBytes = SHA512.Create().ComputeHash(keyBytes);
        byte[] saltBytes = digest.digest(keyBytes);

        // 256bit (AES256은 256bit의 키, 128bit의 블록사이즈를 가짐.)
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password.toCharArray(), saltBytes, 65536, 256);
        Key secretKey = factory.generateSecret(pbeKeySpec);

        // 256bit = 32byte
        byte[] key = new byte[32];
        System.arraycopy(secretKey.getEncoded(), 0, key, 0, 32);
        String data = new String(key);
        System.out.println(byteArrayToHexaString(key));
        return key;
        //AES 알고리즘을 적용하여 암호화키 생성


    }
    public static String byteArrayToHexaString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();

        for (byte data : bytes) {
            builder.append(String.format("%02X ", data));
        }

        return builder.toString();
    }


}
