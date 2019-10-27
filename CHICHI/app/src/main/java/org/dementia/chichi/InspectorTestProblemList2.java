package org.dementia.chichi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class InspectorTestProblemList2 extends Fragment {
    public InspectorTestScreen activity;
    Button TestProblemListbig1, TestProblemListbig2;
    Bundle problem_string;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_list2, container, false);
        TestProblemListbig1 = view.findViewById(R.id.TestProblemListbig1);
        TestProblemListbig2 = view.findViewById(R.id.TestProblemListbig2);


        TestProblemListbig1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //activity.onChangeFragment();
            }
        });
        TestProblemListbig2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // activity.onChangeFragment();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public void onResume() {
        super.onResume();
        //액티비티에서 보낸 번들 확인
        problem_string = getArguments();;
        TestProblemListbig1.setText(problem_string.get("1").toString());
        TestProblemListbig2.setText(problem_string.get("2").toString());
    }
}
