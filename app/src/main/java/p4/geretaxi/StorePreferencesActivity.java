package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import se.simbio.encryption.Encryption;

public class StorePreferencesActivity extends AppCompatActivity {

    public static final String FRAGTAG = "BasicAndroidKeyStoreFragment";

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

        if (Helper.isNetworkAvailable(getApplicationContext())){
            if(true) {//TODO fazer o registo no webservice


                sharedPreference.save(getApplicationContext(),email, Constants.EMAIL);
                sharedPreference.save(getApplicationContext(), encrypted, Constants.PASS);
                System.out.println(encrypted);
            }else {

            }
        } else  {
            sharedPreference.save(getApplicationContext(),email, Constants.EMAIL);
            sharedPreference.save(getApplicationContext(), encrypted, Constants.PASS);
            sharedPreference.save(getApplicationContext(), Constants.REGISTO_SEM_NET, Constants.REGISTO_SEM_NET);

        }

        if(sharedPreference.findValue(getApplicationContext(), Constants.EMAIL)) {
            System.out.println("GRAVA CARAGO");
        }
        Intent intent = new Intent(this, MainActivity.class );
        startActivity(intent);



    }
}
