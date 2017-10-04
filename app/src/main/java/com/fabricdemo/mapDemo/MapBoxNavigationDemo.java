package com.fabricdemo.mapDemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.fabricdemo.R;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.services.android.navigation.v5.navigation.MapboxNavigation;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.api.directions.v5.models.DirectionsResponse;
import com.mapbox.services.commons.models.Position;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapBoxNavigationDemo extends AppCompatActivity {

    private com.mapbox.mapboxsdk.maps.MapView mapView;
    private MapboxMap mMapBoxMap;
    private android.widget.Button btnSetNavigation;
    LatLng point1 = new LatLng(22.3099771, 73.1754511);
    LatLng point2 = new LatLng(22.3089299, 73.1753277);
    LatLng point3 = new LatLng(22.3092947, 73.1752446);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
        initListeners();
    }

    private void initListeners() {
        // From Mapbox to The White House
        Position origin = Position.fromLngLat(point1.getLatitude(), point1.getLongitude());
        Position destination = Position.fromLngLat(38.90992, -77.03613);

        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() { //remember the imports
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

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
        MapboxNavigation navigation = new MapboxNavigation(this, getString(R.string.com_mapbox_mapboxsdk_accessToken));
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapBoxMap = mapboxMap;
                mMapBoxMap.setStyle(Style.SATELLITE_STREETS);

            }
        });

    }

    private void getDeviceLocation() {
        mMapBoxMap.setMyLocationEnabled(true);
        if (mMapBoxMap.getMyLocation() != null) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mMapBoxMap.getMyLocation()))      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            mMapBoxMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000);
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