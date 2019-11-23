package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

public class InspectorTestFinalScore extends AppCompatActivity {
    int score;
    BarChart graph;
    TextView scoreTextView, FinalScoreTextView;
    String[] Labels = new String[]{"첫번째", "두번째", "세번째", "추가테스트"};
    int day;
    float totalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_final_score);
        Intent intent = getIntent();
        score = intent.getExtras().getInt("score");
        totalScore = intent.getExtras().getFloat("totalScore");
        graph = findViewById(R.id.Inspecto_Last_Score_Graph);
        day = Integer.parseInt(MainActivity.firestoreManagement.user.get("day").toString());
        scoreTextView = findViewById(R.id.LastScoreTextView);
        FinalScoreTextView = findViewById(R.id.FinalScoreTextView);

        scoreTextView.setText(score + "점");
        setResultScore();
        setBarChart();
    }

    private void setResultScore() {
        if(day==2){
            if(totalScore<18)
                FinalScoreTextView.setText("심한 치매");
            else if(totalScore<24)
                FinalScoreTextView.setText("경도 치매");
        }
        if(day==3){
            if(score<4)
                FinalScoreTextView.setText("경도 치매");
            else if(score<8)
                FinalScoreTextView.setText("정상 (치매 없음)");
        }
    }

    // 막대 차트 설정
    private void setBarChart() {
        graph.clearChart();

        for (int i = 0; i < day+1; i++) {
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
