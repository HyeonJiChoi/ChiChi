package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class InspectorTestScreen extends AppCompatActivity {
    int nextProblem = 1;
    FragmentTransaction fragmentTransaction;
    InspectorTestProblemList3 currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_screen);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        currentFragment = (InspectorTestProblemList3) getSupportFragmentManager().findFragmentById(R.id.inpectorTestProblemList_first);
        switch (nextProblem) {
            case 0:
                fragmentTransaction.hide(currentFragment)
                        .add(R.id.inpectorTestProblemList, new InspectorTestProblemList3())
                        .commit();
                break;
            case 1:
                fragmentTransaction.hide(currentFragment)
                        .add(R.id.inpectorTestProblemList, new InspectorTestProblemList2())
                        .commit();
                break;
        }
        setActionbar();
    }

    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
    }
}
