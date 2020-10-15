package com.example.findit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class detail extends FragmentActivity implements OnMapReadyCallback {


    String nama_spot, alamat, lokasi, lokasi2;
    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    double locate, locate2;
    ArrayList markerPoints = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent data = getIntent();
        nama_spot = data.getStringExtra("nama");
        alamat    = data.getStringExtra("alamat");
        lokasi    = data.getStringExtra("lokasi");
        lokasi2   = data.getStringExtra("lokasi2");
        locate    = Double.parseDouble(lokasi);
        locate2   = Double.parseDouble(lokasi2);
        TextView namaspot = (TextView) findViewById(R.id.nama_spotnya);
        TextView alamatnya = (TextView) findViewById(R.id.alamat);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        namaspot.setText(nama_spot);
        alamatnya.setText(alamat);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerPoints.clear();

        mMap = googleMap;
        final LatLng bandarLampung = new LatLng(-5.3977811015226,105.265276953578);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bandarLampung, 12));
        final LatLng target = new LatLng(locate, locate2);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(target,15));
        markerPoints.add(target);
        MarkerOptions options = new MarkerOptions();
        options.position(target);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerspot));
        options.title(nama_spot + "," + alamat);
        mMap.addMarker(options).showInfoWindow();
    }

    public void rutep(View view){
        Intent s = new Intent(getApplicationContext(), MapsRute.class);
        s.putExtra("nama", nama_spot);
        s.putExtra("lokasi", lokasi);
        s.putExtra("lokasi2", lokasi2);
        s.putExtra("alamat", alamat);
        startActivity(s);
    }
}
