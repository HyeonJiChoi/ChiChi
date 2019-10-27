package org.dementia.chichi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class InspectorSurvey1  extends AppCompatActivity {

    String question1,question2,question3;
    Button btn1,btn2,btn3;
    TextView textView1,textView2,textView3;
    EditText editText;
    Spinner yearSpineer;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_survey1);
        Intent intent = new Intent(this.getIntent()); // main screen에서 사전조사창으로 이동한 intent 받기
        editText=findViewById(R.id.editText1);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.next);
        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);

        yearSpineer = findViewById(R.id.spinner);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.year,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpineer.setAdapter(adapter);

        btn1.setOnClickListener(new View.OnClickListener(){ // 다음화면으로 이동
            @Override
            public void onClick(View v) {
                String sex =btn1.getText().toString();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){ // 다음화면으로 이동
            @Override
            public void onClick(View v) {
                String sex =btn2.getText().toString();
            }
        });
        String name = editText.getText().toString();
        String birth = yearSpineer.getSelectedItem().toString();

        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("Sex","sex");
        temp.put("Name","name");
        temp.put("Birth","birth");

        btn3.setOnClickListener(new View.OnClickListener(){ // 다음화면으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorMainScreen.class);
                startActivity(intent);
            }
        });
    }
}
