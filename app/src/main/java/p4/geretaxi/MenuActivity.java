package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class MenuActivity extends AppCompatActivity {

    Button btnGerirClientes;
    GereBD gereBD;
    List<Cliente> listaClientes;
    boolean permission = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnGerirClientes = (Button) findViewById(R.id.btnGerirClientes);
        btnGerirClientes.setEnabled(false);
        if (Helper.isSessionEnabled()){
            btnGerirClientes.setEnabled(true);
        } else if (Helper.isNetworkAvailable(MyApplication.getAppContext())){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        gereBD = new GereBD();
        if(Helper.isNetworkAvailable(getApplicationContext())){
            listaClientes = gereBD.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
            if(listaClientes.size()>0){
                permission = true;
            }
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

    public void onClickIniciarServico(View v) {
        if(permission){
            EscolherServicoDialogFragment dialogFragment = EscolherServicoDialogFragment.newInstance();
            dialogFragment.show(this.getFragmentManager(), "EscolheServico");
        }else{
            Toast.makeText(getApplicationContext(),"Ainda não possui clientes, deverá inserir na opção Gestão de Clientes.", Toast.LENGTH_LONG).show();
        }
    }
    public void onClickGerirClientes(View v){
        Intent i = new Intent(this,GerirClientesActivity.class);
        startActivity(i);
    }

    public void onClickGerirServicos(View v){
        Intent i = new Intent(this,GerirServicosActivity.class);
        startActivity(i);
    }


}
