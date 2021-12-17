package com.apuliacreativehub.eculturetool.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apuliacreativehub.eculturetool.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Locale;

public class MapFragment extends Fragment {
    private MapView map;
    View v;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            centerMapAccordingToLocation();
        }else {
            centerMapAccordingToLocale();
        }
    });

    public MapFragment() {
        super(R.layout.osm_map);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.osm_map, null);
        map = v.findViewById(R.id.mapview);
        map.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS);
        requestLocationPermission();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            centerMapAccordingToLocation();
        } else {
            centerMapAccordingToLocale();
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {

                showRationaleDialog(getString(R.string.rationale_location_permission_title), Manifest.permission.ACCESS_COARSE_LOCATION);
            } else {
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_COARSE_LOCATION);
            }
        }
    }

    private void showRationaleDialog(String title, String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title).setMessage(R.string.rationale_location_permission_message)
                .setPositiveButton(R.string.rationale_ok_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermissionLauncher.launch(
                                permission);
                    }
                }).setNegativeButton(R.string.rationale_cancel_button, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                centerMapAccordingToLocale();
            }
        });
        builder.create().show();
    }

    private void centerMapAccordingToLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        @SuppressLint("MissingPermission") Task<Location> t = fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, null);
        t.addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d("LOCATION", String.valueOf(location));
                    map.getMapboxMap().setCamera(new CameraOptions.Builder().center(Point.fromLngLat(location.getLongitude(), location.getLatitude())).zoom(3.0).build());
                }
            }
        });
    }

    private void centerMapAccordingToLocale() {
        String nominatimApi = "https://nominatim.openstreetmap.org/search?format=json&q=";
        Locale currentLocale = ConfigurationCompat.getLocales(getResources().getConfiguration()).get(0);
        RequestQueue queue = Volley.newRequestQueue(requireContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, nominatimApi + currentLocale.getCountry(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    double lat = jsonArray.getJSONObject(0).getDouble("lat");
                    double lon = jsonArray.getJSONObject(0).getDouble("lon");
                    Log.d("INFERRED LOCATION", "Lat: " + lat);
                    Log.d("INFERRED LOCATION", "Lon: " + lon);
                    map.getMapboxMap().setCamera(new CameraOptions.Builder().center(Point.fromLngLat(lon, lat)).zoom(3.0).build());
                } catch (JSONException e) {
                    map.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(3.0).build());
                    e.printStackTrace();
                }
            }
        }, null);
        queue.add(stringRequest);
    }

}
