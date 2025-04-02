package com.example.jeevanjyotandroidapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;
import com.example.jeevanjyotandroidapplication.databinding.ActivityGoogleMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.googlemapproject.databinding.ActivityGoogleMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GoogleMapsActivity extends DrawerBaseForPatient implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int Request_User_Location_Code = 99;
    private Marker currentUserLocationMarker;
    private ActivityGoogleMapsBinding binding;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    ImageButton searchb,hospitalb,medicalb;
    String hospital="hospital";
    String pharmacy="pharmacy";
    double latitude,longitude;
    Object transferData[]=new Object[2];
    private int ProximityRadius=10000;
    GetNearbyPlaces getNearbyPlaces=new GetNearbyPlaces();
    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        getLayoutInflater().inflate(R.layout.activity_google_maps, activityDashboardBinding.contentFrame, true);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkUserLocationPermission();
        }

        // Initialize the Map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Initialize Location Provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Define Location Callback
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    updateLocationUI(location);
                }
            }
        };
        searchb=findViewById(R.id.search_address);
        searchb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText addressField=(EditText) findViewById(R.id.location_search);
                String address=addressField.getText().toString();
                List<Address> addressList=null;
//                MarkerOptions userMarkerOptions=new MarkerOptions();
                if(!TextUtils.isEmpty(address)){
                    Geocoder geocoder=new Geocoder(v.getContext(), Locale.getDefault());
                    try {
                        addressList=geocoder.getFromLocationName(address,6);
                        if(addressList!=null){
                            for(int i=0;i<addressList.size();i++){
                                Address userAddress=addressList.get(i);
                                LatLng latLng=new LatLng(userAddress.getLatitude(),userAddress.getLongitude());
                                MarkerOptions userMarkerOptions = new MarkerOptions()
                                        .position(latLng)
                                        .title(address)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                mMap.addMarker(userMarkerOptions);
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                            }
                        }
                        else{
                            Toast.makeText(v.getContext(),"Location Not Found",Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
                else{
                    Toast.makeText(v.getContext(),"please enter the location",Toast.LENGTH_SHORT).show();
                }
            }
        });
        hospitalb=findViewById(R.id.hospitals_nearby);
        hospitalb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                String url=getUrl(latitude,longitude,hospital);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(v.getContext(),"Searching for hospitals",Toast.LENGTH_SHORT).show();
                Toast.makeText(v.getContext(),"Showing hospitals",Toast.LENGTH_SHORT).show();
            }
        });
        medicalb=findViewById(R.id.medical_nearby);
        medicalb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                String url=getUrl(latitude,longitude,pharmacy);
                transferData[0]=mMap;
                transferData[1]=url;
                getNearbyPlaces.execute(transferData);
                Toast.makeText(v.getContext(),"Searching for Medicals",Toast.LENGTH_SHORT).show();
                Toast.makeText(v.getContext(),"Showing Medicals",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getUrl(double latitude,double longitude,String nearplace){
        StringBuilder googleURL=new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleURL.append("location="+latitude+","+longitude);
        googleURL.append("&radius="+ProximityRadius);
        googleURL.append("&type="+nearplace);
        googleURL.append("&sensor=true");
        googleURL.append("&key="+"AIzaSyAWTDccYaChtwhwlfFr24VItwzNGXBfdmk");
        Log.d("GoogleMapsActivity","url="+googleURL.toString());
        return googleURL.toString();
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            startLocationUpdates();
        }
    }

    private boolean checkUserLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_User_Location_Code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    startLocationUpdates();
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void startLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);  // Update every 5 seconds
        locationRequest.setFastestInterval(2000);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void updateLocationUI(Location location) {
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        if (location == null) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        if (currentUserLocationMarker != null) {
            currentUserLocationMarker.remove();
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Your Current Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        currentUserLocationMarker = mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        Log.d("Location", "Updated Location: " + latLng.latitude + ", " + latLng.longitude);
    }
    @Override
    protected void onStop() {
        super.onStop();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

}