package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.maps.model.LatLng;

import java.util.List;

public class AcidenteDeTabalhoActivity extends AppCompatActivity {

    List<LatLng> mCapturedLocations;

    Button terminar;
    EditText editTextProcesso;
    Helper helper= new Helper();
    GPSHandler gpsHandler = new GPSHandler(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_em_viagem);
        terminar = (Button) findViewById(R.id.buttonTermina);
        terminar.setVisibility(View.INVISIBLE);
        editTextProcesso = (EditText) findViewById(R.id.editTextProcesso);
    }

    public void onClickIniciar(View v) {


    }


}