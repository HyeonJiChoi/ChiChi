package org.dementia.chichi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import static android.speech.tts.TextToSpeech.ERROR;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class InspectorTestProblemDrawSound extends Fragment {
    private TextToSpeech tts;
    String resultText = "";
    public InspectorTestScreen activity;
    Button RepeatButton;
    ImageButton inputButton;
    String answer;
    DrawViewCanvas canvasView;
    private String[] currentTopLabels;
    private HangulClassifier classifier;
    private static final String LABEL_FILE = "2350-common-hangul.txt";
    private static final String MODEL_FILE = "optimized_hangul_tensorflow.pb";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_draw_sound, container, false);
        tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });

        RepeatButton = view.findViewById(R.id.InspectorTestProblemDrawSoundRepeatButton);
        canvasView= view.findViewById(R.id.InspectorTestProblemDrawSoundCanvas);
        inputButton = view.findViewById(R.id.InspectorTestProblemDrawSoundButton);
        inputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify();
                canvasView.reset();
                canvasView.invalidate();
            }
        });
        RepeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tts.speak(answer,TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        loadModel();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        answer = newBundle.getString("answer");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                tts.setSpeechRate(0.5f);
                tts.speak(answer,TextToSpeech.QUEUE_FLUSH, null);
            }
        };
        timer.schedule(timerTask, 500);
        canvasView.onResume();
    }

    /**
     * Perform the classification, updating UI elements with the results.
     */
    private void classify() {
        float pixels[] = canvasView.getPixelData();
        currentTopLabels = classifier.classify(pixels);
        resultText = resultText.concat(currentTopLabels[0]);
        if (resultText.equals(answer)) {
            activity.set_score(true);
            activity.onChangeFragment();
        } else if (resultText.length() == answer.length()) {
            activity.onChangeFragment();
        }
    }

    /**
     * Load pre-trained model in memory.
     */
    private void loadModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = HangulClassifier.create(getActivity().getAssets(),
                            MODEL_FILE, LABEL_FILE, canvasView.FEED_DIMENSION,
                            "input", "keep_prob", "output");
                } catch (final Exception e) {
                    throw new RuntimeException("Error loading pre-trained model.", e);
                }
            }
        }).start();
    }

}
