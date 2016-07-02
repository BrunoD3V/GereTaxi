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
import p4.geretaxi.WebServiceClass.GereBD;
/**
 * Created by belchior on 01/07/2016.
 */
public class TarefaSincronizar {

    GeoApiContext mContext;
    File file;
    List<LatLng> mCapturedLocations;
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
            servicos = parser.parseServico(Xml.newPullParser());//cria uma lista de serviços caso o ficheiro exista

            if(servicos.size() >= 0) { // se a lista não está vazia
                for (Servico s: servicos) {

                    s = defineServico(s);// chama as APIs para completar o trajecto e obter a informação complementar
                    if(bd.inserirServico(s))
                        servicos.remove(s);//se inseriu com sucesso elimina o serviço da lista
                }
                if(servicos.size()<1)// se a lista está vazia apaga o ficheiro
                    file.delete();
            }
        }
    }

    private Servico defineServico(Servico servico) {
        ServicoHandler handler = new ServicoHandler();
        mContext = new GeoApiContext().setApiKey("AIzaSyBwzAAoIwsMN9ia5B9fzpwv80LJlYo0PcM");
        mCapturedLocations = parser.loadTrajecto(Xml.newPullParser(), servico.getTrajeto());//cria uma lista de pontos com o trajecto capturado
        try {
            //obtém o geocoding da origem e destino
            GeocodingResult origem =handler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(0));
            GeocodingResult destino =handler.reverseGeocodeSnappedPoint(mContext, mCapturedLocations.get(mCapturedLocations.size() - 1));
            servico.setDestino(destino.formattedAddress);
            servico.setOrigem(origem.formattedAddress);
            //obtem as direções da praça de taxis até ao início do serviço e do final até à praça de taxis
            mCapturedLocations = handler.mergeCapture(mCapturedLocations);
            //elimina os pontos com o mesmo id e faz a interpolação
            mCapturedLocations = handler.getRoute(mCapturedLocations, mContext);
            servico.setTrajeto(parser.trajectoToString(mCapturedLocations));
            double distance = handler.getDistance();
            servico.setIdMotorista(SharedPreference.getIdMotoristaSharedPreferences(MyApplication.getAppContext()));
            servico.setDistancia(distance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servico;
    }


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

    }
}
