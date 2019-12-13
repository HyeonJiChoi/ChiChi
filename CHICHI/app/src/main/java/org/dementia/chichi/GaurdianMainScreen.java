package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GaurdianMainScreen extends AppCompatActivity {
    AllowCallPermission allowCallPermission = new AllowCallPermission();
    Button testButton;
    TextView name, home, number;
    CircleImageView inspectorMainScreenProfile;
    StorageReference riversRef;
    Toolbar tb;
    private DrawerLayout mDrawerLayout;
    public Map<String, Object> user;

    Button locationButton;
    TextView locationText;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gaurdian_main_screen);
        user = MainActivity.firestoreManagement.user;

        setActionbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.gaurdianNavigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.navigation_item_score:
                        //
                        break;

                    case R.id.navigation_item_location:
                        //
                        break;
                    case R.id.navigation_item_edit:
                        //
                        break;

                    case R.id.navigation_item_logout:
                        //
                        break;

                }

                return true;
            }

        });

        riversRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://chichi-cef38.appspot.com");
        testButton = findViewById(R.id.gaurdianMainScreenTestButton);
        inspectorMainScreenProfile = findViewById(R.id.gaurdianMainScreenProfile);
        name = findViewById(R.id.gaurdianMainScreenTextViewName);
        home = findViewById(R.id.gaurdianMainScreenTextViewHome);
        number = findViewById(R.id.gaurdianMainScreenTextViewNumber);
        name.setText(MainActivity.name);
        home.setText(MainActivity.firestoreManagement.user.get("home").toString());
        number.setText(MainActivity.firestoreManagement.user.get("number").toString());

        System.out.println("userProfiles/" + MainActivity.name + "_" + MainActivity.password + ".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(riversRef.child("userProfiles/" + MainActivity.name + "_" + MainActivity.password + ".jpg"))
                .into(inspectorMainScreenProfile);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int day = Integer.parseInt(MainActivity.firestoreManagement.user.get("day").toString());
                if(day==0){
                    Toast.makeText(getApplicationContext(), "아직 검사자가 검사를 본 기록이 없습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "현재까지 결과는 "+getTotalScore()+"점입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public float getTotalScore() {
        float TotalScore = 0;
        for (int i = 0; i < Integer.parseInt(user.get("day").toString()) ; i++)
            TotalScore += Float.parseFloat(MainActivity.firestoreManagement.user.get(i + "_day_score").toString());

        return TotalScore;
    }

    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        tb = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(tb) ;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
