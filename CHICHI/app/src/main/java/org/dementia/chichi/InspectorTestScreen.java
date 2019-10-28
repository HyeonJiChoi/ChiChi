package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
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
    InspectorTestProblemProfile inspectorTestProblemProfile = new InspectorTestProblemProfile();
    private int score = 0;

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
        if (testNumber < problems.length) {
            nextProblem = Integer.parseInt(problems[testNumber].toString());
            final int answer;
            String todayDate = getDateString();
            String[] wordDate = todayDate.split("-");
            //문제들을 순서대로 갖고와서 시험 보는거~
            switch (nextProblem) {
                case 0: //지금 연도 월
                    fragmentProblemQuestionText.setText("지금 연도와 월은\n언제인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    String[] Dates = new String[3];

                    //답의 위치를 랜덤으로 구하기
                    answer = (int)(Math.random()*10)%3;

                    //날짜를 랜덤으로 구하기
                    for(int i=0; i<3; i++){
                        if(i==answer){
                            Dates[i]=wordDate[0] + "년 "+wordDate[1]+"월";;
                        }
                        else{
                            Dates[i] = (Integer.parseInt(wordDate[0])+(int)(Math.random()*10)%5) + "년 "+((Integer.parseInt(wordDate[1])+(int)(Math.random()*10)%5)%12+1)+"월";
                        }
                    }

                    show_3problem(Dates[0], Dates[1], Dates[2],answer);

                    break;
                case 1: //계절알기
                    fragmentProblemQuestionText.setText("지금 계절은 무엇인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    answer = (int)(Math.random()*10)%2;

                    String now_seasons = getSeason(Integer.parseInt(wordDate[1]));
                    String other_seasons = getSeason((int)(Math.random()*10)%12);
                    //안겹치게 넣기
                    while(now_seasons.equals(other_seasons))
                        other_seasons = getSeason((int)(Math.random()*10)%12);

                    String[] seasons = new String[2];
                    //날짜를 랜덤으로 구하기
                    for(int i=0; i<2; i++){
                        if(i==answer){
                            seasons[i]=now_seasons;
                        }
                        else{
                            seasons[i]=other_seasons;
                        }
                    }
                    show_2problem(seasons[0], seasons[1], answer);

                    break;

                case 2: // 기억 회상 (그림)
                    fragmentProblemQuestionText.setText("다음 그림을 보고\n무엇인지 맞춰주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    answer = (int)(Math.random()*10)%2;
                    //답의 위치를 랜덤으로 구하기
                    int select_picture_number = (int)(Math.random()*10)%MainActivity.firestoreManagement.picture_number.size();
                    int other_picture_number = (int)(Math.random()*10)%MainActivity.firestoreManagement.picture_number.size();
                    //겹치지 않게 하기
                    while(other_picture_number==select_picture_number){
                        other_picture_number = (int)(Math.random()*10)%MainActivity.firestoreManagement.picture_number.size();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("picture_number",select_picture_number);
                    inspectorTestProblemPicture.setArguments(bundle);

                    if (testNumber == 0) {
                        fragmentTransaction.hide(currentFragment)
                                .add(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    } else {
                        fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    }
                    fragmentTransaction.commit();


                    final String[] objects = new String[2];
                    //날짜를 랜덤으로 구하기
                    for(int i=0; i<2; i++){
                        if(i==answer){
                            objects[i]=MainActivity.firestoreManagement.picture_number.get(Integer.toString(select_picture_number)).toString();
                        }
                        else{
                            objects[i]=MainActivity.firestoreManagement.picture_number.get(Integer.toString(other_picture_number)).toString();
                        }
                    }

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            show_2problem(objects[0], objects[1], answer);
                        }
                    };
                    timer.schedule(timerTask,2000);
                    break;
                case 3: //프로필사진 맞추기
                    fragmentProblemQuestionText.setText("누가 당신입니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    answer = (int)(Math.random()*10)%2;
                    String userId = "최현지chj159258357";
                    String otherId = "최현수090909";

                    show_profileProblem(userId, otherId, answer);

            }
            testNumber++;
        }
    }
    public void show_3problem(String str1, String str2, String str3, int answer){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putString("3", str3);
        newBundle.putInt("answer",answer+1);
        inspectorTestProblemList3.setArguments(newBundle);
        inspectorTestProblemList3.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(currentFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemList3);
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList3);
        }
        fragmentTransaction.commit();
    }
    public void show_2problem(String str1, String str2, int answer){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putInt("answer",answer+1);
        inspectorTestProblemList2.setArguments(newBundle);
        inspectorTestProblemList2.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(currentFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemList2);
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemList2);
        }
        fragmentTransaction.commit();
    }
    public void show_profileProblem(String str1, String str2, int answer){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putInt("answer",answer+1);
        inspectorTestProblemProfile.setArguments(newBundle);
        inspectorTestProblemProfile.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(currentFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemProfile);
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemProfile);
        }
        fragmentTransaction.commit();
    }

    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
    }

    //날짜 구하는거
    public String getDateString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        String str_date = df.format(new Date());

        return str_date;
    }
    public String getSeason(int month){
        if(month>=3 && month<=5) return "봄";
        else if (month>=6 && month<=8) return "여름";
        else if (month>=9 && month<=11) return "가을";
        else return "겨울";
    }
    public void set_score(boolean collected){
        if(collected) score+=1;
        System.out.println(score);
    }
}
