package p4.geretaxi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.WebServiceClass.GereBD;
import p4.geretaxi.ClassesHelper.Helper;
import p4.geretaxi.ClassesHelper.PasswordEncrypt;
import p4.geretaxi.ClassesHelper.PasswordValidator;
import p4.geretaxi.R;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.TarefasAssincronas.TarefaMail;


public class StorePreferencesActivity extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    EditText edtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_preferences);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
    }

    public void onClickbtnSubmeterInfo(View v) {
        if (Helper.isEmpty(edtEmail) || Helper.isEmpty(edtPassword) || Helper.isEmpty(edtConfirmPassword)) {
            Toast.makeText(getApplicationContext(), "Por favor, deverá preencher todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = edtEmail.getText().toString();
        if(!Helper.isValidEmail(email)){
            Toast.makeText(getApplicationContext(), "Insira um email válido.", Toast.LENGTH_LONG).show();
            return;
        }
        String pass = edtPassword.getText().toString();
        String pass2 = edtConfirmPassword.getText().toString();
        if (pass.compareTo(pass2) != 0) {
            Toast.makeText(getApplicationContext(), "As passwords devem coincidir.", Toast.LENGTH_SHORT).show();
            return;
        }
        PasswordValidator passwordValidator = new PasswordValidator();

        if (!passwordValidator.validate(pass)){

            Toast.makeText(getApplicationContext(), "A password tem que conter pelo menos 1 dígito, " +
                    "1 mínuscula, 1 maiuscula, 1 caracter especial, não pode ter espaços em branco " +
                    "e um mínimo de 8 caracteres", Toast.LENGTH_LONG).show();
            return;
        }
        PasswordEncrypt passwordEncrypt = new PasswordEncrypt();
        String encrypted = passwordEncrypt.getEncrypted(pass);

        SharedPreference sharedPreference = new SharedPreference();
        GereBD bd = new GereBD();

        if (Helper.isNetworkAvailable(getApplicationContext())) {
            switch (bd.checkLogin(email, encrypted.trim())) {
                case -2:
                    Toast.makeText(getApplicationContext(), "Erro no registo tente mais tarde", Toast.LENGTH_LONG).show();
                    return;
                case -1:
                    int res = bd.registarMotorista(email, encrypted);
                    sharedPreference.save(getApplicationContext(), email, Constants.EMAIL);
                    sharedPreference.save(getApplicationContext(), encrypted.trim(), Constants.PASS);
                    sharedPreference.save(getApplicationContext(), res, Constants.ID_MOTORISTA);
                    sharedPreference.save(getApplicationContext(), Helper.getExpirationDate(), Constants.VALIDADE);
                    sharedPreference.save(getApplicationContext(), Constants.TRUE, Constants.SESSION);
                    TarefaMail tarefaMail = new TarefaMail();
                    tarefaMail.mailInfo.execute(email, pass);
                    Intent i = new Intent(getApplicationContext(), CoordenadasActivity.class);
                    startActivity(i);
                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Este utilizador já existe", Toast.LENGTH_LONG).show();
                    return;
                case 1:
                    Toast.makeText(getApplicationContext(), "Este utilizador já existe", Toast.LENGTH_LONG).show();
                    return;
                default:
                    break;
            }
        }
        else {

            Helper helper = new Helper();
            helper.displayPromptEnableWifi(this);
        }
    }

    public void onClickLoginActivity(View v){
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
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
