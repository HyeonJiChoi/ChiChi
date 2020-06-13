package org.dementia.cdt_application;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class InspectorTestProblemCDT extends Fragment {
    DrawCDTCanvas drawCDTCanvas;
    int answer = 12, cdt_score;
    ImageButton resetButton;
    Button completeButton;
    public TestScreen activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_cdt, container, false);
        drawCDTCanvas =  view.findViewById(R.id.InspectorTestProblemCDTCanvas);
        resetButton =  view.findViewById(R.id.InspectorTestProblemCDTButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCDTCanvas.reset();
            }
        });
        completeButton =  view.findViewById(R.id.InspectorTestProblemCDTCompleteButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetCDTScore newGetCDTScore = new GetCDTScore(drawCDTCanvas, answer) ;
                cdt_score = newGetCDTScore.get_score();
                System.out.println(cdt_score);
                activity.CDTscore = cdt_score;
                activity.onChangeFragment();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        answer = newBundle.getInt("answer");
    }
}
