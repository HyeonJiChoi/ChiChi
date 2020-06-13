package org.dementia.chichi;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class InspectorTestProblemCDT extends Fragment {
    DrawCDTCanvas drawCDTCanvas;
    int answer;
    public InspectorTestScreen activity;
    ImageButton resetButton;
    Button completeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_cdt, container, false);
        drawCDTCanvas = view.findViewById(R.id.InspectorTestProblemCDTCanvas);
        resetButton = view.findViewById(R.id.InspectorTestProblemCDTButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawCDTCanvas.reset();
            }
        });
        completeButton = view.findViewById(R.id.InspectorTestProblemCDTCompleteButton);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Bundle> moving_points = drawCDTCanvas.moving_points;
                if (moving_points.size() > 2) {
                    int[] max_index = new int[2];
                    int[] second_index = new int[2];
                    Float[] newdatas = new Float[moving_points.size() / 2];
                    int j = 0;
                    for (int i = 0; i < moving_points.size(); i += 2) {
                        float distance = (float) Math.sqrt(Math.pow(Float.parseFloat(moving_points.get(i).get("X").toString()) - Float.parseFloat(moving_points.get(i + 1).get("X").toString()), 2)
                                + Math.pow(Float.parseFloat(moving_points.get(i).get("Y").toString()) - Float.parseFloat(moving_points.get(i + 1).get("Y").toString()), 2));
                        newdatas[j] = distance;
                        j++;
                    }
                    ArrayList<Float> datas = new ArrayList<Float>(Arrays.asList(newdatas));
                    float max_length = Collections.max(datas);
                    max_index[0] = datas.indexOf(max_length) * 2;
                    max_index[1] = max_index[0] + 1;
                    float first_distance = (float) Math.sqrt(Math.pow(Float.parseFloat(moving_points.get(max_index[0]).get("X").toString()) - drawCDTCanvas.width / 2, 2)
                            + Math.pow(Float.parseFloat(moving_points.get(max_index[0]).get("Y").toString()) - drawCDTCanvas.height / 2, 2));
                    float second_distance = (float) Math.sqrt(Math.pow(Float.parseFloat(moving_points.get(max_index[1]).get("X").toString()) - drawCDTCanvas.width / 2, 2)
                            + Math.pow(Float.parseFloat(moving_points.get(max_index[1]).get("Y").toString()) - drawCDTCanvas.height / 2, 2));
                    if (first_distance > second_distance) {
                        int tmp = max_index[0];
                        max_index[0] = max_index[1];
                        max_index[1] = tmp;
                    }
                    datas.remove(max_length);
                    float second_length = Collections.max(datas);
                    second_index[0] = (datas.indexOf(second_length) + 1) * 2;
                    second_index[1] = second_index[0] + 1;
                    first_distance = (float) Math.sqrt(Math.pow(Float.parseFloat(moving_points.get(second_index[0]).get("X").toString()) - drawCDTCanvas.width / 2, 2)
                            + Math.pow(Float.parseFloat(moving_points.get(second_index[0]).get("Y").toString()) - drawCDTCanvas.height / 2, 2));
                    second_distance = (float) Math.sqrt(Math.pow(Float.parseFloat(moving_points.get(second_index[1]).get("X").toString()) - drawCDTCanvas.width / 2, 2)
                            + Math.pow(Float.parseFloat(moving_points.get(second_index[1]).get("Y").toString()) - drawCDTCanvas.height / 2, 2));
                    if (first_distance > second_distance) {
                        int tmp = second_index[0];
                        second_index[0] = second_index[1];
                        second_index[1] = tmp;
                    }

                    int width = drawCDTCanvas.width, height = drawCDTCanvas.height;
                    if (max_length > width / 4) {
                        activity.CDTscore++;
                        if (width / 2 + width / 25 >= Float.parseFloat(moving_points.get(max_index[0]).get("X").toString()) && width / 2 - width / 25 <= Float.parseFloat(moving_points.get(max_index[0]).get("X").toString())
                                && height + height / 25 >= Float.parseFloat(moving_points.get(max_index[0]).get("Y").toString()) && height / 2 - height / 25 <= Float.parseFloat(moving_points.get(max_index[0]).get("Y").toString())) {
                            activity.CDTscore++;
                            if (width / 2 + width / 25 >= Float.parseFloat(moving_points.get(second_index[0]).get("X").toString()) && width / 2 - width / 25 <= Float.parseFloat(moving_points.get(max_index[0]).get("X").toString())
                                    && height + height / 25 >= Float.parseFloat(moving_points.get(second_index[0]).get("Y").toString()) && height / 2 - height / 25 <= Float.parseFloat(moving_points.get(max_index[0]).get("Y").toString())) {
                                activity.CDTscore++;
                                //진짜 측정
                                //분침
                                int sample_answer = 0;
                                int previous_answer = 11;
                                int after_answer = 1;

                                //지금 정각의 이전 현재 이후 분침을 알아냄
                                Bundle previous = drawCDTCanvas.number_points.get(previous_answer);
                                Bundle current = drawCDTCanvas.number_points.get(sample_answer);
                                Bundle after = drawCDTCanvas.number_points.get(after_answer);
                                float x_minutes_1 = (float) (previous.getDouble("X") - width / 2 + current.getDouble("X") - width / 2) / 2;
                                float x_minutes_details_1 = x_minutes_1 / 2;
                                float x_minutes_2 = (float) (current.getDouble("X") - width / 2 + after.getDouble("X") - width / 2) / 2;
                                float x_minutes_details_2 = x_minutes_2 / 2;
                                float y_minutes_1 = (float) ((previous.getDouble("Y") - height / 2 + current.getDouble("Y") - height / 2) / 2);
                                float y_minutes_2 = (float) ((current.getDouble("Y") - height / 2 + after.getDouble("Y") - height / 2) / 2);
                                float minutes_a_1 = y_minutes_1 / x_minutes_1;
                                float minutes_a_2 = y_minutes_2 / x_minutes_2;
                                float minutes_a_details_1 = y_minutes_1 / x_minutes_details_1;
                                float minutes_a_details_2 = y_minutes_2 / x_minutes_details_2;

                                float minutes_user_x = Float.parseFloat(moving_points.get(max_index[1]).get("X").toString()) - width / 2;
                                float minutes_user_y = Float.parseFloat(moving_points.get(max_index[1]).get("Y").toString()) - height / 2;
                                if (minutes_a_1 * minutes_user_x < minutes_user_y && minutes_a_2 * minutes_user_x < minutes_user_y) {
                                    activity.CDTscore++;
                                    if (minutes_a_details_1 * minutes_user_x < minutes_user_y && minutes_a_details_2 * minutes_user_x < minutes_user_y) {
                                        activity.CDTscore++;
                                    }
                                }

                                //시침
                                sample_answer = answer;
                                previous_answer = sample_answer - 1;
                                after_answer = sample_answer + 1;
                                if (sample_answer == 12) {
                                    sample_answer = 0;
                                    previous_answer = 11;
                                    after_answer = 1;
                                } else if (sample_answer == 11) {
                                    after_answer = 0;
                                }

                                previous = drawCDTCanvas.number_points.get(previous_answer);
                                current = drawCDTCanvas.number_points.get(sample_answer);
                                after = drawCDTCanvas.number_points.get(after_answer);
                                x_minutes_1 = (float) (previous.getDouble("X") - width / 2 + current.getDouble("X") - width / 2) / 2;
                                x_minutes_details_1 = x_minutes_1 / 2;
                                x_minutes_2 = (float) (current.getDouble("X") - width / 2 + after.getDouble("X") - width / 2) / 2;
                                x_minutes_details_2 = x_minutes_2 / 2;
                                y_minutes_1 = (float) ((previous.getDouble("Y") - height / 2 + current.getDouble("Y") - height / 2) / 2);
                                y_minutes_2 = (float) ((current.getDouble("Y") - height / 2 + after.getDouble("Y") - height / 2) / 2);
                                minutes_a_1 = y_minutes_1 / x_minutes_1;
                                minutes_a_2 = y_minutes_2 / x_minutes_2;
                                minutes_a_details_1 = y_minutes_1 / x_minutes_details_1;
                                minutes_a_details_2 = y_minutes_2 / x_minutes_details_2;

                                minutes_user_x = Float.parseFloat(moving_points.get(second_index[1]).get("X").toString()) - width / 2;
                                minutes_user_y = Float.parseFloat(moving_points.get(second_index[1]).get("Y").toString()) - height / 2;
                                if (sample_answer == 0) {
                                    if (minutes_a_1 * minutes_user_x < minutes_user_y && minutes_a_2 * minutes_user_x < minutes_user_y) {
                                        activity.CDTscore++;
                                        if (minutes_a_details_1 * minutes_user_x < minutes_user_y && minutes_a_details_2 * minutes_user_x < minutes_user_y) {
                                            activity.CDTscore++;
                                        }
                                    }
                                }
                                if (sample_answer == 6) {
                                    if (minutes_a_1 * minutes_user_x > minutes_user_y && minutes_a_2 * minutes_user_x > minutes_user_y) {
                                        activity.CDTscore++;
                                        if (minutes_a_details_1 * minutes_user_x > minutes_user_y && minutes_a_details_2 * minutes_user_x > minutes_user_y) {
                                            activity.CDTscore++;
                                        }
                                    }
                                } else if (sample_answer > 0 && sample_answer < 6) {
                                    if (minutes_a_1 * minutes_user_x > minutes_user_y && minutes_a_2 * minutes_user_x < minutes_user_y) {
                                        activity.CDTscore++;
                                        if (minutes_a_details_1 * minutes_user_x > minutes_user_y && minutes_a_details_2 * minutes_user_x < minutes_user_y) {
                                            activity.CDTscore++;
                                        }
                                    }
                                } else {
                                    if (minutes_a_1 * minutes_user_x < minutes_user_y && minutes_a_2 * minutes_user_x > minutes_user_y) {
                                        activity.CDTscore++;
                                        if (minutes_a_details_1 * minutes_user_x < minutes_user_y && minutes_a_details_2 * minutes_user_x > minutes_user_y) {
                                            activity.CDTscore++;
                                        }
                                    }
                                }
                            }
                            System.out.println(activity.CDTscore);
                        }
                    }
                }
                activity.onChangeFragment();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        answer = newBundle.getInt("answer");
    }
}
