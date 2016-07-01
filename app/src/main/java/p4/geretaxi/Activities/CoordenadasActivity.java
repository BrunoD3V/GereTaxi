package p4.geretaxi.Activities;

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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.ClassesHelper.GPSHandler;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.R;
import p4.geretaxi.ClassesHelper.SharedPreference;

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
        String location = location_tf.getText().toString();
        List<Address> addressList = null;
        System.out.println(location);

        //VERIFICA SE O USER ESCREVER ALGUM ENDEREÇO A PESQUISAR
        if(!helper.isEmpty(location_tf)) {

            Geocoder geocoder = new Geocoder(this);
            try {
                //INICIALIZA A LOCALIZAÇÃO
                addressList = geocoder.getFromLocationName(location, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(addressList!=null){
                //ESCREVE O ENDEREÇO
                Address address = addressList.get(0);
                //CONVERSAO PARA LATLNG
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                //MARCA NO MAPA
                setUpMap(latLng);
                //FAZ FOCO NO PONTO MARCADO
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }

        }else{
            Toast.makeText(getApplicationContext(),"Por favor introduza um Endereço válido", Toast.LENGTH_LONG).show();
        }
    }

    //MUDA O TIPO DE MAPA ENTRE MAPA NORMAL E MAPA SATÉLITE
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

            if (mMap == null) {
                mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                            .getMap();
            }
            getLocationForMap(this);
    }
    //MARCA POSIÇOES NO MAPA
    private void setUpMap(Location newLocation){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(newLocation.getLatitude(),newLocation.getLongitude())).title("MyLocation"));
        mMap.setMyLocationEnabled(true);
        edtLongitude.setText(String.valueOf(newLocation.getLongitude()));
        edtLatitude.setText(String.valueOf(newLocation.getLatitude()));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(newLocation.getLatitude(), newLocation.getLongitude())));
    }
    //POLYMORFED
    private void setUpMap(LatLng latLng){
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(new LatLng(latLng.latitude,latLng.longitude)).title("MyLocation"));
        mMap.setMyLocationEnabled(true);
        edtLongitude.setText(String.valueOf(latLng.longitude));
        edtLatitude.setText(String.valueOf(latLng.latitude));
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
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
        if(Helper.isEmpty(edtLatitude) && Helper.isEmpty(edtLongitude)){
            //GUARDA AS COORDENADAS NAS PREFERENCIAS
            sharedPreference.save(getApplicationContext(),String.valueOf(latLng.latitude), Constants.LAT);
            sharedPreference.save(getApplicationContext(), String.valueOf(latLng.longitude), Constants.LON);
        }else{
            sharedPreference.save(getApplicationContext(),edtLatitude.getText().toString(), Constants.LAT);
            sharedPreference.save(getApplicationContext(), edtLongitude.getText().toString(), Constants.LON);
        }
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.logout_id:
                SharedPreference sharedPreference = new SharedPreference();
                sharedPreference.save(getApplicationContext(), " ", Constants.EMAIL);
                sharedPreference.save(getApplicationContext(), " ", Constants.PASS);
                sharedPreference.save(getApplicationContext(), -1, Constants.ID_MOTORISTA);
                sharedPreference.save(getApplicationContext(), Constants.FALSE, Constants.SESSION);

                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;

            case R.id.settings_id:                 Intent in = new Intent(this, CoordenadasActivity.class);                 startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
