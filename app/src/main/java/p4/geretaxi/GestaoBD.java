package p4.geretaxi;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by belchior on 26/06/2016.
 */
public class GestaoBD {

    private Servico servico = new Servico();
    private Cliente cliente = new Cliente();

    private boolean result;

    //region Login
    public boolean insereProcesso(final String processo1){

        new Thread(new Runnable() {
            @Override
            public void run() {

                 String NAMESPACE = "http://GereTaxiPackage/";
                 String URL = "http://"+Constants.IP+":8080/GereTaxi/WSGereTaxi";
                 String  METHOD_NAME = "inserirPorProcesso";
                 String  SOAP_ACTION = "http://GereTaxiPackage/inserirPorProcesso";

                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                PropertyInfo processo =new PropertyInfo();
                processo.type=PropertyInfo.STRING_CLASS;
                processo.setName("processo");
                processo.setValue(processo1);



                request.addProperty(processo);


                //Parametrizacao do SOAP
                SoapSerializationEnvelope envelope = new  SoapSerializationEnvelope(SoapEnvelope.VER11);

                //O envelope SOAP que se associa ao pedido
                envelope.setOutputSoapObject(request);
                //Classe: Indicar como o Servidor de WS pode ser alcancado

                try {

                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    //SOAP Request(Pedido)
                    androidHttpTransport.call(SOAP_ACTION, envelope);

                    //IR buscar a Resposta que me foi enviado pelo Servidor de WS
                    SoapPrimitive resultsRequestSOAP = (SoapPrimitive) envelope.getResponse();



                    String status = String.valueOf(resultsRequestSOAP);
                    result = Boolean.valueOf(status);



                }catch (IOException e){

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
//endregion Login
}
