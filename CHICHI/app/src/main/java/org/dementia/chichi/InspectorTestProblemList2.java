package org.dementia.chichi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class InspectorTestProblemList2 extends Fragment {
    Bundle problem;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inspector_test_problem_list2, container, false);
    }

    public void onResume() {
        super.onResume();

    }
}
