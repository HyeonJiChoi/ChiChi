package org.dementia.cdt_application;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InspectorTestProblemList3 extends Fragment {
    Button TestProblemListSmall1;
    Button TestProblemListSmall2;
    Button TestProblemListSmall3;
    Bundle problem_string;
    public TestScreen activity;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_list3, container, false);
        TestProblemListSmall1 = view.findViewById(R.id.TestProblemListSmall1);
        TestProblemListSmall2 = view.findViewById(R.id.TestProblemListSmall2);
        TestProblemListSmall3 = view.findViewById(R.id.TestProblemListSmall3);


        TestProblemListSmall1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(problem_string.getInt("answer")==1){
                    activity.set_score(true);
                }
                activity.onChangeFragment();
            }
        });
        TestProblemListSmall2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(problem_string.getInt("answer")==2){
                    activity.set_score(true);
                }
                activity.onChangeFragment();
            }
        });
        TestProblemListSmall3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(problem_string.getInt("answer")==3){
                    activity.set_score(true);
                }
                activity.onChangeFragment();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //액티비티에서 보낸 번들 확인
        problem_string = getArguments();
        TestProblemListSmall1.setText(problem_string.get("1").toString());
        TestProblemListSmall2.setText(problem_string.get("2").toString());
        TestProblemListSmall3.setText(problem_string.get("3").toString());
    }
}
