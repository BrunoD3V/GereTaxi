package p4.geretaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class TesteSoap extends AppCompatActivity {

    Servico;
    ArrayList<Servico> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_soap);


        Listar();
    }

    public void Listar(){

        new Thread(new Runnable() {
            public void run() {
                GereServico manager = new GereServico();
                 lista = manager.listarAcidentesDeTrabalho();
                Log.d("Listagem:", lista.toString());
            }
        }).start();
    }
}
