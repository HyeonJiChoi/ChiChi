package org.dementia.chichi;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InspectorTestProblemPictureSpeak extends Fragment {
    ImageView inspectorTestProblemPictureSpeak;
    ImageButton inspectorTestProblemPictureSpeakButton;
    TextView inspectorTestProblemPictureSpeakText;
    StorageReference riversRef;
    Intent intent;
    String answer;
    public InspectorTestScreen activity;
    public SpeechRecognizer mRecognizer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_picture_speak, container, false);
        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");
        inspectorTestProblemPictureSpeak = view.findViewById(R.id.inspectorTestProblemPictureSpeak);
        inspectorTestProblemPictureSpeakButton = view.findViewById(R.id.inspectorTestProblemPictureSpeakButton);
        inspectorTestProblemPictureSpeakText = view.findViewById(R.id.inspectorTestProblemPictureSpeakText);
        inspectorTestProblemPictureSpeakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mRecognizer.startListening(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        answer = newBundle.getString("answer");
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(riversRef.child("Orientation_picture/"+newBundle.get("picture_number").toString()+".png"))
                .into(inspectorTestProblemPictureSpeak);
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
            inspectorTestProblemPictureSpeakText.setText("듣고 있어요...");
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            inspectorTestProblemPictureSpeakText.setText("다시 버튼을 눌러 말씀해주세요");
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onResults(Bundle results) {
            inspectorTestProblemPictureSpeakText.setText("듣기 확인 중...");
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
