package com.example.mectow_mechanic.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatToggleButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.mectow_mechanic.Activity_rate;
import com.example.mectow_mechanic.MapsActivity;
import com.example.mectow_mechanic.MessagingActivity;
import com.example.mectow_mechanic.R;
import com.example.mectow_mechanic.login_mechanic;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.DefaultSliderView;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.view.View.GONE;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener, RoutingListener {
    GoogleMap Map, mMap2;
    ImageView locationbtn;
    Boolean status = false;
    SupportMapFragment mapFragment;
    SwitchCompat toggle;
    public static double totalcharges = 0;
    public static double mech_totalcharges = 0;
    Boolean state = false;
    String key;
    String _state;
    Location CurrentLocation;
    FirebaseAuth auth;
    float[] results = new float[1];
    DatabaseReference reference;
    String services;
    public static String customer_uid;
    String mechtype;
    public static String customerrequestuid;
    String call = "";
    ConstraintLayout approvalRequestLayout, _approvalRequestLayout;
    Button cancel_approvaRequestLayout, accept_approvaRequestLayout;
    LottieAnimationView calldial, message;
    TextView name_approvaRequestLayout, services_approvaRequestLayout, charges_approvaRequestLayout, category_approvaRequestLayout, subcategory_approvaRequestLayout;
    TextView _name_approvaRequestLayout, _services_approvaRequestLayout, _charges_approvaRequestLayout, _category_approvaRequestLayout, _subcategory_approvaRequestLayout;
    //    Fragment googlemapfragment;
    RelativeLayout endworklayout1, startworklayout1, Arrivedworklayout1, Arrivingworklayout1;
    TextView arriving1, arrived1, startwork1, endwork1;

    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.PolyBlue};

    FusedLocationProviderClient fusedLocationProviderClient;
    Double lat, lng;
    Marker mGlobalMarker;
    LocationCallback mLocationCallback;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationCallback != null)
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

                Log.d("Location", locationResult.toString());

                HashMap<String, Object> userlocation = new HashMap<>();
                Location location = locationResult.getLocations().get(locationResult.getLocations().size() - 1);
                userlocation.put("latitude", String.valueOf(location.getLatitude()));
                userlocation.put("longitude", String.valueOf(location.getLongitude()));

                reference
                        .child(Objects.requireNonNull(auth.getUid()))
                        .updateChildren(userlocation);

                if (mGlobalMarker != null && mMap2 != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mGlobalMarker.setPosition(latLng);
                }

            }
        };
        mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map1);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("WorkerState");
        toggle = (SwitchCompat) view.findViewById(R.id.toggle);
        approvalRequestLayout = (ConstraintLayout) view.findViewById(R.id.upperBox);
        name_approvaRequestLayout = (TextView) view.findViewById(R.id.name_text1);
        category_approvaRequestLayout = (TextView) view.findViewById(R.id.user_usbcatagory);
        subcategory_approvaRequestLayout = (TextView) view.findViewById(R.id.user_subcatagory);
        services_approvaRequestLayout = (TextView) view.findViewById(R.id.Service_field);
        charges_approvaRequestLayout = (TextView) view.findViewById(R.id.charges);
        cancel_approvaRequestLayout = (Button) view.findViewById(R.id.cancelbtn);
        accept_approvaRequestLayout = (Button) view.findViewById(R.id.acceptbtn);
        _approvalRequestLayout = (ConstraintLayout) view.findViewById(R.id._uperBox);
        _name_approvaRequestLayout = (TextView) view.findViewById(R.id._name_text);
        _category_approvaRequestLayout = (TextView) view.findViewById(R.id._user_usbcatagory);
        _subcategory_approvaRequestLayout = (TextView) view.findViewById(R.id._user_subcatagory);
        _services_approvaRequestLayout = (TextView) view.findViewById(R.id._Service_field);
        _charges_approvaRequestLayout = (TextView) view.findViewById(R.id._charges);
        arrived1 = (TextView) view.findViewById(R.id.arrived);
        startwork1 = (TextView) view.findViewById(R.id.startwork);
        arriving1 = (TextView) view.findViewById(R.id.arriving);
        endwork1 = (TextView) view.findViewById(R.id.endwork);
        endworklayout1 = (RelativeLayout) view.findViewById(R.id.endworklayout);
        endworklayout1.setVisibility(GONE);
        startworklayout1 = (RelativeLayout) view.findViewById(R.id.startworklayout);
        startworklayout1.setVisibility(GONE);
        Arrivedworklayout1 = (RelativeLayout) view.findViewById(R.id.Arrivedworklayout);
        Arrivedworklayout1.setVisibility(GONE);
        Arrivingworklayout1 = (RelativeLayout) view.findViewById(R.id.Arrivingworklayout);
        Arrivingworklayout1.setVisibility(GONE);

        calldial = view.findViewById(R.id._call);
        message = view.findViewById(R.id._message);
        calldial.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + call));
            startActivity(intent);
        });
        message.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MessagingActivity.class);
            intent.putExtra("cid", customer_uid);
            startActivity(intent);
        });
        startworklayout1.setOnClickListener(v -> {
            DatabaseReference reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
            HashMap<String, Object> updateData = new HashMap<>();
            updateData.put("state", "startworking");
            reff.updateChildren(updateData).addOnSuccessListener(aVoid -> {
                startworklayout1.setVisibility(GONE);
                endworklayout1.setVisibility(View.VISIBLE);
            });
        });
        endworklayout1.setOnClickListener(v -> {
            DatabaseReference reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
            HashMap<String, Object> updateData = new HashMap<>();
            updateData.put("state", "end");

            reff.updateChildren(updateData).addOnSuccessListener(aVoid -> {
                endworklayout1.setVisibility(GONE);
                Intent intent = new Intent(getContext(), Activity_rate.class);
                startActivity(intent);
            });
        });
        Arrivedworklayout1.setOnClickListener(v -> {
            DatabaseReference reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
            HashMap<String, Object> updateData = new HashMap<>();
            updateData.put("state", "arrived");

            reff.updateChildren(updateData).addOnSuccessListener(aVoid -> {
                Arrivedworklayout1.setVisibility(GONE);
                startworklayout1.setVisibility(View.VISIBLE);
            });
        });
        Arrivingworklayout1.setOnClickListener(v -> {
            DatabaseReference reff = (DatabaseReference) FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid);
            HashMap<String, Object> updateData = new HashMap<>();
            updateData.put("state", "moving");
            reff.updateChildren(updateData).addOnSuccessListener(aVoid -> {
                Arrivingworklayout1.setVisibility(GONE);
                Arrivedworklayout1.setVisibility(View.VISIBLE);
            });

        });
        polylines = new ArrayList<>();
        toggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (status) {
                if (isChecked) {

                    HashMap<String, Object> userstate = new HashMap<>();
                    userstate.put("status", "online");
                    FirebaseDatabase.getInstance().getReference("WorkerState").child(Objects.requireNonNull(auth.getUid()))
                            .updateChildren(userstate).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    final Handler handler = new Handler();
                                    handler.postDelayed(this::fetchMechanicState, 3000);
                                    //fetchComplainttow(login_mechanic.services);
                                }
                            });

                } else {
                    HashMap<String, Object> userstate = new HashMap<>();
                    userstate.put("status", "offline");
                    FirebaseDatabase.getInstance().getReference("WorkerState").child(Objects.requireNonNull(auth.getUid()))
                            .updateChildren(userstate).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "Worker is Offline", Toast.LENGTH_SHORT).show();

                                }

                            });
                }
            } else {
                isChecked = false;
                toggle.setChecked(false);
                Toast.makeText(getContext(), "fetch location first", Toast.LENGTH_SHORT).show();
            }
        });
        locationbtn = (ImageView) view.findViewById(R.id.locationbtn);
        locationbtn.setOnClickListener(v -> {
            status = true;
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getContext()));
            getcurrentlocation();
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity())
                , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions((Activity) getContext()
                    , new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    }, 100);
        }

        return view;
    }

    private void fetchMechanicState() {
        FirebaseDatabase.getInstance().getReference("WorkerState").child(Objects.requireNonNull(auth.getUid()))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child("state").exists()) {
                            snapshot.child("state");
                            _state = Objects.requireNonNull(snapshot.child("state").getValue()).toString();
                            if (_state.equals("free")) {
                                fetchComplaint(login_mechanic.services);
                            }
                        } else {
                            fetchComplaint(login_mechanic.services);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchComplaint(String services) {

        FirebaseDatabase.getInstance().getReference("Complaint")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (!dataSnapshot.child("state").exists()) {
                                mechtype = Objects.requireNonNull(dataSnapshot.child("type").getValue()).toString();
                                if (login_mechanic.services.equals("mechanic")) {
                                    double lat = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("lat").getValue()).toString());
                                    double lng = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("lng").getValue()).toString());
                                    LatLng latLng = new LatLng(lat, lng);
                                    key = dataSnapshot.getKey();
                                    LatLng current = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
                                    Double meter = CalculationByDistance(current, latLng);

                                    //if (meter <= 5.5) // NEAREST MECHANIC REQUEST...............DONE!y
                                    // {
                                    services_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("service").getValue()).toString());
                                    charges_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("charges").getValue()).toString());
                                    category_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Category").getValue()).toString());
                                    subcategory_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Subcategory").getValue()).toString());
                                    _services_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("service").getValue()).toString());
                                    _charges_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("charges").getValue()).toString());
                                    _category_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Category").getValue()).toString());
                                    _subcategory_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Subcategory").getValue()).toString());
                                    String uid = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                                    customer_uid = uid;
                                    FirebaseDatabase.getInstance().getReference("Customers").child(uid)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    name_approvaRequestLayout.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                                                    _name_approvaRequestLayout.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                                                    call = Objects.requireNonNull(snapshot.child("phonenumber").getValue()).toString();
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng);
                                    mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                    mMap2.addMarker(options);
                                    mMap2.setOnMarkerClickListener(marker -> {
                                        approvalRequestLayout.setVisibility(View.VISIBLE);
                                        accept_approvaRequestLayout.setOnClickListener(v -> {
                                            customerrequestuid = dataSnapshot.getKey();
                                            fetchMechanicState();
                                            AcceptRequest(key);
                                            getRouteToMarker(latLng);
                                            _approvalRequestLayout.setVisibility(View.VISIBLE);
                                            approvalRequestLayout.setVisibility(GONE);
                                            Arrivingworklayout1.setVisibility(View.VISIBLE);
                                        });

                                        cancel_approvaRequestLayout.setOnClickListener(v -> {
                                            if (_state != null) {
                                                if (_state.equals("free")) {
                                                    fetchComplaint(mechtype);
                                                    approvalRequestLayout.setVisibility(GONE);

                                                }
                                            } else {
                                                fetchComplaint(mechtype);
                                                approvalRequestLayout.setVisibility(GONE);
                                            }
                                        });
                                        return false;
                                    });
                                    //}
                                } else if (login_mechanic.services.equals("cartow")) {
                                    double lat = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("lat").getValue()).toString());
                                    double lng = Double.parseDouble(Objects.requireNonNull(dataSnapshot.child("lng").getValue()).toString());
                                    LatLng latLng = new LatLng(lat, lng);

                                    LatLng current = new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude());
                                    Double meter = CalculationByDistance(current, latLng);

                                    //if (meter <= 5.5) // NEAREST CARTOW REQUEST...............DONE!y
                                    // {
                                    services_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("service").getValue()).toString());
                                    category_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Category").getValue()).toString());
                                    subcategory_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Subcategory").getValue()).toString());
                                    _services_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("service").getValue()).toString());
                                    _category_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Category").getValue()).toString());
                                    _subcategory_approvaRequestLayout.setText(Objects.requireNonNull(dataSnapshot.child("Subcategory").getValue()).toString());
                                    String uid = Objects.requireNonNull(dataSnapshot.child("uid").getValue()).toString();
                                    customer_uid = uid;
                                    FirebaseDatabase.getInstance().getReference("Customers").child(uid)
                                            .addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    customerrequestuid = dataSnapshot.getKey();
                                                    name_approvaRequestLayout.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                                                    _name_approvaRequestLayout.setText(Objects.requireNonNull(snapshot.child("name").getValue()).toString());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                    MarkerOptions options = new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(latLng);
                                    mMap2.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                    mMap2.addMarker(options);
                                    mMap2.setOnMarkerClickListener(marker -> {
                                        approvalRequestLayout.setVisibility(View.VISIBLE);
                                        accept_approvaRequestLayout.setOnClickListener(v -> {
                                            customerrequestuid = dataSnapshot.getKey();

                                            AcceptRequest(customerrequestuid);
                                            getRouteToMarker(latLng);
                                            _approvalRequestLayout.setVisibility(View.VISIBLE);
                                            approvalRequestLayout.setVisibility(GONE);
                                            Arrivingworklayout1.setVisibility(View.VISIBLE);
                                        });

                                        cancel_approvaRequestLayout.setOnClickListener(v -> {
                                            if (_state != null) {
                                                if (_state.equals("free")) {
                                                    fetchComplaint(mechtype);
                                                    approvalRequestLayout.setVisibility(GONE);

                                                }
                                            } else {
                                                fetchComplaint(mechtype);
                                                approvalRequestLayout.setVisibility(GONE);
                                            }
                                        });
                                        return false;
                                    });
                                    //}
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    private void AcceptRequest(String key) {
        DatabaseReference refer = FirebaseDatabase.getInstance().getReference("Complaint").child(key);
        HashMap<String, Object> updatedComplaint = new HashMap<>();
        updatedComplaint.put("state", "accept");
        updatedComplaint.put("mid", auth.getUid());
        refer.updateChildren(updatedComplaint).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                  Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                UpdateMechanicState();
            }
        });

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.parseInt(newFormat.format(valueResult));
        double meter = valueResult % 1000;
        int meterInDec = Integer.parseInt(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return meter;
    }

    private void UpdateMechanicState() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("WorkerState").child(Objects.requireNonNull(auth.getUid()));
        HashMap<String, Object> updatedState = new HashMap<>();
        updatedState.put("state", "working");
        reference.updateChildren(updatedState).addOnCompleteListener(task -> {
            task.isSuccessful();// Toast.makeText(getContext(), "State saved", Toast.LENGTH_SHORT).show();
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]
                == PackageManager.PERMISSION_GRANTED)) {
        } else {
            Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getcurrentlocation() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(
                Context.LOCATION_SERVICE
        );
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
            fusedLocationProviderClient.requestLocationUpdates(LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(5000)
                            .setFastestInterval(1000)
                    , mLocationCallback, Looper.getMainLooper());


            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
                Location location = task.getResult();
                if (location != null) {
                    CurrentLocation = location;
                    lat = location.getLatitude();
                    lng = location.getLongitude();

                    mapFragment.getMapAsync(googleMap -> {
                        LatLng latLng = new LatLng(lat, lng);
                        MarkerOptions options = new MarkerOptions().position(latLng);
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                        mMap2 = googleMap;
                        if (mGlobalMarker == null)
                            mGlobalMarker = googleMap.addMarker(options);
                    });

                    FirebaseDatabase.getInstance().getReference("Mechanic").child(Objects.requireNonNull(auth.getUid()))
                            .get().addOnCompleteListener(task3 -> {
                                DataSnapshot snapshot = task3.getResult();
                                if (snapshot.getValue() != null) {
                                    services = Objects.requireNonNull(snapshot.child("service").getValue()).toString();
                                    HashMap<String, Object> userlocation = new HashMap<>();
                                    userlocation.put("status", "offline");
                                    userlocation.put("service", services);
                                    userlocation.put("latitude", lat.toString());
                                    userlocation.put("longitude", lng.toString());

                                    reference.child(auth.getUid()).updateChildren(userlocation).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            Toast.makeText(getContext(), "Location Save", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                }
            });
        } else {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//        Map = googleMap;
//        // Add a marker in Sydney and move the camera
//        LatLng Karachi = new LatLng(25.045500, 67.025583);
//        Map.addMarker(new MarkerOptions()
//                .position(Karachi)
//                .title("Karachi"));
//        Map.moveCamera(CameraUpdateFactory.newLatLng(Karachi));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortindex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap2.addPolyline(polyOptions);
            polylines.add(polyline);
            Toast.makeText(getContext(), route.get(i).getDistanceText(), Toast.LENGTH_SHORT).show();

            if (login_mechanic.services.equals("mechanic")) {
                mech_totalcharges = ((30 * (Double.parseDouble(String.valueOf(route.get(i).getDistanceValue())))) + (Double.parseDouble(charges_approvaRequestLayout.getText().toString())));
                UpdateCharges(String.valueOf(mech_totalcharges));

            } else if (login_mechanic.services.equals("cartow")) {
                totalcharges = ((30 * (Double.parseDouble(String.valueOf(route.get(i).getDistanceValue())))) + 100);
                UpdateCharges(String.valueOf(totalcharges));
            }
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void getRouteToMarker(LatLng location) {


        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .key("AIzaSyBQUC-3IP9u-CsIHB97MHIOX9vpoYvsloU")
                .waypoints(new LatLng(CurrentLocation.getLatitude(), CurrentLocation.getLongitude()), location)
                .build();
        routing.execute();
    }

    public void UpdateCharges(String charges) {
        HashMap<String, Object> chargemap = new HashMap<>();
        if (login_mechanic.services.equals("cartow")) {
            chargemap.put("totalcharges", String.valueOf(charges));
        }
        FirebaseDatabase.getInstance().getReference("Complaint").child(customerrequestuid).updateChildren(chargemap);
    }
}