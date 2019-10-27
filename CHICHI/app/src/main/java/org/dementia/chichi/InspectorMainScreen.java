package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class InspectorMainScreen extends AppCompatActivity {
    Button testButton;
    TextView name, home, number;
    CircleImageView inspectorMainScreenProfile;
    StorageReference riversRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main_screen);
        setActionbar();
        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");
        testButton = findViewById(R.id.inspectorMainScreenTestButton);
        inspectorMainScreenProfile = findViewById(R.id.inspectorMainScreenProfile);
        name = findViewById(R.id.inspectorMainScreenTextViewName);
        home = findViewById(R.id.inspectorMainScreenTextViewHome);
        number = findViewById(R.id.inspectorMainScreenTextViewNumber);

        name.setText(MainActivity.name);
        home.setText(MainActivity.firestoreManagement.user.get("home").toString());
        number.setText(MainActivity.firestoreManagement.user.get("number").toString());

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
    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(tb) ;
    }
}
