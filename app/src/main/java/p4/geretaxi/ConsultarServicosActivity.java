package p4.geretaxi;

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

public class ConsultarServicosActivity extends AppCompatActivity {

    ListView listViewServicos;
    List<Servico> servicos  ;
    ArrayAdapter<Servico> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_servicos);

        listViewServicos = (ListView) findViewById(R.id.listViewServicos);
        //SE POSSUI INTERNET VAI TENTAR PREENCHER A LIST VIEW COM DADOS DA BASE DE DADOS
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            GereBD bd = new GereBD();

            servicos = bd.listarServico(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            adapter = new ArrayAdapter<>(this, R.layout.item_list, servicos);
            listViewServicos.setAdapter(adapter);
        }else {//SE NAO TEM INTERNET VAI PREENCHER COM OS CLIENTES QUE ESTÃO ARMAZENADOS LOCALMENTE NO TELEFONE
            Toast.makeText(getApplicationContext(), "MODO OFFLINE: Serão apenas listados os Serviços guardados localmente.", Toast.LENGTH_LONG).show();
            XMLHandler parser = new  XMLHandler();
            servicos = parser.parseServico(Xml.newPullParser());
            int i = 1;
            for(Servico s: servicos) {
                System.out.println(i);
                System.out.println(s.getTrajeto());
                i++;
            }
            adapter = new ArrayAdapter<>(this, R.layout.item_list, servicos);
            listViewServicos.setAdapter(adapter);
        }

        listViewServicos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), MostraServicoActivity.class);
                intent.putExtra("ser", servicos.get(i));
                boolean atualizar = true;
                intent.putExtra("atualiza", atualizar);
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
