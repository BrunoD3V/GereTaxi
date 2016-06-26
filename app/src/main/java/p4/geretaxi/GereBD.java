package p4.geretaxi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class GereBD {

    private static String NAMESPACE = "http://GereTaxiPackage/";
    private static String URL = "http://"+Constants.IP+":8080/GereTaxi/WSGereTaxi?xsd=1";
    private static String METHOD_NAME;
    private static String SOAP_ACTION = NAMESPACE+METHOD_NAME;
    private Boolean result;
    private int loginID;
    private int res;
    Servico servicoGlobal;
    Cliente clienteGlobal;

    public static void setMethodName(String methodName) {
        METHOD_NAME = methodName;
    }

    public int registarMotorista(final String email, final String password){
        loginID = -2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("registarMotorista");

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo Em = new PropertyInfo();
                Em.type = PropertyInfo.STRING_CLASS;
                Em.setName("email");
                Em.setValue(email);

                PropertyInfo Pass= new PropertyInfo();
                Pass.type = PropertyInfo.STRING_CLASS;
                Pass.setName("password");
                Pass.setValue(password);

                request.addProperty(Em);
                request.addProperty(Pass);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.implicitTypes = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(SOAP_ACTION, envelope);

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    loginID = Integer.valueOf(response.toString());

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }


            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return loginID;
    }

    public int checkLogin(final String email, final String password) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("checkLogin");

                SoapObject request = new SoapObject("http://GereTaxiPackage/","checkLogin");

                PropertyInfo em = new PropertyInfo();
                em.type = PropertyInfo.STRING_CLASS;
                em.setName("email");
                em.setValue(email);

                PropertyInfo pass = new PropertyInfo();
                pass.type = PropertyInfo.STRING_CLASS;
                pass.setName("password");
                em.setValue(password);

                request.addProperty(em);
                request.addProperty(pass);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.implicitTypes = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call("http://GereTaxiPackage/checkLogin", envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    res = Integer.parseInt(response.toString());
                    System.out.println("RES DENTRO DA THREAD: " + res);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }
/*
    public boolean inserirServico(final Servico servico){

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("inserirServico");
                SoapObject insert = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo id = new PropertyInfo();
                id.type = ;

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
                sServico.addProperty("tipo", servico.getTipo());
                sServico.addProperty("trajeto", servico.getTrajeto());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                insert.addSoapObject(sServico);

                envelope.setOutputSoapObject(insert);

                System.out.println(sServico.toString());

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(SOAP_ACTION, envelope);

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    result = Boolean.parseBoolean(response.toString());

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (HttpResponseException e) {
                    e.printStackTrace();
                } catch (SoapFault soapFault) {
                    soapFault.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = false;
            }
        }).start();

    return result;

    }
*/

    public boolean excluirServico(String processo){
        this.setMethodName("excluirServico");
        return true;
    }

    public ArrayList<Servico> listarServico(){
        ArrayList<Servico> lista = new ArrayList<>();

        this.setMethodName("listarServicos");
        SoapObject listarServico = new SoapObject(NAMESPACE,METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(listarServico);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        System.out.println("MethodName: " + METHOD_NAME);
        try {
            http.call(SOAP_ACTION, envelope);

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
        this.setMethodName("pesquisarServico");
        SoapObject pesquisarServico = new SoapObject(NAMESPACE,METHOD_NAME);
        pesquisarServico.addProperty("processo", processo);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(pesquisarServico);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(URL);

        System.out.println("MethodName: " + METHOD_NAME);
        try {
            http.call(SOAP_ACTION, envelope);

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

    public boolean inserirCliente(Cliente cliente){

        this.setMethodName("inserirCliente");
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



    public boolean excluirCliente(String nomeCliente){
        this.setMethodName("excluirCliente");



        return true;
    }

    public ArrayList<Cliente> listarClientes(){

        this.setMethodName("listarClientes");
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

        this.setMethodName("pesquisarCliente");
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