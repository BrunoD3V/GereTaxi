package p4.geretaxi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalFloat;
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

        SoapObject insert = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());


        SoapObject sServico = new SoapObject(soapHandler.getNAMESPACE(),"servico");

        sServico.addProperty("custoPortagens", String.valueOf(servico.getCustoPortagens()));
        sServico.addProperty("data", servico.getData());
        sServico.addProperty("destino", servico.getDestino());
        sServico.addProperty("distancia", String.valueOf(servico.getDistancia()));
        sServico.addProperty("horaDeInicio", servico.getHoraDeInicio());
        sServico.addProperty("horasDeEspera", String.valueOf(servico.getHorasDeEspera()));
        sServico.addProperty("id", servico.getId());
        sServico.addProperty("nomeCliente", servico.getNomeCliente());
        sServico.addProperty("numPassageiros", servico.getNumPassageiros());
        sServico.addProperty("origem", servico.getOrigem());
        sServico.addProperty("processo", servico.getProcesso());
        sServico.addProperty("tipo", "tipo");
        sServico.addProperty("trajeto", "olaa");

        insert.addSoapObject(sServico);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(insert);

        System.out.println(sServico.toString());



        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        try {
            http.call(soapHandler.getSoapAction(), envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

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
        soapServico.addProperty("destino", servico.getDestino());
        soapServico.addProperty("distancia", servico.getDistancia());
        soapServico.addProperty("horaDeInicio", servico.getHoraDeInicio());
        soapServico.addProperty("horasDeEsoera", servico.getHorasDeEspera());
        soapServico.addProperty("id", servico.getId());
        soapServico.addProperty("nomeCliente", servico.getNomeCliente());
        soapServico.addProperty("numPassageiros", servico.getNumPassageiros());
        soapServico.addProperty("origem", servico.getOrigem());
        soapServico.addProperty("processo", servico.getProcesso());
        soapServico.addProperty("tipo", servico.getTipo());
        soapServico.addProperty("trajeto", servico.getTrajeto());

        excluirServico.addSoapObject(soapServico);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirServico);

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

                Servico servico = new Servico();

                servico.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                servico.setProcesso(soapObject.getProperty("processo").toString());
                servico.setNomeCliente(soapObject.getProperty("nomeCliente").toString());
                servico.setData(soapObject.getProperty("data").toString());
                servico.setHoraDeInicio(soapObject.getProperty("horaDeInicio").toString());
                servico.setOrigem(soapObject.getProperty("origem").toString());
                servico.setDestino(soapObject.getProperty("destino").toString());
                servico.setHorasDeEspera(Double.parseDouble(soapObject.getProperty("horasDeEspera").toString()));
                servico.setProcesso(soapObject.getProperty("processo").toString());
                servico.setDistancia(Double.parseDouble(soapObject.getProperty("distancia").toString()));
                servico.setNumPassageiros(Integer.parseInt(soapObject.getProperty("numPassageiros").toString()));
                servico.setCustoPortagens(Double.parseDouble(soapObject.getProperty("custoPortagens").toString()));


                System.out.println(servico.getProcesso());
                lista.add(servico);
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

        Servico servico = new Servico();
        soapHandler = new SoapHandler("pesquisarServico");
        SoapObject pesquisarServico = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());
        pesquisarServico.addProperty("processo", processo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(pesquisarServico);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        System.out.println("MethodName: " + soapHandler.getMethodName());
        try {
            http.call(soapHandler.getSoapAction(), envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            servico.setId(Integer.parseInt(response.getProperty("id").toString()));
            servico.setProcesso(response.getProperty("processo").toString());
            servico.setNomeCliente(response.getProperty("nomeCliente").toString());
            servico.setData(response.getProperty("data").toString());
            servico.setHoraDeInicio(response.getProperty("horaDeInicio").toString());
            servico.setOrigem(response.getProperty("origem").toString());
            servico.setDestino(response.getProperty("destino").toString());
            servico.setHorasDeEspera(Double.parseDouble(response.getProperty("horasDeEspera").toString()));
            servico.setProcesso(response.getProperty("processo").toString());
            servico.setDistancia(Double.parseDouble(response.getProperty("distancia").toString()));
            servico.setNumPassageiros(Integer.parseInt(response.getProperty("numPassageiros").toString()));
            servico.setCustoPortagens(Double.parseDouble(response.getProperty("custoPortagens").toString()));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

        return servico;
    }
}