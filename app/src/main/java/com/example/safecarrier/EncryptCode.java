package com.example.safecarrier;


import android.util.Base64;
import android.util.Log;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptCode {
    public static byte[] iv = { 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16 };

    // 사용자 지정 키로 AES256 암호화
    public static byte[] encByKey(String key, String value) throws Exception {
        return encByKey(key.getBytes(), value.getBytes());
    }

    // 사용자 지정 키로 AES256 복호화
    public static byte[] encByKey(byte[] key, byte[] value) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] randomKey = cipher.doFinal(value);
        // return Base64.encodeToString(randomKey, 0);
        return Base64.encode(randomKey,0);
    }

    // 사용자 지정 키로 AES256 복호화
    public static String decByKey(String key, String plainText) throws Exception {
        return decByKey(key.getBytes(), Base64.decode(plainText, 0));
    }

    // 사용자 지정 키로 AES256 복호화
    public static String decByKey(byte[] key, byte[] encText) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));
        byte[] secureKey = cipher.doFinal(encText);
        return new String(secureKey);
    }

    public static byte[] PBKDF1(String password) throws Exception {
        int dkLen = 32;
        int iteration = 1000;
        byte[] salt = new byte[]{0x78, 0x57, (byte) 0x8e, 0x5a, 0x5d, 0x63, (byte) 0xcb, 0x06};

        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md = MessageDigest.getInstance("SHA1");

        byte[] input = new byte[Utils.toByteArray(password).length + salt.length];

        System.arraycopy(Utils.toByteArray(password), 0, input, 0, Utils.toByteArray(password).length);
        System.arraycopy(salt, 0, input, Utils.toByteArray(password).length, salt.length); //password와 salt를 input에 넣는다

        md.update(input);

        for (int i = 0; i < iteration - 1; i++) { //iteration번 만큼 반복한다
            byte T[] = md.digest();
            md.update(T);
        }
        byte output[]=md.digest(); //결과값을 output에 저장한다
        Log.v("test", "part6 "+output.toString());
        byte[] result=new byte[dkLen];

        System.arraycopy(output,0,result,0,dkLen); //output의 결과값을 dkLen만큼만 잘라서 result에 저장한다

        return result;


    }
}
