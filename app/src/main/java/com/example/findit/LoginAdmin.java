package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginAdmin extends AppCompatActivity {

    private DatabaseReference myDatabase;
    private FirebaseAuth myAuth;
    private EditText txtEmail, txtPasswd;
    private Button btnMasuk;
    private Button btnKembali;
    private FirebaseAuth.AuthStateListener fStateListener;

    private static final String TAG = LoginAdmin.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);


        //Get Firebase auth instance

        myAuth = FirebaseAuth.getInstance();

        if(myAuth.getCurrentUser() != null){
            startActivity(new Intent(LoginAdmin.this, paneladmin.class));
            finish();
        }

        txtEmail = (EditText) findViewById(R.id.admin_email);
        txtPasswd = (EditText) findViewById(R.id.admin_passwd);
        btnMasuk = (Button) findViewById(R.id.btn_masuk);
        btnKembali = (Button) findViewById(R.id.btn_back);

        //get firebase auth instance

        myAuth = FirebaseAuth.getInstance();

        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = txtEmail.getText().toString();
                final String password = txtPasswd.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Harap Masukkan Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Harap Masukkan Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginAdmin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());

                        /**
                         * Jika sign in gagal, tampilkan pesan ke user. Jika sign in sukses
                         * maka auth state listener akan dipanggil dan logic untuk menghandle
                         * signed in user bisa dihandle di listener.
                         */


                        if (!task.isSuccessful()) {

                            Log.w(TAG, "SignInWithEmail:failed", task.getException());
                            Toast.makeText(LoginAdmin.this, "Proses Login Gagal\n", Toast.LENGTH_SHORT).show();

                        } else {

                            /**
                             * set enabled pada button logout apabila user berhasil login
                             */
                            Toast.makeText(LoginAdmin.this, "Login Berhasil\n" + "Email " + email, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginAdmin.this, paneladmin.class);
                            startActivity(intent);
                            finish();
                           // DetailInfoSpot.btn_updatedata.setVisibility(View.VISIBLE);
                          //  DetailInfoSpot.btn_hapusdata.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginAdmin.this, MenuUtama.class));
                finish();
            }
        });
    }
}


