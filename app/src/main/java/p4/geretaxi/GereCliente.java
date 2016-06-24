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

/**
 * Created by KittenRoullete on 24-Jun-16.
 */
public class GereCliente {

    SoapHandler soapHandler;
    
    public boolean inserirCliente(Cliente cliente){
        soapHandler = new SoapHandler("inserirCliente");
        SoapObject inserirCliente = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapObject soapCliente = new SoapObject(soapHandler.getNAMESPACE(),"cliente");

        soapCliente.addProperty("codigoPostal", cliente.getCodigoPostal());
        soapCliente.addProperty("contacto", cliente.getContacto());
        soapCliente.addProperty("email", cliente.getEmail());
        soapCliente.addProperty("id", cliente.getId());
        soapCliente.addProperty("morada", cliente.getMorada());
        soapCliente.addProperty("nif", cliente.getMorada());
        soapCliente.addProperty("nome", cliente.getNome());
        soapCliente.addProperty("tipo", cliente.getTipo());

        inserirCliente.addSoapObject(soapCliente);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirCliente);

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

    public boolean excluirCliente(Cliente cliente){
        soapHandler = new SoapHandler("excluirCliente");
        SoapObject excluirCliente = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapObject soapCliente = new SoapObject(soapHandler.getNAMESPACE(),"cliente");

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

    public boolean excluirCliente(String nomeCliente){
        return excluirCliente(new Cliente(nomeCliente));
    }

    public ArrayList<Cliente> listarClientes(){
        ArrayList<Cliente> lista = new ArrayList<>();

        soapHandler = new SoapHandler("listarClientes");
        SoapObject listarClientes = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarClientes);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        System.out.println("MethodName: " + soapHandler.getMethodName());
        try {
            http.call(soapHandler.getSoapAction(), envelope);

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

        soapHandler = new SoapHandler("pesquisarCliente");
        SoapObject pesquisarCliente = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(pesquisarCliente);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());

        System.out.println("MethodName: " + soapHandler.getMethodName());
        try {
            http.call(soapHandler.getSoapAction(), envelope);

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
