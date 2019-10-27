package org.dementia.chichi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InspectorSurvey1 extends AppCompatActivity  {


    Button btn1, btn2, btn3;
    TextView textView1, textView2, textView3, textView4, textView5;
    EditText editText1, editText2,editText3;
    Spinner yearSpineer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_survey1);
        Intent intent = new Intent(this.getIntent()); // main screen에서 사전조사창으로 이동한 intent 받기
        editText1 = findViewById(R.id.editText1);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.next);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textView5 = findViewById(R.id.textView5);

        yearSpineer = findViewById(R.id.spinner);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.year, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpineer.setAdapter(adapter);

        btn1.setOnClickListener(new View.OnClickListener() { // Q1에 따른 남성성별
            @Override
            public void onClick(View v) {
                String sex = btn1.getText().toString();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() { // Q1에 따른 여성성별
            @Override
            public void onClick(View v) {
                String sex = btn2.getText().toString();
            }
        });
        String name = editText1.getText().toString(); // Q2에 따른 성함
        String birth = yearSpineer.getSelectedItem().toString(); // Q3에 따른 태어난 연도
        String phone = editText2.getText().toString(); // Q4에 따른 태어난 연도
        String address = editText3.getText().toString();

        HashMap<String, String> temp = new HashMap<String, String>();
        temp.put("Sex", "sex");
        temp.put("Name", "name");
        temp.put("Birth", "birth");
        temp.put("Phone","phone");
        temp.put("Address",address);

        btn3.setOnClickListener(new View.OnClickListener() { // 다음화면으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorMainScreen.class);
                startActivity(intent);
            }
        });



    }


}
