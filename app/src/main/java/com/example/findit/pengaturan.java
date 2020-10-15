package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

public class pengaturan extends AppCompatActivity {

    ListView about_app;
    ImageView Admin_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan);

        Admin_Login = (ImageView) findViewById(R.id.adminlog);
        Admin_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentLoadNewActivity = new Intent(v.getContext(), LoginAdmin.class);
                startActivity(intentLoadNewActivity);
            }
        });



        about_app = (ListView) findViewById(R.id.list1);
        String[] values = new String[]{"Tentang Aplikasi","Hubungi Kami" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(pengaturan.this, android.R.layout.simple_list_item_1, android.R.id.text1, values);

        about_app.setAdapter(adapter);

        about_app.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0) {
                    AboutAppDialog1();
                }

                if(position == 1) {
                    CallusDialog1();
                }
            }
        });
    }

    public void AboutAppDialog1(){
        final Dialog MyDialog1 = new Dialog(pengaturan.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.aboutapp);

        Button close = (Button)MyDialog1.findViewById(R.id.close);
        close.setEnabled(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog1.cancel();
            }
        });
        MyDialog1.show();
    }

    public void CallusDialog1(){
        final Dialog MyDialog1 = new Dialog(pengaturan.this);
        MyDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog1.setContentView(R.layout.hubungi);

        Button close = (Button)MyDialog1.findViewById(R.id.close);
        close.setEnabled(true);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog1.cancel();
            }
        });
        MyDialog1.show();
    }
}
