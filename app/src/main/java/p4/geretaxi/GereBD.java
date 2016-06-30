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
import java.util.List;
import java.util.Vector;

public class GereBD {

    private static String NAMESPACE = "http://GereTaxiPackage/";
    private static String URL = "http://"+Constants.IP+":8080/GereTaxi/WSGereTaxi?xsd=1";
    private static String METHOD_NAME;

    private Boolean result;
    private int loginID;
    private int res;
    ArrayList<Servico> lista = new ArrayList<>();
    ArrayList<Cliente> listaClientes = new ArrayList<>();
    Servico servicoGlobal = new Servico();
    Cliente clienteGlobal = new Cliente();

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
                    http.call(NAMESPACE+METHOD_NAME, envelope);

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
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
        res = -2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("checkLogin");

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo mail = new PropertyInfo();
                mail.type = PropertyInfo.STRING_CLASS;
                mail.setName("email");
                mail.setValue(email);

                PropertyInfo pass = new PropertyInfo();
                pass.type = PropertyInfo.STRING_CLASS;
                pass.setName("password");
                pass.setValue(password);



                request.addProperty(mail);

                request.addProperty(pass);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                http.debug = true;

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);
                    System.out.println(http.requestDump);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
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

    public int getMotoristaId(final String email) {
        res = -2;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("getMotoristaId");

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo mail = new PropertyInfo();
                mail.type = PropertyInfo.STRING_CLASS;
                mail.setName("email");
                mail.setValue(email);

                request.addProperty(mail);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                http.debug = true;

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);
                    System.out.println(http.requestDump);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
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

    public boolean inserirServico(final Servico servico){
        result = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("inserirServico");
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo processo = new PropertyInfo();
                processo.type = PropertyInfo.STRING_CLASS;
                processo.setName("processo");
                processo.setValue(servico.getProcesso());

                PropertyInfo custoPortagens = new PropertyInfo();
                custoPortagens.type = PropertyInfo.STRING_CLASS;
                custoPortagens.setName("custoPortagens");
                custoPortagens.setValue(servico.getCustoPortagens().toString());

                PropertyInfo data = new PropertyInfo();
                data.type = PropertyInfo.STRING_CLASS;
                data.setName("data");
                data.setValue(servico.getData());

                PropertyInfo destino = new PropertyInfo();
                destino.type = PropertyInfo.STRING_CLASS;
                destino.setName("destino");
                destino.setValue(servico.getDestino());

                PropertyInfo distancia = new PropertyInfo();
                distancia.type = PropertyInfo.STRING_CLASS;
                distancia.setName("distancia");
                distancia.setValue(servico.getDistancia().toString());

                PropertyInfo horaDeInicio = new PropertyInfo();
                horaDeInicio.type = PropertyInfo.STRING_CLASS;
                horaDeInicio.setName("horaDeInicio");
                horaDeInicio.setValue(servico.getHoraDeInicio());

                PropertyInfo horasDeEspera = new PropertyInfo();
                horasDeEspera.type = PropertyInfo.STRING_CLASS;
                horasDeEspera.setName("horasDeEspera");
                horasDeEspera.setValue(servico.getHorasDeEspera().toString());

                PropertyInfo nomeCliente = new PropertyInfo();
                nomeCliente.type = PropertyInfo.STRING_CLASS;
                nomeCliente.setName("nomeCliente");
                nomeCliente.setValue(servico.getNomeCliente());

                PropertyInfo numPassageiros = new PropertyInfo();
                numPassageiros.type = PropertyInfo.INTEGER_CLASS;
                numPassageiros.setName("numPassageiros");
                numPassageiros.setValue(servico.getNumPassageiros());

                PropertyInfo origem = new PropertyInfo();
                origem.type = PropertyInfo.STRING_CLASS;
                origem.setName("origem");
                origem.setValue(servico.getOrigem());

                PropertyInfo tipo = new PropertyInfo();
                tipo.type = PropertyInfo.STRING_CLASS;
                tipo.setName("tipo");
                tipo.setValue(servico.getTipo());

                PropertyInfo trajeto = new PropertyInfo();
                trajeto.type = PropertyInfo.STRING_CLASS;
                trajeto.setName("trajeto");
                trajeto.setValue(servico.getTrajeto());

                PropertyInfo idMotorista = new PropertyInfo();
                idMotorista.type = PropertyInfo.INTEGER_CLASS;
                idMotorista.setName("idMotorista");
                idMotorista.setValue(servico.getIdMotorista());

                request.addProperty(processo);
                request.addProperty(custoPortagens);
                request.addProperty(data);
                request.addProperty(destino);
                request.addProperty(distancia);
                request.addProperty(horaDeInicio);
                request.addProperty(horasDeEspera);
                request.addProperty(nomeCliente);
                request.addProperty(numPassageiros);
                request.addProperty(origem);
                request.addProperty(tipo);
                request.addProperty(trajeto);
                request.addProperty(idMotorista);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
                        result = Boolean.parseBoolean(response.toString());
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

    return result;

    }


    public boolean excluirServico(final String processo, final int idMotorista){

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("excluirServico");

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo processoP = new PropertyInfo();
                processoP.type = PropertyInfo.STRING_CLASS;
                processoP.setName("processo");
                processoP.setValue(processo);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                request.addProperty(processoP);
                request.addProperty(motorista);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.implicitTypes = true;

                envelope.setOutputSoapObject(request);

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME,envelope);
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

                    if(response!=null)
                        result = Boolean.parseBoolean(response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<Servico> listarServico(final int idMotorista){

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("listarServicos");
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                request.addProperty(motorista);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);


                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);
                    SoapObject response = (SoapObject) envelope.bodyIn;

                    if(response!=null) {
                        int i;
                        int responseCount = response.getPropertyCount();
                        System.out.println("RESPONSE COUNT" + responseCount);
                        for (i = 0; i < responseCount; i++) {
                            Object property = response.getProperty(i);
                            if (property instanceof SoapObject) {
                                SoapObject soapObject = (SoapObject) property;

                                Servico servico = new Servico();

                                servico.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                                servico.setProcesso(soapObject.getProperty("processo").toString());
                                servico.setNomeCliente(soapObject.getProperty("nomeCliente").toString());
                                servico.setData(soapObject.getProperty("data").toString());
                                servico.setHoraDeInicio(soapObject.getProperty("horaDeInicio").toString());
                                servico.setOrigem(soapObject.getProperty("origem").toString());
                                servico.setDestino(soapObject.getProperty("destino").toString());
                                servico.setProcesso(soapObject.getProperty("processo").toString());
                                servico.setDistancia(Double.parseDouble(soapObject.getProperty("distancia").toString()));
                                servico.setNumPassageiros(Integer.parseInt(soapObject.getProperty("numPassageiros").toString()));
                                servico.setIdMotorista(Integer.parseInt(soapObject.getProperty("idMotorista").toString()));
                                servico.setCustoPortagens(Double.parseDouble(soapObject.getProperty("custoPortagens").toString()));
                                servico.setHorasDeEspera(Double.parseDouble(soapObject.getProperty("horasDeEspera").toString()));
                                servico.setTipo(soapObject.getProperty("tipo").toString());

                                lista.add(servico);
                            }
                        }
                    }
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
        System.out.println(lista.toString());
        return lista;
    }

    public ArrayList<Servico> pesquisarServicosPorCliente(final String nomeCliente, final int idMotorista){

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("pesquisarServicosPorCliente");
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo nome = new PropertyInfo();
                nome.type = PropertyInfo.STRING_CLASS;
                nome.setName("nomeCliente");
                nome.setValue(nomeCliente);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                request.addProperty(motorista);

                request.addProperty(nome);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);

                    Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                    if(response!=null) {
                        for (SoapObject soapObject : response) {

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
                            servico.setIdMotorista(Integer.parseInt(soapObject.getProperty("idMotorista").toString()));

                            lista.add(servico);
                        }
                    }
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

        return lista;
    }

    public Servico pesquisarServico(final String processo, final int idMotorista){

        new Thread(new Runnable() {
            @Override
            public void run() {

                setMethodName("pesquisarServico");

                SoapObject pesquisarServico = new SoapObject(NAMESPACE,METHOD_NAME);


                PropertyInfo processop = new PropertyInfo();
                processop.type = PropertyInfo.STRING_CLASS;
                processop.setName("processo");
                processop.setValue(processo);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                pesquisarServico.addProperty(motorista);
                pesquisarServico.addProperty(processop);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(pesquisarServico);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                System.out.println("MethodName: " + METHOD_NAME);
                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);

                    SoapObject response = (SoapObject) envelope.getResponse();
                    if(response!=null) {
                        servicoGlobal.setId(Integer.parseInt(response.getProperty("id").toString()));
                        servicoGlobal.setProcesso(response.getProperty("processo").toString());
                        servicoGlobal.setNomeCliente(response.getProperty("nomeCliente").toString());
                        servicoGlobal.setData(response.getProperty("data").toString());
                        servicoGlobal.setHoraDeInicio(response.getProperty("horaDeInicio").toString());
                        servicoGlobal.setOrigem(response.getProperty("origem").toString());
                        servicoGlobal.setDestino(response.getProperty("destino").toString());
                        servicoGlobal.setHorasDeEspera(Double.parseDouble(response.getProperty("horasDeEspera").toString()));
                        servicoGlobal.setProcesso(response.getProperty("processo").toString());
                        servicoGlobal.setDistancia(Double.parseDouble(response.getProperty("distancia").toString()));
                        servicoGlobal.setNumPassageiros(Integer.parseInt(response.getProperty("numPassageiros").toString()));
                        servicoGlobal.setCustoPortagens(Double.parseDouble(response.getProperty("custoPortagens").toString()));
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return servicoGlobal;

    }

    public boolean inserirCliente(final Cliente cliente){

        result = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("inserirCliente");
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo nome = new PropertyInfo();
                nome.type = PropertyInfo.STRING_CLASS;
                nome.setName("nome");
                nome.setValue(cliente.getNome());

                PropertyInfo morada = new PropertyInfo();
                morada.type = PropertyInfo.STRING_CLASS;
                morada.setName("morada");
                morada.setValue(cliente.getMorada());

                PropertyInfo codigoPostal = new PropertyInfo();
                codigoPostal.type = PropertyInfo.INTEGER_CLASS;
                codigoPostal.setName("codigoPostal");
                codigoPostal.setValue(cliente.getCodigoPostal());

                PropertyInfo nif = new PropertyInfo();
                nif.type = PropertyInfo.INTEGER_CLASS;
                nif.setName("nif");
                nif.setValue(cliente.getNif());

                PropertyInfo contacto = new PropertyInfo();
                contacto.type = PropertyInfo.INTEGER_CLASS;
                contacto.setName("contacto");
                contacto.setValue(cliente.getContacto());

                PropertyInfo email = new PropertyInfo();
                email.type = PropertyInfo.STRING_CLASS;
                email.setName("email");
                email.setValue(cliente.getEmail());

                PropertyInfo tipo = new PropertyInfo();
                tipo.type = PropertyInfo.STRING_CLASS;
                tipo.setName("tipo");
                tipo.setValue(cliente.getTipo());

                PropertyInfo idMotorista = new PropertyInfo();
                idMotorista.type = PropertyInfo.INTEGER_CLASS;
                idMotorista.setName("idMotorista");
                idMotorista.setValue(cliente.getIdMotorista());

                request.addProperty(nif);
                request.addProperty(nome);
                request.addProperty(morada);
                request.addProperty(codigoPostal);
                request.addProperty(contacto);
                request.addProperty(email);
                request.addProperty(tipo);
                request.addProperty(idMotorista);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
                        result = Boolean.parseBoolean(response.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    result = false;
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    result = false;
                }
            }
        }).start();

        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    return result;

    }

    public boolean excluirCliente(final String nomeCliente, final int idMotorista){
        result = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("excluirCliente");
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo nome = new PropertyInfo();
                nome.type = PropertyInfo.STRING_CLASS;
                nome.setName("nome");
                nome.setValue(nomeCliente);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                request.addProperty(motorista);
                request.addProperty(nome);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME,envelope);

                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    if(response!=null)
                        result = Boolean.parseBoolean(response.toString());

                } catch (IOException e) {
                    e.printStackTrace();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return result;
    }

        public ArrayList<Cliente> listarClientes(final int idMotorista){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        setMethodName("listarClientes");

                        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                        PropertyInfo motorista = new PropertyInfo();
                        motorista.type = PropertyInfo.INTEGER_CLASS;
                        motorista.setName("idMotorista");
                        motorista.setValue(idMotorista);

                        request.addProperty(motorista);

                        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                        envelope.setOutputSoapObject(request);

                        envelope.implicitTypes = true;

                        HttpTransportSE http = new HttpTransportSE(URL);

                        System.out.println("MethodName: " + METHOD_NAME);
                        try {
                            http.call(NAMESPACE + METHOD_NAME, envelope);
                                SoapObject response = (SoapObject) envelope.bodyIn;
                                if (response != null) {
                                    int i;
                                    int responseCount = response.getPropertyCount();
                                    System.out.println("RESPONSE COUNT" + responseCount);
                                    for (i = 0; i < responseCount; i++) {
                                        Object property = response.getProperty(i);
                                        if (property instanceof SoapObject) {
                                            SoapObject soapObject = (SoapObject) property;
                                            Cliente cliente = new Cliente();

                                            cliente.setId(Integer.parseInt(soapObject.getProperty("id").toString()));
                                            cliente.setNome(soapObject.getProperty("nome").toString());
                                            cliente.setMorada(soapObject.getProperty("morada").toString());
                                            cliente.setCodigoPostal(soapObject.getProperty("codigoPostal").toString());
                                            cliente.setNif(Integer.parseInt(soapObject.getProperty("nif").toString()));
                                            cliente.setContacto(Integer.parseInt(soapObject.getProperty("contacto").toString()));
                                            cliente.setEmail(soapObject.getProperty("email").toString());
                                            cliente.setTipo(soapObject.getProperty("tipo").toString());

                                            listaClientes.add(cliente);
                                        }
                                    }
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (XmlPullParserException e1) {
                                e1.printStackTrace();
                            }

                        }
                    }).start();
                    try{
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return listaClientes;

            }

    public Cliente pesquisarCliente(final String nomeC, final int idMotorista){

        new Thread(new Runnable() {
            @Override
            public void run() {
                setMethodName("pesquisarCliente");

                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);

                PropertyInfo nome = new PropertyInfo();
                nome.type = PropertyInfo.STRING_CLASS;
                nome.setName("nome");
                nome.setValue(nomeC);

                PropertyInfo motorista = new PropertyInfo();
                motorista.type = PropertyInfo.INTEGER_CLASS;
                motorista.setName("idMotorista");
                motorista.setValue(idMotorista);

                request.addProperty(motorista);
                request.addProperty(nome);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                envelope.implicitTypes = true;

                HttpTransportSE http = new HttpTransportSE(URL);

                try {
                    http.call(NAMESPACE+METHOD_NAME, envelope);

                    SoapObject response = (SoapObject) envelope.getResponse();

                    if(response != null) {

                        clienteGlobal.setId(Integer.parseInt(response.getProperty("id").toString()));
                        clienteGlobal.setNome(response.getProperty("nome").toString());
                        clienteGlobal.setMorada(response.getProperty("morada").toString());
                        clienteGlobal.setCodigoPostal(response.getProperty("codigoPostal").toString());
                        clienteGlobal.setNif(Integer.parseInt(response.getProperty("nif").toString()));
                        clienteGlobal.setContacto(Integer.parseInt(response.getProperty("contacto").toString()));
                        clienteGlobal.setEmail(response.getProperty("email").toString());
                        clienteGlobal.setTipo(response.getProperty("tipo").toString());
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return clienteGlobal;
    }
}
