package p4.geretaxi;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EliminaClienteActivity extends AppCompatActivity {

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
                XMLHandler parser = new XMLHandler();
                List<Cliente> clientes;
                clientes = parser.parseClientes(Xml.newPullParser());
                if (clientes.size()<1) {
                    for (Cliente c : clientes) {
                        if (c.getEmail().equalsIgnoreCase(cliente.getEmail())) {
                            clientes.remove(c);
                        }
                    }
                    File file = new File(Environment.getExternalStorageDirectory(), "clientes.xml");
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
                Toast.makeText(getApplicationContext(),"Cliente Apagado com Sucesso.", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Não foi possivel eliminar o Cliente que pretendia.", Toast.LENGTH_LONG).show();
            }
        }


        //TODO: SO PODE APAGAR SE TIVER NET?
    }

    public void onClickButtonPesquisar(View v) {
        if(!Helper.isEmpty(editTextEliminarCliente)) {
            GereBD bd = new GereBD();
            listItems= new ArrayList<>();
            cliente = bd.pesquisarCliente(editTextEliminarCliente.getText().toString(), SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if (cliente.getNome().equals(editTextEliminarCliente.getText().toString())) {
                populateListView();
                adapter = new ArrayAdapter<>(this, R.layout.item_list, listItems);
                listViewEliminaCliente.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Não foi possivel encontrar o cliente que procurava.", Toast.LENGTH_SHORT).show();
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
}
