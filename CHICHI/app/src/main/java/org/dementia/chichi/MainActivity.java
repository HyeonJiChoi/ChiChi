package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    static String name, password;
    Button button;
    static FirestoreManagement firestoreManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.

        String loginName = auto.getString("inputName", null);
        String loginPwd = auto.getString("inputPwd", null);
        final String character = auto.getString("Character", null);
        //MainActivity로 들어왔을 때 둘다 null이 아니면 값이 있다는 뜻 = 자동로그인
        if (loginName != null && loginPwd != null) {
            name = loginName;
            password = loginPwd;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(character.equals("inspector")){
                    Intent intent = new Intent(getApplicationContext(), InspectorMainScreen.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "'" + name + "'" + "님 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                    }
                    else{
                        Intent intent = new Intent(getApplicationContext(), GaurdianMainScreen.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "'" + name + "'" + "님의 보호자분 자동로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }, 2500);
        } else if (loginName == null && loginPwd == null) {   //아무것도 안 적혀있으면 최초화면 보여주기
            Intent intent = new Intent(getApplicationContext(), FirstStartScrean.class);
            startActivity(intent);
            finish();
        }
        firestoreManagement = new FirestoreManagement();
    }

}
