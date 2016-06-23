package p4.geretaxi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity3 extends FragmentActivity implements OnMapReadyCallback, DialogCustoPortagemFragment.Communicator{

    private GoogleMap mMap;

    List<LatLng> mCapturedLocations;
    AssistenciaEmViagem assistenciaEmViagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps3);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ArrayList<Double> lats = (ArrayList<Double>)  getIntent().getSerializableExtra("lat");
        ArrayList<Double> lngs = (ArrayList<Double>) getIntent().getSerializableExtra("lng");

        mCapturedLocations = new ArrayList<>();
        for (int i = 0; i < lats.size(); i++) {
            LatLng e = new LatLng(lats.get(i), lngs.get(i));
            mCapturedLocations.add(i,e);
        }



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
    public void onDialogMessage(String portagem) {

    }
}
