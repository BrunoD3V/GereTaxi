package p4.geretaxi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import p4.geretaxi.ClassesDados.Cliente;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.WebServiceClass.GereBD;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.R;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;

public class ConsultarClientesActivity extends AppCompatActivity {

    ListView listViewListaClientes;
    List<Cliente> clientes;
    ArrayAdapter<Cliente> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_clientes);

        listViewListaClientes = (ListView) findViewById(R.id.listView);

        //SE POSSUI INTERNET VAI TENTAR PREENCHER A LIST VIEW COM DADOS DA BASE DE DADOS
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            GereBD bd = new GereBD();

            clientes = bd.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            adapter = new ArrayAdapter<>(this, R.layout.item_list, clientes);
            listViewListaClientes.setAdapter(adapter);
        }else {//SE NAO TEM INTERNET VAI PREENCHER COM OS CLIENTES QUE ESTÃO ARMAZENADOS LOCALMENTE NO TELEFONE
            Toast.makeText(getApplicationContext(), "MODO OFFLINE: Serão apenas listados os Clientes guardados localmente.", Toast.LENGTH_LONG).show();
            XMLHandler parser = new  XMLHandler();
            clientes = parser.parseClientes(Xml.newPullParser());
            adapter = new ArrayAdapter<>(this, R.layout.item_list, clientes);
            listViewListaClientes.setAdapter(adapter);
        }

        listViewListaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MostraClienteActivity.class);
                intent.putExtra("cliente", clientes.get(i));
                startActivity(intent);
            }
        });

    }

    public void onClickMenuPrincipal(View v){
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
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
                sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
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
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
