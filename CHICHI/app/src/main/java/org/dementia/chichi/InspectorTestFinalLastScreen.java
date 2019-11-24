package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

public class InspectorTestFinalLastScreen extends AppCompatActivity {
    int score = 0;
    Button endButton;
    BarChart graph;
    TextView scoreTextView, FinalScoreTextView;
    String[] Labels = new String[]{"첫번째", "두번째", "세번째", "추가테스트"};
    int day;
    int TestCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_final_last_screen);
        graph = findViewById(R.id.Inspector_Final_Score_Graph);
        scoreTextView = findViewById(R.id.FinalTotalScoreTextView);
        FinalScoreTextView = findViewById(R.id.FinalTotalResultTextView);
        endButton = findViewById(R.id.FinalLastEndButton);

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        for (int i = 0; i < 3; i++) {
            score += Integer.parseInt(MainActivity.firestoreManagement.user.get(i + "_day_score").toString());
        }
        scoreTextView.setText(score + "점");
        setBarChart();
        setResultScore();
    }

    private void setResultScore() {
        if (TestCount == 3) {
            if (score < 18)
                FinalScoreTextView.setText("심한 치매");
            else if (score < 24)
                FinalScoreTextView.setText("경도 치매");
        }
        if (TestCount == 4) {
            score = Integer.parseInt(MainActivity.firestoreManagement.user.get(3 + "_day_score").toString());
            if (score < 4)
                FinalScoreTextView.setText("경도 치매");
            else if (score < 8)
                FinalScoreTextView.setText("정상 (치매 없음)");
        }
    }

    // 막대 차트 설정
    private void setBarChart() {
        graph.clearChart();
        while (MainActivity.firestoreManagement.user.containsKey(TestCount + "_day_score")) {
            graph.addBar(new BarModel(Labels[TestCount], Float.parseFloat(MainActivity.firestoreManagement.user.get(TestCount + "_day_score").toString()), 0xFFE7BDBB));
            TestCount++;
        }

        graph.startAnimation();

    }
}
