package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button btnGerirClientes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnGerirClientes = (Button) findViewById(R.id.btnGerirClientes);
        btnGerirClientes.setEnabled(false);


        if(Helper.isNetworkAvailable(this)) {
            SharedPreference preference = new SharedPreference();
            if (preference.getValueString(getApplicationContext(), Constants.SESSION).equals(Constants.FALSE)) {
                boolean res = Helper.attemptLogin();
                if (res) {
                    preference.save(getApplicationContext(), Constants.TRUE, Constants.SESSION);
                    btnGerirClientes.setEnabled(true);
                } else
                {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void onClickIniciarServico(View v) {
        EscolherServicoDialogFragment dialogFragment = EscolherServicoDialogFragment.newInstance();
        dialogFragment.show(this.getFragmentManager(), "EscolheServico");
    }

    public void onClickGerirClientes(View v){


        Intent i = new Intent(this,GerirClientesActivity.class);
        startActivity(i);
    }
}
