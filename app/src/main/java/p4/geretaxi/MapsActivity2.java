package p4.geretaxi;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, DialogCustoPortagem.Communicator {

    private GoogleMap mMap;

    private AssistenciaEmViagem assistenciaEmViagem;
    private ServicoParticular servicoParticular;
    private AcidentesDeTrabalho acidentesDeTrabalho;
    private String tipo;
    private boolean portagens;
    List<LatLng> mCapturedLocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        tipo = getIntent().getExtras().getString("tipo");
        portagens = getIntent().getBooleanExtra("portagem", false);
        assistenciaEmViagem = (AssistenciaEmViagem) getIntent().getSerializableExtra("servico");
        ArrayList<Double> lats = (ArrayList<Double>)  getIntent().getSerializableExtra("lat");
        ArrayList<Double> lngs = (ArrayList<Double>) getIntent().getSerializableExtra("lng");


        mCapturedLocations = new ArrayList<>();
        for (int i = 0; i < lats.size(); i++) {
            LatLng e = new LatLng(lats.get(i), lngs.get(i));
            mCapturedLocations.add(i,e);
        }



    }

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

    public void onClickAceitarServico(View v) {

        if (portagens) {
            android.app.FragmentManager manager = getFragmentManager();
            DialogCustoPortagem dialogCustoPortagem = new DialogCustoPortagem();
            dialogCustoPortagem.show(manager, "DialogPortagens");

        }

    }

    @Override
    public void onDialogMessage(String portagem) {

        switch (tipo) {
            case "Viagem":
                assistenciaEmViagem.setCustoPortagens(Float.parseFloat(portagem));
                break;
            case "Acidente":
                acidentesDeTrabalho.setCustoPortagens(Float.parseFloat(portagem));
                break;
            case "Particular":
                servicoParticular.setCustoPortagens(Float.parseFloat(portagem));
                break;
            default:
                break;

        }

    }
}
