package org.dementia.chichi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

public class InspectorTestScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspector_test_screen);
        setActionbar();
    }
    public void setActionbar() {
        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        Toolbar tb = (Toolbar) findViewById(R.id.toolbar) ;
        setSupportActionBar(tb) ;
    }
}
