package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class gaurdianLoginScreen extends AppCompatActivity {
    EditText nameEditText, passwordEditText;
    Button guardianLoginButton;
    String name, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaurdian_login_screen);
        guardianLoginButton = findViewById(R.id.guardianLoginButton);
        nameEditText = findViewById(R.id.guardianLoginName);
        passwordEditText = findViewById(R.id.guardianLoginPassword);

        guardianLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                System.out.println(name);
                System.out.println(password);

                System.out.println(MainActivity.firestoreManagement.userIds);
                if (MainActivity.firestoreManagement.userIds.contains(name + "_" + password)) {
                    MainActivity.name = name;
                    MainActivity.password = password;
                    SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor autoLogin = auto.edit();
                    autoLogin.putString("inputName", name);
                    autoLogin.putString("inputPwd", password);
                    autoLogin.putString("Character", "guardian");
                    autoLogin.commit();

                    Intent intent = new Intent(getApplicationContext(), GaurdianMainScreen.class);
                    startActivity(intent);
                    finish();

                }
                else{
                    Toast.makeText(getApplicationContext(), "존재하지 않는 성함 또는 비밀번호입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
