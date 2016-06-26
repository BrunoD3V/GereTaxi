package p4.geretaxi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GereBD {

    private static String NAMESPACE = "http://GereTaxiPackage/";
    private static String URL = "http://192.168.1.5:8080/GereTaxi/WSGereTaxi?xsd=1";
    private static String  METHOD_NAME = "";
    private static String SOAP_ACTION = NAMESPACE+METHOD_NAME;

    public boolean inserirServico(Servico servico){

        SoapObject insert = new SoapObject(NAMESPACE,METHOD_NAME);

        SoapObject sServico = new SoapObject(NAMESPACE,"servico");

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



        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);


        insert.addSoapObject(sServico);

        envelope.setOutputSoapObject(insert);

        System.out.println(sServico.toString());


        HttpTransportSE http = new HttpTransportSE(URL);

        http.debug = true;


        try {
            http.call(SOAP_ACTION, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(response.toString());

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        } catch (SoapFault soapFault) {
            soapFault.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;


    }
/*
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

    public boolean meteID(String id) {
        return false;
    }*/

    public boolean inserirCliente(Cliente cliente){

        SoapObject inserirCliente = new SoapObject(NAMESPACE,METHOD_NAME);

        PropertyInfo nome = new PropertyInfo();
        nome.type = PropertyInfo.STRING_CLASS;
        nome.setName("nome");
        nome.setValue(cliente.getNome());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirCliente);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call(SOAP_ACTION, envelope);

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

    public boolean excluirCliente(Cliente cliente){
        SoapObject excluirCliente = new SoapObject(NAMESPACE,METHOD_NAME);

        SoapObject soapCliente = new SoapObject(NAMESPACE,"cliente");

        soapCliente.addProperty("codigoPostal", cliente.getCodigoPostal());
        soapCliente.addProperty("contacto", cliente.getContacto());
        soapCliente.addProperty("email", cliente.getEmail());
        soapCliente.addProperty("id", cliente.getId());
        soapCliente.addProperty("morada", cliente.getMorada());
        soapCliente.addProperty("nif", cliente.getMorada());
        soapCliente.addProperty("nome", cliente.getNome());
        soapCliente.addProperty("tipo", cliente.getTipo());

        excluirCliente.addSoapObject(soapCliente);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(excluirCliente);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        try {
            http.call(SOAP_ACTION, envelope);

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

    public boolean excluirCliente(String nomeCliente){
        return excluirCliente(new Cliente(nomeCliente));
    }

    public ArrayList<Cliente> listarClientes(){
        ArrayList<Cliente> lista = new ArrayList<>();


        SoapObject listarClientes = new SoapObject(NAMESPACE,METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarClientes);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        System.out.println("MethodName: " + METHOD_NAME);
        try {
            http.call(SOAP_ACTION, envelope);

            Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();

            for (SoapObject soapObject: response) {
                Cliente cliente =  new Cliente();

                cliente.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                cliente.setNome(soapObject.getProperty("nome").toString());
                cliente.setMorada(soapObject.getProperty("morada").toString());
                cliente.setCodigoPostal(soapObject.getProperty("codigoPostal").toString());
                cliente.setNif(Integer.parseInt(soapObject.getProperty("nif").toString()));
                cliente.setContacto(Integer.parseInt(soapObject.getProperty("contacto").toString()));
                cliente.setEmail(soapObject.getProperty("email").toString());
                cliente.setTipo(soapObject.getProperty("tipo").toString());

                lista.add(cliente);
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

    public Cliente pesquisarCliente(int id){
        Cliente cliente= null;

        SoapObject pesquisarCliente = new SoapObject(NAMESPACE,METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(pesquisarCliente);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        System.out.println("MethodName: " + METHOD_NAME);
        try {
            http.call(SOAP_ACTION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();

            cliente.setId(Integer.parseInt(response.getProperty("id").toString()));
            cliente.setNome(response.getProperty("nome").toString());
            cliente.setMorada(response.getProperty("morada").toString());
            cliente.setCodigoPostal(response.getProperty("codigoPostal").toString());
            cliente.setNif(Integer.parseInt(response.getProperty("nif").toString()));
            cliente.setContacto(Integer.parseInt(response.getProperty("contacto").toString()));
            cliente.setEmail(response.getProperty("email").toString());
            cliente.setTipo(response.getProperty("tipo").toString());

        }

        catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

        return cliente;
    }
}