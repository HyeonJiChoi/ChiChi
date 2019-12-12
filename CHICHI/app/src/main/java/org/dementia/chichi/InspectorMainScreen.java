package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class InspectorMainScreen extends AppCompatActivity implements LocationListener {
    private static final int PERMISSIONS_RECORD_AUDIO = 300;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSION_STORAGE_CODE = 400;
    private static final int PERMISSION_LOCATION_CODE = 500;
    private static final String TAG = "InspectorMainScreen";
    AllowCallPermission allowCallPermission = new AllowCallPermission();
    Button testButton;
    TextView name, home, number;
    CircleImageView inspectorMainScreenProfile;
    StorageReference riversRef;
    Toolbar tb;
    private DrawerLayout mDrawerLayout;

    Button locationButton;
    TextView locationText;
    LocationManager locationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_main_screen);
        //액션바 설정
        setActionbar();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
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
        testButton = findViewById(R.id.inspectorMainScreenTestButton);
        inspectorMainScreenProfile = findViewById(R.id.inspectorMainScreenProfile);
        name = findViewById(R.id.inspectorMainScreenTextViewName);
        home = findViewById(R.id.inspectorMainScreenTextViewHome);
        number = findViewById(R.id.inspectorMainScreenTextViewNumber);
        locationButton = findViewById(R.id.locationButton);
        name.setText(MainActivity.name);
        home.setText(MainActivity.firestoreManagement.user.get("home").toString());
        number.setText(MainActivity.firestoreManagement.user.get("number").toString());

        System.out.println("userProfiles/"+MainActivity.name +"_"+MainActivity.password+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(riversRef.child("userProfiles/"+MainActivity.name +"_"+MainActivity.password+".jpg"))
                .into(inspectorMainScreenProfile);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorTestScreen.class);
                startActivity(intent);
            }
        });

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0));

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

            Log.i(TAG, "Address: "+address + "\n" + "City: "+city + "\n"+"State: " +state+ "\n"+ "Country: "+country+"\n"+ "Postal code: "+postalCode);

        }catch(Exception e)
        {

        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(InspectorMainScreen.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    // permission 요청 결과
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean callLogAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callLogAccepted) {
                        if (!allowCallPermission.checkPermissionContent()) {
                            allowCallPermission.requestPermissionContent();
                        }
                    } else finish();
                }
                break;
            case PERMISSIONS_REQUEST_READ_CONTACTS:
                if (grantResults.length > 0) {
                    boolean callLogAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callLogAccepted) {
                        if (!allowCallPermission.checkPermissionStorage()) {
                            //권한을 허용하지 않는 경우
                            allowCallPermission.requestPermissionStorage();
                        }
                    } else finish();
                }
                break;
            case PERMISSIONS_RECORD_AUDIO:
                if (grantResults.length > 0) {
                    boolean callLogAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callLogAccepted) {
                        if (!allowCallPermission.checkPermissionCall()) {
                            //권한을 허용하지 않는 경우
                            allowCallPermission.requestPermissionCall();
                        }
                    } else finish();
                }
                break;
            case PERMISSION_STORAGE_CODE:
                if (grantResults.length > 0) {
                    boolean callLogAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callLogAccepted) {
                    } else finish();
                }
                break;

            case PERMISSION_LOCATION_CODE:
                if(grantResults.length>0){
                    boolean callLogAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (callLogAccepted) {
                        if (!allowCallPermission.checkPermissionLocation()) {
                            //권한을 허용하지 않는 경우
                            allowCallPermission.requestPermissionLocation();
                        }
                    } else finish();
                }
                break;
        }
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
