package com.apuliacreativehub.eculturetool.ui.places.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apuliacreativehub.eculturetool.R;
import com.apuliacreativehub.eculturetool.data.ErrorStrings;
import com.apuliacreativehub.eculturetool.data.entity.Place;
import com.apuliacreativehub.eculturetool.data.repository.RepositoryNotification;
import com.apuliacreativehub.eculturetool.ui.component.Dialog;
import com.apuliacreativehub.eculturetool.ui.component.MapboxHelper;
import com.apuliacreativehub.eculturetool.ui.component.ModalBottomSheetUtils;
import com.apuliacreativehub.eculturetool.ui.places.ModalBottomSheetPlace;
import com.apuliacreativehub.eculturetool.ui.places.viewmodel.MapFragmentViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class MapFragment extends Fragment {
    private MapView map;
    private MapFragmentViewModel mapFragmentViewModel;
    View v;
    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            centerMapAccordingToLocation();
        }else {
            centerMapAccordingToLocale();
        }
    });

    final Observer<RepositoryNotification<ArrayList<Place>>> getPlacesObserver = notification -> {
        ErrorStrings errorStrings = ErrorStrings.getInstance(getResources());
        if (notification.getException() == null) {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", String.valueOf(notification.getData()));
            if (notification.getErrorMessage() == null) {
                mapFragmentViewModel.setPointsAndPlaces(notification.getData());
                map.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS, style -> {
                    for (Point point : mapFragmentViewModel.getPoints())
                        addAnnotationToMap(point);
                });
                addOnClickMapboxListener(Arrays.asList(mapFragmentViewModel.getPoints()), MapboxHelper.DEFAULT_TOLERANCE);
                requestLocationPermission();
            } else {
                Log.d("Dialog", "show dialog here");
                new Dialog(getString(R.string.error_dialog_title), errorStrings.errors.get(notification.getErrorMessage()), "GET_PLACES_ERROR").show(getChildFragmentManager(), Dialog.TAG);
            }
        } else {
            Log.d("CALLBACK", "I am in thread " + Thread.currentThread().getName());
            Log.d("CALLBACK", "An exception occurred: " + notification.getException().getMessage());
            new Dialog(getString(R.string.error_dialog_title), getString(R.string.unexpected_exception_dialog), "GET_PLACES_EXCEPTION").show(getChildFragmentManager(), Dialog.TAG);
        }
    };

    public MapFragment() {
        super(R.layout.fragment_map);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_map, null);

        map = v.findViewById(R.id.mapview);

        mapFragmentViewModel = new ViewModelProvider(this).get(MapFragmentViewModel.class);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapFragmentViewModel.getAllPlaces().observe(getViewLifecycleOwner(), getPlacesObserver);
    }

    public void addOnClickMapboxListener(List<Point> points, float tolerance) {
        this.map.getMapboxMap().gesturesPlugin((gesturesPlugin -> {
            gesturesPlugin.addOnMapClickListener(new OnMapClickListener() {
                @Override
                public boolean onMapClick(@NonNull Point possiblePoint) {
                    MapboxHelper mapboxHelper = new MapboxHelper(possiblePoint, tolerance);
                    Collections.sort(points, mapboxHelper);
                    if (mapboxHelper.isPointValid(points.get(0))) {
                        ModalBottomSheetPlace modalBottomSheet = new ModalBottomSheetPlace(getContext(), mapFragmentViewModel.getPlaceFromPoint(points.get(0)));
                        modalBottomSheet.show(getChildFragmentManager(), ModalBottomSheetUtils.TAG);
                    }
                    return true;
                }
            });
            return true;
        }));
    }

    private void addAnnotationToMap(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions();
        Bitmap bitmap = convertDrawableToBitmap(AppCompatResources.getDrawable(requireContext(), R.drawable.red_marker));
        pointAnnotationOptions.withPoint(point).withIconImage(Objects.requireNonNull(bitmap));

        AnnotationPlugin annotationApi = AnnotationPluginImplKt.getAnnotations(map);
        PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationApi, map);
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private Bitmap convertDrawableToBitmap(Drawable sourceDrawable) {
        Bitmap bitmap;

        if (sourceDrawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable)sourceDrawable).getBitmap();
        } else {
            Drawable drawable = sourceDrawable.getConstantState().newDrawable().mutate();
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }

        return bitmap;
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
