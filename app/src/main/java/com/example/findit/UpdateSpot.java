package com.example.findit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class UpdateSpot extends AppCompatActivity {

    private String UploadDataID;
    private EditText Updt_NamaSpot, Updt_Deskripsi, Updt_Biaya, Updt_Kategori, Updt_RekomWkt, Updt_Longitude, Updt_Latitude, Updt_Alamat;
    private ImageView gambarview_1, gambarview_2, gambarview_3;
    data_spot DataSpot = new data_spot();

    StorageReference storageRef;
    DatabaseReference databaseRef;

    ProgressDialog progressDialog;

    private boolean foto1 = false;
    private boolean foto2 = false;
    private boolean foto3 = false;
    public static final int REQUEST_CODE_GALERY = 1;
    public static final int REQUEST_CODE_GALERY_2 = 2;
    public static final int REQUEST_CODE_GALERY_3 = 3;

    Uri imageUri;

    private ByteArrayOutputStream baos;

    private String gambar_1, gambar_2, gambar_3;

    private boolean dataspotEdit = false;

    private Button btn_upload_1;
    private Button btn_upload_2;
    private Button btn_upload_3;
    private Button btn_pickPlace;

    private static final String Storage_Path = "Photo_Spot_Sample/";

    public static final String Database_Path = "All_DataSpot_Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_spot);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference(Database_Path);

        gambarview_1 = (ImageView) findViewById(R.id.gambarspot1);
        gambarview_2 = (ImageView) findViewById(R.id.gambarspot2);
        gambarview_3 = (ImageView) findViewById(R.id.gambarspot3);

        Updt_NamaSpot = findViewById(R.id.edit_namaspot);
        Updt_Deskripsi = findViewById(R.id.edit_deskripsi);
        Updt_Biaya = findViewById(R.id.edit_infobiaya);
        Updt_Kategori = findViewById(R.id.edit_kategori);
        Updt_RekomWkt = findViewById(R.id.edit_rekom_waktu);
        Updt_Alamat = findViewById(R.id.alamat_spot);
        Updt_Latitude = findViewById(R.id.latitude);
        Updt_Longitude = findViewById(R.id.longitude);

        Button btn_updateData = findViewById(R.id.btn_editdata);
        btn_upload_1 = findViewById(R.id.btn_editfoto_1);
        btn_upload_2 = findViewById(R.id.btn_editfoto_2);
        btn_upload_3 = findViewById(R.id.btn_editfoto_3);
        btn_pickPlace = findViewById(R.id.btn_placepicker);

        progressDialog = new ProgressDialog(UpdateSpot.this);

        Intent intent = getIntent();

        UploadDataID = intent.getStringExtra("IDSpot");
        System.out.println(UploadDataID);

        gambar_1 = intent.getStringExtra("imageURL_1");
        gambar_2 = intent.getStringExtra("imageURL_2");
        gambar_3 = intent.getStringExtra("imageURL_3");
        DataSpot.setImageURL_1(gambar_1);
        DataSpot.setImageURL_2(gambar_2);
        DataSpot.setImageURL_3(gambar_3);

        Glide.with(this).load(gambar_1).into(gambarview_1);
        Glide.with(this).load(gambar_2).into(gambarview_2);
        Glide.with(this).load(gambar_3).into(gambarview_3);


        Updt_NamaSpot.setText(getIntent().getStringExtra("nama_spot"));
        Updt_Deskripsi.setText(getIntent().getStringExtra("deskripsi"));
        Updt_Biaya.setText(getIntent().getStringExtra("biaya"));
        Updt_RekomWkt.setText(getIntent().getStringExtra("rekom_wkt"));
        Updt_Kategori.setText(getIntent().getStringExtra("kategori"));
        Updt_Latitude.setText(getIntent().getStringExtra("Lat"));
        Updt_Longitude.setText(getIntent().getStringExtra("Long"));
        Updt_Alamat.setText(getIntent().getStringExtra("alamat"));

        btn_upload_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //  intentGallery.setType("image/*");
                startActivityForResult(intentGallery, REQUEST_CODE_GALERY);
            }
        });

        btn_upload_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, REQUEST_CODE_GALERY_2);
            }
        });

        btn_upload_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentGallery, REQUEST_CODE_GALERY_3);

            }
        });

        btn_updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateFilepot();
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        byte[] compImg;

        if (resultCode == RESULT_OK && data != null && data.getData() != null && ((requestCode == REQUEST_CODE_GALERY) || (requestCode == REQUEST_CODE_GALERY_2) || (requestCode == REQUEST_CODE_GALERY_3))) {

            imageUri = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                baos = new ByteArrayOutputStream();
                bitmap = getResizedBitmap(bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                compImg = baos.toByteArray();
                Bitmap bitmap2 = BitmapFactory.decodeByteArray(compImg, 0, compImg.length);

                if (requestCode == REQUEST_CODE_GALERY) {
                    gambarview_1.setImageBitmap(bitmap2);
                    // btn_upload_1.setText("Foto Telah di Pilih");
                    foto1 = true;
                } else if (requestCode == REQUEST_CODE_GALERY_2) {
                    gambarview_2.setImageBitmap(bitmap2);
                    //  btn_upload_2.setText("Foto Telah di Pilih");
                    foto2 = true;
                } else if (requestCode == REQUEST_CODE_GALERY_3) {
                    gambarview_3.setImageBitmap(bitmap2);
                    //  btn_upload_3.setText("Foto Telah di Pilih");
                    foto3 = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap getResizedBitmap(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 640;
            height = (int) (width / bitmapRatio);
        } else {
            height = 640;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
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

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void UpdateFilepot() {

        if (foto1) {

            progressDialog.setTitle("Data Sedang di Upload . . .");
            progressDialog.show();

            if (foto2) {
                removeImageFile(DataSpot.getImageURL_2());

                Bitmap bitmap_two = ((BitmapDrawable) gambarview_2.getDrawable()).getBitmap();
                ByteArrayOutputStream imageTwoBytes = new ByteArrayOutputStream();
                bitmap_two.compress(Bitmap.CompressFormat.JPEG, 100, imageTwoBytes);
                byte[] data_kedua = imageTwoBytes.toByteArray();

                StorageReference filePathTwo = storageRef.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                UploadTask uploadTaskTwo = filePathTwo.putBytes(data_kedua);

                uploadTaskTwo.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return filePathTwo.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    Uri downloadUri2 = task.getResult();
                    if (downloadUri2 != null) {
                        DataSpot.setImageURL_2(downloadUri2.toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }

            if (foto3) {
                removeImageFile(DataSpot.getImageURL_3());

                Bitmap bitmap_three = ((BitmapDrawable) gambarview_3.getDrawable()).getBitmap();
                ByteArrayOutputStream imageThreeBytes = new ByteArrayOutputStream();
                bitmap_three.compress(Bitmap.CompressFormat.JPEG, 100, imageThreeBytes);
                byte[] data_ketiga = imageThreeBytes.toByteArray();

                StorageReference filePathThree = storageRef.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                UploadTask uploadTaskThree = filePathThree.putBytes(data_ketiga);

                uploadTaskThree.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return filePathThree.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    Uri downloadUri3 = task.getResult();
                    if (downloadUri3 != null) {
                        DataSpot.setImageURL_3(downloadUri3.toString());
                    }


                }).addOnFailureListener(e -> {
                });
            }

            removeImageFile(DataSpot.getImageURL_1());

            Bitmap bitmap_one = ((BitmapDrawable) gambarview_1.getDrawable()).getBitmap();
            ByteArrayOutputStream imageOneBytes = new ByteArrayOutputStream();
            bitmap_one.compress(Bitmap.CompressFormat.JPEG, 100, imageOneBytes);
            byte[] data_kesatu = imageOneBytes.toByteArray();

            StorageReference filePathOne = storageRef.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(imageUri));
            UploadTask uploadTaskOne = filePathOne.putBytes(data_kesatu);

            uploadTaskOne.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }
                return filePathOne.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                Uri downloadUri1 = task.getResult();
                if (downloadUri1 != null) {
                    DataSpot.setImageURL_1(downloadUri1.toString());

                }
                UpdateDataSpot();

            }).addOnFailureListener(exception -> {
                exception.getMessage();
            });
        } else {
            UpdateDataSpot();
        }
    }

    private void UpdateDataSpot() {

        DataSpot.setNama_spot(Updt_NamaSpot.getText().toString().trim());
        DataSpot.setDeskripsi(Updt_Deskripsi.getText().toString().trim());
        DataSpot.setBiaya(Updt_Biaya.getText().toString().trim());
        DataSpot.setRekom_wkt(Updt_RekomWkt.getText().toString().trim());
        DataSpot.setKategori(Updt_Kategori.getText().toString().trim());
        DataSpot.setLat(Updt_Latitude.getText().toString().trim());
        DataSpot.setLong(Updt_Longitude.getText().toString().trim());
        DataSpot.setAlamat(Updt_Alamat.getText().toString().trim());
        System.out.println(UploadDataID);
//        databaseRef.child(UploadDataID).removeValue();
        databaseRef.child(UploadDataID).setValue(DataSpot);

        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(), "Berhasil Update Data", Toast.LENGTH_SHORT).show();
        final Intent intent = new Intent(this, Admin_LihatSpot.class);
        startActivity(intent);
    }

    private void clear() {

        gambarview_1.setImageResource(android.R.color.transparent);
        gambarview_2.setImageResource(android.R.color.transparent);
        gambarview_3.setImageResource(android.R.color.transparent);

        btn_upload_1.setText("Upload Foto 1");
        btn_upload_2.setText("Upload Foto 2");
        btn_upload_3.setText("Upload Foto 3");

        Updt_NamaSpot.setText("");
        Updt_Deskripsi.setText("");
        Updt_Biaya.setText("");
        Updt_RekomWkt.setText("");
        Updt_Kategori.setText("");
        Updt_Alamat.setText("");
        Updt_Latitude.setText("");
        Updt_Longitude.setText("");

        foto1 = false;
        foto2 = false;
        foto3 = false;


    }

    public void next (View v){
        Intent next = new Intent(UpdateSpot.this, LihatSpot.class);
        UpdateSpot.this.startActivity(next);
    }
}
