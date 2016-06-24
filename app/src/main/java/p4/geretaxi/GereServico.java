package p4.geretaxi;

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
            http.call(soapHandler.getSoapAction(), envelope);

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

    public boolean excluirServico(Servico servico){
        soapHandler = new SoapHandler("excluirServico");
        SoapObject excluirServico = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

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

        excluirServico.addSoapObject(soapServico);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirServico);

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

    public boolean excluirServico(String processo){
        return excluirServico(new Servico(processo));
    }

    public ArrayList<Servico> listarServico(){
        ArrayList<Servico> lista = new ArrayList<>();

        soapHandler = new SoapHandler("listarServico");
        SoapObject listarServico = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarServico);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        System.out.println("MethodName: " + soapHandler.getMethodName());
        try {
            http.call(soapHandler.getSoapAction(), envelope);

            Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();

            for (SoapObject soapObject: response) {
                Servico Servico = new Servico();

                Servico.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                Servico.setIdCliente(Integer.parseInt(soapObject.getProperty("idCliente").toString()));
                Servico.setData(soapObject.getProperty("data").toString());
                Servico.setHoraDeInicio(soapObject.getProperty("horaDeInicio").toString());
                Servico.setOrigem(soapObject.getProperty("origem").toString());
                Servico.setDestino(soapObject.getProperty("destino").toString());
                Servico.setHorasDeEspera(Double.parseDouble(soapObject.getProperty("horasDeEspera").toString()));
                Servico.setProcesso(soapObject.getProperty("processo").toString());
                Servico.setDistancia(Double.parseDouble(soapObject.getProperty("distancia").toString()));
                Servico.setNumPassageiros(Integer.parseInt(soapObject.getProperty("numPassageiros").toString()));
                Servico.setCustoPortagens(Double.parseDouble(soapObject.getProperty("custoPortagens").toString()));


                System.out.println(Servico.getProcesso());
                lista.add(Servico);
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

    public Servico pesquisarServico(String processo){
        Servico servico = null;

        return servico;
    }
}