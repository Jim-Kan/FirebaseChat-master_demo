package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    //註冊頁
    EditText username,email,password,user_class;
    Button btn_register;
    Spinner class_spinner;

    FirebaseAuth firebaseAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //獲取狀態欄改變背景顏色
        getWindow().setStatusBarColor(getColor(R.color.colorGreen500));

        //ToolBar設定
        Toolbar actbar = findViewById(R.id.actbar);
        setSupportActionBar(actbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Toolbar設定返回鍵
        actbar.setTitleTextColor(Color.WHITE);
        actbar.setBackgroundColor(getResources().getColor(R.color.colorGreen400));

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        class_spinner = findViewById(R.id.ClassSpinner);
        btn_register = findViewById(R.id.btn_register);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyin_username = username.getText().toString();
                String keyin_email = email.getText().toString();
                String keyin_password = password.getText().toString();
                String keyin_classroom = class_spinner.getSelectedItem().toString();

                if(TextUtils.isEmpty(keyin_username) || TextUtils.isEmpty(keyin_email) || TextUtils.isEmpty(keyin_password)
                        || TextUtils.isEmpty(keyin_classroom)){
                    Toast.makeText(RegisterActivity.this, "輸入錯誤/不可空白",Toast.LENGTH_SHORT).show();
                }else if (keyin_password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "密碼必須6字元以上",Toast.LENGTH_SHORT).show();
                }else {
                    register(keyin_username,keyin_email,keyin_password,keyin_classroom);
                }

            }
        });

        //Spinner setting
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.ClassName,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        class_spinner.setAdapter(adapter);
        class_spinner.setOnItemSelectedListener(spnOnItemSelectedListener);

    }

    private void register(final String username, final String email, String password, final String user_class){

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String user_ID = firebaseUser.getUid();

                            //連結到資料庫　建立的資料會存在"Users"根目錄下　子目錄以(user_ID)命名
                            reference = FirebaseDatabase.getInstance().getReference("Users").child(user_ID);

                            HashMap<String , String> hashMap = new HashMap<>();
                            //上傳後會依下列"名稱"建立節點標籤及對應值
                            hashMap.put("id", user_ID);
                            hashMap.put("username" , username);
                            hashMap.put("userClass", user_class);
                            hashMap.put("imageURL" , "default");
                            hashMap.put("status" , "offline");
                            hashMap.put("search" , username.toLowerCase());

                            //將資料建立到Database
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                //註冊成功後自動連結主畫面
                                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                                //釋放掉目前執行Activity的Task | 並開啟一個新的Task去執行此次intent的目標(MainActivity)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });

                        }else {
                            Toast.makeText(RegisterActivity.this, "此信箱已註冊 or 信箱格式錯誤",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private AdapterView.OnItemSelectedListener spnOnItemSelectedListener
            =new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
