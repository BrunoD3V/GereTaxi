package p4.geretaxi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ParticularActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particular);
        Helper helper = new Helper();
        AssistenciaEmViagem assistenciaEmViagem = new AssistenciaEmViagem();
        assistenciaEmViagem.setNumProcesso("2");
        assistenciaEmViagem.setData(helper.getDate());
        assistenciaEmViagem.setHoraDeInicio(helper.getTime());
        assistenciaEmViagem.setCompanhia("Europ");
        assistenciaEmViagem.setNumPassageiros(3);
        assistenciaEmViagem.setOrigem("Mirandela");
        assistenciaEmViagem.setDestino("Porto");
        XMLHandler parser = new XMLHandler();
        Boolean result = parser.writeAssitenciaEmViagem(assistenciaEmViagem);
        System.out.println(result.toString());
    }
}
