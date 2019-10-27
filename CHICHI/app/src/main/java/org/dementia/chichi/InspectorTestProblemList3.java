package org.dementia.chichi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InspectorTestProblemList3 extends Fragment {
    Bundle problem;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inspector_test_problem_list3, container, false);
    }

    public void onResume() {
        super.onResume();

    }
}
