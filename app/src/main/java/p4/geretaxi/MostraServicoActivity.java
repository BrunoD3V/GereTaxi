package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostraServicoActivity extends AppCompatActivity implements DialogCorrigeDadosS.CommunicatorCorrige{

    TextView textViewMostraServico;
    ListView listViewMostraServico;
    private Servico servico;



    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private String tipo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_servico);

        listViewMostraServico = (ListView) findViewById(R.id.listViewMostraServico);
        textViewMostraServico = (TextView) findViewById(R.id.textViewMostraServico);
        tipo = getIntent().getExtras().getString(Constants.TIPO_SERVICO);




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

                textViewMostraServico.setText(R.string.assistencia_em_viagem);
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
        inserirServico();

        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    public void inserirServico() {
        new Thread(new Runnable() {
            public void run() {
                GereServico manager = new GereServico();
                Boolean resultado = manager.inserirServico(servico);
                Log.d("Objeto:", servico.toString());
                Log.d("Resposta:", resultado.toString());
            }
        }).start();
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

        listItems.clear();


        populateListView();
        //porque só mostra às vezes?
        adapter.notifyDataSetChanged();
    }
}
