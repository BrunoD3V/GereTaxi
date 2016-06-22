package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.maps.GeoApiContext;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AssistenciaEmViagemActivity extends AppCompatActivity {

    List<LatLng> mCapturedLocations;
    private GeoApiContext mContext;
    Button terminar;
    EditText editTextProcesso;
    Helper helper= new Helper();
    GPSHandler gpsHandler = new GPSHandler(this);
    AssistenciaEmViagem assistenciaEmViagem = new AssistenciaEmViagem();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_em_viagem);
        terminar = (Button) findViewById(R.id.buttonTermina);
        terminar.setVisibility(View.INVISIBLE);
        editTextProcesso = (EditText) findViewById(R.id.editTextProcesso);
        mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));

    }

    public void onClickIniciar(View v) {

        if (helper.isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        String processo = editTextProcesso.getText().toString();
        assistenciaEmViagem.setData(helper.getDate());
        assistenciaEmViagem.setHoraDeInicio(helper.getTime());


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

    public void onClickTerminar(View v) throws Exception {
        gpsHandler.listenerClose();
        ServicoHandler servicoHandler = new ServicoHandler(this);
        String processo = editTextProcesso.getText().toString();

        if(helper.isNetworkAvailable(this)){

            mCapturedLocations = servicoHandler.mergeCapture("lisboa");

            if (mCapturedLocations.size() == 0){
                Toast.makeText(getApplicationContext(), "Erro na captura ou directions API", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }

            mCapturedLocations = servicoHandler.getRoute(mCapturedLocations, mContext);
            double distance = servicoHandler.getDistance();
            System.out.println("Distancia");
            System.out.println(distance);
            assistenciaEmViagem.setDistancia(distance);
            boolean portagens = servicoHandler.getPortagens();


            ArrayList<Double> lats = new ArrayList<>();

            ArrayList<Double> lngs = new ArrayList<>();

            for (int i = 0; i < mCapturedLocations.size(); i++) {
                lats.add(i,mCapturedLocations.get(i).lat);
                lngs.add(i,mCapturedLocations.get(i).lng);
            }
            Intent intent = new Intent(this, MapsActivity2.class);
            intent.putExtra("lat",lats);
            intent.putExtra("lng", lngs);
            intent.putExtra("tipo", "Viagem");
            intent.putExtra("servico", assistenciaEmViagem);

            startActivity(intent);
        }
        else {
            helper.displayPromptEnableWifi(this);
        }
    }
}