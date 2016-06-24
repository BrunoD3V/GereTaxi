package p4.geretaxi;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GereServico {

    SoapHandler soapHandler;

    public boolean inserirServico(Servico servico){

        soapHandler = new SoapHandler("inserirServico");
        SoapObject inserirServico = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapObject soapServico = new SoapObject(soapHandler.getNAMESPACE(),"servico");

        soapServico.addProperty("custoPortagens", servico.getCustoPortagens());
        soapServico.addProperty("data", servico.getData());
        soapServico.addProperty("distancia", servico.getDistancia());
        soapServico.addProperty("horaDeInicio", servico.getHoraDeInicio());
        soapServico.addProperty("custoPortagens", servico.getCustoPortagens());
        soapServico.addProperty("id", servico.getId());
        soapServico.addProperty("idCliente", servico.getIdCliente());
        soapServico.addProperty("numPassageiros", servico.getNumPassageiros());
        soapServico.addProperty("origem", servico.getOrigem());
        soapServico.addProperty("trajeto", servico.getTrajeto());
        soapServico.addProperty("horasDeEspera", servico.getHorasDeEspera());
        soapServico.addProperty("processo", servico.getProcesso());

        inserirServico.addSoapObject(soapServico);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirServico);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        try {
            http.call("uri:" + soapHandler.getMethodName(), envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirAcidentesDeTrabalho(AcidentesDeTrabalho acidente){
        soapHandler = new SoapHandler("excluirAcidenteDeTrabalho");
        SoapObject excluirAcidenteDeTrabalho = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapObject soapAcidente = new SoapObject(soapHandler.getNAMESPACE(),"acidente");

        soapAcidente.addProperty("custoPortagens", acidente.getCustoPortagens());
        soapAcidente.addProperty("data", acidente.getData());
        soapAcidente.addProperty("distancia", acidente.getDistancia());
        soapAcidente.addProperty("horaDeInicio", acidente.getHoraDeInicio());
        soapAcidente.addProperty("custoPortagens", acidente.getCustoPortagens());
        soapAcidente.addProperty("id", acidente.getId());
        soapAcidente.addProperty("idCliente", acidente.getIdCliente());
        soapAcidente.addProperty("numPassageiros", acidente.getNumPassageiros());
        soapAcidente.addProperty("origem", acidente.getOrigem());
        soapAcidente.addProperty("trajeto", acidente.getTrajeto());
        soapAcidente.addProperty("horasDeEspera", acidente.getHorasDeEspera());
        soapAcidente.addProperty("numProcesso", acidente.getNumProcesso());

        excluirAcidenteDeTrabalho.addSoapObject(soapAcidente);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirAcidenteDeTrabalho);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        try {
            http.call("uri:" + soapHandler.getMethodName(), envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluirAcidentesDeTrabalho(String numProcesso){
        return excluirAcidentesDeTrabalho(new AcidentesDeTrabalho(numProcesso));
    }

    public ArrayList<AcidentesDeTrabalho> listarAcidentesDeTrabalho(){
        ArrayList<AcidentesDeTrabalho> lista = new ArrayList<>();

        soapHandler = new SoapHandler("listarAcidentesDeTrabalho");
        SoapObject listarAcidentesDeTrabalho = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarAcidentesDeTrabalho);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        System.out.println("MethodName: " + soapHandler.getMethodName());
        try {
            http.call(soapHandler.getSoapAction(), envelope);

            Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();

            for (SoapObject soapObject: response) {
                AcidentesDeTrabalho acidenteDeTrabalho = new AcidentesDeTrabalho();

                acidenteDeTrabalho.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                acidenteDeTrabalho.setIdCliente(Integer.parseInt(soapObject.getProperty("idCliente").toString()));
                acidenteDeTrabalho.setData(soapObject.getProperty("data").toString());
                acidenteDeTrabalho.setHoraDeInicio(soapObject.getProperty("horaDeInicio").toString());
                acidenteDeTrabalho.setOrigem(soapObject.getProperty("origem").toString());
                acidenteDeTrabalho.setDestino(soapObject.getProperty("destino").toString());
                acidenteDeTrabalho.setHorasDeEspera(Float.parseFloat(soapObject.getProperty("horasDeEspera").toString()));
                acidenteDeTrabalho.setNumProcesso(soapObject.getProperty("numProcesso").toString());
                acidenteDeTrabalho.setDistancia(Double.parseDouble(soapObject.getProperty("distancia").toString()));
                acidenteDeTrabalho.setNumPassageiros(Integer.parseInt(soapObject.getProperty("numPassageiros").toString()));
                acidenteDeTrabalho.setCustoPortagens(Float.parseFloat(soapObject.getProperty("custoPortagens").toString()));


                System.out.println(acidenteDeTrabalho.getNumProcesso());
                lista.add(acidenteDeTrabalho);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

        return lista;
    }

    public AcidentesDeTrabalho pesquisarAcidentesDeTrabalho(String numProcesso){
        AcidentesDeTrabalho acidente = null;

        return acidente;
    }
}