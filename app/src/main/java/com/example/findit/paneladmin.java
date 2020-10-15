package com.example.findit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class paneladmin extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private Button btn_LogOut;
    private FirebaseAuth.AuthStateListener fStateListener;
    private Button btn_menu_utama, btn_tambahspot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paneladmin);

        btn_LogOut = (Button) findViewById(R.id.btn_signout);
        btn_menu_utama = (Button) findViewById(R.id.btn_view);
        btn_tambahspot = (Button) findViewById(R.id.btn_tambah);

        myAuth = FirebaseAuth.getInstance();

        btn_LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // DetailInfoSpot.btn_updatedata.setVisibility(View.GONE);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(paneladmin.this);
                alertDialogBuilder.setTitle("LOGOUT");
                alertDialogBuilder.setMessage("Anda Ingin Logout ?").setIcon(R.drawable.ic_tanya_24).setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myAuth.signOut();
                        startActivity(new Intent(paneladmin.this, LoginAdmin.class));
                        finish();
                        Toast.makeText(paneladmin.this, "Berhasil Logout", Toast.LENGTH_LONG).show();

                    }
                }).setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btn_menu_utama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(paneladmin.this, Admin_LihatSpot.class));
                finish();
            }
        });

        btn_tambahspot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(paneladmin.this, TambahSpot.class));
                finish();
            }
        });
    }
}
