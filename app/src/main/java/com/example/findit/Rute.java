package com.example.findit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.algorithms.AStar;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

public class Rute extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    DbHelper dbHelper;
    Cursor cursor;
    int simpul_awal;
    int simpul_tujuan;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_rute);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng hcmus = new LatLng(10.762963, 106.682394);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hcmus, 16));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Đại học Khoa học tự nhiên")
                .position(hcmus)));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    public void carijalurdjisktra(){
        simpul_awal = originMarkers.indexOf(0);
        simpul_tujuan = destinationMarkers.indexOf(0);

        int start = 0;
        dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor.moveToFirst();
        String[] koord = cursor.getString(0).split(",");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(koord[0]), Double.parseDouble(koord[1])), 12));

    }

    public void rekonstruksi(){
        MapsRute peta = new MapsRute();

        peta.carijalurdjikstra();
        AStar aStar = new AStar();
        aStar.jalurastar(simpul_awal, simpul_tujuan);
        aStar.getAmbilNeighbor(0);
        aStar.getopenset(2);
    }
}
