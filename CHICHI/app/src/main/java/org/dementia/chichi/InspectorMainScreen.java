package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class InspectorMainScreen extends AppCompatActivity {
    Button testButton;
    CircleImageView inspectorMainScreenProfile;
    StorageReference riversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main_screen);
        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");;
        testButton = findViewById(R.id.inspectorMainScreenTestButton);
        inspectorMainScreenProfile = findViewById(R.id.inspectorMainScreenProfile);;

        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(riversRef.child("userProfiles/"+"최현지chj159258357"+".jpg"))
                .into(inspectorMainScreenProfile);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorTestScreen.class);
                startActivity(intent);
            }
        });
    }
}
