package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.R;
import p4.geretaxi.TarefasAssincronas.TarefaSincronizar;

public class MainActivity extends AppCompatActivity {

    ArrayList<Servico> lista;
    Boolean resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Helper helper = new Helper();
        SharedPreference sharedPreference= new SharedPreference();

        switch (sharedPreference.checkAppStart()) {
            case NORMAL:
                boolean res = Helper.attemptLogin();

                if (res) {
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                    if(Helper.isNetworkAvailable(this)) {
                        TarefaSincronizar sincrona = new TarefaSincronizar();
                        sincrona.sincronizar.execute();
                    }
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



    public void onClickbuttonLoginMain(View v) {


       Intent intent = new Intent(this, LoginActivity.class);
       startActivity(intent);
    }


}
