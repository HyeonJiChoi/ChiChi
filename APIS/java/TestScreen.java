package org.dementia.cdt_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class TestScreen extends AppCompatActivity {
    public int CDTscore = 0;
    public int realCount = 0;
    public int testNumber = 0, total_testNumber = 4;
    public Map<String, Object> user;
    Object[] problems;
    FragmentTransaction fragmentTransaction;
    InspectorBlankFragment firstFragment;
    TextView fragmentProblemQuestionText, fragmentProblemQuestionQNumber;
    InspectorTestProblemList3 inspectorTestProblemList3 = new InspectorTestProblemList3();
    InspectorTestProblemList2 inspectorTestProblemList2 = new InspectorTestProblemList2();
    InspectorTestProblemCDT inspectorTestProblemCDT = new InspectorTestProblemCDT();
    private int score = 0;
    private int currentFragment = 0;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_screen);
        firstFragment = (InspectorBlankFragment) getSupportFragmentManager().findFragmentById(R.id.inpectorTestProblemList_first);
        fragmentProblemQuestionText = findViewById(R.id.fragmentProblemQuestionText);
        fragmentProblemQuestionQNumber = findViewById(R.id.fragmentProblemQuestionQNumber);
        onChangeFragment();
        System.out.println(score);
    }

    public void onChangeFragment() {
        int answer=0;
        timer.cancel();
        if (testNumber < total_testNumber) {
            System.out.println(testNumber);
            String todayDate = getDateString();
            String[] wordDate = todayDate.split("-");
            //문제들을 순서대로 갖고와서 시험 보는거~
            switch (testNumber) {
                case 0: //지금 연도 월
                    System.out.println(testNumber);
                    fragmentProblemQuestionText.setText("지금 연도와 월은\n언제인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1));

                    String[] Dates = new String[3];

                    //답의 위치를 랜덤으로 구하기
                    answer = (int) (Math.random() * 10) % 3;

                    //날짜를 랜덤으로 구하기
                    for (int i = 0; i < 3; i++) {
                        if (i == answer) {
                            Dates[i] = wordDate[0] + "년 " + wordDate[1] + "월";
                        } else if (i < answer) {
                            Dates[i] = (Integer.parseInt(wordDate[0]) + (int) (Math.random() * 10) % 5 + 1) + "년 " + ((Integer.parseInt(wordDate[1]) - (int) (Math.random() * 10) % 7) % 12 - 1) + "월";
                        } else if (i > answer) {
                            Dates[i] = (Integer.parseInt(wordDate[0]) - (int) (Math.random() * 10) % 5 - 1) + "년 " + ((Integer.parseInt(wordDate[1]) + (int) (Math.random() * 10) % 7) % 12 + 1) + "월";
                        }
                    }

                    show_3problem(Dates[0], Dates[1], Dates[2], answer);
                    break;
                case 1: //계산문제
                    int num1 = (int) (Math.random() * 10) % 10 + 1;
                    int num2 = (int) (Math.random() * 10) % 10 + 1;
                    int operation = (int) (Math.random() * 10) % 4;
                    if (operation == 3) {
                        while (num1 % num2 != 0) {
                            num1 = (int) (Math.random() * 10) % 10 + 1;
                            num2 = (int) (Math.random() * 10) % 10 + 1;
                        }
                    }

                    int answer_number = Calculation(num1, num2, operation);
                    fragmentProblemQuestionText.setText(Integer.toString(num1) + " " + operation_return(operation) + " " + Integer.toString(num2) + " = ?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1));
                    answer = (int) (Math.random() * 10) % 3;

                    String[] calculateNumbers = new String[3];

                    //값을 랜덤으로 구하기
                    for (int i = 0; i < 3; i++) {
                        if (i == answer) {
                            calculateNumbers[i] = Integer.toString(answer_number);
                        } else if (i < answer) {
                            calculateNumbers[i] = Integer.toString(answer_number - (int) ((Math.random() * 10) % 5) - 5);
                        } else if (i > answer) {
                            calculateNumbers[i] = Integer.toString(answer_number + (int) ((Math.random() * 10) % 5) + 5);
                        }
                    }

                    show_3problem(calculateNumbers[0], calculateNumbers[1], calculateNumbers[2], answer);
                    break;
                case 2: //계절알기
                    fragmentProblemQuestionText.setText("지금 계절은 무엇인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1));

                    answer = (int) (Math.random() * 10) % 2;

                    String now_seasons = getSeason(Integer.parseInt(wordDate[1]));
                    String other_seasons = getSeason((int) (Math.random() * 10) % 12);
                    //안겹치게 넣기
                    while (now_seasons.equals(other_seasons))
                        other_seasons = getSeason((int) (Math.random() * 10) % 12);

                    String[] seasons = new String[2];
                    //날짜를 랜덤으로 구하기
                    for (int i = 0; i < 2; i++) {
                        if (i == answer) {
                            seasons[i] = now_seasons;
                        } else {
                            seasons[i] = other_seasons;
                        }
                    }
                    show_2problem(seasons[0], seasons[1], answer);

                    break;
                case 3: //CDT
                    int random_hour = (int) (Math.random() * 10) % 12 + 1;
                    int minutes = 0;
                    fragmentProblemQuestionText.setText(random_hour + "시 정각을 그려주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(realCount + 1));
                    show_CDTProblem(random_hour);
                    break;
            }
            testNumber++;
            System.out.println("aaa");
                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onChangeFragment();
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 20000);  //20초 딜레이를 주는 것
        }else{
            finish();
        }
    }

    public void show_3problem(String str1, String str2, String str3, int answer) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putString("3", str3);
        newBundle.putInt("answer", answer + 1);
        inspectorTestProblemList3.setArguments(newBundle);
        inspectorTestProblemList3.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemList3);
        } else if (currentFragment == 0) {
            inspectorTestProblemList3.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList3);
        }
        currentFragment = 0;
        fragmentTransaction.commit();
        realCount++;
    }

    public void show_2problem(String str1, String str2, int answer) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putInt("answer", answer + 1);
        inspectorTestProblemList2.setArguments(newBundle);
        inspectorTestProblemList2.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemList2);
        } else if (currentFragment == 1) {
            inspectorTestProblemList2.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList2);
        }
        currentFragment = 1;
        fragmentTransaction.commit();
        realCount++;
    }

    public void show_CDTProblem(int answer) {
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putInt("answer", answer);
        inspectorTestProblemCDT.setArguments(newBundle);
        inspectorTestProblemCDT.activity = this;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemCDT);
        } else if (currentFragment == 7) {
            inspectorTestProblemCDT.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemCDT);
        }
        currentFragment = 7;
        fragmentTransaction.commit();
        realCount++;
    }
    //날짜 구하는거
    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }

    public String getSeason(int month) {
        if (month >= 3 && month <= 5) return "봄";
        else if (month >= 6 && month <= 8) return "여름";
        else if (month >= 9 && month <= 11) return "가을";
        else return "겨울";
    }

    public int Calculation(int num1, int num2, int operation) {
        switch (operation) {
            case 0:
                return num1 + num2;
            case 1:
                return num1 - num2;
            case 2:
                return num1 * num2;
            case 3:
                return num1 / num2;
        }
        return num1 + num2;
    }

    public String operation_return(int operation) {
        switch (operation) {
            case 0:
                return "+";
            case 1:
                return "-";
            case 2:
                return "X";
            case 3:
                return "/";
        }
        return "";
    }

    public void set_score(boolean collected) {
        if (collected) score += 1;
        System.out.println(score);
    }
}
