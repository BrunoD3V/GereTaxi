package p4.geretaxi;

import java.io.Serializable;

public class ServicoParticular extends Servico implements Serializable{

    private float HorasDeEspera;


    public float getHorasDeEspera() {
        return HorasDeEspera;
    }


    public void setHorasDeEspera(float horasDeEspera) {
        HorasDeEspera = horasDeEspera;
    }
}
