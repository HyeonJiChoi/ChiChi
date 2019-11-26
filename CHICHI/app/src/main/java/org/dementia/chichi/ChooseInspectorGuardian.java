package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseInspectorGuardian extends AppCompatActivity {
    Button inpectorButton, GuardianButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_inspector_guardian);

        inpectorButton = findViewById(R.id.InspectorChooseButton);
        GuardianButton = findViewById(R.id.GuardianChooseButton);

        inpectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.inpectorGuardian = 0;
                Intent intent = new Intent(getApplicationContext(), InspectorSurvey1.class);
                startActivity(intent);
            }
        });
        GuardianButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.inpectorGuardian = 1;
                Intent intent = new Intent(getApplicationContext(), ChooseInspectorGuardian.class);
                startActivity(intent);
            }
        });
    }
}