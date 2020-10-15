package com.example.findit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Admin_DetailInfoSpot extends AppCompatActivity {

    private String DataSpotID;
    private String  Img_1, Img_2, Img_3, Nama, Deskripsi, Biaya, Wkt_Rekom, Kategori, Latitude, Longitude, Alamat;


    private DatabaseReference databaseRef;
    public static final String Database_Path = "All_DataSpot_Database";

    data_spot DataSpot = new data_spot();



    public static Button btn_updatedata, btn_hapusdata, btn_maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_info_spot);

        databaseRef = FirebaseDatabase.getInstance().getReference(Database_Path);

        ImageView img_view_1 = (ImageView)findViewById(R.id.detailgambar_1);
        ImageView img_view_2 = (ImageView) findViewById(R.id.detailgambar_2);
        ImageView img_view_3 = (ImageView) findViewById(R.id.detailgambar_3);
        TextView nama_view = (TextView) findViewById(R.id.nama_spot_foto);
        TextView deskripsi_view = (TextView) findViewById(R.id.deskripsi_spot);
        TextView biaya_view = (TextView) findViewById(R.id.biaya_spot);
        TextView rekomWkt_view = (TextView) findViewById(R.id.rekomendasi_wkt_spot);
        TextView kategori_view = (TextView) findViewById(R.id.kategori_spot);
        TextView alamat_spot   = (TextView) findViewById(R.id.alamatSpot);

        btn_updatedata = (Button) findViewById(R.id.btn_editdata);
        // btn_updatedata.setVisibility(View.GONE);



        btn_hapusdata = (Button) findViewById(R.id.btn_hapusdata);
        //  btn_hapusdata.setVisibility(View.GONE);

        Intent intent = getIntent();
        DataSpotID = intent.getStringExtra("IDSpot");
        // System.out.println(DataSpotID);
        Img_2 = intent.getStringExtra("imageURL_2");
        Img_1 = intent.getStringExtra("imageURL_1");
        Img_3 = intent.getStringExtra("imageURL_3");
        Nama = intent.getStringExtra("nama_spot");
        Deskripsi = intent.getStringExtra("deskripsi");
        Biaya = intent.getStringExtra("biaya");
        Wkt_Rekom = intent.getStringExtra("rekom_wkt");
        Kategori = intent.getStringExtra("kategori");
        Latitude = intent.getStringExtra("Lat");
        Longitude = intent.getStringExtra("Long");
        Alamat = intent.getStringExtra("alamat");


        Glide.with(this).load(Img_1).into(img_view_1);
        Glide.with(this).load(Img_2).into(img_view_2);
        Glide.with(this).load(Img_3).into(img_view_3);

        nama_view.setText(Nama);
        deskripsi_view.setText(Deskripsi);
        biaya_view.setText(Biaya);
        rekomWkt_view.setText(Wkt_Rekom);
        kategori_view.setText(Kategori);
        alamat_spot.setText(Alamat);


        btn_updatedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_DetailInfoSpot.this, UpdateSpot.class);
                intent.putExtra("IDSpot", DataSpotID);
                intent.putExtra("nama_spot", Nama);
                intent.putExtra("deskripsi", Deskripsi);
                intent.putExtra("biaya", Biaya);
                intent.putExtra("rekom_wkt", Wkt_Rekom);
                intent.putExtra("kategori", Kategori);
                intent.putExtra("imageURL_1", Img_1);
                intent.putExtra("imageURL_2", Img_2);
                intent.putExtra("imageURL_3", Img_3);
                intent.putExtra("Lat", Latitude);
                intent.putExtra("Long", Longitude);
                intent.putExtra("alamat", Alamat);
                startActivity(intent);
            }
        });

        btn_hapusdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Admin_DetailInfoSpot.this);
                alertDialogBuilder.setTitle("Anda Ingin Menghapus Data Ini ?");
                alertDialogBuilder.setMessage("Klik Ya Untuk Menghapus Data").setIcon(R.drawable.ic_tanya_24).setCancelable(false).setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseRef.child(DataSpotID).removeValue();
                        System.out.println(DataSpotID);
                        removeImageFile(DataSpot.getImageURL_1());
                        removeImageFile(DataSpot.getImageURL_2());
                        removeImageFile(DataSpot.getImageURL_3());
                        startActivity(new Intent(Admin_DetailInfoSpot.this, LihatSpot.class));
                        finish();
                        Toast.makeText(getApplicationContext(), "Berhasil Hapus Data", Toast.LENGTH_SHORT).show();
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

        FloatingActionButton fab = findViewById(R.id.fab_back);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_DetailInfoSpot.this, LihatSpot.class));
            }
        });
    }

    private void removeImageFile(String url) {
        if (!url.contains("null")) {
            String urlStr = url.split("Photo_Spot_Sample%2F")[1];
            String urlStr2 = urlStr.split("\\?")[0];
            StorageReference fileDelete = FirebaseStorage.getInstance().getReference().child("Photo_Spot_Sample/" + urlStr2);
            fileDelete.delete().addOnSuccessListener(aVoid -> {

            });
        }
    }
}
