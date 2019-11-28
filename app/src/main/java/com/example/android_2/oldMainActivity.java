package com.example.android_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class oldMainActivity extends AppCompatActivity {
    private Button mBtnDatos;
    private Button otrosDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_old);
    }
}
