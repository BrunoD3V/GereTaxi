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

    public Boolean inserirAssistenciaEmViagem(AssistenciaEmViagem assistenciaEmViagem) throws IOException, XmlPullParserException
    {
        final String METHOD_NAME = "inserirAssistenciaEmViagem";

        soapHandler = new SoapHandler(METHOD_NAME);

        AssistenciaEmViagem assEmViagem = assistenciaEmViagem;

        SoapObject request = new SoapObject(soapHandler.getNAMESPACE(), METHOD_NAME);

        PropertyInfo NumProcesso = new PropertyInfo();
        NumProcesso.type = PropertyInfo.STRING_CLASS;
        NumProcesso.setName("NumProcesso");
        NumProcesso.setValue(assEmViagem.getNumProcesso());

        PropertyInfo IdCompanhia = new PropertyInfo();
        IdCompanhia.type = PropertyInfo.INTEGER_CLASS;
        IdCompanhia.setName("IdCompanhia");
        IdCompanhia.setValue(assEmViagem.getIdCompanhia());

        PropertyInfo Origem = new PropertyInfo();
        Origem.type = PropertyInfo.STRING_CLASS;
        Origem.setName("Origem");
        Origem.setValue(assEmViagem.getOrigem());

        PropertyInfo Destino = new PropertyInfo();
        Destino.type = PropertyInfo.STRING_CLASS;
        Destino.setName("Destino");
        Destino.setValue(assEmViagem.getDestino());

        PropertyInfo Distancia = new PropertyInfo();
        Distancia.setType(Double.class);
        Distancia.setName("Distancia");
        Distancia.setValue(assEmViagem.getDistancia());

        PropertyInfo HoraDeInicio = new PropertyInfo();
        HoraDeInicio.type = PropertyInfo.STRING_CLASS;
        HoraDeInicio.setName("HoraDeInicio");
        HoraDeInicio.setValue(assEmViagem.getHoraDeInicio());

        PropertyInfo Data = new PropertyInfo();
        Data.type = PropertyInfo.STRING_CLASS;
        Data.setName("Data");
        Data.setValue(assEmViagem.getData());

        PropertyInfo NumPassageiros = new PropertyInfo();
        NumPassageiros.type = PropertyInfo.INTEGER_CLASS;
        NumPassageiros.setName("NumPassageiros");
        NumPassageiros.setValue(assEmViagem.getNumPassageiros());

        PropertyInfo CustoPortagens = new PropertyInfo();
        CustoPortagens.setType(Float.class);
        CustoPortagens.setName("CustoPortagens");
        CustoPortagens.setValue(assEmViagem.getCustoPortagens());

        request.addProperty(NumProcesso);
        request.addProperty(IdCompanhia);
        request.addProperty(Origem);
        request.addProperty(Destino);
        request.addProperty(Distancia);
        request.addProperty(HoraDeInicio);
        request.addProperty(Data);
        request.addProperty(NumPassageiros);
        request.addProperty(CustoPortagens);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);

        envelope.implicitTypes = true;

        HttpTransportSE http = new HttpTransportSE(soapHandler.getURL());
        try {

            http.call("uri:" + METHOD_NAME, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(response.toString());

        } catch (IOException e) {

            e.printStackTrace();
            return false;

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return true;
    }




}
