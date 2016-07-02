package p4.geretaxi.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.LatLng;

import java.util.List;

import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.DialogFragments.DialogCustoPortagemFragment;
import p4.geretaxi.R;

public class MostraTrajectoMapsActivity extends FragmentActivity implements OnMapReadyCallback, DialogCustoPortagemFragment.Communicator {

    private GoogleMap mMap;

    List<LatLng> mCapturedLocations;
    Servico servico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        servico = (Servico) getIntent().getSerializableExtra("trajecto");
        XMLHandler parser = new XMLHandler();
        mCapturedLocations = parser.loadTrajecto(Xml.newPullParser(), servico.getTrajeto());



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

        com.google.android.gms.maps.model.LatLng[] mapPoints =
                new com.google.android.gms.maps.model.LatLng[mCapturedLocations.size()];
        int i = 0;
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (LatLng point : mCapturedLocations) {
            mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.lat,
                    point.lng);
            bounds.include(mapPoints[i]);
            i += 1;
        }

        mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));

    }

    @Override
    public void onDialogMessage(String portagem, int confirm) {

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
                sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;

            case R.id.settings_id:
                Intent in = new Intent(this, CoordenadasActivity.class);
                startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
