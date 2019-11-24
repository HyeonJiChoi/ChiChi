package org.dementia.chichi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class InspectorTestProblemProfile extends Fragment {
    public InspectorTestScreen activity;
    Button TestProblemListProfile1, TestProblemListProfile2;
    CircleImageView TestProblemProfile1, TestProblemProfile2;
    Bundle newBundle;
    StorageReference riversRef;
    Timer timer;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_profile, container, false);
        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");

        TestProblemListProfile1 = view.findViewById(R.id.TestProblemListProfile1);
        TestProblemListProfile2 = view.findViewById(R.id.TestProblemListProfile2);
        TestProblemProfile1 = view.findViewById(R.id.TestProblemProfile1);
        TestProblemProfile2 = view.findViewById(R.id.TestProblemProfile2);

        TestProblemListProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newBundle.getInt("answer")==1){
                    activity.set_score(true);
                }
                activity.onChangeFragment();
            }
        });
        TestProblemListProfile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newBundle.getInt("answer")==2){
                    activity.set_score(true);
                }
                activity.onChangeFragment();

            }
        });

        return view;
    }
    public void onResume() {
        super.onResume();
        //액티비티에서 보낸 번들 확인
        newBundle = getArguments();
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(riversRef.child("userProfiles/"+newBundle.get("1").toString()+".jpg"))
                .into(TestProblemProfile1);
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(riversRef.child("userProfiles/"+newBundle.get("2").toString()+".jpg"))
                .into(TestProblemProfile2);
    }
}
