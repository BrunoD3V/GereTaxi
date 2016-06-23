package p4.geretaxi;


import java.io.Serializable;

public class AcidentesDeTrabalho extends Servico implements Serializable{

    private String NumProcesso;
    private float HorasDeEspera;

    public AcidentesDeTrabalho(String numProcesso) {
        NumProcesso = numProcesso;
    }

    public AcidentesDeTrabalho(){

    }

    public float getHorasDeEspera() {
        return HorasDeEspera;
    }

    public void setHorasDeEspera(float horasDeEspera) {
        HorasDeEspera = horasDeEspera;
    }

    public String getNumProcesso() {
        return NumProcesso;
    }

    public void setNumProcesso(String numProcesso) {
        NumProcesso = numProcesso;
    }

    @Override
    public String toString() {
        return "Processo :" + NumProcesso + "\n" + "Data :"
                + getData() + "\n" + "Hora :" + getHoraDeInicio() + "\n" + "Local de carga: "
                + getOrigem() + "\n" + "Local de destino: " + getDestino() + "\n" + "Distância percorrida: "
                + getDistancia() + "\n" + "Número de passageiros: " + getNumPassageiros() + "\n";
    }
}
