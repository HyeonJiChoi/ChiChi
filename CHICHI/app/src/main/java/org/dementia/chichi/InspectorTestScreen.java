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
import java.util.Timer;
import java.util.TimerTask;

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
    InspectorTestProblemPicture inspectorTestProblemPicture = new InspectorTestProblemPicture();

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

    public void onChangeFragment() {
        if (testNumber < 2) {
            nextProblem = Integer.parseInt(problems[testNumber].toString());
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle newBundle;
            //문제들을 순서대로 갖고와서 시험 보는거~
            switch (nextProblem) {
                case 0:
                    fragmentProblemQuestionText.setText("지금 연도와 월은\n언제인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    //프래그먼트에 전달해줄 거 정해주기
                    newBundle = new Bundle();
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
                    fragmentProblemQuestionText.setText("다음 그림을 보고\n무엇인지 맞춰주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    if (testNumber == 0) {
                        fragmentTransaction.hide(currentFragment)
                                .add(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    } else {
                        fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    }

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            show_2problem();
                        }
                    };
                    timer.schedule(timerTask,2000);
                    break;
            }
            testNumber++;
            fragmentTransaction.commit();
        }
    }
    public void show_2problem(){
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("1", "전화기");
        newBundle.putString("2", "청소기");
        inspectorTestProblemList2.setArguments(newBundle);
        inspectorTestProblemList2.activity = this;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList2).commit();
    }

    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
    }
}
