package com.example.indangernew;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;

import com.example.indangernew.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private final long MIN_TIME = 10000;
    private final long MIN_DIST = 5;

    private LatLng latLng;

    private String emergencyContact1;
    private String emergencyContact2;
    private String emergencyContact3;
    private String emergencyContact4;

    private FirebaseAuth auth;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
        reference.child("Contacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Contacts contacts = snapshot.getValue(Contacts.class);

                emergencyContact1 = contacts.contact1.trim();
                emergencyContact2 = contacts.contact2.trim();
                emergencyContact3 = contacts.contact3.trim();
                emergencyContact4 = contacts.contact4.trim();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title("My Position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                    if (!emergencyContact1.isEmpty()) {
                        String phoneNumber = emergencyContact1;
                        String myLatitude = String.valueOf(location.getLatitude());
                        String myLongitude = String.valueOf(location.getLongitude());

                        String message = "Hey, " + "I am in DANGER, I need help. Please urgently reach me out. I'm sending you my live location now" +
                                ".\n " + "http://maps.google.com/?q=" + myLatitude + "," + myLongitude;

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    }
                    if (!emergencyContact2.isEmpty()) {
                        String phoneNumber = emergencyContact2;
                        String myLatitude = String.valueOf(location.getLatitude());
                        String myLongitude = String.valueOf(location.getLongitude());

                        String message = "Hey, " + "I am in DANGER, I need help. Please urgently reach me out. I'm sending you my live location now" +
                                ".\n " + "http://maps.google.com/?q=" + myLatitude + "," + myLongitude;

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    }
                    if (!emergencyContact3.isEmpty()) {
                        String phoneNumber = emergencyContact3;
                        String myLatitude = String.valueOf(location.getLatitude());
                        String myLongitude = String.valueOf(location.getLongitude());

                        String message = "Hey, " + "I am in DANGER, I need help. Please urgently reach me out. I'm sending you my live location now" +
                                ".\n " + "http://maps.google.com/?q=" + myLatitude + "," + myLongitude;

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    }
                    if (!emergencyContact4.isEmpty()) {
                        String phoneNumber = emergencyContact4;
                        String myLatitude = String.valueOf(location.getLatitude());
                        String myLongitude = String.valueOf(location.getLongitude());

                        String message = "Hey, " + "I am in DANGER, I need help. Please urgently reach me out. I'm sending you my live location now" +
                                ".\n " + "http://maps.google.com/?q=" + myLatitude + "," + myLongitude;

                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapsActivity.this, MainActivity.class));
        finish();
    }
}