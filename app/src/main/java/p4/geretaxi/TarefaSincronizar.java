package p4.geretaxi;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Xml;

import java.io.File;
import java.util.List;

/**
 * Created by belchior on 01/07/2016.
 */
public class TarefaSincronizar {

    AsyncTask<Void, Void, Void> sincronizar = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            sincronizaClientes();

            return null;
        }
    };


    //TODO: FAZER O MESMO PARA OS SERVIÇOS
    private void sincronizaClientes() {
        XMLHandler parser = new XMLHandler();
        GereBD bd = new GereBD();
        boolean res = false;

        File file;
        List<Cliente> clientes;
        SharedPreference shared = new SharedPreference();

        file = new File(Environment.getExternalStorageDirectory(), "novocliente.xml");
        if (file.exists()) {
            clientes = parser.parseNovosClientes(Xml.newPullParser());
            //envia os clientes gravados localmente para a BD
            if (clientes.size() > 0) {
                for (Cliente c : clientes) {
                    c.setIdMotorista(shared.getValueInt(MyApplication.getAppContext(), Constants.ID_MOTORISTA));
                    res = bd.inserirCliente(c);
                    if (res) {
                        clientes.remove(c);
                    }
                }
            }
            if(clientes.size() < 1)
                file.delete();
        }
        //vai buscar os clientes a bd
        file = new File(Environment.getExternalStorageDirectory(), "clientes.xml");
        file.delete();

        clientes = bd.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(MyApplication.getAppContext()));

        if (clientes.size() > 0) {
            for (Cliente c : clientes) {
                parser.writeClientes(c);
            }
        }
        /*
        // grava localmente clientes que estejam na BD mas não estejam no aparelho

        if (clientes.size() < 1)
            clientes.clear();
        clientes = bd.listarClientes(SharedPreference.getIdMotoristaSharedPreferences(getApplicationContext()));
        if(clientes.size() < 1) {
            List<Cliente> clientesLocais;
            clientesLocais = parser.parseClientes(Xml.newPullParser());
            if (clientesLocais.size() < 1) {
                for (Cliente c : clientes) {
                    for (Cliente l: clientesLocais) {
                        if(!c.getEmail().equalsIgnoreCase(l.getEmail())){

                            parser.writeClientes(c);
                        }

                    }
                }
            }
        }*/
    }
}
