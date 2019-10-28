package org.dementia.chichi;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class InspectorTestProblemPicture extends Fragment {
    ImageView inspectorTestProblemPicture;
    StorageReference riversRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspector_test_problem_picture, container, false);
        inspectorTestProblemPicture = view.findViewById(R.id.inspectorTestProblemPicture);
        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle newBundle = getArguments();
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(riversRef.child("Orientation_picture/"+newBundle.get("picture_number").toString()+".png"))
                .into(inspectorTestProblemPicture);
    }
}
