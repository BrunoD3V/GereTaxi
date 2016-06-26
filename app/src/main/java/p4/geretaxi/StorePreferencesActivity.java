package p4.geretaxi;

import android.content.Intent;
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

        Encryption encryption = Encryption.getDefault(Constants.KEY, Constants.SALT, new byte[16]);

        String encrypted = encryption.encryptOrNull(pass);
        SharedPreference sharedPreference = new SharedPreference();
        GereBD bd = new GereBD();
        if (Helper.isNetworkAvailable(getApplicationContext())){
            switch (bd.checkLogin(email, encrypted)) {
                case -2 :
                        registoOffline(email, encrypted);
                    break;
                case -1:
                    int res = bd.registarMotorista(email, encrypted);
                    if (res == -1){
                        Toast.makeText(getApplicationContext(), "Erro no registo", Toast.LENGTH_LONG).show();
                        return;
                    }

                    sharedPreference.save(getApplicationContext(),email, Constants.EMAIL);
                    sharedPreference.save(getApplicationContext(), encrypted, Constants.PASS);

                   // sharedPreference.save(getApplicationContext(),);
                    System.out.println(encrypted);
                    break;
            }
            if(bd.checkLogin(email, encrypted)== -1) {//TODO fazer o registo no webservice

            }else {
                if (true){

                }

            }
        } else  {


        }


        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);



    }

    private void registoOffline(String email, String password) {

        SharedPreference sharedPreference = new SharedPreference();
        sharedPreference.save(getApplicationContext(),email, Constants.EMAIL);
        sharedPreference.save(getApplicationContext(),password, Constants.PASS);
        sharedPreference.save(getApplicationContext(),Constants.TRUE , Constants.REGISTO_SEM_NET);
    }
}
