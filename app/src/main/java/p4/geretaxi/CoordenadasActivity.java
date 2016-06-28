package p4.geretaxi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.List;

public class CoordenadasActivity extends FragmentActivity{

    Button btnSateliteMap;
    private GoogleMap mMap;
    private LocationListener lListener;
    private LocationManager lManager;
    private GPSHandler handler;
    private LatLng latLng;
    EditText location_tf;
    EditText edtLatitude;
    EditText edtLongitude;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);
        handler = new GPSHandler(this);
        helper = new Helper();
        btnSateliteMap = (Button) findViewById(R.id.btnSateliteMap);
        edtLatitude = (EditText) findViewById(R.id.edtLatitude);
        edtLongitude = (EditText) findViewById(R.id.edtLongitude);
        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }
    public void onSearch(View view)
    {
        location_tf = (EditText)findViewById(R.id.edtSearch);
        if(!helper.isEmpty(location_tf)) {


            String location = location_tf.getText().toString();
            List<Address> addressList = null;

            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);//TODO verificar se está preenchida primeiro
            latLng = new LatLng(address.getLatitude(), address.getLongitude());
            setUpMap(latLng);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    public void changeType(View view)
    {
        if(mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL)
        {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            btnSateliteMap.setText("Map");
        }
        else {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            btnSateliteMap.setText("Satélite");
        }
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
            if (mMap == null) {
                    // Try to obtain the map from the SupportMapFragment.
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                            .getMap();
            }
            getLocationForMap(this);
    }
    private void setUpMap(Location newLocation){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(),newLocation.getLongitude())).title("MyLocation"));
        mMap.setMyLocationEnabled(true);
        edtLongitude.setText(String.valueOf(newLocation.getLongitude()));
        edtLatitude.setText(String.valueOf(newLocation.getLatitude()));
    }
    //POLYMORFED
    private void setUpMap(LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)).title("MyLocation"));
        mMap.setMyLocationEnabled(true);
        edtLongitude.setText(String.valueOf(latLng.latitude));
        edtLatitude.setText(String.valueOf(latLng.longitude));
    }
    public void getLocationForMap(final Activity activity) {
        lManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if(lManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            Toast.makeText(getApplicationContext(),"A Obter a sua Localização atual.", Toast.LENGTH_LONG).show();
        }
        else
        {
            handler.displayPromptForEnablingGPS(activity);
        }

        lListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setUpMap(location);
                Toast.makeText(getApplicationContext(),"Nova Posição detetada.", Toast.LENGTH_LONG).show();
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

        try {
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000000, 10 , lListener);
        } catch (SecurityException se) {
        }
    }

    public void onClickGuardarLocation(View v){
        handler.listenerClose();
        SharedPreference sharedPreference = new SharedPreference();
        if(helper.isEmpty(edtLatitude) && helper.isEmpty(edtLongitude)){
            sharedPreference.save(getApplicationContext(),String.valueOf(latLng.latitude), Constants.LAT);
            sharedPreference.save(getApplicationContext(), String.valueOf(latLng.longitude), Constants.LON);
        }else{
            sharedPreference.save(getApplicationContext(),edtLatitude.getText().toString(), Constants.LAT);
            sharedPreference.save(getApplicationContext(), edtLongitude.getText().toString(), Constants.LON);
        }
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }
}
