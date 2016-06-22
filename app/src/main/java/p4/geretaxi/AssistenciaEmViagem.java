package p4.geretaxi;

import java.io.Serializable;

public class AssistenciaEmViagem extends ServicoContratado implements Serializable {

    private String NumProcesso;
    private int IdCompanhia;

    public int getIdCompanhia() {
        return IdCompanhia;
    }

    public String getNumProcesso() {
        return NumProcesso;
    }

    public void setIdCompanhia(int idCompanhia) {
        IdCompanhia = idCompanhia;
    }

    public void setNumProcesso(String numProcesso) {
        NumProcesso = numProcesso;
    }

    @Override
    public void setData(String data ) {

        super.setData(data);
    }

    @Override
    public void setHoraDeInicio(String horaDeInicio) {
        super.setHoraDeInicio(horaDeInicio);
    }

    @Override
    public void setDistancia(Double distancia) {
        super.setDistancia(distancia);
    }
}