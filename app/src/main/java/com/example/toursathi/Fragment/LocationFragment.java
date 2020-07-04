package com.example.toursathi.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.toursathi.Model.UserLocation;
import com.example.toursathi.R;
import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupDetailModel;
import com.example.toursathi.UserClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.android.SphericalUtil;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Waypoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LocationFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {
    View v;
    public static final int DEFAULT_ZOOM=2;
    private GoogleMap mMap;
    public View mapView;
    FirebaseFirestore mDb;

    User user =new User();
    double userlat, userlon;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int LOCATION_UPDATE_INTERVAL = 3000;
    private Location mLastKnownLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    HashMap<String,List<Marker>> usersmarkermap =new HashMap<>();
    HashMap<String,Float> markercolorcodemap =new HashMap<>();
    RoutingEngine routingEngine;
    List<singleGroupDetailModel> groupDetailList = new ArrayList<>();
    List<Polyline> polylineslist = new ArrayList<>();
    public LocationFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.locationfragmentlayout, container, false);
        user = ((UserClient)getContext().getApplicationContext()).getUser();
        groupDetailList=((UserClient)getContext().getApplicationContext()).getGroupdetaillist();
        for (int i = 0;i<groupDetailList.size();i++)
        {
            usersmarkermap.put(groupDetailList.get(i).getMobileno(),new ArrayList<Marker>());
            markercolorcodemap.put(groupDetailList.get(i).getMobileno(),new Float((i*20)%360));
        }
        userlat=((UserClient)getContext().getApplicationContext()).getUserLocation().geo_point.getLatitude();
        userlon=((UserClient)getContext().getApplicationContext()).getUserLocation().geo_point.getLongitude();

        try {
            routingEngine = new RoutingEngine();
        } catch (InstantiationErrorException e) {
            new RuntimeException("Initialization of RoutingEngine failed: " + e.error.name());
        }


        mDb= FirebaseFirestore.getInstance();
        SupportMapFragment supportMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.googleMap);
        supportMapFragment.getMapAsync(this);
        mapView = supportMapFragment.getView();
        return v;
    }
    private void startUserLocationsRunnable(){
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                getUserLocation();
                mHandler.postDelayed(mRunnable, LOCATION_UPDATE_INTERVAL);
            }
        }, LOCATION_UPDATE_INTERVAL);
    }

    private void stopLocationUpdates(){
        mHandler.removeCallbacks(mRunnable);
    }


    void getUserLocation(){

        for (int i=0;i<groupDetailList.size();i++)
        {
            if (!user.getMobileno().equals(groupDetailList.get(i).getMobileno()))
            {
                DocumentReference locationRef = mDb.collection("UserLocation")
                        .document(groupDetailList.get(i).getMobileno());


                final int finalI = i;
                locationRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            UserLocation userLocation; userLocation = task.getResult().toObject(UserLocation.class);
                            showUserLocation(userLocation, finalI);
                        }
                    }
                });
            }
        }
    }
    void showUserLocation(UserLocation userLocation,int position)
    {
        for (int i=0;i<usersmarkermap.get(groupDetailList.get(position)).size();i++)
        {
            usersmarkermap.get(groupDetailList.get(position)).get(i).remove();
        }

        Marker m1 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userLocation.getGeo_point().getLatitude()
                        , userLocation.getGeo_point().getLongitude()))
                .title(groupDetailList.get(position).getName())
                .icon(BitmapDescriptorFactory.defaultMarker(markercolorcodemap.get(groupDetailList.get(position).getMobileno())))
                .snippet("do you wanna find route to "+groupDetailList.get(position).getName()));

        usersmarkermap.get(groupDetailList.get(position)).add(m1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopLocationUpdates();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        // getDeliveryBoyLocationFirstTime();
        LatLngBounds latLngBounds=toBounds(new LatLng(userlat
                ,userlon),1000);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,2));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds,2));
        Marker m2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(userlat
                        ,userlon))
                .icon(BitmapDescriptorFactory.defaultMarker(240))
                .title("You")
                .snippet(user.getName()));
        mMap.setOnInfoWindowClickListener(this);
        startUserLocationsRunnable();
    }


    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        calculateDirections(marker.getPosition());
    }

    void calculateDirections(LatLng latLng)
    {
        Waypoint startWaypoint = new Waypoint(new GeoCoordinates(userlat,userlon));
        Waypoint destinationWaypoint = new Waypoint(new GeoCoordinates(latLng.latitude,latLng.longitude));

        List<Waypoint> waypoints =
                new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

        routingEngine.calculateRoute(
                waypoints,
                new RoutingEngine.CarOptions(),
                new CalculateRouteCallback() {
                    @Override
                    public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {
                        if (routingError == null) {
                            Route route = routes.get(0);
                            Toast.makeText(getContext()
                                    ,"trip time: "+route.getTravelTimeInSeconds()+"seconds\ntrip length: "+route.getLengthInMeters()+"meters"
                            ,Toast.LENGTH_SHORT).show();
                            List<LatLng> newDecodedPath = new ArrayList<>();
                            List<GeoCoordinates> geoCoordinates=route.getShape();
                            for (int i=0;i<geoCoordinates.size();i++)
                            {
                                newDecodedPath.add(new LatLng(geoCoordinates.get(i).latitude
                                        ,geoCoordinates.get(i).longitude));
                            }
                            for (int j=0;j<polylineslist.size();j++)
                            {
                                polylineslist.get(j).remove();
                            }
                            Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                            polyline.setColor(R.color.darkgray);
                            polylineslist.add(polyline);

                        } else {
                            Toast.makeText(getContext(),routingError.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
