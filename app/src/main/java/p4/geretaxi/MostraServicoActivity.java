package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MostraServicoActivity extends AppCompatActivity implements DialogCorrigeDadosS.CommunicatorCorrige{

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
                textViewMostraServico.setText(R.string.assistencia_em_viagem);
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

    public void onClickSubmeterServico(View v) {
            //TODO
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogMessage(String dados, int num) {
        switch (tipo) {
            case Constants.VIAGEM:
                switch (num) {
                    case 0:
                        assistenciaEmViagem.setNumProcesso(dados);
                        break;
                    case 1:
                        assistenciaEmViagem.setCompanhia(dados);
                        break;
                    case 2:
                        assistenciaEmViagem.setData(dados);
                        break;
                    case 3:
                        assistenciaEmViagem.setHoraDeInicio(dados);
                        break;
                    case 4:
                        assistenciaEmViagem.setOrigem(dados);
                        break;
                    case 5:
                        assistenciaEmViagem.setDestino(dados);
                        break;
                    case 6:
                        assistenciaEmViagem.setNumPassageiros(Integer.parseInt(dados));
                        break;
                    case 7:
                        assistenciaEmViagem.setDistancia(Double.parseDouble(dados));
                        break;
                    default:
                        break;
                }
                break;
            case Constants.ACIDENTE:
                switch (num) {
                    case 0:
                        acidentesDeTrabalho.setNumProcesso(dados);
                        break;
                    case 1:
                        acidentesDeTrabalho.setCompanhia(dados);
                        break;
                    case 2:
                        acidentesDeTrabalho.setData(dados);
                        break;
                    case 3:
                        acidentesDeTrabalho.setHoraDeInicio(dados);
                        break;
                    case 4:
                        acidentesDeTrabalho.setOrigem(dados);
                        break;
                    case 5:
                        acidentesDeTrabalho.setDestino(dados);
                        break;
                    case 6:
                        acidentesDeTrabalho.setNumPassageiros(Integer.parseInt(dados));
                        break;
                    case 7:
                        acidentesDeTrabalho.setDistancia(Double.parseDouble(dados));
                        break;
                    default:
                        break;
                }
                break;
            case Constants.PARTICULAR:
               //TODO
                break;
            default:
                break;

        }
    }
}
