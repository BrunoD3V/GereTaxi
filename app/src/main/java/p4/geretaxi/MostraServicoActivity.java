package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostraServicoActivity extends AppCompatActivity {

    TextView textViewMostraServico;
    ListView listViewMostraServico;
    private AssistenciaEmViagem assistenciaEmViagem;
    private AcidentesDeTrabalho acidentesDeTrabalho;
    private ServicoParticular servicoParticular;

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

        switch (tipo) {
            case Constants.VIAGEM:
                assistenciaEmViagem = (AssistenciaEmViagem) getIntent().getSerializableExtra("ser");

                listItems.add(Constants.PROCESSO + assistenciaEmViagem.getNumProcesso());
                listItems.add(Constants.COMPANHIA + assistenciaEmViagem.getCompanhia());
                listItems.add(Constants.DATA + assistenciaEmViagem.getData());
                listItems.add(Constants.HORA + assistenciaEmViagem.getHoraDeInicio());
                listItems.add(Constants.ORIGEM + assistenciaEmViagem.getOrigem());
                listItems.add(Constants.DESTINO + assistenciaEmViagem.getDestino());
                listItems.add(Constants.PASSAGEIROS + assistenciaEmViagem.getNumPassageiros().toString());
                listItems.add(Constants.DISTANCIA +
                        String.valueOf(assistenciaEmViagem.getDistancia() + Constants.KMS));
                break;
            case Constants.ACIDENTE:
                acidentesDeTrabalho = (AcidentesDeTrabalho) getIntent().getSerializableExtra(Constants.INTENT_SERVICO);
                break;
            case Constants.PARTICULAR:
                servicoParticular = (ServicoParticular) getIntent().getSerializableExtra(Constants.INTENT_SERVICO);
                break;
            default:
                break;
        }
        adapter = new ArrayAdapter<String>(this, R.layout.item_list, listItems);
        listViewMostraServico.setAdapter(adapter);



    }

    public void onClickSubmeterServico(View v) {

    }
}
