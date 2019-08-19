package com.curtcaldwell.gotennachallenge;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;

import java.util.List;


public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {


    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private PinAdapter adapter;
    private RecyclerView recyclerView;
    private SymbolManager symbolManager;
    private Symbol symbol;
    private PinViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Mapbox.getInstance(this, getString(R.string.access_token));

        model = ViewModelProviders.of(this).get(PinViewModel.class);
        model.getPins().observe(this, pins -> {
//            model.getPinArrayList().addAll(pins);
            mapView.getMapAsync(MainActivity.this);
            adapter.updateList(pins);
        });


        setContentView(R.layout.activity_main);

        mapView = findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        recyclerView = findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        adapter = new PinAdapter(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        model.loadPinData();
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MainActivity.this.mapboxMap = mapboxMap;
        model.getPins().observe(this, pins -> {
            mapboxMap.setStyle(new Style.Builder().fromUri(PinViewModel.ACCESS_CODE),
                    style -> {
                        enableLocationComponent(style);
                        symbolManager = new SymbolManager(mapView, mapboxMap, style);
                        symbolManager.setIconAllowOverlap(true);
                        symbolManager.setTextAllowOverlap(true);

                        for (int i = 0; i < pins.size(); i++) {
                            symbol = symbolManager.create(new SymbolOptions()
                                    .withLatLng(new LatLng(pins.get(i).getLatitude(), pins.get(i).getLongitude()))
                                    .withIconImage(PinViewModel.MARKER_IMAGE)
                                    .withIconSize(2.0f)

                            );
                            model.getSymbolMap().put(symbol, pins.get(i));
                        }

                        symbolManager.addClickListener(symbol -> {
                            TextView mapDataName = findViewById(R.id.pin_data_name);
                            TextView mapDataDescription = findViewById(R.id.pin_data_description);
                            if (model.getSymbolMap().get(symbol) != null && model.getSymbolMap().get(symbol).getName() != null && model.getSymbolMap().get(symbol).getDescription() != null) {
                                mapDataName.setText(model.getSymbolMap().get(symbol).getName());
                                mapDataDescription.setText(model.getSymbolMap().get(symbol).getDescription());
                                mapDataName.setVisibility(View.VISIBLE);
                                mapDataDescription.setVisibility(View.VISIBLE);
                            }
                        });
                    });
        } );


    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(style -> enableLocationComponent(style));
        } else {
            Toast.makeText(this, R.string.permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}