package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

import org.w3c.dom.Text;

public class PilihanMenu extends AppCompatActivity {

    String arrayMenu[]= {"Lihat Spot", "Pengaturan", "Lihat Spot Terdekat"};

    CircleMenu circleMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan_menu);


        circleMenu = (CircleMenu) findViewById(R.id.menu_circle);
        circleMenu.setMainMenu(Color.parseColor("#1976d2"), R.mipmap.ic_fyos_big, R.drawable.ic_cancel_black_24dp)
                .addSubMenu(Color.parseColor("#ffb74d"), R.mipmap.ic_maps_big)
                .addSubMenu(Color.parseColor("#4dd0e1"), R.mipmap.ic_setting_big)
                .addSubMenu(Color.parseColor("#ffe082"), R.mipmap.ic_view_big)
                .addSubMenu(Color.parseColor("#ffe082"), R.mipmap.ic_fyos_24)
                .addSubMenu(Color.parseColor("#ffb74d"), R.drawable.ic_markeruser)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int i) {
                        if (i == 0){
                            Intent intent = new Intent(PilihanMenu.this, MapsUser.class);
                            startActivity(intent);
                        }

                        if (i==1) {
                            Intent intent = new Intent(PilihanMenu.this, pengaturan.class);
                            startActivity(intent);
                        }

                        if (i == 2) {
                            Intent intent = new Intent(PilihanMenu.this, LihatSpot.class);
                            startActivity(intent);
                        }

                        if (i == 3) {
                            Intent intent = new Intent(PilihanMenu.this, ListTerdekat.class);
                            startActivity(intent);
                        }

                        if (i == 3) {
                            Intent intent = new Intent(PilihanMenu.this, MapsRute.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
