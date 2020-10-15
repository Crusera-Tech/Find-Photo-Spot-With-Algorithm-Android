package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ListTerdekat extends AppCompatActivity implements LocationListener {

    private GoogleMap nMap;
    // ImageButton btngmbr;


    LocationManager locationManager;
    String provider;
    String[] daftar, alamat, location, location2, id;
    //   String stat;
  // String[] daftar, telepon, alamat, location, location2, id, jarakantara, fasilitas, layanan, jadwal;

    String kirim1, kirim2, kirim3, kirim4, kirim5, kirim6, kirim7, kirim8, kirim9, terimadata;
    String lokasi, lokasi2;
    double radius;
    AdapterNearby adapter;
    ArrayList<String> records;
    protected Cursor cursor;
    DbHelper dbcenter;
    DecimalFormat form = new DecimalFormat("0.00");
    double locate, locate2;
    double latitude, longtitude;
    float results[] = new float[10];
    String no;
    double g;
    int posisi = 0;
    Marker mCurrLocationMarker;
    ListView listview;
    ArrayList markerPoints = new ArrayList();
    int sudah = 1;
    EditText txtCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_terdekat);

        dbcenter = new DbHelper(this);

        txtCari = (EditText) findViewById(R.id.cari);
        listview = (ListView) findViewById(R.id.List_menu);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        records = new ArrayList<String>();
        adapter = new AdapterNearby(this, R.layout.layout_terdekat, R.id.textid, records);

        if (provider != null && !provider.equals("")) {

            // Get the location from the given provider
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;

            }
            Location location = locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 1, this);

            if (location != null)
                onLocationChanged(location);
            else
                Toast.makeText(getBaseContext(), "Menunggu Respon GPS . . .", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getBaseContext(), "No Provider Found", Toast.LENGTH_SHORT).show();

        }
        txtCari.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                records.clear();
                SQLiteDatabase db = dbcenter.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM spot_foto WHERE nama_spot LIKE '%" + s + "%' ORDER BY jarak ASC LIMIT 20", null);
                id = new String[cursor.getCount()];
                daftar = new String[cursor.getCount()];
                alamat = new String[cursor.getCount()];
                location = new String[cursor.getCount()];
                location2 = new String[cursor.getCount()];
                cursor.moveToFirst();

                int posisi = 0;
                for (int cc = 0; cc < cursor.getCount(); cc++) {
                    cursor.moveToPosition(cc);
                    kirim1 = cursor.getString(0).toString();
                    kirim2 = cursor.getString(1).toString();
                    kirim3 = cursor.getString(2).toString();
                    kirim4 = cursor.getString(3).toString();
                    kirim5 = cursor.getString(4).toString();
                    kirim6 = cursor.getString(5).toString();

                    id[cc] = cursor.getString(0).toString();
                    radius = cursor.getDouble(5);
                    daftar[cc] = cursor.getString(1).toString();
                    alamat[cc] = cursor.getString(2).toString();
                    location[cc] = cursor.getString(3).toString();
                    location2[cc] = cursor.getString(4).toString();


                    kirim6 = form.format(radius);
                    String item = kirim1 + "__" + kirim2 + "__" + kirim6;
                    records.add(item);
                    posisi++;
                }
                RefreshList();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        loaddata();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void loaddata() {

        records.clear();

        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM spot_foto ORDER BY nama_spot ASC", null);
        daftar = new String[cursor.getCount()];
        alamat = new String[cursor.getCount()];
        location = new String[cursor.getCount()];
        location2 = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            lokasi = cursor.getString(3).toString();
            lokasi2 = cursor.getString(4).toString();
            no = cursor.getString(0).toString();
            locate = Double.parseDouble(lokasi);
            locate2 = Double.parseDouble(lokasi2);
            Location.distanceBetween(latitude, longtitude, locate, locate2, results);

            g = results[0] / 1000;

            db.execSQL("update spot_foto set jarak = '" + g + "' where id='" + no + "'");
        }

        cursor = cursor = db.rawQuery("SELECT * FROM spot_foto ORDER BY jarak ASC LIMIT 20", null);
        id = new String[cursor.getCount()];
        daftar = new String[cursor.getCount()];
        alamat = new String[cursor.getCount()];
        location = new String[cursor.getCount()];
        location2 = new String[cursor.getCount()];
        cursor.moveToFirst();

        int posisi = 0;
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            kirim1 = cursor.getString(0).toString();
            kirim2 = cursor.getString(1).toString();
            kirim3 = cursor.getString(2).toString();
            kirim4 = cursor.getString(3).toString();
            kirim5 = cursor.getString(4).toString();
            kirim6 = cursor.getString(5).toString();

            id[cc] = cursor.getString(0).toString();
            radius = cursor.getDouble(5);
            daftar[cc] = cursor.getString(1).toString();
            alamat[cc] = cursor.getString(2).toString();
            location[cc] = cursor.getString(3).toString();
            location2[cc] = cursor.getString(4).toString();


            kirim6 = form.format(radius);
            String item = kirim1 + "__" + kirim2 + "__" + kirim6;
            records.add(item);
            posisi++;
        }
        RefreshList();
    }

    public void RefreshList() {
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {

                final String selection = daftar[arg2];
                final String lokasi = location[arg2];
                final String lokasi2 = location2[arg2];
                final String tempat = alamat[arg2];

                Intent s = new Intent(getApplicationContext(), detail.class);
                s.putExtra("nama", selection);
                s.putExtra("lokasi", lokasi);
                s.putExtra("lokasi2",lokasi2);
                s.putExtra("alamat",tempat);
                startActivity(s);
            }
        });
        ((ArrayAdapter) listview.getAdapter()).notifyDataSetInvalidated();
    }
}
