package p4.geretaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class StorePreferencesActivity extends AppCompatActivity {

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

        Shared
    }
}
