package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        if (helper.isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String processo = editTextProcesso.getText().toString();
        AssistenciaEmViagem assistenciaEmViagem = new AssistenciaEmViagem();
        assistenciaEmViagem.setData(helper.getDate());
        assistenciaEmViagem.setHoraDeInicio(helper.getTime());
        XMLHandler writer = new XMLHandler();
        //writer.writeAssitenciaEmViagem(assistenciaEmViagem,processo);

        boolean result=helper.inicializarDados(processo);
        if (result == true) {
            Toast toast = Toast.makeText(getApplicationContext(), "Serviço Inicializado",Toast.LENGTH_SHORT);
            toast.show();
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "Erro na inicialização", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        gpsHandler.initGPS(processo,this);
        terminar.setVisibility(View.VISIBLE);
    }


}