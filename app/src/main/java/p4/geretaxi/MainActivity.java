package p4.geretaxi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Servico> lista;
    Boolean resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helper helper = new Helper();

        switch (helper.checkAppStart()) {
            case NORMAL:
                boolean res = Helper.attemptLogin();

                if (res) {
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    sincronizar.execute();

                } else{
                    if (Helper.isNetworkAvailable(getApplicationContext())){
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getApplicationContext(), "Está em modo offline, não terá acesso à maior parte das funcionalidades",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case FIRST_TIME_VERSION:

                Toast.makeText(getApplicationContext(), " PRIMEIRA VEZ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, StorePreferencesActivity.class);
                startActivity(i);
                break;
            case FIRST_TIME:

                Intent intent = new Intent(this, StorePreferencesActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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



    public void onClickMain(View v) {




        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
    }

    AsyncTask<Void, Void, Void> sincronizar = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
           sincronizaClientes();

            return null;
        }
    };


//TODO: FAZER O MESMO PARA OS SERVIÇOS
    private void sincronizaClientes() {
        XMLHandler parser = new XMLHandler();
        GereBD bd = new GereBD();
        boolean res = false;
        List<Cliente> clientes;
        SharedPreference shared = new SharedPreference();
        clientes = parser.parseNovosClientes(Xml.newPullParser());

        //envia os clientes gravados localmente para a BD
        if(clientes.size() < 1) {
            for (Cliente c : clientes) {
                c.setIdMotorista(shared.getValueInt(MyApplication.getAppContext(),Constants.ID_MOTORISTA));
                res = bd.inserirCliente(c);
                if (res) {
                    clientes.remove(c);
                }
            }
            File file = new File(Environment.getExternalStorageDirectory(), "novocliente.xml");
            boolean deleted = file.delete();
            if(deleted) {
                for (Cliente c : clientes) {
                    parser.writenovoCliente(c);
                }
            }
        }
        // grava localmente clientes que estejam na BD mas não estejam no aparelho
        if (clientes.size() < 1)
            clientes.clear();
        clientes = bd.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
        if(clientes.size() < 1) {
            List<Cliente> clientesLocais;
            clientesLocais = parser.parseClientes(Xml.newPullParser());
            if (clientesLocais.size() < 1) {
                for (Cliente c : clientes) {
                    for (Cliente l: clientesLocais) {
                        if(!c.getEmail().equalsIgnoreCase(l.getEmail())){

                            parser.writeClientes(c);
                        }

                    }
                }
            }
        }
    }
}
