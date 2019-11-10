package org.dementia.chichi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InspectorTestProblemDrawText extends Fragment {
    String resultText = "";
    ImageButton InspectorTestProblemDrawTextButton;
    public InspectorTestScreen activity;
    TextView InspectorTestProblemDrawText;
    String answer;
    DrawViewCanvas canvasView;
    private String[] currentTopLabels;
    private HangulClassifier classifier;
    private static final String LABEL_FILE = "2350-common-hangul.txt";
    private static final String MODEL_FILE = "optimized_hangul_tensorflow.pb";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_draw_text, container, false);
        // Inflate the layout for this fragment
        canvasView = view.findViewById(R.id.InspectorTestProblemDrawTextCanvas);
        InspectorTestProblemDrawText = view.findViewById(R.id.InspectorTestProblemDrawText);
        InspectorTestProblemDrawTextButton = view.findViewById(R.id.InspectorTestProblemDrawTextButton);
        InspectorTestProblemDrawTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classify();
                canvasView.reset();
                canvasView.invalidate();
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
        InspectorTestProblemDrawText.setText(answer);
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
