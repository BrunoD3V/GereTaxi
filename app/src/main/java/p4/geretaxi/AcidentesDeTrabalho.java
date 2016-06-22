package p4.geretaxi;

public class AcidentesDeTrabalho extends Servico{

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
