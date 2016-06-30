package p4.geretaxi;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback, DialogCustoPortagemFragment.Communicator {

    private GoogleMap mMap;
    private Servico servico;
    private String tipo;
    private boolean portagens;
    List<LatLng> mCapturedLocations;

    Button buttonAceitar;
    Button buttonInserePortagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        buttonAceitar = (Button) findViewById(R.id.buttonAceitar);
        buttonInserePortagens = (Button) findViewById(R.id.buttonInserePortagens);

        portagens = getIntent().getBooleanExtra("portagem", false);

        if (portagens) {
            buttonInserePortagens.setVisibility(View.VISIBLE);
            buttonAceitar.setVisibility(View.INVISIBLE);
        } else {
            buttonInserePortagens.setVisibility(View.INVISIBLE);
            buttonAceitar.setVisibility(View.VISIBLE);
        }

        servico = (Servico) getIntent().getSerializableExtra(Constants.INTENT_SERVICO);

        ArrayList<Double> lats = (ArrayList<Double>)  getIntent().getSerializableExtra("lat");
        ArrayList<Double> lngs = (ArrayList<Double>) getIntent().getSerializableExtra("lng");

        mCapturedLocations = new ArrayList<>();
        for (int i = 0; i < lats.size(); i++) {
            LatLng e = new LatLng(lats.get(i), lngs.get(i));
            mCapturedLocations.add(i,e);
        }
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

    public void onClickPortagens(View v){
        android.app.FragmentManager manager = getFragmentManager();
        DialogCustoPortagemFragment dialogCustoPortagemFragment = new DialogCustoPortagemFragment();
        dialogCustoPortagemFragment.show(manager, "DialogPortagens");
    }

    public void onClickAceitarServico(View v) {
        XMLHandler writer = new XMLHandler();
        servico.setTrajeto(writer.trajectoToString(mCapturedLocations));
        writer.writeTrajecto(mCapturedLocations, servico.getProcesso());
        Intent intent = new Intent(this, MostraServicoActivity.class);
        intent.putExtra("ser",servico);
        startActivity(intent);
    }

    public void onClickRejeitarServico(View v) {
        Intent intent = new Intent(this, FormularioDeServico.class);
        startActivity(intent);
    }

    @Override
    public void onDialogMessage(String portagem, int confirm) {
        servico.setCustoPortagens(Double.parseDouble(portagem));
        int DialogResponse = confirm;
        switch (confirm) {
            case 0:
                Toast.makeText(getApplicationContext(),"Por favor insira o custo das Portagens.", Toast.LENGTH_LONG).show();
                break;
            case 1:
                buttonAceitar.setVisibility(View.VISIBLE);
                buttonInserePortagens.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
}
