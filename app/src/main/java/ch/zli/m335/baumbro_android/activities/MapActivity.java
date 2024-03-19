package ch.zli.m335.baumbro_android.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.CancellationTokenSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import ch.zli.m335.baumbro_android.R;
import ch.zli.m335.baumbro_android.adapters.TreeAdapter;
import ch.zli.m335.baumbro_android.database.AppDatabase;
import ch.zli.m335.baumbro_android.database.Tree;
import ch.zli.m335.baumbro_android.database.TreeDao;
import ch.zli.m335.baumbro_android.utilities.LocationUtilities;

public class MapActivity extends AppCompatActivity
        implements OnMapReadyCallback{

    private FusedLocationProviderClient fusedLocationClient;
    private AppDatabase db;
    List<Tree> trees;
    private RecyclerView recyclerView;

    Button buttonReset;

    LocationUtilities locationUtility;

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // initialize the location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // initialize the map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        db = AppDatabase.getInstance(this);
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
        float zoomLevel = 18;

        // set the map type to satellite so we can actually see the trees
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        // get the current location and then set the camera to that position
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null
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
                    trees = getTrees(Float.valueOf(String.valueOf(longitude.get())), Float.valueOf(String.valueOf(latitude.get())));
                    setMarkers(trees, googleMap);
                    TreeAdapter adapter = new TreeAdapter(trees);
                    recyclerView.setAdapter(adapter);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude.get(), longitude.get()), zoomLevel));
                });

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                TreeDao treeDao = db.treeDao();
                Tree markerTree = treeDao.findByTreeNumber(marker.getSnippet());

                List<Tree> filteredTrees = new ArrayList<>();

                filteredTrees.add(markerTree);

                TreeAdapter adapter = (TreeAdapter) recyclerView.getAdapter();
                adapter.setTrees(filteredTrees);
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        buttonReset = findViewById(R.id.buttonReset);

        buttonReset.setOnClickListener(v -> {
            TreeAdapter adapter = (TreeAdapter) recyclerView.getAdapter();
            if (adapter != null){
                adapter.setTrees(trees);
                adapter.notifyDataSetChanged();
            }
        });
    }

    public List<Tree> getTrees(float longitude, float latitude){
        TreeDao treeDao = db.treeDao();
        HashMap<String, Float> minMaxValues = LocationUtilities.calculateQueryValues(longitude,  latitude, 200);

        List<Tree> treesVicinity = treeDao.getNearbyTrees(minMaxValues.get("minLatitude"),
                                                            minMaxValues.get("minLongitude"),
                                                            minMaxValues.get("maxLatitude"),
                                                            minMaxValues.get("maxLongitude"));
        return treesVicinity;
    };

    public void setMarkers(List<Tree> trees, GoogleMap googleMap){
        int iconResourceId = R.drawable.tree_icon;

        for (Tree tree : trees){
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(tree.getLatitude(), tree.getLongitude()))
                    .title(tree.getBaumnamedeu())
                    .snippet(tree.getBaumnummer())
                    .icon(BitmapDescriptorFactory.fromResource(iconResourceId));

            googleMap.addMarker(markerOptions);
        }
    }
}