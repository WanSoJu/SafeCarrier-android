package com.example.safecarrier;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomDialog extends Dialog {
    private final DialogListener DialogListener;
    public String FileName = "first";
    public CustomDialog(Context context,DialogListener DialogListener) {
        super(context);
        this.DialogListener = DialogListener;
    }
    public interface DialogListener{
        void clickBtn(Intent data);
    }
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //타이틀 바 삭제
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_en);

        Button testBtn = (Button)findViewById(R.id.button2);
        TextView name= (TextView)findViewById(R.id.editTextTextPersonName);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                DialogListener.clickBtn(intent);
                dismiss();
            }


        });
        name.setText(FileName);
    }
}
