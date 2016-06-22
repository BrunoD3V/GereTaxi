package p4.geretaxi;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SoapHandler {

    private static String NAMESPACE;
    private static String URL;
    private static String METHOD_NAME;
    private static String SOAP_ACTION;
    private static String IP = "192.168.1.5";

    public SoapHandler(String methodName){
        NAMESPACE = "http://p4.app.com/";
        URL = "http://"+IP+"/WSAlunos/WSAlunos";
        METHOD_NAME = methodName;
        SOAP_ACTION = "http://p4.app.com/"+METHOD_NAME;
    }
    /*
    public void onClickEnviarInsert(View v)
    {
        new Thread(new Runnable() {
            public void run() {
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

                PropertyInfo NumMec = new PropertyInfo();
                NumMec.type = PropertyInfo.INTEGER_CLASS;
                NumMec.setName("numMec");
                NumMec.setValue(edtNumMec.getText().toString());

                PropertyInfo Nome = new PropertyInfo();
                Nome.type = PropertyInfo.STRING_CLASS;
                Nome.setName("Nome");
                Nome.setValue(edtNome.getText().toString());

                PropertyInfo Curso = new PropertyInfo();
                Curso.type = PropertyInfo.STRING_CLASS;
                Curso.setName("Curso");
                Curso.setValue(edtCurso.getText().toString());

                PropertyInfo Ano = new PropertyInfo();
                Ano.type = PropertyInfo.INTEGER_CLASS;
                Ano.setName("Ano");
                Ano.setValue(edtAno.getText().toString());

                request.addProperty(NumMec);
                request.addProperty(Nome);
                request.addProperty(Curso);
                request.addProperty(Ano);

                SoapSerializationEnvelope envelope = new  SoapSerializationEnvelope(SoapEnvelope.VER11);

                envelope.setOutputSoapObject(request);

                try {
                    HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                    //SOAP Request(Pedido)
                    androidHttpTransport.call(SOAP_ACTION, envelope);
                    //IR buscar a Resposta que me foi enviado pelo Servidor de WS
                    status =  envelope.getResponse().toString();
                    Log.d("Inseriu? ",status.toString());
                }catch (IOException e){
                    Log.d("erro", "ERRO IOEXCEPTION");
                } catch (XmlPullParserException e) {
                    Log.d("erro", "ERRO PARSEREXCEPTION");
                }
            }
        }).start();
        Toast.makeText(this.getApplicationContext(),"RES: "+status,Toast.LENGTH_SHORT).show();
    }

    */

}
