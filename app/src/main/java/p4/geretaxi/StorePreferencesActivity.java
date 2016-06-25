package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import se.simbio.encryption.Encryption;

public class StorePreferencesActivity extends AppCompatActivity {

    public static final String FRAGTAG = "BasicAndroidKeyStoreFragment";


    EditText edtEmail;
    EditText edtPassword;
    EditText edtConfirmPassword;
    EditText edtLatitude;
    EditText edtLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_preferences);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
        edtLatitude = (EditText) findViewById(R.id.edtLatitude);
        edtLongitude = (EditText) findViewById(R.id.edtLongitude);
    }

    public void onClickDetetar(View v) {


    }

    public void onClickSubmeter(View v) {

        Encryption encryption = Encryption.getDefault("Key", "Salt", new byte[16]);
        String encrypted = encryption.encryptOrNull(edtPassword.getText().toString());
        String decrypted = encryption.decryptOrNull(encrypted);



    }
}
