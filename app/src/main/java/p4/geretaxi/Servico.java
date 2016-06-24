package p4.geretaxi;
import com.google.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Servico implements Serializable{

    private String tipo;
    private int id;
    private int idCliente;
    private String nomeCliente;
    private String processo;
    private Double distancia;
    private Integer numPassageiros;
    private float custoPortagens;
    private String origem;
    private String destino;
    private String data;
    private String horaDeInicio;
    private float horasDeEspera;
    private List<LatLng> trajeto;


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Servico(String processo) {
        this.processo = processo;
    }

    public Servico(){

    }
    public float getCustoPortagens() {
        return custoPortagens;
    }

    public void setCustoPortagens(float custoPortagens) {
        this.custoPortagens = custoPortagens;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public String getHoraDeInicio() {
        return horaDeInicio;
    }

    public void setHoraDeInicio(String horaDeInicio) {
        this.horaDeInicio = horaDeInicio;
    }

    public float getHorasDeEspera() {
        return horasDeEspera;
    }

    public void setHorasDeEspera(float horasDeEspera) {
        this.horasDeEspera = horasDeEspera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public Integer getNumPassageiros() {
        return numPassageiros;
    }

    public void setNumPassageiros(Integer numPassageiros) {
        this.numPassageiros = numPassageiros;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getProcesso() {
        return processo;
    }

    public void setProcesso(String processo) {
        this.processo = processo;
    }

    public List<LatLng> getTrajeto() {
        return trajeto;
    }

    public void setTrajeto(List<LatLng> trajeto) {
        this.trajeto = trajeto;
    }
}
