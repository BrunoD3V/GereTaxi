package p4.geretaxi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class TesteSoap extends AppCompatActivity {

    ArrayList<Servico> lista;
    Boolean resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_soap);


      inserirServico();

    }

    public void ListarServicos(){

        new Thread(new Runnable() {
            public void run() {
                GereServico manager = new GereServico();
                 lista = manager.listarServico();
                Log.d("Listagem:", lista.toString());
            }
        }).start();
    }

    public void inserirServico(){
        new Thread(new Runnable() {
            public void run() {
                GereServico manager = new GereServico();
                resultado = manager.inserirServico(new Servico());
                Log.d("Resposta:", resultado.toString());
            }
        }).start();
    }
}
