package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.Mail;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.Fragments.DialogCorrigeDadosFragment;
import p4.geretaxi.Fragments.DialogSpinnerFragment;
import p4.geretaxi.KSoapClass.GereBD;
import p4.geretaxi.R;

public class MostraServicoActivity extends AppCompatActivity implements DialogCorrigeDadosFragment.CommunicatorCorrige, DialogSpinnerFragment.CommunicatorCorrige {

    TextView textViewMostraServico;
    ListView listViewMostraServico;
    private Servico servico;
    Helper helper = new Helper();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    boolean atualiza;
    Button btnSubmeter;
    Button btnAtualizar;
    GereBD gereBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_servico);
        btnSubmeter = (Button) findViewById(R.id.buttonSubmeterServico);
        btnAtualizar = (Button) findViewById(R.id.btnAtualizarServico);
        listViewMostraServico = (ListView) findViewById(R.id.listViewMostraServico);
        textViewMostraServico = (TextView) findViewById(R.id.textViewMostraServico);
        servico = (Servico)getIntent().getSerializableExtra("ser");
        atualiza = getIntent().getBooleanExtra("atualiza",false);

        populateListView();

        if(atualiza){
            btnSubmeter.setVisibility(View.INVISIBLE);
            btnAtualizar.setVisibility(View.VISIBLE);
        }else{
            btnAtualizar.setVisibility(View.INVISIBLE);
            btnSubmeter.setVisibility(View.VISIBLE);
        }

        listViewMostraServico.setAdapter(adapter);
        listViewMostraServico.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        if(position==1){
                            DialogFragment newFragment2 = DialogSpinnerFragment.newInstance(1);
                            newFragment2.show(fragmentTransaction, "dialogo");
                        }else{
                            DialogFragment newFragment = DialogCorrigeDadosFragment.newInstance(position);
                            newFragment.show(fragmentTransaction, "dialog");
                        }

                    }
                }
        );
    }


 public void populateListView() {

        adapter = new ArrayAdapter<>(this, R.layout.item_list, listItems);
        switch (servico.getTipo()) {
            case Constants.VIAGEM:
                textViewMostraServico.setText(R.string.assistencia_em_viagem);
                break;
            case Constants.ACIDENTE:
                textViewMostraServico.setText(R.string.acidente_de_trabalho);
                break;
            case Constants.PARTICULAR:
                textViewMostraServico.setText(R.string.particular);
                break;
            default:
                break;
        }
        servico = (Servico) getIntent().getSerializableExtra("ser");

        listItems.add(Constants.PROCESSO_TOSTRING + servico.getProcesso());
        listItems.add(Constants.CLIENTE_TOSTRING + servico.getNomeCliente());
        listItems.add(Constants.DATA_TOSTRING + servico.getData());
        listItems.add(Constants.HORA_DE_INICIO_TOSTRING + servico.getHoraDeInicio());
        listItems.add(Constants.ORIGEM_TOSTRING + servico.getOrigem());
        listItems.add(Constants.DESTINO_TOSTRING + servico.getDestino());
        listItems.add(Constants.NUM_PASSAGEIROS_TOSTRING + servico.getNumPassageiros().toString());
        listItems.add(Constants.CUSTO_PORTAGENS_TOSTRING + servico.getCustoPortagens().toString());
        listItems.add(Constants.HORAS_DE_ESPERA_TOSTRING + servico.getHorasDeEspera());
        listItems.add(Constants.DISTANCIA_TOSTRING + String.valueOf(servico.getDistancia() + Constants.KMS_TOSTRING));
    }

    public void onClickAtualizar(View v){
        gereBD = new GereBD();
        if(Helper.isNetworkAvailable(getApplicationContext())){
            System.out.println("SERVICO: " + servico.getTrajeto());
            boolean result = gereBD.atualizarServico(servico);
            if(result){
                Toast.makeText(getApplicationContext(),"Serviço Atualizado com Sucesso.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(),"Não foi possivel atualizar o Serviço.", Toast.LENGTH_LONG).show();
                return;
            }
        }else{
            Toast.makeText(getApplicationContext(),"Não está ligado a Internet.", Toast.LENGTH_LONG).show();
            return;
        }

        Intent i = new Intent(this, GerirClientesActivity.class);
        startActivity(i);

    }
    public void onClickSubmeterServico(View v) {

        if(helper.isNetworkAvailable(getApplicationContext())){
            if (!SharedPreference.isSessionEnabled()){
               Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return;
            }
            SharedPreference sharedPreference = new SharedPreference();
            servico.setIdMotorista(sharedPreference.getValueInt(this, Constants.ID_MOTORISTA));

            gereBD = new GereBD();
            boolean res = gereBD.inserirServico(servico);
            if (!res){
                Toast.makeText(getApplicationContext(), "Erro na inserção", Toast.LENGTH_SHORT).show();
                XMLHandler xmlHandler = new XMLHandler();
                xmlHandler.writeServico(servico);
            }else {
                Toast.makeText(getApplicationContext(), "Serviço inserido com sucesso", Toast.LENGTH_SHORT).show();
                enviaMail.execute();
            }

        }
        else{
            XMLHandler xmlHandler = new XMLHandler();
            xmlHandler.writeServico(servico);

        }

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void onClickVerTrajeto(View v) {
        Intent intent = new Intent(this, MostraTrajectoMapsActivity.class);
        intent.putExtra("trajecto", servico);
        startActivity(intent);
    }

    @Override
    public void onDialogMessage(String dados, int num) {

        switch (num) {
            case 0:
                servico.setProcesso(dados);
                break;
            case 1:
                servico.setNomeCliente(dados);
                break;
            case 2:
                servico.setData(dados);
                break;
            case 3:
                servico.setHoraDeInicio(dados);
                break;
            case 4:
                servico.setOrigem(dados);
                break;
            case 5:
                servico.setDestino(dados);
                break;
            case 6:
                servico.setNumPassageiros(Integer.parseInt(dados));
                break;
            case 7:
                servico.setCustoPortagens(Double.parseDouble(dados));
                break;
            case 8:
                servico.setHorasDeEspera(Double.parseDouble(dados));
                break;
            case 9:
                servico.setDistancia(Double.parseDouble(dados));
                break;
            default:
                break;
        }
        //TODO: METODO ATUALIZAR SERVICO
        listItems.clear();
        listViewMostraServico.setAdapter(null);
        populateListView();
        listViewMostraServico.setAdapter(adapter);


    }
    AsyncTask<Void, Void, Boolean> enviaMail =
            new AsyncTask<Void, Void, Boolean>() {
                @Override
                protected Boolean doInBackground(Void... params) {
                    boolean result = false;
                    Mail mail = new Mail();
                    String[] toArr = {mail.get_from()};
                    mail.set_to(toArr);
                    mail.set_subject(servico.getProcesso());
                    mail.set_body(servico.toString() + "\n Mensagem enviada pela GereTaxiApp");

                    try {
                        mail.addAttachment(Environment.getExternalStorageDirectory() + "/" + Constants.TRAJETO +
                                servico.getProcesso() + Constants.PONTO_XML);
                        result = mail.send();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                   return result;
                }

                protected void onPostExecute(Boolean result){
                    File file = new File(Environment.getExternalStorageDirectory(), Constants.TRAJETO +
                        servico.getProcesso() + Constants.PONTO_XML);
                    boolean deleted = file.delete();
                    System.out.println("Apaga o trajecto" + deleted);
                    file = new File(Environment.getExternalStorageDirectory(), servico.getProcesso()+Constants.PONTO_XML);
                    deleted = file.delete();
                    System.out.println("Apaga a captura" + deleted);
                }
            };

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
