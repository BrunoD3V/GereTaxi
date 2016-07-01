package p4.geretaxi.TarefasAssincronas;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Xml;

import com.google.maps.GeoApiContext;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.File;
import java.util.List;

import p4.geretaxi.ClassesDados.Cliente;
import p4.geretaxi.ClassesDados.Servico;
import p4.geretaxi.ClassesHelper.MyApplication;
import p4.geretaxi.ClassesHelper.ServicoHandler;
import p4.geretaxi.ClassesHelper.SharedPreference;
import p4.geretaxi.ClassesHelper.XMLHandler;
import p4.geretaxi.Constantes.Constants;
import p4.geretaxi.KSoapClass.GereBD;
/**
 * Created by belchior on 01/07/2016.
 */
public class TarefaSincronizar {

    GeoApiContext mContext;
    File file;
    List<LatLng> mCapturedLocations;
    Servico servico;
    List<Servico> servicos;
    XMLHandler parser = new XMLHandler();


    public AsyncTask<Void, Void, Void> sincronizar = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... voids) {
            sincronizaClientes();
            sincronzaServicos();

            return null;
        }
    };

    private void sincronzaServicos() {

        file = new File(Environment.getExternalStorageDirectory(), "servicos.xml");
        GereBD bd = new GereBD();

        if (file.exists()) {

            servicos = parser.parseServico(Xml.newPullParser());
            System.out.println(servicos.get(0).toString());
            System.out.println("TAMANHO: " + servicos.size());
            if(servicos.size() >= 0) {
                for (Servico s: servicos) {

                    s = defineServico(s);
                    if(bd.inserirServico(s))
                        servicos.remove(s);
                }
                if(servicos.size()<1)
                    file.delete();
            }
        }
    }

    private Servico defineServico(Servico servico) {
        ServicoHandler handler = new ServicoHandler();
        mContext = new GeoApiContext().setApiKey("AIzaSyBwzAAoIwsMN9ia5B9fzpwv80LJlYo0PcM");
        mCapturedLocations = parser.loadTrajecto(Xml.newPullParser(), servico.getTrajeto());
        try {
            GeocodingResult origem =handler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(0));
            GeocodingResult destino =handler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(mCapturedLocations.size() - 1));
            servico.setDestino(destino.formattedAddress);
            servico.setOrigem(origem.formattedAddress);
            mCapturedLocations = handler.mergeCapture(mCapturedLocations);
            mCapturedLocations = handler.getRoute(mCapturedLocations, mContext);
            parser.trajectoToString(mCapturedLocations);
            double distance = handler.getDistance();
            servico.setIdMotorista(SharedPreference.getIdMotoristaSharedPreferences(MyApplication.getAppContext()));
            servico.setDistancia(distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servico;
    }

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
            if (clientes.size() >= 0) {
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

        if (clientes.size() >= 0) {
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
