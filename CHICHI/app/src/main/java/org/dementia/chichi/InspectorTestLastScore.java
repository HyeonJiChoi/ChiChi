package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


public class InspectorTestLastScore extends AppCompatActivity {
    int score;
    BarChart graph;
    TimePicker picker;
    Button gotoNextTest, DecideNextTest;
    TextView initialtext, scoreTextView, nextTestTextView;
    String[] Labels = new String[]{"첫번째", "두번째", "세번째", "추가테스트"};
    int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_last_score);
        Intent intent = getIntent();
        score = intent.getExtras().getInt("score");
        graph = findViewById(R.id.Inspecto_Last_Score_Graph);
        picker = (TimePicker) findViewById(R.id.timePicker);
        picker.setIs24HourView(true);
        day = Integer.parseInt(MainActivity.firestoreManagement.user.get("day").toString());

        initialtext = findViewById(R.id.initialtext);
        scoreTextView = findViewById(R.id.scoreTextView);
        nextTestTextView = findViewById(R.id.nextTestTextView);
        DecideNextTest = findViewById(R.id.nextTestDecideButton);
        gotoNextTest = findViewById(R.id.newTestButton);

        initialtext.setText("오늘의 "+Labels[day]+" 테스트는");
        scoreTextView.setText(score+"점");
        nextTestTextView.setText("\n다음 테스트는 "+Labels[day+1]+" 테스트 입니다.");

        DecideNextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        gotoNextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorTestScreen.class);
                startActivity(intent);
            }
        });


        setBarChart();

        MainActivity.firestoreManagement.addScore(day,score,MainActivity.name,MainActivity.password);
        MainActivity.firestoreManagement.fixedDay(day+1,MainActivity.name,MainActivity.password);
    }

    // 막대 차트 설정
    private void setBarChart() {
        graph.clearChart();

        for (int i = 0; i < 3; i++) {
            if (i < day)
                graph.addBar(new BarModel(Labels[i], Float.parseFloat(MainActivity.firestoreManagement.user.get(i + "_day_score").toString()), 0xFFE7BDBB));
            else if (i == day)
                graph.addBar(new BarModel(Labels[i], score, 0xFFE7BDBB));
            else
                graph.addBar(new BarModel(Labels[i], 0f, 0xFFE7BDBB));
        }

        graph.startAnimation();

    }


}
