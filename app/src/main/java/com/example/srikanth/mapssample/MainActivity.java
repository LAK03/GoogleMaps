package com.example.srikanth.mapssample;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import static android.widget.Toast.LENGTH_LONG;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    EditText _street;
    EditText _place;
    EditText _city;
    EditText _pinCode;
    EditText _storeAdd;
    Button _Maps;
    public static int LocationValue = -1;
    public static int ShippingLocation = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _street = (EditText)findViewById(R.id.street);
        _place=(EditText)findViewById(R.id.place);
        _city=(EditText)findViewById(R.id.city);
        _pinCode=(EditText)findViewById(R.id.pinCode);



        _storeAdd = (EditText)findViewById(R.id.storeAdd);
        _Maps =(Button)findViewById(R.id.Map);



    ListView listView = (ListView) findViewById(R.id.listView);

        places.add("Beaverton OR");
        locations.add(new LatLng(45.4846189,-122.8755135));
        places.add("los angles");
        locations.add(new LatLng(34.0201812,-118.6919132));
        places.add("SanFranscisco");
        locations.add(new LatLng(37.7576948,-122.4726194));

    arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            _storeAdd.setText(places.get(i));
            LocationValue = i;


        }

    });

        _Maps.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String finalString="";

            String street = _street.getText().toString().trim();
            String place = _place.getText().toString().trim();
            String city =_city.getText().toString().trim();
            String pinCode= _pinCode.getText().toString().trim();


            if(street!=null) {
                finalString += street;
                ShippingLocation =1;
            }
            if(place!=null) {
                finalString = finalString+","+place;
                ShippingLocation =1;
            }
            if (city != null) {
                finalString = finalString+","+ city;
                ShippingLocation=1;
            }
            if(pinCode!=null) {
                finalString = finalString+","+ pinCode;
                ShippingLocation =1;
            }
            else {
                    finalString = "";
                    ShippingLocation= -1;
            }

        /*    Log.i("finalString",finalString);
            double latitude=0;
            double longitude=0;
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            Log.i("geocode",finalString);
            try {
                List<Address> listAddresses = geocoder.getFromLocationName(finalString,1);
                if(listAddresses.size() > 0)
                {
                    latitude = listAddresses.get(0).getLatitude();
                    longitude = listAddresses.get(0).getLongitude();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra("storeTitle",places.get(LocationValue));
            intent.putExtra("StoreLatlng",LocationValue);
            intent.putExtra("ShippingVal",ShippingLocation);
            intent.putExtra("Ship Address",finalString);
            startActivity(intent);
        }
    });


}


}
