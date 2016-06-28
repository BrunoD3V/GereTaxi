package p4.geretaxi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class GerirClientesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_clientes);
    }

    public void onClickInserirCliente(View v){
        Intent i = new Intent(getApplicationContext(), InserirNovoClienteActivity.class);
        startActivity(i);
    }
    public void onClickListarClientes(View v){
        Toast.makeText(getApplicationContext(),"TODO: onClickListarClientes - GerirClientesActivity",Toast.LENGTH_LONG).show();
    }
    public void onClickServicoPorCliente(View v){
        Toast.makeText(getApplicationContext(),"TODO: onClickServicoPorCliente - GerirClientesActivity",Toast.LENGTH_LONG).show();
    }
    public void onClickEliminarCliente(View v){
        Toast.makeText(getApplicationContext(),"TODO: onClickEliminarCliente - GerirClientesActivity",Toast.LENGTH_LONG).show();
    }
}
