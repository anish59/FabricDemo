package com.fabricdemo.mapDemo;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.fabricdemo.R;
import com.fabricdemo.helper.FunctionHelper;
import com.google.gson.Gson;
import com.gun0912.tedpermission.PermissionListener;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.api.directions.v5.DirectionsCriteria;
import com.mapbox.services.api.directions.v5.MapboxDirections;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.commons.models.Position;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapBoxNavigationDemo extends AppCompatActivity {
    private MapboxNavigation navigation;
    private com.mapbox.mapboxsdk.maps.MapView mapView;
    private MapboxMap mMapBoxMap;
    private android.widget.Button btnSetNavigation;
    private Context mContext;
    LatLng point1 = new LatLng(22.3099771, 73.1754511);
    LatLng point2 = new LatLng(22.3089299, 73.1753277);
    LatLng point3 = new LatLng(22.3092947, 73.1752446);
    private NavigationMapRoute navigationMapRoute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        Mapbox.getInstance(this, getString(R.string.com_mapbox_mapboxsdk_accessToken));
        FunctionHelper.askForPermission(mContext, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                getDeviceLocation();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                return;
            }
        });
        init(savedInstanceState);
        initListeners();
    }

    private void initListeners() {

        btnSetNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doNavigation();
            }
        });


    }

    private void doNavigation() {
        // From Mapbox to The White House
        Position origin = Position.fromLngLat(point1.getLongitude(), point1.getLatitude());
        Position destination = Position.fromLngLat(point2.getLongitude(), point2.getLatitude());
        MapboxDirections directions = new MapboxDirections.Builder()
                .setOrigin(origin)
                .setDestination(destination)
                .setAccessToken(Mapbox.getAccessToken())
                .setProfile(DirectionsCriteria.PROFILE_DRIVING_TRAFFIC)
                .setOverview(DirectionsCriteria.OVERVIEW_FULL)
                .setAnnotation(DirectionsCriteria.ANNOTATION_CONGESTION)
                .setSteps(true)
                .build();

        directions.enqueueCall(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                String resspBody = new Gson().toJson(response.body());
                System.out.println("resBody: " + resspBody);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }

    private void init(Bundle savedInstanceState) {
        setContentView(R.layout.activity_map_box_navigation_demo);
        this.btnSetNavigation = (Button) findViewById(R.id.btnSetNavigation);
        this.mapView = (MapView) findViewById(R.id.mapView);
//        MapboxNavigation navigation = new MapboxNavigation(this, getString(R.string.com_mapbox_mapboxsdk_accessToken));
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapBoxMap = mapboxMap;
                mMapBoxMap.setMyLocationEnabled(true);
                mMapBoxMap.setStyle(Style.SATELLITE_STREETS);
                if (mMapBoxMap.getMyLocation() != null) {
                    getDeviceLocation();
                }

            }
        });

    }

    private void getDeviceLocation() {
        mMapBoxMap.setMyLocationEnabled(true);
        if (mMapBoxMap.getMyLocation() != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(point1))      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            mMapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (navigation != null) {
            navigation.endNavigation();
            navigation.onDestroy();
        }
    }
}


  /*  If your navigation involves a bunch of pick-up and drop-off points, you can add up to 25 coordinates to the NavigationRoute builder; these are considered stops in between the origin and destination Positions (in the order that you add them - first waypoint is the first stop):*/
   /*     NavigationRoute.Builder builder = NavigationRoute.builder()
        .accessToken(Mapbox.getAccessToken())
        .origin(origin)
        .destination(destination);

        for (Position waypoint : waypoints) {
        builder.addWaypoint(waypoint);
        }

        builder.build()*/;



/*
*   Reference : https://www.mapbox.com/android-docs/navigation/overview/#lifecycle-methods
 *  */