package org.dementia.chichi;


import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InspectorTestProblemTextSpeak extends Fragment {
    public InspectorTestScreen activity;
    public SpeechRecognizer mRecognizer;
    Intent intent;
    ImageButton speakButton;
    TextView inspectorTestProblemTextSpeakText, inspectorTestProblemTextSpeak;
    String answer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_text_speak, container, false);


        speakButton = view.findViewById(R.id.inspectorTestProblemTextSpeakButton);
        inspectorTestProblemTextSpeakText = view.findViewById(R.id.inspectorTestProblemTextSpeakText);
        inspectorTestProblemTextSpeak = view.findViewById(R.id.inspectorTestProblemTextSpeak);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowCallPermission allowCallPermission = new AllowCallPermission();
                allowCallPermission.activity = activity;
                if (allowCallPermission.checkPermissionRecordAudio()) {
                    //권한을 허용하지 않는 경우
                    allowCallPermission.requestPermissionsRecordAudio();
                } else {
                    //권한을 허용한 경우
                    try {
                        mRecognizer.startListening(intent);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        answer = newBundle.getString("answer");
        inspectorTestProblemTextSpeak.setText(answer);
    }

    //음성인식
    public RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
            inspectorTestProblemTextSpeakText.setText("듣고 있어요...");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            inspectorTestProblemTextSpeakText.setText("다시 버튼을 눌러 말씀해주세요");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onResults(Bundle results) {
            inspectorTestProblemTextSpeakText.setText("듣기 확인 중...");
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = results.getStringArrayList(key);
            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);
            System.out.println(rs[0]);
            if (rs[0].equals(answer)) {
                activity.set_score(true);
            }
            activity.onChangeFragment();
        }
    };


}
