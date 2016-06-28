package p4.geretaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
            SharedPreference sharedPreference = new SharedPreference();
            GereBD bd = new GereBD();
            switch (bd.checkLogin(email, encrypted.trim())) {
                case -2:
                    Toast.makeText(getApplicationContext(), "Erro no registo tente mais tarde", Toast.LENGTH_LONG).show();
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
}
