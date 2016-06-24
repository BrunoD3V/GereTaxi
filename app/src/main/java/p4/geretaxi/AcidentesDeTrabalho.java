package p4.geretaxi;


import java.io.Serializable;

public class AcidentesDeTrabalho extends Servico implements Serializable{

    private String NumProcesso;
    private float HorasDeEspera;
    private String Companhia;


    public AcidentesDeTrabalho(String numProcesso) {
        NumProcesso = numProcesso;
    }

    public AcidentesDeTrabalho() {
    }

    public String getCompanhia() {
        return Companhia;
    }

    public void setCompanhia(String companhia) {
        Companhia = companhia;
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
}
