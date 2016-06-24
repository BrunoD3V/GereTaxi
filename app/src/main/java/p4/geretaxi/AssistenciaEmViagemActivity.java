package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.maps.GeoApiContext;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class AssistenciaEmViagemActivity extends AppCompatActivity {

    List<LatLng> mCapturedLocations;
    private GeoApiContext mContext;
    Button terminar;
    EditText editTextProcesso;
    EditText editTextSeguradora;
    EditText editTextPassageiros;
    Helper helper= new Helper();
    GPSHandler gpsHandler = new GPSHandler(this);
    private AssistenciaEmViagem assistenciaEmViagem = new AssistenciaEmViagem();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_em_viagem);
        terminar = (Button) findViewById(R.id.buttonTermina);
        terminar.setVisibility(View.INVISIBLE);
        editTextProcesso = (EditText) findViewById(R.id.editTextProcesso);
        editTextSeguradora =(EditText) findViewById(R.id.editTextSeguradora);
        editTextPassageiros = (EditText) findViewById(R.id.editTextPassageiros);
        mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));

    }

    public void onClickIniciar(View v) {

        if (Helper.isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (Helper.isEmpty(editTextSeguradora)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir a seguradora", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (Helper.isEmpty(editTextPassageiros)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o número de passageiros", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }


        editTextProcesso.setEnabled(false);
        editTextSeguradora.setEnabled(false);
        editTextPassageiros.setEnabled(false);

        String processo = editTextProcesso.getText().toString();
        assistenciaEmViagem.setNumProcesso(processo);
        assistenciaEmViagem.setData(helper.getDate());
        assistenciaEmViagem.setHoraDeInicio(helper.getTime());
        assistenciaEmViagem.setCompanhia(editTextSeguradora.getText().toString());
        assistenciaEmViagem.setNumPassageiros(Integer.parseInt(editTextPassageiros.getText().toString()));


        boolean result=helper.inicializarDados(processo);
        if (result) {
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
        ServicoHandler servicoHandler = new ServicoHandler();
        String processo = editTextProcesso.getText().toString();

        if(helper.isNetworkAvailable(this)){
            XMLHandler parser = new XMLHandler();
            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), "teste");
            if (mCapturedLocations == null || mCapturedLocations.size()<2){  //nunca executa este método
                Toast.makeText(getApplicationContext(), "Erro na captura ou directions API", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
            }

            GeocodingResult origem = servicoHandler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(0));
            GeocodingResult destino = servicoHandler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(mCapturedLocations.size()-1));
            assistenciaEmViagem.setDestino(destino.formattedAddress);
            assistenciaEmViagem.setOrigem(origem.formattedAddress);
            mCapturedLocations = servicoHandler.mergeCapture(mCapturedLocations);



            mCapturedLocations = servicoHandler.getRoute(mCapturedLocations, mContext);
            double distance = servicoHandler.getDistance();

            assistenciaEmViagem.setDistancia(distance);
            boolean portagens = servicoHandler.getPortagens();

            System.out.println(assistenciaEmViagem.toString());



            ArrayList<Double> lats = new ArrayList<>();

            ArrayList<Double> lngs = new ArrayList<>();

            for (int i = 0; i < mCapturedLocations.size(); i++) {
                lats.add(i,mCapturedLocations.get(i).lat);
                lngs.add(i,mCapturedLocations.get(i).lng);
            }


            Intent intent = new Intent(this, MapsActivity2.class);
            intent.putExtra("lat",lats);
            intent.putExtra("lng", lngs);
            intent.putExtra(Constants.TIPO_SERVICO, Constants.VIAGEM);
            intent.putExtra(Constants.INTENT_SERVICO, assistenciaEmViagem);
            intent.putExtra("portagem", portagens);

            startActivity(intent);
        }
        else {
            helper.displayPromptEnableWifi(this);
            XMLHandler handler = new XMLHandler();
            mCapturedLocations = handler.loadGpxData(Xml.newPullParser(), "teste");

            assistenciaEmViagem.setOrigem(mCapturedLocations.get(0).toString());
            assistenciaEmViagem.setDestino(mCapturedLocations.get(mCapturedLocations.size()-1).toString());
            handler.writeAssitenciaEmViagem(assistenciaEmViagem);
        }
    }
}