package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        initialtext.setText("오늘의 " + Labels[day] + " 테스트는 ");
        scoreTextView.setText(score + "점");
        nextTestTextView.setText("\n다음 테스트는 " + Labels[day + 1] + " 테스트 입니다.");

        DecideNextTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour, hour_24, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour_24 = picker.getHour();
                    minute = picker.getMinute();
                } else {
                    hour_24 = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if (hour_24 > 12) {
                    am_pm = "PM";
                    hour = hour_24 - 12;
                } else {
                    hour = hour_24;
                    am_pm = "AM";
                }

                // 현재 지정된 시간으로 알람 시간 설정
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY, hour_24);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                // 이미 지난 시간을 지정했다면 다음날 같은 시간으로 설정
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                }

                Date currentDateTime = calendar.getTime();
                String date_text = new SimpleDateFormat("yyyy년 MM월 dd일 EE요일 a hh시 mm분 ", Locale.getDefault()).format(currentDateTime);
                Toast.makeText(getApplicationContext(), date_text + "에 다음 시험이 치뤄집니다!", Toast.LENGTH_SHORT).show();

                //  Preference에 설정한 값 저장
                SharedPreferences.Editor editor = getSharedPreferences("daily alarm", MODE_PRIVATE).edit();
                editor.putLong("nextNotifyTime", (long) calendar.getTimeInMillis());
                editor.apply();


                diaryNotification(calendar);
                finish();
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

        MainActivity.firestoreManagement.addScore(day, score, MainActivity.name, MainActivity.password);
        MainActivity.firestoreManagement.fixedDay(day + 1, MainActivity.name, MainActivity.password);
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
    void diaryNotification(Calendar calendar)
    {
        Boolean dailyNotify = true; // 무조건 알람을 사용

        PackageManager pm = this.getPackageManager();
        ComponentName receiver = new ComponentName(this, DeviceBootReceiver.class);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // 사용자가 매일 알람을 허용했다면
        if (dailyNotify) {


            if (alarmManager != null) {

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                }
            }

            // 부팅 후 실행되는 리시버 사용가능하게 설정
            pm.setComponentEnabledSetting(receiver,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);

        }
    }


}
