package com.example.srikanth.mapssample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ArrayList<Marker> markers= new ArrayList<Marker>();

    LocationManager locationManager;

    LocationListener locationListener;



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LocationOnMap(lastKnownLocation, "Device location");

            }


        }

    }


    public void LocationOnMap(Location location, String title) {

        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        mMap.clear();
        // mMap.addPolygon(new PolygonOptions().add(new LatLng(location.getLatitude(), location.getLongitude())));
        markers.add(mMap.addMarker(new MarkerOptions().position(userLocation).title(title).icon(getBitmapdescriptor(R.drawable.userlocation))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 2));

    }

    private BitmapDescriptor getBitmapdescriptor(int  i){
        Drawable d = getDrawable(i);
        return BitmapDescriptorFactory.fromBitmap(drawableToBitmap(d));
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        Intent intent = getIntent();
        String StoreAdd = intent.getStringExtra("storeTitle");
        int storelat = intent.getIntExtra("StoreLatlng",0);
        int ShipVal = intent.getIntExtra("ShippingVal",0);
        String ShippingAdd= intent.getStringExtra("Ship Address");


        Log.i("Store address",StoreAdd);
        Log.i("location value",String.valueOf(storelat));
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LocationOnMap(location, "Device location");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                LocationOnMap(lastKnownLocation, "Device location");

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }


        }
        if(storelat >= 0)
        {
            Location placeLocation = new Location(LocationManager.GPS_PROVIDER);
            placeLocation.setLatitude(MainActivity.locations.get(storelat).latitude);
            placeLocation.setLongitude(MainActivity.locations.get(storelat).longitude);
            LatLng storeLocation = new LatLng(placeLocation.getLatitude(), placeLocation.getLongitude());
            markers.add(mMap.addMarker(new MarkerOptions().position(storeLocation).title("Store Location").icon(getBitmapdescriptor(R.drawable.storeicon))));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLocation, 2));

        }
        if(ShipVal==1)
        {
            double latitude=0;
            double longitude=0;
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            Log.i("geocode",ShippingAdd);
            try {
                List<Address> listAddresses = geocoder.getFromLocationName(ShippingAdd,1);
                if(listAddresses.size() > 0)
                {
                    latitude = listAddresses.get(0).getLatitude();
                    longitude = listAddresses.get(0).getLongitude();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("latitude::", String.valueOf(latitude));
            Log.i("longitude::",String.valueOf(longitude));
            LatLng shippingLocation = new LatLng(latitude,longitude);

            markers.add(mMap.addMarker(new MarkerOptions().position(shippingLocation).title("Shipping Address").icon(getBitmapdescriptor(R.drawable.homeicon))));


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker m:markers) {
                builder.include(m.getPosition());
            }

            LatLngBounds bounds = builder.build();
            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cu);

        }

    }


}
