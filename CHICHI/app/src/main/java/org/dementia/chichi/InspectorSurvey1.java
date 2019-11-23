package org.dementia.chichi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class InspectorSurvey1 extends AppCompatActivity {

    CircleImageView InspectorSurveyProfile;
    Button nextbtn;
    EditText editText1, editText2, editText3, editText4, editTextPassword;
    CheckBox checkBox1, checkBox2;
    Spinner yearSpineer, marrySpinner;
    String sex;
    String name, birth, phone, address, marry, children, password;
    private Uri filePath;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_survey1);
        Intent intent = new Intent(this.getIntent()); // main screen에서 사전조사창으로 이동한 intent 받기

        InspectorSurveyProfile = findViewById(R.id.InspectorSurveyProfile);
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        editText4 = findViewById(R.id.editText4);
        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        editTextPassword = findViewById(R.id.editTextPassword);
        nextbtn = findViewById(R.id.next);
        yearSpineer = findViewById(R.id.spinner1);
        marrySpinner = findViewById(R.id.spinner2);


        ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.year, R.layout.support_simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpineer.setAdapter(adapter1);

        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.marry, R.layout.support_simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marrySpinner.setAdapter(adapter2);

        InspectorSurveyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);
            }
        });
        checkBox1.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
                if (checkBox1.isChecked()) {
                    sex = checkBox1.getText().toString();
                }
            }
        });
        checkBox2.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
                if (checkBox2.isChecked()) {
                    sex = checkBox2.getText().toString();
                }
            }
        });

        // 프로필사진


        nextbtn.setOnClickListener(new View.OnClickListener() { // 다음화면으로 이동
            @Override
            public void onClick(View v) {
                name = editText1.getText().toString(); // Q2에 따른 성함
                birth = yearSpineer.getSelectedItem().toString(); // Q3에 따른 태어난 연도
                phone = editText2.getText().toString(); // Q4에 따른 태어난 연도
                address = editText3.getText().toString();
                marry = marrySpinner.getSelectedItem().toString();
                children = editText4.getText().toString();
                password = editTextPassword.getText().toString();

                final HashMap<String, Object> temp = new HashMap<String, Object>();
                temp.put("sex", sex);
                temp.put("age", 22);
                temp.put("number", phone);
                temp.put("home", address);
                temp.put("married", marry);
                temp.put("child", children);
                temp.put("day", 0);
                MainActivity.name = name;
                MainActivity.password = password;
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor autoLogin = auto.edit();
                autoLogin.putString("inputName", name);
                autoLogin.putString("inputPwd", password);
                autoLogin.commit();
                MainActivity.firestoreManagement.add(name,password,temp);

                StorageReference riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com")
                        .child("userProfiles/" + name +"_"+password+ ".jpg");
                if (filePath != null) {
                    riversRef.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            System.out.println("성공______________");
                            MainActivity.firestoreManagement.user = temp;
                            Intent intent = new Intent(getApplicationContext(), InspectorMainScreen.class);
                            startActivity(intent);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("실패______________");
                        }
                    });

                } else {
                }
            }
        });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            Glide.with(this)
                    .load(filePath)
                    .into(InspectorSurveyProfile);
        }
    }


}
