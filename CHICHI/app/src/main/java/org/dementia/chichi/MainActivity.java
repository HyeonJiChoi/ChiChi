package org.dementia.chichi;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSIONS_RECORD_AUDIO = 300;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private static final int PERMISSION_STORAGE_CODE = 400;
    static String name, password;
    static int inpectorGuardian;
    AllowCallPermission allowCallPermission = new AllowCallPermission();
    Button button;
    static FirestoreManagement firestoreManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = "돈까쮸";
        password = "donkaka";
        button = findViewById(R.id.mainButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InspectorMainScreen.class);
                startActivity(intent);
            }
        });

        allowCallPermission.activity = this;
        firestoreManagement = new FirestoreManagement();
        if (!allowCallPermission.checkPermissionRecordAudio()) {
            //권한을 허용하지 않는 경우
            allowCallPermission.requestPermissionsRecordAudio();
        } else if (!allowCallPermission.checkPermissionContent()) {
            allowCallPermission.requestPermissionContent();
        } else if (!allowCallPermission.checkPermissionCall()) {
            //권한을 허용하지 않는 경우
            allowCallPermission.requestPermissionCall();
        } else if (!allowCallPermission.checkPermissionStorage()) {
            //권한을 허용하지 않는 경우
            allowCallPermission.requestPermissionStorage();
        }
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


        }
    }
}
