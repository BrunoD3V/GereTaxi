package p4.geretaxi;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class GereAssistenciaEmViagem {

    SoapHandler soapHandler;

    public boolean inserirAssistenciaEmViagem(AssistenciaEmViagem acidente){

        soapHandler = new SoapHandler("inserirAssistenciaEmViagem");
        SoapObject inserirAssistenciaEmViagem = new SoapObject(soapHandler.getNAMESPACE(),soapHandler.getMethodName());

        SoapObject soapAcidente = new SoapObject(soapHandler.getNAMESPACE(),"assEmViagem");

        //soapAcidente.addProperty("custoPortagens", acidente.getCustoPortagens());
        //soapAcidente.addProperty("data", acidente.getData());
        //soapAcidente.addProperty("distancia", acidente.getDistancia());
        //soapAcidente.addProperty("horaDeInicio", acidente.getHoraDeInicio());
        //soapAcidente.addProperty("custoPortagens", acidente.getCustoPortagens());
        soapAcidente.addProperty("id", 0);
        soapAcidente.addProperty("idCliente", 1);
        //soapAcidente.addProperty("numPassageiros", acidente.getNumPassageiros());
        //soapAcidente.addProperty("origem", acidente.getOrigem());
       // soapAcidente.addProperty("trajeto", acidente.getTrajeto());
        soapAcidente.addProperty("numProcesso", acidente.getNumProcesso());

        inserirAssistenciaEmViagem.addSoapObject(soapAcidente);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(inserirAssistenciaEmViagem);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL().toString());

        System.out.println(soapHandler.getURL().toString());
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

}
