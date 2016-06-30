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

    List<String> listitems;
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
                List<Cliente> clientes = new ArrayList<>();
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
                        Toast.makeText(getApplicationContext(), "Cliente apagado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void onClickButtonPesquisar(View v) {
        if(!Helper.isEmpty(editTextEliminarCliente)) {
            GereBD bd = new GereBD();
            listitems= new ArrayList<>();
            SharedPreference sharedPreference = new SharedPreference();
            cliente= bd.pesquisarCliente(editTextEliminarCliente.getText().toString(), SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if (cliente != null) {
                populateListView();
                adapter = new ArrayAdapter<String>(this, R.layout.item_list, listitems);
                listViewEliminaCliente.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "Não existem clientes com esse nome", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void populateListView() {
        listitems.add(Constants.CLIENTE_TOSTRING + cliente.getNome());
        listitems.add(Constants.MORADA_XML +": "+ cliente.getMorada());
        listitems.add(Constants.CODIGO_POSTAL_XML +": "+ cliente.getCodigoPostal());
        listitems.add(Constants.NIF_XML +": "+ cliente.getNif());
        listitems.add(Constants.CONTACTO_XML +": "+ cliente.getContacto());
        listitems.add(Constants.EMAIL +": "+ cliente.getEmail());
        listitems.add(Constants.TIPO_XML +": "+ cliente.getTipo());
    }
}
