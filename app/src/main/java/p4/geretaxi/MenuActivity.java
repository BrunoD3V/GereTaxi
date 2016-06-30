package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button btnGerirClientes;

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

            case R.id.settings_id:                 Intent in = new Intent(this, CoordenadasActivity.class);                 startActivity(in);

                break;

            case R.id.inicio_id:
                Intent intent = new Intent(this, MenuActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickIniciarServico(View v) {
        EscolherServicoDialogFragment dialogFragment = EscolherServicoDialogFragment.newInstance();
        dialogFragment.show(this.getFragmentManager(), "EscolheServico");
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
