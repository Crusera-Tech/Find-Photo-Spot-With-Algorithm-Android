package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MenuUtama extends AppCompatActivity implements View.OnClickListener {

    ImageView menu1, menu2, menu3, menu4;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);

        menu1 = (ImageView) findViewById(R.id.klik_titik_spot);
        menu2 = (ImageView) findViewById(R.id.klik_lihat_spot);
        menu3 = (ImageView) findViewById(R.id.klik_terdekat);
        menu4 = (ImageView) findViewById(R.id.klik_pengaturan);

        menu1.setOnClickListener(this);
        menu2.setOnClickListener(this);
        menu3.setOnClickListener(this);
        menu4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.klik_titik_spot:
                Intent intent = new Intent(MenuUtama.this, MapsUser.class);
                startActivity(intent);
                break;

            case R.id.klik_lihat_spot:
                Intent intent2 = new Intent(MenuUtama.this, LihatSpot.class);
                startActivity(intent2);
                break;

            case R.id.klik_terdekat:
                Intent intent3 = new Intent(MenuUtama.this, ListTerdekat.class);
                startActivity(intent3);
                break;

            case R.id.klik_pengaturan:
                Intent intent4 = new Intent(MenuUtama.this, pengaturan.class);
                startActivity(intent4);
                break;
        }

    }
}
