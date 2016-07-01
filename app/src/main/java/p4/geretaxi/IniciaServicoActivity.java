package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    EditText editTextPassageiros;
    Spinner spinner;
    private boolean portagens;
    private String nomeCliente;
    GereBD gereBD;
    ArrayList<String> clientesSpinnerParticular = new ArrayList<>();
    ArrayList<String> clientesSpinnerCompanhia = new ArrayList<>();
    XMLHandler handler;
    GPSHandler gpsHandler = new GPSHandler(this);
    private Servico servico = new Servico();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_em_viagem);

        terminar = (Button) findViewById(R.id.buttonTermina);
        terminar.setVisibility(View.INVISIBLE);
        editTextProcesso = (EditText) findViewById(R.id.editTextProcesso);
        editTextPassageiros = (EditText) findViewById(R.id.editTextPassageiros);
        mContext = new GeoApiContext().setApiKey(getString(R.string.google_maps_web_services_key));
        servico = (Servico) getIntent().getSerializableExtra(Constants.INTENT_SERVICO);

        //CASO SEJA UM SERVIÇO PARTICULAR O NUMERO DO SERVIÇO É GERADO PELA CONCATENAÇÃO SP+DATA+HORA E NAO PERMITE AO USER MODIFICAR O NUMERO DO PROCESSO
        if(servico.getTipo().equals("Serviço Particular")){
            editTextProcesso.setText("SPD"+Helper.getDate()+"H"+Helper.getTime());
            editTextProcesso.setEnabled(false);
        }
        handler = new XMLHandler();
        gereBD = new GereBD();
        List<Cliente> clientes;

        if(Helper.isNetworkAvailable(getApplicationContext())){
            clientes = gereBD.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));//listar clientes está a mostrar todos
            for (Cliente c: clientes) {
                //SE FOR UM CLIENTE PARTICULAR INSERE NA LISTA DE CLIENTES PARTICULARES
                if(c.getTipo().trim().equals("Particular")){
                    clientesSpinnerParticular.add(c.getNome());
                }else{//SE FOR UMA COMPANHIA INSERE NA LISTA DE COMPANHIAS
                    clientesSpinnerCompanhia.add(c.getNome());
                }
            }
        }else{
            clientes = handler.parseClientes(Xml.newPullParser());
            for (Cliente c: clientes) {
                if(c.getTipo().trim().equals("Particular")){
                    clientesSpinnerParticular.add(c.getNome());
                }else{//SE FOR UMA COMPANHIA INSERE NA LISTA DE COMPANHIAS
                    clientesSpinnerCompanhia.add(c.getNome());
                }
            }
        }
        spinner = (Spinner) findViewById(R.id.spinner);
        if(servico.getTipo().trim().equals(Constants.PARTICULAR)){
            //PREENCHE O SPINNER APENAS COM CLIENTES PARTICULARES
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, clientesSpinnerParticular);
        }else {//PREENCHE O SPINNER APENAS COM COMPANHIAS
            adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientesSpinnerCompanhia);
        }

        //AO CLICAR NO SPINNER É ESCOLHIDO O CLIENTE
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                nomeCliente= item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner.setAdapter(adapter);
    }

    public void onClickIniciar(View v) {

        if (Helper.isEmpty(editTextProcesso)){
            Toast toast = Toast.makeText(getApplicationContext(), "Tem que inserir o processo", Toast.LENGTH_SHORT);
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
        editTextPassageiros.setEnabled(false);
        spinner.setEnabled(false);

        String processo = editTextProcesso.getText().toString();
        servico.setProcesso(processo);
        servico.setData(Helper.getDate());
        servico.setHoraDeInicio(Helper.getTime());
        servico.setNomeCliente(nomeCliente);
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

            mCapturedLocations = parser.loadGpxData(Xml.newPullParser(), "comPortagem");

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
            mCapturedLocations = handler.loadGpxData(Xml.newPullParser(), "comPortagem");//TODO a mudar depois dos testes
            if(mCapturedLocations.size() > 1) {
                handler.writeTrajecto(mCapturedLocations, processo);
                servico.setOrigem(mCapturedLocations.get(0).toString());
                servico.setDestino(mCapturedLocations.get(mCapturedLocations.size() - 1).toString());
                if (handler.writeServico(servico))
                    Toast.makeText(getApplicationContext(), "Servido inserido localmente", Toast.LENGTH_SHORT);
            }

            Intent i = new Intent(this, MenuActivity.class);
            startActivity(i);
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
}
