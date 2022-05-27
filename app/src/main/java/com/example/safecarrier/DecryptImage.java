package com.example.safecarrier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DecryptImage extends AppCompatActivity {
    EncryptCode encryptCode = new EncryptCode();
    //byte[] decImageByte;
    ImageView imageView;
    TextView tempText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        Intent intent = getIntent();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE); //캡쳐방지
        imageView=(ImageView) findViewById(R.id.imageView);
        tempText=(TextView) findViewById(R.id.tempText);

        byte[] encDataByte=intent.getByteArrayExtra("encDataByte");
        String fileName=intent.getStringExtra("fileName");
        String password=intent.getStringExtra("password");

        tempText.setText(encDataByte.toString());
        try {

            byte[] decImageByte = EncryptCode.decByKey(password.getBytes(), encDataByte);
            Toast.makeText(getApplicationContext(),"zero", Toast.LENGTH_LONG).show();
            byte[] decByte = Base64.decode(decImageByte, Base64.DEFAULT);

            Toast.makeText(getApplicationContext(),"one", Toast.LENGTH_LONG).show();
            Bitmap bitImage= BitmapFactory.decodeByteArray( decByte, 0, decByte.length );
            Toast.makeText(getApplicationContext(),"two", Toast.LENGTH_LONG).show();
            imageView.setImageBitmap(bitImage);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"catch", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


    }
}
