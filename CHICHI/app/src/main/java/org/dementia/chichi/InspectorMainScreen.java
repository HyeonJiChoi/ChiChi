package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InspectorMainScreen extends AppCompatActivity {
    Button testButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main_screen);

        testButton = findViewById(R.id.inspectorMainScreenTestButton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorTestScreen.class);
                startActivity(intent);

            }
        });
    }
}
