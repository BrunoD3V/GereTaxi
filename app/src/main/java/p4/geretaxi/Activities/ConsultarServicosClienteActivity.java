package p4.geretaxi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.List;

import p4.geretaxi.ClassesDados.Cliente;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.KSoapClass.GereBD;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.R;
import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.SharedPreference;

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
        //VERIFICA SE O USER ESCREVEU ALGUMA COISA PARA PESQUISAR
        if(Helper.isEmpty(edtProcurarPorCliente)){
            Toast.makeText(getApplicationContext(),"Insira o Nome do cliente.", Toast.LENGTH_LONG).show();
            return;
        }
        String nomeCliente = edtProcurarPorCliente.getText().toString();
        //SE TEM INTERNET VAI PESQUISAR SE O CLIENTE EXISTE
        if(Helper.isNetworkAvailable(getApplicationContext())){
            gereBD = new GereBD();
            clienteGlobal = gereBD.pesquisarCliente(nomeCliente, SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if(clienteGlobal!=null){
                //SE O CLIENTE EXISTE PESQUISA TODOS OS SERVICOS DO CLIENTE ESPECIFICADO
                listaServicos = gereBD.pesquisarServicosPorCliente(nomeCliente,SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
                adapter = new ArrayAdapter<>(this, R.layout.item_list, listaServicos);
                listView.setAdapter(adapter);
            }
        }else{
            //SEM INTERNET
            Toast.makeText(getApplicationContext(), "TODO: PESQUISAR SEM NET", Toast.LENGTH_LONG).show();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MostraServicoActivity.class);
                intent.putExtra("ser", listaServicos.get(i));
                boolean atualizar = true;
                intent.putExtra("atualiza", atualizar);
                startActivity(intent);
            }
        });
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

            case R.id.settings_id:                 Intent in = new Intent(this, CoordenadasActivity.class);                 startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
