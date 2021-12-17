package com.apuliacreativehub.eculturetool.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.apuliacreativehub.eculturetool.R;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import java.util.ArrayList;

public class MapFragment extends Fragment {
    private MapView map;
    View v;

    public MapFragment() {
        super(R.layout.osm_map);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.osm_map, null);
        map = v.findViewById(R.id.mapview);
        map.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        requestPermissionsIfNecessary(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION});
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if(isGranted) {
                // TODO: Center map accordingly to user's nation (based on GPS)
            }else{
                map.getMapboxMap().setCamera(new CameraOptions.Builder().center(Point.fromLngLat(16.871871, 41.117143)).zoom(3.0).build());
            }
        });

        return v;
    }

    private void requestPermissionsIfNecessary(@NonNull String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(requireContext(), permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if(shouldShowRequestPermissionRationale(permission)){
                    // TODO: Launch rationale Fragment
                }else{
                    permissionsToRequest.add(permission);
                }
            }
        }
        if (permissionsToRequest.size() > 0) {
            int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

}
