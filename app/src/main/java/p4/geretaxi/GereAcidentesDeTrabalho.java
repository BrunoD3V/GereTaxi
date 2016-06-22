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
 * Created by KittenRoullete on 22-Jun-16.
 */
public class GereAcidentesDeTrabalho {

    SoapHandler soapHandler;
    
    public Boolean inserirAcidentesDeTrabalho(AcidentesDeTrabalho acidentesDeTrabalho) throws IOException, XmlPullParserException {

        final String METHOD_NAME = "inserirAcidentesDeTrabalho";
        soapHandler = new SoapHandler(METHOD_NAME);

        AcidentesDeTrabalho accDeTrabalho = acidentesDeTrabalho;

        SoapObject request = new SoapObject(soapHandler.getNAMESPACE(), METHOD_NAME);

        PropertyInfo HorasDeEspera = new PropertyInfo();
        HorasDeEspera.setType(Float.class);
        HorasDeEspera.setName("HorasDeEspera");
        HorasDeEspera.setValue(accDeTrabalho.getHorasDeEspera());
        
        PropertyInfo NumProcesso = new PropertyInfo();
        NumProcesso.type = PropertyInfo.STRING_CLASS;
        NumProcesso.setName("NumProcesso");
        NumProcesso.setValue(accDeTrabalho.getNumProcesso());

        PropertyInfo IdCliente = new PropertyInfo();
        IdCliente.type = PropertyInfo.INTEGER_CLASS;
        IdCliente.setName("IdCliente");
        IdCliente.setValue(accDeTrabalho.getIdCliente());

        PropertyInfo Origem = new PropertyInfo();
        Origem.type = PropertyInfo.STRING_CLASS;
        Origem.setName("Origem");
        Origem.setValue(accDeTrabalho.getOrigem());

        PropertyInfo Destino = new PropertyInfo();
        Destino.type = PropertyInfo.STRING_CLASS;
        Destino.setName("Destino");
        Destino.setValue(accDeTrabalho.getDestino());

        PropertyInfo Distancia = new PropertyInfo();
        Distancia.setType(Double.class);
        Distancia.setName("Distancia");
        Distancia.setValue(accDeTrabalho.getDistancia());

        PropertyInfo HoraDeInicio = new PropertyInfo();
        HoraDeInicio.type = PropertyInfo.STRING_CLASS;
        HoraDeInicio.setName("HoraDeInicio");
        HoraDeInicio.setValue(accDeTrabalho.getHoraDeInicio());

        PropertyInfo Data = new PropertyInfo();
        Data.type = PropertyInfo.STRING_CLASS;
        Data.setName("Data");
        Data.setValue(accDeTrabalho.getData());

        PropertyInfo NumPassageiros = new PropertyInfo();
        NumPassageiros.type = PropertyInfo.INTEGER_CLASS;
        NumPassageiros.setName("NumPassageiros");
        NumPassageiros.setValue(accDeTrabalho.getNumPassageiros());

        PropertyInfo CustoPortagens = new PropertyInfo();
        CustoPortagens.setType(Float.class);
        CustoPortagens.setName("CustoPortagens");
        CustoPortagens.setValue(accDeTrabalho.getCustoPortagens());

        request.addProperty(NumProcesso);
        request.addProperty(IdCliente);
        request.addProperty(Origem);
        request.addProperty(Destino);
        request.addProperty(Distancia);
        request.addProperty(HoraDeInicio);
        request.addProperty(Data);
        request.addProperty(NumPassageiros);
        request.addProperty(CustoPortagens);
        request.addProperty(HorasDeEspera);


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
