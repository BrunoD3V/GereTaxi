package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import se.simbio.encryption.Encryption;


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
            Toast.makeText(getApplicationContext(), "tem que preencher todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = edtEmail.getText().toString();
        String pass = edtPassword.getText().toString();
        String pass2 = edtConfirmPassword.getText().toString();
        if (pass.compareTo(pass2) != 0) {
            Toast.makeText(getApplicationContext(), "As passwords têm que coincidir", Toast.LENGTH_SHORT).show();
            return;
        }
        PasswordValidator passwordValidator = new PasswordValidator();
       /* if (!passwordValidator.validate(pass)){
            Toast.makeText(getApplicationContext(), "A password tem que conter pelo menos 1 dígito, " +
                    "1 mínuscula, 1 maiuscula, 1 caracter especial, não pode ter espaços em branco " +
                    "e um mínimo de 8 caracteres", Toast.LENGTH_LONG).show();
            return;
        }*/


        Encryption encryption = Encryption.getDefault(Constants.KEY, Constants.SALT, new byte[16]);

        String encrypted = encryption.encryptOrNull(pass);
        SharedPreference sharedPreference = new SharedPreference();
        GereBD bd = new GereBD();
        if (Helper.isNetworkAvailable(getApplicationContext())) {
            switch (bd.checkLogin(email, encrypted)) {
                case -2:
                    Toast.makeText(getApplicationContext(), "Erro no registo tente mais tarde", Toast.LENGTH_LONG).show();

                    break;
                case -1:
                    int res = bd.registarMotorista(email, encrypted);
                    sharedPreference.save(getApplicationContext(), email, Constants.EMAIL);
                    sharedPreference.save(getApplicationContext(), encrypted, Constants.PASS);
                    sharedPreference.save(getApplicationContext(), res, Constants.ID_MOTORISTA);
                    System.out.println(encrypted);

                    break;
                case 0:
                    Toast.makeText(getApplicationContext(), "Este utilizador já existe", Toast.LENGTH_LONG).show();

                    break;

                case 1:
                    Toast.makeText(getApplicationContext(), "Este utilizador já existe", Toast.LENGTH_LONG).show();

                    break;
            }

        }
        else {
            //Toast.makeText(getApplicationContext(), "Necessita de net para fazer o registo", Toast.LENGTH_LONG).show();
            Helper helper = new Helper();
            helper.displayPromptEnableWifi(this);

        }





    }


}
