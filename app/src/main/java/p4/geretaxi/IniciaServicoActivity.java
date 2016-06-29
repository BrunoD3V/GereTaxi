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

public class IniciaServicoActivity extends AppCompatActivity {

    List<LatLng> mCapturedLocations;
    private GeoApiContext mContext;
    Button terminar;
    EditText editTextProcesso;
    EditText editTextSeguradora;
    EditText editTextPassageiros;
    private boolean portagens;


   // Helper helper= new Helper();
    GPSHandler gpsHandler = new GPSHandler(this);
    private Servico servico = new Servico();

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


        servico = (Servico) getIntent().getSerializableExtra(Constants.INTENT_SERVICO);
        System.out.println(servico.getTipo());
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
        int i = Integer.parseInt(editTextPassageiros.getText().toString());
        if (i < 1 || i > 8) {
            Toast.makeText(getApplicationContext(), Constants.N_PASSAGEIROS_VALIDO, Toast.LENGTH_SHORT).show();
            return;
        }


        editTextProcesso.setEnabled(false);
        editTextSeguradora.setEnabled(false);
        editTextPassageiros.setEnabled(false);

        String processo = editTextProcesso.getText().toString();
        servico.setProcesso(processo);
        servico.setData(Helper.getDate());
        servico.setHoraDeInicio(Helper.getTime());
        servico.setNomeCliente(editTextSeguradora.getText().toString());
        servico.setNumPassageiros(i);


        boolean result=Helper.inicializarDados(processo);
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

        if(Helper.isNetworkAvailable(this)){

            SharedPreference preference = new SharedPreference();



            preference.save(getApplicationContext(), Constants.TRUE, Constants.SESSION);
            XMLHandler parser = new XMLHandler();



            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), "lisboa");

            if (mCapturedLocations.size() < 1) {
                Toast.makeText(getApplicationContext(), "Erro na captura ou directions API", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
                return;
            }
            GeocodingResult origem = servicoHandler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(0));
            GeocodingResult destino = servicoHandler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(mCapturedLocations.size() - 1));
            servico.setDestino(destino.formattedAddress);
            servico.setOrigem(origem.formattedAddress);

            mCapturedLocations = servicoHandler.mergeCapture(mCapturedLocations);
            mCapturedLocations = servicoHandler.getRoute(mCapturedLocations, mContext);
            double distance = servicoHandler.getDistance();
            servico.setDistancia(distance);
            portagens = servicoHandler.getPortagens();




            ArrayList<Double> lats = new ArrayList<>();

            ArrayList<Double> lngs = new ArrayList<>();

            for (int i = 0; i < mCapturedLocations.size(); i++) {
                lats.add(i, mCapturedLocations.get(i).lat);
                lngs.add(i, mCapturedLocations.get(i).lng);
            }

            Intent intent = new Intent(this, MapsActivity2.class);
            intent.putExtra("lat", lats);
            intent.putExtra("lng", lngs);

            intent.putExtra(Constants.INTENT_SERVICO, servico);
            intent.putExtra("portagem", portagens);
            startActivity(intent);



        } else {
            Helper helper = new Helper();
            helper.displayPromptEnableWifi(this);
            XMLHandler handler = new XMLHandler();
            handler.writeTrajecto(mCapturedLocations, processo);
            mCapturedLocations = handler.loadGpxData(Xml.newPullParser(), "xxx");
            servico.setOrigem(mCapturedLocations.get(0).toString());
            servico.setDestino(mCapturedLocations.get(mCapturedLocations.size()-1).toString());
            handler.writeServico(servico);
        }
    }
}