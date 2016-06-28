package p4.geretaxi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
