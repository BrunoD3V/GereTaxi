package p4.geretaxi;


import java.io.Serializable;

public class AcidentesDeTrabalho extends Servico implements Serializable{

    private String NumProcesso;
    private float HorasDeEspera;

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
}
