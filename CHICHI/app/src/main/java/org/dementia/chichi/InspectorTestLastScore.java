package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;


public class InspectorTestLastScore extends AppCompatActivity {
    int score;
    BarChart graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_last_score);
        Intent intent = getIntent();
        score = intent.getExtras().getInt("score");
        graph = findViewById(R.id.Inspecto_Last_Score_Graph);

        setBarChart();


    }

    // 막대 차트 설정
    private void setBarChart() {

        String[] Labels = new String[]{"첫번째", "두번째", "세번째", "추가테스트"};
        int day = Integer.parseInt(MainActivity.firestoreManagement.user.get("day").toString());
        graph.clearChart();

        for (int i = 0; i < 4; i++) {
            if(i<day)
                graph.addBar(new BarModel(Labels[i], Float.parseFloat(MainActivity.firestoreManagement.user.get(i+"_day_score").toString()), 0xFFE7BDBB));
            else if(i==day)
                graph.addBar(new BarModel(Labels[i], score, 0xFFE7BDBB));
            else
                graph.addBar(new BarModel(Labels[i], 0f, 0xFFE7BDBB));
        }

        graph.startAnimation();

    }


}
