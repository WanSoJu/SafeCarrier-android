package com.example.safecarrier;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DecryptText extends AppCompatActivity {
    TextView textView;

    String password = "password";
    byte[] S=new byte[] {0x78, 0x57, (byte)0x8e, 0x5a, 0x5d, 0x63, (byte)0xcb, 0x06};
    int c=1000;
    int dkLen=16;
    byte[] derivedKey=PBKDF1(password,S,dkLen,c);

    public DecryptText() throws Exception {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_text);
        textView= (TextView) findViewById(R.id.showText);
        try {
            fileEnc(S, derivedKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static byte[] PBKDF1(String password, byte[] salt, int dkLen, int iteration) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        MessageDigest md=MessageDigest.getInstance("SHA1");

        byte[] input=new byte[password.length()+salt.length];

        System.arraycopy(Utils.toByteArray(password), 0, input, 0, password.length());
        System.arraycopy(salt, 0, input, password.length(), salt.length); //password와 salt를 input에 넣는다

        md.update(input);

        for(int i=0;i<iteration-1;i++) { //iteration번 만큼 반복한다
            byte T[]=md.digest();
            md.update(T);
        }

        byte output[]=md.digest(); //결과값을 output에 저장한다
        byte[] result=new byte[dkLen];

        System.arraycopy(output,0,result,0,dkLen); //output의 결과값을 dkLen만큼만 잘라서 result에 저장한다

        return result;
    }

    void fileEnc(byte[] salt, byte[] derivedKey) throws Exception
    {
        Security.addProvider(new BouncyCastleProvider());

        //derivedKey와 salt를 이어붙여 해쉬함수에 넣어 해쉬값을 만든후, 16바이트만 자른다
        MessageDigest hash=MessageDigest.getInstance("SHA1");
        byte[] hash_input=new byte[derivedKey.length+salt.length];
        System.arraycopy(derivedKey, 0, hash_input, 0, derivedKey.length);
        System.arraycopy(salt, 0, hash_input, derivedKey.length, salt.length);
        hash.update(hash_input);
        byte password_check[]=hash.digest();
        byte password_check_finish[] = new byte[16];
        System.arraycopy(password_check,0,password_check_finish,0,16);

        byte[] ivBytes=new byte[] {
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00,
                0x07, 0x06, 0x05, 0x04, 0x03, 0x02, 0x01, 0x00};

        SecretKeySpec key=new SecretKeySpec(derivedKey,"AES");
        IvParameterSpec iv=new IvParameterSpec(ivBytes);

        Cipher cipher=null;

        cipher=Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        int BUF_SIZE=1024;

        byte[] encData=new byte[BUF_SIZE];

        //System.out.println("= = = = = 암호화 = = = = =");
        String text="Hello World"; //짧은것만 되겠지만 일단 텍스트 암호화

        encData=cipher.update(text.getBytes());
        encData=cipher.doFinal();

        textView.setText(Utils.toHexString(encData));

    }
}