package p4.geretaxi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MostraServicoActivity extends AppCompatActivity implements DialogCorrigeDadosS.CommunicatorCorrige{

    TextView textViewMostraServico;
    ListView listViewMostraServico;
    private Servico servico;
    Helper helper = new Helper();

    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_servico);

        listViewMostraServico = (ListView) findViewById(R.id.listViewMostraServico);
        textViewMostraServico = (TextView) findViewById(R.id.textViewMostraServico);
        servico = (Servico)getIntent().getSerializableExtra("ser");

        populateListView();

        listViewMostraServico.setAdapter(adapter);
        listViewMostraServico.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        DialogFragment newFragment = DialogCorrigeDadosS.newInstance(position);
                        newFragment.show(fragmentTransaction, "dialog");

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

        listItems.add(Constants.PROCESSO + servico.getProcesso());
        listItems.add(Constants.NOME_CLIENTE + servico.getNomeCliente());
        listItems.add(Constants.DATA + servico.getData());
        listItems.add(Constants.HORA + servico.getHoraDeInicio());
        listItems.add(Constants.ORIGEM + servico.getOrigem());
        listItems.add(Constants.DESTINO + servico.getDestino());
        listItems.add(Constants.PASSAGEIROS + servico.getNumPassageiros().toString());
        listItems.add(Constants.PORTAGENS + servico.getCustoPortagens().toString());
        listItems.add(Constants.ESPERA + servico.getHorasDeEspera());
        listItems.add(Constants.DISTANCIA + String.valueOf(servico.getDistancia() + Constants.KMS));

    }

    public void onClickSubmeterServico(View v) {

        if(helper.isNetworkAvailable(getApplicationContext())){
            SharedPreference sharedPreference = new SharedPreference();
            servico.setIdMotorista(sharedPreference.getValueInt(this, Constants.ID_MOTORISTA));

            GereBD gereBD = new GereBD();
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

        //porque só mostra às vezes?
        adapter.notifyDataSetChanged();
        populateListView();



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

}
