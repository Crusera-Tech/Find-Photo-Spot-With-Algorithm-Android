package com.example.findit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class TambahSpot extends AppCompatActivity {

    private ImageView gambar_1, gambar_2, gambar_3;
    private boolean foto1 = false;
    private boolean foto2 = false;
    private boolean foto3 = false;
    private Button btn_upload_1;
    private Button btn_upload_2;
    private Button btn_upload_3;

    private String imgURL1 = "null";
    private String imgURL2 = "null";
    private String imgURL3 = "null";

    private ByteArrayOutputStream baos;

    //public static final int REQUEST_CODE_CAMERA = 001;
    public static final int REQUEST_CODE_GALERY = 1;
    public static final int REQUEST_CODE_GALERY_2 = 2;
    public static final int REQUEST_CODE_GALERY_3 = 3;

    Uri imageUri;

    private FirebaseAuth myAuth;

    private EditText NamaSpot, Deskripsi, Biaya, Kategori, RekomWkt, Latitude, Longitude, Alamat_spot;


    private static final String Storage_Path = "Photo_Spot_Sample/";

    public static final String Database_Path = "All_DataSpot_Database";

    StorageReference storageRef;
    DatabaseReference databaseRef;

    ProgressDialog progressDialog;


    private Button btn_PickPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_spot);

        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference(Database_Path);

        gambar_1 = findViewById(R.id.gambarspot1);
        gambar_2 = findViewById(R.id.gambarspot2);
        gambar_3 = findViewById(R.id.gambarspot3);

        btn_upload_1 = findViewById(R.id.btn_inputfoto_1);
        btn_upload_2 = findViewById(R.id.btn_inputfoto_2);
        btn_upload_3 = findViewById(R.id.btn_inputfoto_3);

        Button btn_simpan = findViewById(R.id.btn_simpandata);

        NamaSpot  = findViewById(R.id.namaspot);
        Deskripsi = findViewById(R.id.deskripsi);
        Biaya     = findViewById(R.id.infobiaya);
        Kategori  = findViewById(R.id.kategori);
        RekomWkt  = findViewById(R.id.rekom_waktu);
        Latitude  = findViewById(R.id.latitude);
        Longitude = findViewById(R.id.longitude);
        Alamat_spot = findViewById(R.id.alamat_spot);

        Intent intent = getIntent();
        Latitude.setText(getIntent().getStringExtra("lat"));
        Longitude.setText(getIntent().getStringExtra("long"));

        progressDialog = new ProgressDialog(TambahSpot.this);

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

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ((!NamaSpot.getText().toString().isEmpty())&& (!Latitude.getText().toString().isEmpty()) && (!Longitude.getText().toString().isEmpty())) {

                    UploadDataToFirebase();

                } else{
                    Toast.makeText(getApplicationContext(), "Mohon Lengkapi Data", Toast.LENGTH_SHORT).show();

                }

            }
        });



        btn_PickPlace = (Button) findViewById(R.id.btn_placepicker);
        btn_PickPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(TambahSpot.this, MapsActivity.class);
                startActivity(intent);
            }
              /**  PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(TambahSpot.this), PLACE_PICKER_REQ);
                }catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }**/
        });

    }

    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    /**    if (requestCode == PLACE_PICKER_REQ) {
            if (resultCode == RESULT_OK){
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("", place.getAddress());
                txt_alamat.setText(toastMsg);
                toastMsg = String.format("", place.getLatLng());
                txt_longitude.setText(toastMsg);
            }
        } **/

        byte[] compImg;

        if (resultCode == RESULT_OK && data != null && data.getData() != null && ((requestCode==REQUEST_CODE_GALERY) || (requestCode==REQUEST_CODE_GALERY_2) || (requestCode==REQUEST_CODE_GALERY_3))) {

            imageUri = data.getData();

            try   {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                baos = new ByteArrayOutputStream();
                bitmap = getResizedBitmap(bitmap);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                compImg = baos.toByteArray();
                Bitmap  bitmap2 = BitmapFactory.decodeByteArray(compImg,0, compImg.length);

                if (requestCode == REQUEST_CODE_GALERY){
                    gambar_1.setImageBitmap(bitmap2);
                    // btn_upload_1.setText("Foto Telah di Pilih");
                    foto1 = true;
                }else if (requestCode == REQUEST_CODE_GALERY_2) {
                    gambar_2.setImageBitmap(bitmap2);
                    //  btn_upload_2.setText("Foto Telah di Pilih");
                    foto2 = true;
                }else if (requestCode == REQUEST_CODE_GALERY_3) {
                    gambar_3.setImageBitmap(bitmap2);
                    //  btn_upload_3.setText("Foto Telah di Pilih");
                    foto3 = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private  Bitmap getResizedBitmap(Bitmap image){
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = 640;
            height = (int) (width / bitmapRatio);
        } else {
            height = 640;
            width= (int) (height * bitmapRatio);
        }
        return  Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void next (View v){
        Intent next = new Intent(TambahSpot.this, Admin_LihatSpot.class);
        TambahSpot.this.startActivity(next);
    }

    public String GetFileExtension (Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    public void UploadDataToFirebase() {

        if (foto1) {
            progressDialog.setTitle("Data Sedang di upload . . .");
            progressDialog.show();

            if (foto2) {
                Bitmap bitmap_two = ((BitmapDrawable) gambar_2.getDrawable()).getBitmap();
                ByteArrayOutputStream imageTwoBytes = new ByteArrayOutputStream();
                bitmap_two.compress(Bitmap.CompressFormat.JPEG, 100, imageTwoBytes);
                byte[] data_kedua = imageTwoBytes.toByteArray();

                final StorageReference filePathTwo = storageRef.child(Storage_Path + System.currentTimeMillis() + "." + GetFileExtension(imageUri));
                UploadTask uploadTaskTwo = filePathTwo.putBytes(data_kedua);

                uploadTaskTwo.continueWithTask(task -> {
                    if (!task.isSuccessful()) {
                        throw Objects.requireNonNull(task.getException());
                    }
                    return filePathTwo.getDownloadUrl();
                }).addOnCompleteListener(task -> {
                    Uri downloadUri2 = task.getResult();
                    if (downloadUri2 != null) {
                        imgURL2 = downloadUri2.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }

            if (foto3) {
                Bitmap bitmap_three = ((BitmapDrawable) gambar_3.getDrawable()).getBitmap();
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
                        imgURL3 = downloadUri3.toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }

            Bitmap bitmap_one = ((BitmapDrawable) gambar_1.getDrawable()).getBitmap();
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
                imgURL1 = downloadUri1.toString();

                final String getNama_Spot = NamaSpot.getText().toString().trim();
                final String getDeskripsi = Deskripsi.getText().toString().trim();
                final String getInfoBiaya = Biaya.getText().toString().trim();
                final String getKategori = Kategori.getText().toString().trim();
                final String getRekomWkt = RekomWkt.getText().toString().trim();
                final String getLat = Latitude.getText().toString().trim();
                final String getLong = Longitude.getText().toString().trim();
                final String getAlamat = Alamat_spot.getText().toString().trim();

                progressDialog.dismiss();
                clear();
                Toast.makeText(getApplicationContext(), "Berhasil Upload Data", Toast.LENGTH_SHORT).show();


                foto1 = false;
                foto2 = false;
                foto3 = false;

                try {
                    baos.flush();
                    baos.close();
                } catch (IOException E) {
                    E.printStackTrace();
                }
                final String uploadDataID = databaseRef.push().getKey();

                data_spot uploadDataSpot = new data_spot(uploadDataID, getNama_Spot, getDeskripsi, getInfoBiaya, getKategori, getRekomWkt, getLat, getLong, getAlamat, imgURL1, imgURL2, imgURL3);

                databaseRef.child(uploadDataID).setValue(uploadDataSpot);

            }).addOnFailureListener(exception -> {
                progressDialog.dismiss();
                exception.getMessage();
            }).addOnCompleteListener(taskSnapshot -> progressDialog.setMessage("Proses ..."));
        }
    }

    private void clear(){

        gambar_1.setImageResource(android.R.color.transparent);
        gambar_2.setImageResource(android.R.color.transparent);
        gambar_3.setImageResource(android.R.color.transparent);

        btn_upload_1.setText("Upload Foto 1");
        btn_upload_2.setText("Upload Foto 2");
        btn_upload_3.setText("Upload Foto 3");

        NamaSpot.setText("");
        Deskripsi.setText("");
        Biaya.setText("");
        Kategori.setText("");
        RekomWkt.setText("");
        Latitude.setText("");
        Longitude.setText("");
        Alamat_spot.setText("");


    }

}
