package p4.geretaxi;

import java.io.Serializable;

public class AssistenciaEmViagem extends Servico implements Serializable {

    private String numProcesso;
    private int IdCompanhia;
    private String Companhia;

    public String getCompanhia() {
        return Companhia;
    }

    public void setCompanhia(String companhia) {
        Companhia = companhia;
    }

    public int getIdCompanhia() {
        return IdCompanhia;
    }

    public String getNumProcesso() {
        return numProcesso;
    }

    public void setIdCompanhia(int idCompanhia) {
        IdCompanhia = idCompanhia;
    }

    public void setNumProcesso(String numProcesso) {
        this.numProcesso = numProcesso;
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

    @Override
    public void setNumPassageiros(int numPassageiros) {
        super.setNumPassageiros(numPassageiros);
    }

    @Override
    public Integer getNumPassageiros() {
        return super.getNumPassageiros();
    }

    @Override
    public String toString() {
        return "Processo :" + numProcesso + "\n" + "Companhia :" + Companhia + "\n" + "Data :"
                + getData() + "\n" + "Hora :" + getHoraDeInicio() + "\n" + "Local de carga: "
                + getOrigem() + "\n" + "Local de destino: " + getDestino() + "\n" + "Distância percorrida: "
                + getDistancia() + "\n" + "Número de passageiros: " + getNumPassageiros() + "\n";
    }
}