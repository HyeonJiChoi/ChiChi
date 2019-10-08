package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    ActionBar ab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main_screen);

        setActionbar();

        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");;
        testButton = findViewById(R.id.inspectorMainScreenTestButton);
        inspectorMainScreenProfile = findViewById(R.id.inspectorMainScreenProfile);

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
        //액션바 만들기
        ab = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        ab.setDisplayShowCustomEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);            //액션바 아이콘을 업 네비게이션 형태로 표시합니다.
        ab.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정합니다.
        ab.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.
        ab.setHomeAsUpIndicator(R.drawable.menu_icon);

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        View mCustomView = LayoutInflater.from(this).inflate(R.layout.actionbar_inspector_main_screen, null);
        ab.setCustomView(mCustomView);
    }
}
