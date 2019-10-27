package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InspectorTestScreen extends AppCompatActivity {
    public int testNumber = 0;
    int nextProblem = 0;
    public Map<String, Object> user;
    Set<String> todayProblems;
    Object[] problems;
    FragmentTransaction fragmentTransaction;
    InspectorBlankFragment currentFragment;
    FirestoreManagement firestoreManagement;
    TextView fragmentProblemQuestionText, fragmentProblemQuestionQNumber;
    InspectorTestProblemList3 inspectorTestProblemList3 = new InspectorTestProblemList3();
    InspectorTestProblemList2 inspectorTestProblemList2 = new InspectorTestProblemList2();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestoreManagement = MainActivity.firestoreManagement;
        user = firestoreManagement.user;
        todayProblems = firestoreManagement.todayProblems;
        setContentView(R.layout.activity_inspector_test_screen);
        setActionbar();
        currentFragment = (InspectorBlankFragment) getSupportFragmentManager().findFragmentById(R.id.inpectorTestProblemList_first);
        fragmentProblemQuestionText = findViewById(R.id.fragmentProblemQuestionText);
        fragmentProblemQuestionQNumber = findViewById(R.id.fragmentProblemQuestionQNumber);
        problems = todayProblems.toArray();

        onChangeFragment();
    }

    public void onChangeFragment(){
        nextProblem = Integer.parseInt(problems[testNumber].toString());
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //문제들을 순서대로 갖고와서 시험 보는거~
        switch (nextProblem) {
            case 0:
                fragmentProblemQuestionText.setText("지금 연도와 월은\n언제인가요?");
                fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                //프래그먼트에 전달해줄 거 정해주기
                Bundle newBundle = new Bundle();
                newBundle.putString("1", "2017월 3일");
                newBundle.putString("2", "2017월 3일");
                newBundle.putString("3", "2017월 3일");
                inspectorTestProblemList3.setArguments(newBundle);
                inspectorTestProblemList3.activity = this;

                if (testNumber == 0) {
                    fragmentTransaction.hide(currentFragment)
                            .add(R.id.inpectorTestProblemList, inspectorTestProblemList3);
                } else {
                    fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList3);
                }
                break;
            case 1:
                if (testNumber == 0) {
                    fragmentTransaction.hide(currentFragment)
                            .add(R.id.inpectorTestProblemList, inspectorTestProblemList2);
                } else {
                    fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList2);
                }
                break;
        }
        testNumber++;
        fragmentTransaction.commit();
    }

    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
    }
}
