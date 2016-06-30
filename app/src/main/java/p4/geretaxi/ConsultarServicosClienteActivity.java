package p4.geretaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

public class ConsultarServicosClienteActivity extends AppCompatActivity {

    EditText edtProcurarPorCliente;
    Cliente clienteGlobal;
    GereBD gereBD;
    List<Servico> listaServicos;
    ArrayAdapter<Servico> adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_servicos_cliente);
        edtProcurarPorCliente = (EditText) findViewById(R.id.edtProcurarPorCliente);
        listView = (ListView) findViewById(R.id.listView);
    }
    public void onClickPesquisar(View v){
        /*TODO: PESQUISAR SERVIÇOS POR CLIENTE
        1-Ler da edit text
        2-Verificar se existe internet
        se há net
        3-Pesquisar se Existe o cliente na base de dados
        3.1-Se não existe
             Toast
        3.2-se existe Query dos serviços e disponibilizar na lista
        se não há net
        4-Pesquisar se Existe o cliente localmente
        4.1-se nao existe
             toast
        4.2-se existe parse sericos do cliente e mostrar serviços na lista*/
        if(Helper.isEmpty(edtProcurarPorCliente)){
            Toast.makeText(getApplicationContext(),"Insira o Nome do cliente.", Toast.LENGTH_LONG).show();
            return;
        }
        String nomeCliente = edtProcurarPorCliente.getText().toString();
        if(Helper.isNetworkAvailable(getApplicationContext())){
            gereBD = new GereBD();
            clienteGlobal = gereBD.pesquisarCliente(nomeCliente,SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if(clienteGlobal!=null){

                listaServicos = gereBD.pesquisarServicosPorCliente(nomeCliente,SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
                adapter = new ArrayAdapter<>(this, R.layout.item_list, listaServicos);
                listView.setAdapter(adapter);
            }
        }else{
            //SEM INTERNET
            Toast.makeText(getApplicationContext(), "TODO: PESQUISAR SEM NET", Toast.LENGTH_LONG).show();
        }
    }
}
