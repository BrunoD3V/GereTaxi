package p4.geretaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ConsultarClientesActivity extends AppCompatActivity {

    ListView listViewListaClientes;
    List<Cliente> clientes;
    ArrayAdapter<Cliente> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_clientes);

        listViewListaClientes = (ListView) findViewById(R.id.listViewListaClientes);

        if (Helper.isNetworkAvailable(getApplicationContext())) {
            GereBD bd = new GereBD();
            clientes = bd.listarClientes();
            adapter = new ArrayAdapter<Cliente>(this, R.layout.item_list, clientes);
            listViewListaClientes.setAdapter(adapter);
        }else {
            Toast.makeText(getApplicationContext(), "Como está offline só serão listados so clientes guardados localmente", Toast.LENGTH_LONG).show();
            XMLHandler parser = new  XMLHandler();
            clientes = parser.parseClientes(Xml.newPullParser());
            adapter = new ArrayAdapter<Cliente>(this, R.layout.item_list, clientes);
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
}
