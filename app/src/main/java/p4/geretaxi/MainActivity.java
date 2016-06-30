package p4.geretaxi;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Xml;
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
        SharedPreference sharedPreference = new SharedPreference();
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
                Toast.makeText(getApplicationContext(), " PRIMEIRA VEZ ESTA VERSÂO", Toast.LENGTH_SHORT).show();
                break;
            case FIRST_TIME:
                Toast.makeText(getApplicationContext(), " PRIMEIRA VEZ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, StorePreferencesActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void onClickMostraCenas(View v){
        Helper helper = new Helper();
        switch (helper.checkAppStart()) {
            case NORMAL:
                Toast.makeText(getApplicationContext(), " NORMAL", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, StorePreferencesActivity.class);
                startActivity(i);
                break;
            case FIRST_TIME_VERSION:
                Toast.makeText(getApplicationContext(), " PRIMEIRA VEZ ESTA VERSÂO", Toast.LENGTH_SHORT).show();
                break;
            case FIRST_TIME:
                Toast.makeText(getApplicationContext(), " PRIMEIRA VEZ", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
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

    private void sincronizaClientes() {
        XMLHandler parser = new XMLHandler();
        GereBD bd = new GereBD();
        boolean res = false;
        List<Cliente> clientes = new ArrayList<>();
        SharedPreference shared = new SharedPreference();
        clientes = parser.parseNovosClientes(Xml.newPullParser());

        //envia os clientes gravados localmente para a BD
        if(clientes != null) {
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
        if (clientes != null)
            clientes.clear();
        clientes = bd.listarClientes();
        if(clientes != null) {
            List<Cliente> clientesLocais = new ArrayList<>();
            clientesLocais = parser.parseClientes(Xml.newPullParser());
            if (clientesLocais != null) {
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
