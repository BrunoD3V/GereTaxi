package p4.geretaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GerirServicosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_servicos);
    }

    public void onClickConsultarServico(View v){
        Intent i = new Intent(this, ConsultarServicosActivity.class);
        startActivity(i);
    }

    public void onClickAlterarServic(View v){
        Intent i = new Intent(this, AlterarServicoActivity.class);
        startActivity(i);
    }
    public void onClickEliminarServico(View v){
        Intent i = new Intent(this, EliminarServicoActivity.class);
        startActivity(i);
    }
}
