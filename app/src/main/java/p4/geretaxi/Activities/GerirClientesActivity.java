package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.R;

public class GerirClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_clientes);
    }

    public void onClickInserirCliente(View v){
        Intent i = new Intent(getApplicationContext(), InserirNovoClienteActivity.class);
        startActivity(i);
    }
    public void onClickListarClientes(View v){

        Intent i = new Intent(this, ConsultarClientesActivity.class);
        startActivity(i);
    }
    public void onClickEliminarCliente(View v){
        Intent intent = new Intent(this, EliminarClienteActivity.class);
        startActivity(intent);
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
                sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
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
