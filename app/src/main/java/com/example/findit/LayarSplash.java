package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

public class LayarSplash extends AppCompatActivity {

    TextView tv_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layar_splash);


        setContentView(R.layout.activity_layar_splash);

        tv_splash = (TextView) findViewById(R.id.textsplash);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MenuUtama.class));
                finish();
            }
        }, 3000L);
    }
}
