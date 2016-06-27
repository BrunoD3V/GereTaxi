package p4.geretaxi;

import android.app.Activity;
import android.content.Context;
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
    LocationListener lListener;
    LocationManager lManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordenadas);
        btnSateliteMap = (Button) findViewById(R.id.btnSateliteMap);
        setUpMapIfNeeded();
        lManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    public void onSearch(View view)
    {
        EditText location_tf = (EditText)findViewById(R.id.edtSearch);
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        if(location != null || !location.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
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
       initGPS(this);
    }

    private void setUpMap(Location newLocation) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(),newLocation.getLongitude())).title("MyLocation"));
        mMap.setMyLocationEnabled(true);
        //TODO: MUDAR PARA LOCALIZAÇÃO ATUAL
    }

    public void initGPS(final Activity activity) {

        GPSHandler handler = new GPSHandler(getApplicationContext());

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
            lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, lListener);
        } catch (SecurityException se) {
        }
    }

    public void listenerClose() {
        if(lListener != null){
            lManager.removeUpdates(lListener);
        }
    }

    public void onClickGuardarLocation(View v){
        listenerClose();
    }


}
