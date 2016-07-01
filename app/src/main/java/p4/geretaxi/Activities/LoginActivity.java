package p4.geretaxi.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.PasswordEncrypt;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.KSoapClass.GereBD;
import p4.geretaxi.R;
import p4.geretaxi.TarefasAssincronas.TarefaSincronizar;

public class LoginActivity extends AppCompatActivity {

    EditText editTextLoginMail;
    EditText editTextPassLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPassLogin = (EditText) findViewById(R.id.editTextPassLogin);
        editTextLoginMail = (EditText) findViewById(R.id.editTextLoginMail);
    }


    public void onClickbtnLogin(View v) {

       if(Helper.isEmpty(editTextLoginMail) || Helper.isEmpty(editTextPassLogin))
        {
            Toast.makeText(getApplicationContext(), "Tem que preencher todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Helper.isNetworkAvailable(this)){
            PasswordEncrypt passwordEncrypt = new PasswordEncrypt();
            String encrypted = passwordEncrypt.getEncrypted(editTextPassLogin.getText().toString());
            String email = editTextLoginMail.getText().toString();
            if(!Helper.isValidEmail(email)){
                Toast.makeText(getApplicationContext(), "Insira um email válido.", Toast.LENGTH_LONG).show();
                return;
            }
            SharedPreference sharedPreference = new SharedPreference();
            GereBD bd = new GereBD();
            switch (bd.checkLogin(email, encrypted.trim())) {
                case -2:
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro tente mais tarde", Toast.LENGTH_LONG).show();
                    break;
                case -1:
                    Toast.makeText(getApplicationContext(), "Utilizador inexistente", Toast.LENGTH_LONG).show();
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Password Incorrecta", Toast.LENGTH_LONG).show();
                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Login efectuado com sucesso", Toast.LENGTH_LONG).show();
                    int res = bd.getMotoristaId(email);
                    if (res != -1 && res != -2){
                        sharedPreference.save(getApplicationContext(), email, Constants.EMAIL);
                        sharedPreference.save(getApplicationContext(), encrypted.trim(), Constants.PASS);
                        sharedPreference.save(getApplicationContext(), res, Constants.ID_MOTORISTA);
                        sharedPreference.save(getApplicationContext(), Constants.TRUE, Constants.SESSION);
                        sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
                        TarefaSincronizar tarefa = new TarefaSincronizar();
                        tarefa.sincronizar.execute();
                        Intent intent = new Intent(this, MenuActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Erro, tente mais tarde", Toast.LENGTH_LONG).show();
                        return;
                    }
                    break;
            }
        }
    }

    public void onClickRegistarActivity(View v){
        Intent i = new Intent(getApplicationContext(),StorePreferencesActivity.class);
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
