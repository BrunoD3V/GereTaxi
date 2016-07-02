package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import p4.geretaxi.ClassesDados.Cliente;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.WebServiceClass.GereBD;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.R;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;

public class EliminarClienteActivity extends AppCompatActivity {

    EditText editTextEliminarCliente;
    ListView listViewEliminaCliente;

    List<String> listItems;
    ArrayAdapter<String> adapter;
    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_cliente);

        editTextEliminarCliente = (EditText) findViewById(R.id.editTextEliminarCliente);
        listViewEliminaCliente = (ListView) findViewById(R.id.listViewEliminaCliente);


    }

    public void onClickButtonEliminar(View v) {
        GereBD bd = new GereBD();
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            boolean res = bd.excluirCliente(cliente.getNome(),
                    SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if (res) {
                File file = new File(Environment.getExternalStorageDirectory(), "clientes.xml");
                if (file.exists()) {
                    XMLHandler parser = new XMLHandler();
                    List<Cliente> clientes;
                    clientes = parser.parseClientes(Xml.newPullParser());
                    if (clientes.size() < 1) {
                        for (Cliente c : clientes) {
                            if (c.getEmail().equalsIgnoreCase(cliente.getEmail())) {
                                clientes.remove(c);
                            }
                        }

                        boolean deleted = file.delete();
                        if (deleted) {
                            for (Cliente c : clientes) {
                                parser.writeClientes(c);
                            }
                            Toast.makeText(getApplicationContext(), "Cliente apagado Localmente com Sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    listViewEliminaCliente.setAdapter(null);
                    editTextEliminarCliente.setText("");
                    Toast.makeText(getApplicationContext(), "Cliente Apagado com Sucesso.", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getApplicationContext(), "Não foi possivel eliminar o Cliente que pretendia.", Toast.LENGTH_LONG).show();
            }
        }



    }

    public void onClickButtonPesquisar(View v) {
        if(!Helper.isEmpty(editTextEliminarCliente)) {
            listItems= new ArrayList<>();
           if(Helper.isNetworkAvailable(this)){
               GereBD bd = new GereBD();

               cliente = bd.pesquisarCliente(editTextEliminarCliente.getText().toString(), SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
               if (cliente.getNome().equals(editTextEliminarCliente.getText().toString())) {
                   populateListView();
                   adapter = new ArrayAdapter<>(this, R.layout.item_list, listItems);
                   listViewEliminaCliente.setAdapter(adapter);
               } else {
                   Toast.makeText(getApplicationContext(), "Não foi possivel encontrar o cliente que procurava.", Toast.LENGTH_SHORT).show();
               }
           }else {
               XMLHandler parser = new XMLHandler();
               List<Cliente> clientes;
               clientes = new ArrayList<>();
               List<Cliente> clientesFinais;
               clientesFinais = new ArrayList<>();
               clientes = parser.parseClientes(Xml.newPullParser());

               for(Cliente c: clientes) {
                   if(!c.getNome().equalsIgnoreCase(cliente.getNome())) {
                        clientesFinais.add(c);
                   }
               }
               File file = new File(Environment.getExternalStorageDirectory(), "clientes.xml");
               boolean deleted = file.delete();
               if(deleted) {
                   for( Cliente c: clientesFinais){
                       parser.writeClientes(c);
                   }
               }
           }

        }
    }

    private void populateListView() {
        listItems.add(Constants.CLIENTE_TOSTRING + cliente.getNome());
        listItems.add(Constants.MORADA_TOSTRING + cliente.getMorada());
        listItems.add(Constants.CODIGO_POSTAL_TOSTRING + cliente.getCodigoPostal());
        listItems.add(Constants.NIF_TOSTRING + cliente.getNif());
        listItems.add(Constants.CONTACTO_TOSTRING + cliente.getContacto());
        listItems.add(Constants.EMAIL_TOSTRING + cliente.getEmail());
        listItems.add(Constants.TIPO_CLIENTE_TOSTRING + cliente.getTipo());
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
        }
        return super.onOptionsItemSelected(item);
    }
}
