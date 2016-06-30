package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

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
        listitems.add(Constants.NOME_CLIENTE + cliente.getNome());
        listitems.add(Constants.MORADA + cliente.getMorada());
        listitems.add(Constants.CODIGO_POSTAL + cliente.getCodigoPostal());
        listitems.add(Constants.NIF + cliente.getNif());
        listitems.add(Constants.CONTACTO + cliente.getContacto());
        listitems.add(Constants.EMAIL + cliente.getEmail());
        listitems.add(Constants.TIPO + cliente.getTipo());
    }
}
