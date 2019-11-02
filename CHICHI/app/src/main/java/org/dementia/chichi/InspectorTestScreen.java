package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class InspectorTestScreen extends AppCompatActivity {
    AllowCallPermission allowCallPermission;
    public int testNumber = 0;
    int nextProblem = 0;
    public Map<String, Object> user;
    Set<String> todayProblems;
    Object[] problems;
    FragmentTransaction fragmentTransaction;
    InspectorBlankFragment firstFragment;
    FirestoreManagement firestoreManagement;
    TextView fragmentProblemQuestionText, fragmentProblemQuestionQNumber;
    InspectorTestProblemList3 inspectorTestProblemList3 = new InspectorTestProblemList3();
    InspectorTestProblemList2 inspectorTestProblemList2 = new InspectorTestProblemList2();
    InspectorTestProblemPicture inspectorTestProblemPicture = new InspectorTestProblemPicture();
    InspectorTestProblemProfile inspectorTestProblemProfile = new InspectorTestProblemProfile();
    InspectorTestProblemTextSpeak inspectorTestProblemTextSpeak = new InspectorTestProblemTextSpeak();
    InspectorTestProblemPictureSpeak inspectorTestProblemPictureSpeak = new InspectorTestProblemPictureSpeak();
    private int score = 0;
    private int currentFragment = 0;
    //음성인식을 위한 변수들
    Intent intent;
    SpeechRecognizer mRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firestoreManagement = MainActivity.firestoreManagement;
        user = firestoreManagement.user;
        todayProblems = firestoreManagement.todayProblems;
        setContentView(R.layout.activity_inspector_test_screen);
        setActionbar();
        firstFragment = (InspectorBlankFragment) getSupportFragmentManager().findFragmentById(R.id.inpectorTestProblemList_first);
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
                case 1: //계절알기
                    fragmentProblemQuestionText.setText("지금 계절은 무엇인가요?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

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

                case 2: // 기억 회상 (그림)
                    fragmentProblemQuestionText.setText("다음 그림을 보고\n무엇인지 맞춰주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    answer = (int) (Math.random() * 10) % 2;
                    //답의 위치를 랜덤으로 구하기
                    int select_picture_number = (int) (Math.random() * 10) % MainActivity.firestoreManagement.picture_number.size();
                    int other_picture_number = (int) (Math.random() * 10) % MainActivity.firestoreManagement.picture_number.size();
                    //겹치지 않게 하기
                    while (other_picture_number == select_picture_number) {
                        other_picture_number = (int) (Math.random() * 10) % MainActivity.firestoreManagement.picture_number.size();
                    }
                    Bundle bundle = new Bundle();
                    bundle.putInt("picture_number", select_picture_number);
                    inspectorTestProblemPicture.setArguments(bundle);

                    if (testNumber == 0) {
                        fragmentTransaction.hide(firstFragment)
                                .add(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    } else {
                        fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemPicture);
                    }
                    fragmentTransaction.commit();
                    currentFragment = 3;


                    final String[] objects = new String[2];
                    //날짜를 랜덤으로 구하기
                    for (int i = 0; i < 2; i++) {
                        if (i == answer) {
                            objects[i] = MainActivity.firestoreManagement.picture_number.get(Integer.toString(select_picture_number)).toString();
                        } else {
                            objects[i] = MainActivity.firestoreManagement.picture_number.get(Integer.toString(other_picture_number)).toString();
                        }
                    }

                    Timer timer = new Timer();
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            show_2problem(objects[0], objects[1], answer);
                        }
                    };
                    timer.schedule(timerTask, 2000);
                    break;
                case 3: //프로필사진 맞추기
                    fragmentProblemQuestionText.setText("누가 당신입니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    answer = (int) (Math.random() * 10) % 2;
                    String userId = MainActivity.name + "_" + MainActivity.password;
                    String otherId = MainActivity.firestoreManagement.userIds.get((int) (Math.random() * 10) % MainActivity.firestoreManagement.userIds.size()).toString();
                    while (userId.equals(otherId)) {
                        otherId = MainActivity.firestoreManagement.userIds.get((int) (Math.random() * 10) % MainActivity.firestoreManagement.userIds.size()).toString();
                    }

                    String[] ids = new String[2];

                    for (int i = 0; i < 2; i++) {
                        if (i == answer) {
                            ids[i] = userId;
                        } else {
                            ids[i] = otherId;
                        }
                    }
                    show_profileProblem(ids[0], ids[1], answer);
                    break;
                case 4: //이름문제
                    fragmentProblemQuestionText.setText("당신의 이름은 무엇입니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    answer = (int) (Math.random() * 10) % 2;
                    String userName = MainActivity.name;
                    String otherName = MainActivity.firestoreManagement.userIds.get((int) (Math.random() * 10) % MainActivity.firestoreManagement.userIds.size()).split("_")[0];
                    while (userName.equals(otherName)) {
                        otherName = MainActivity.firestoreManagement.userIds.get((int) (Math.random() * 10) % MainActivity.firestoreManagement.userIds.size()).split("_")[0];
                    }

                    String[] names = new String[2];

                    for (int i = 0; i < 2; i++) {
                        if (i == answer) {
                            names[i] = userName;
                        } else {
                            names[i] = otherName;
                        }
                    }
                    show_2problem(names[0], names[1], answer);
                    break;
                case 5: //나이문제
                    fragmentProblemQuestionText.setText("당신의 나이는 몇입니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    answer = (int) (Math.random() * 10) % 3;

                    String[] ages = new String[3];
                    int userAge = Integer.parseInt(MainActivity.firestoreManagement.user.get("age").toString());

                    //날짜를 랜덤으로 구하기
                    for (int i = 0; i < 3; i++) {
                        if (i == answer) {
                            ages[i] = Integer.toString(userAge);
                        } else if (i < answer) {
                            ages[i] = Integer.toString(userAge - (int) ((Math.random() * 10) % 10) - 5);
                        } else if (i > answer) {
                            ages[i] = Integer.toString(userAge + (int) ((Math.random() * 10) % 10) + 5);
                        }
                    }

                    show_3problem(ages[0], ages[1], ages[2], answer);
                    break;
                case 6: //계산문제
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
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
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
                case 7: //결혼여부
                    fragmentProblemQuestionText.setText("당신은 결혼 하셨습니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    String merried = MainActivity.firestoreManagement.user.get("married").toString();
                    if (merried.equals("true")) answer = 0;
                    else answer = 1;
                    show_2problem("O", "X", answer);
                    break;
                case 8: //집주소 문제
                    fragmentProblemQuestionText.setText("당신의 집주소는 무엇입니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    answer = (int) (Math.random() * 10) % 3;
                    String address = MainActivity.firestoreManagement.user.get("home").toString();
                    String[] homes = new String[3];

                    //날짜를 랜덤으로 구하기
                    for (int i = 0; i < 3; i++) {
                        if (i == answer) {
                            homes[i] = address;
                        } else {
                            String newAddress = MainActivity.firestoreManagement.userAddresses.get((int) (Math.random() * 10)
                                    % MainActivity.firestoreManagement.userIds.size());
                            while (Arrays.asList(homes).contains(newAddress) || newAddress.equals(address))
                                newAddress = MainActivity.firestoreManagement.userAddresses.get((int) (Math.random() * 10)
                                        % MainActivity.firestoreManagement.userIds.size());
                            homes[i] = newAddress;
                        }
                    }

                    show_3problem(homes[0], homes[1], homes[2], answer);
                    break;
                case 9: //자녀수 문제
                    fragmentProblemQuestionText.setText("당신은 몇명의 \n자녀가 있습니까?");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));

                    if (MainActivity.firestoreManagement.user.containsKey("child")) {
                        answer = (int) (Math.random() * 10) % 3;
                        String[] child = new String[3];

                        //값을 랜덤으로 구하기
                        for (int i = 0; i < 3; i++) {
                            if (i == answer) {
                                child[i] = MainActivity.firestoreManagement.user.get("child").toString();
                            } else if (i < answer) {
                                child[i] = Integer.toString(Math.abs(Integer.parseInt(MainActivity.firestoreManagement.user.get("child").toString())
                                        - (int) ((Math.random() * 10) % 5) + 1));
                            } else if (i > answer) {
                                child[i] = Integer.toString(Integer.parseInt(MainActivity.firestoreManagement.user.get("child").toString())
                                        + (int) ((Math.random() * 10) % 5) + 1);
                            }
                        }

                        show_3problem(child[0], child[1], child[2], answer);
                    }
                    break;
                case 10: //통화한 사람찾기
                    allowCallPermission = new AllowCallPermission();
                    allowCallPermission.activity = this;
                    // 권한이 있으면 call log를 가지고 옵니다.
                    allowCallPermission.setCallLog();
                    //시간 차이 구하기 위함
                    SimpleDateFormat f = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
                    long subSec = 0;
                    try {
                        subSec = (f.parse(getTimeString()).getTime() - f.parse(allowCallPermission.callTime).getTime()) / 1000;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //만약 날짜가 같고 시간이 2시간 이하이면,
                    if (getDateString().equals(allowCallPermission.callDate) && (subSec <= 7200) && allowCallPermission.didCall && allowCallPermission.callPerson != null) {
                        answer = (int) (Math.random() * 10) % 3;
                        String[] callPerson = new String[3];

                        // 권한이 있으면 call log를 가지고 옵니다.
                        fragmentProblemQuestionText.setText("가장 최근 통화한 사람은\n누구 입니까?");
                        fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                        allowCallPermission.getCallAddress();


                        //값을 랜덤으로 구하기
                        for (int i = 0; i < 3; i++) {
                            if (i == answer) {
                                callPerson[i] = allowCallPermission.callPerson;
                            } else {
                                String newName = allowCallPermission.AddressNames.get((int) (Math.random() * 10) % allowCallPermission.AddressCount);
                                while (Arrays.asList(callPerson).contains(newName) || newName.equals(allowCallPermission.callPerson))
                                    newName = allowCallPermission.AddressNames.get((int) (Math.random() * 10)
                                            % allowCallPermission.AddressCount);
                                callPerson[i] = newName;
                            }

                        }
                        show_3problem(callPerson[0], callPerson[1], callPerson[2], answer);
                    }
                    break;
                case 11: //단어 말하기

                    //답정하기
                    String speak_answer = MainActivity.firestoreManagement.picture_number.get(Integer.toString((int) (Math.random() * 10) % MainActivity.firestoreManagement.picture_number.size())).toString();
                    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    allowCallPermission = new AllowCallPermission();
                    allowCallPermission.activity = this;

                    inspectorTestProblemTextSpeak.intent = intent;
                    inspectorTestProblemTextSpeak.activity = this;
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                    mRecognizer.setRecognitionListener(inspectorTestProblemTextSpeak.listener);
                    inspectorTestProblemTextSpeak.mRecognizer = mRecognizer;
                    fragmentProblemQuestionText.setText("다음 단어를 말해주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    show_textSpeakProblem(speak_answer);

                    try {
                        mRecognizer.startListening(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    break;
                case 12: //그림말하기
                    //답정하기
                    int picture_number = (int) (Math.random() * 10) % MainActivity.firestoreManagement.picture_number.size();
                    String speak_picture_answer = MainActivity.firestoreManagement.picture_number.get(Integer.toString(picture_number)).toString();
                    intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
                    allowCallPermission = new AllowCallPermission();
                    allowCallPermission.activity = this;

                    inspectorTestProblemPictureSpeak.intent = intent;
                    inspectorTestProblemPictureSpeak.activity = this;
                    mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
                    mRecognizer.setRecognitionListener(inspectorTestProblemPictureSpeak.listener);
                    inspectorTestProblemPictureSpeak.mRecognizer = mRecognizer;
                    fragmentProblemQuestionText.setText("그림에 해당하는 단어를 말해주세요");
                    fragmentProblemQuestionQNumber.setText(Integer.toString(testNumber + 1));
                    show_textSpeakProblem(picture_number, speak_picture_answer);
                    try {
                        mRecognizer.startListening(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                    break;

            }
            testNumber++;
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
    }

    public void show_profileProblem(String str1, String str2, int answer) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("1", str1);
        newBundle.putString("2", str2);
        newBundle.putInt("answer", answer + 1);
        inspectorTestProblemProfile.setArguments(newBundle);
        inspectorTestProblemProfile.activity = this;

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemProfile);
        } else if (currentFragment == 2) {
            inspectorTestProblemProfile.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemProfile);
        }
        currentFragment = 2;
        fragmentTransaction.commit();
    }

    public void show_textSpeakProblem(String answer) {
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("answer", answer);
        inspectorTestProblemTextSpeak.setArguments(newBundle);
        inspectorTestProblemTextSpeak.activity = this;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemTextSpeak);
        } else if (currentFragment == 3) {
            inspectorTestProblemTextSpeak.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemTextSpeak);
        }
        currentFragment = 3;
        fragmentTransaction.commit();
    }
    public void show_textSpeakProblem(int picture_number, String answer) {
        //프래그먼트에 전달해줄 거 정해주기
        Bundle newBundle = new Bundle();
        newBundle.putString("answer", answer);
        newBundle.putInt("picture_number",picture_number);
        inspectorTestProblemPictureSpeak.setArguments(newBundle);
        inspectorTestProblemPictureSpeak.activity = this;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (testNumber == 0) {
            fragmentTransaction.hide(firstFragment)
                    .add(R.id.inpectorTestProblemList, inspectorTestProblemPictureSpeak);
        } else if (currentFragment == 4) {
            inspectorTestProblemPictureSpeak.onResume();
        } else {
            fragmentTransaction.replace(R.id.inpectorTestProblemList, inspectorTestProblemPictureSpeak);
        }
        currentFragment = 4;
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

    //날짜 구하는거
    public String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss", Locale.KOREA);
        String str_time = df.format(new Date());

        return str_time;
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
