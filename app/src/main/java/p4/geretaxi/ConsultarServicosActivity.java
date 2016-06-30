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

public class ConsultarServicosActivity extends AppCompatActivity {

    ListView listViewServicos;
    List<Servico> servicos  ;
    ArrayAdapter<Servico> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_servicos);

        listViewServicos = (ListView) findViewById(R.id.listViewServicos);

        if (Helper.isNetworkAvailable(getApplicationContext())) {
            GereBD bd = new GereBD();

            servicos = bd.listarServico(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            adapter = new ArrayAdapter<>(this, R.layout.item_list, servicos);
            listViewServicos.setAdapter(adapter);
        }else {
            Toast.makeText(getApplicationContext(), "MODO OFFLINE: Serão apenas listados os Serviços guardados localmente.", Toast.LENGTH_LONG).show();
            XMLHandler parser = new  XMLHandler();
            servicos = parser.parseServico(Xml.newPullParser());
            adapter = new ArrayAdapter<>(this, R.layout.item_list, servicos);
            listViewServicos.setAdapter(adapter);
        }

        listViewServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MostraServicoActivity.class);
                intent.putExtra("ser", servicos.get(i));
                startActivity(intent);
            }
        });
    }
}
