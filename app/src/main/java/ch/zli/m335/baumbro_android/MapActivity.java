package ch.zli.m335.baumbro_android;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.concurrent.atomic.AtomicReference;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check if location permission is present
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            final int REQUEST_LOCATION_PERMISSION = 1;

            // if no permission, ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        // initialize the location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // https://developers.google.com/maps/documentation/android-sdk/map#maps_android_map_fragment-java
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        googleMap.setMyLocationEnabled(true);

        AtomicReference<Double> latitude = new AtomicReference<>((double) 0);
        AtomicReference<Double> longitude = new AtomicReference<>((double) 0);

        // how far the camera will be zoomed in. 20 is the closest
        float zoomLevel = 19;

        // set the map type to satellite so we can actually see the trees
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // get the current location and then set the camera to that position
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        latitude.set(location.getLatitude());
                        longitude.set(location.getLongitude());
                        Log.d("MapActivity", String.valueOf(latitude));
                        Log.d("MapActivity", String.valueOf(longitude));
                    } else {
                        Log.d("MapActivity", "Setting standard location");
                        latitude.set(47.376886);
                        longitude.set(8.541694);
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude.get(), longitude.get()), zoomLevel));
                });
    }
}