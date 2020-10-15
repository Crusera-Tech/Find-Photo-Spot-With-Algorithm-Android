package com.example.findit;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import com.example.Modules.DirectionFinder;
import com.example.Modules.DirectionFinderListener;
import com.example.Modules.Route;
import com.example.algorithms.AStar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.connection.util.StringListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MapsRute extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionFinderListener {

    private GoogleMap mMap;
    private static final String GOOGLE_API_KEY = "AIzaSyD9ln4Rev9FZpe9JFDCB5VA9fc3QYaEzc0";
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    //  Button btn_call;

    Marker mCurrLocationMarker;
    LatLng posisi;
    String navigasi = "mode=d";
    double latitude, longtitude;
    ArrayList markerPoints = new ArrayList();
    String nama, alamat, lokasi, lokasi2;
    FloatingActionButton floating;
    String mode = "mode=driving";
    LatLng posisisekarang;
    String label;
    int duration;
    float results[] = new float[10];
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    DecimalFormat form = new DecimalFormat("0.00");
    DecimalFormat form2 = new DecimalFormat("0");

    Double locate, locate2;
    LatLng asal;
    DbHelper dbHelper;
    Cursor cursor;
    AStar astar = new AStar();
    long startTime;
    long endTime;
    long astarTime;

    //  private Button btn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_rute);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        //Check if Google Play Services Available or not
        if (!CheckGooglePlayServices()) {
            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent data = getIntent();
        nama = data.getStringExtra("nama");
        alamat = data.getStringExtra("alamat");
        lokasi = data.getStringExtra("lokasi");
        lokasi2 = data.getStringExtra("lokasi2");
        locate = Double.parseDouble(lokasi);
        locate2 = Double.parseDouble(lokasi2);

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, 0).show();
            }
            return false;
        }
        return true;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Sydney and move the camera
        Toast.makeText(MapsRute.this, "Sedang Mencari Posisi Sekarang . . .", Toast.LENGTH_LONG).show();
        mMap = googleMap;
        final LatLng bandarLampung = new LatLng(locate, locate2);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bandarLampung, 16));
        posisi = new LatLng(locate, locate2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (markerPoints.size() > 0) {
                    markerPoints.clear();
                    mMap.clear();
                    LatLng hcmus = new LatLng(locate, locate2);
                    markerPoints.add(hcmus);
                    MarkerOptions options = new MarkerOptions();
                    options.position(hcmus);
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerspot31));
                    options.title(nama + "," + alamat);
                    mMap.addMarker(options).showInfoWindow();
                }
                markerPoints.add(latLng);

                MarkerOptions options = new MarkerOptions();

                options.position(latLng);
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markeruser31));

                mMap.addMarker(options);

                if (markerPoints.size() >= 1) {
                    asal = (LatLng) markerPoints.get(1);
                    sendRequest();
                }
            }
        });

        markerPoints.add(posisi);
        MarkerOptions options = new MarkerOptions();
        options.position(posisi);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markerspot31));
        options.title(nama + "," + alamat);
        mMap.addMarker(options).showInfoWindow();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChange", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        latitude = location.getLatitude();
        longtitude = location.getLongitude();
        Location.distanceBetween(latitude, longtitude, locate, locate2, results);
        asal = new LatLng(latitude, longtitude);
        duration = Math.round(results[0] / 250 + 1);

        if (duration <= 1) {
            duration = 1;
        }

        if (results[0] < 1000) {
            label = "m";
            results[0] = results[0];
        } else {
            label = "km";
            results[0] = results[0] / 1000;
        }
        double midLat = (latitude + locate) / 2;
        double midLong = (longtitude + locate2) / 2;

        LatLng midPoint = new LatLng(midLat, midLong);

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.draggable(true);
        markerOptions.title("Posisi Sekarang");
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_markeruser31));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(midPoint).zoom(12).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

        String url = getDirectionsUrl(latLng, posisi);

        DownloadTask downloadTask = new DownloadTask();

        downloadTask.execute(url);
        sendRequest();

        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 26;

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    private void sendRequest() {
        String origin = asal.latitude + "," + asal.longitude;
        String destination = locate + "," + locate2;
        startTime = System.nanoTime();

        try {
            new DirectionFinder(this, origin, destination).execute();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDirectionFinderStart() {

        progressDialog = ProgressDialog.show(this, "Mohon Tunggu Sebentar",
                "Sedang Mencari Jalur . . .", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {

        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.duration_rute)).setText("Durasi : " + route.duration.text);
            ((TextView) findViewById(R.id.distance_rute)).setText("Jarak : " + route.distance.text);
            astarTime = route.duration.value / 10;

            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.RED)
                    .width(8);

            int cost;
            for (int s = 0; s < route.points.size(); s++)
                polylineOptions.add(route.points.get(s));
            ((TextView) findViewById(R.id.cost_rute)).setText("Banyak Titik Pada Jalur: " + route.points.size());
            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
        endTime = System.nanoTime();
        long MethodeDuration = (endTime - startTime);
        astarTime = astarTime + (MethodeDuration / 1000000);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng tujuan) {

        //Origin Of Route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        //destinasi
        String str_tuju = "destination=" + tujuan.latitude + "," + tujuan.longitude;

        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_tuju + "&" + sensor + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public void carijalurdjikstra() {
        int simpul_awal = markerPoints.indexOf(1);
        int simpul_tujuan = markerPoints.indexOf(0);

        int start = 0;

        dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        cursor.moveToFirst();
        String[] koord = cursor.getString(0).split(",");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(koord[0]), Double.parseDouble(koord[1])), 12));

    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
