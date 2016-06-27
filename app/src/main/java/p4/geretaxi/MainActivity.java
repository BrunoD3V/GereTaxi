package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;



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
                Toast.makeText(getApplicationContext(), " NORMAL", Toast.LENGTH_SHORT).show();

                GereBD bd = new GereBD();
                SharedPreference sharedPreference = new SharedPreference();

                String email = sharedPreference.getValueString(this, Constants.EMAIL);
                String pass = sharedPreference.getValueString(this, Constants.PASS);

                int res = bd.checkLogin(email, pass.trim());
                System.out.println("RES = " + res);
                if(res == 1) {
                    Intent intent = new Intent(this, MenuActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
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
}
