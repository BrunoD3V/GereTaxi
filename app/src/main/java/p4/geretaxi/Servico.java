package p4.geretaxi;
import com.google.maps.model.LatLng;

import java.io.Serializable;
import java.util.List;

public class Servico implements Serializable{

    private int id;
    private int idCliente;
    private String nomeCliente;
    private String Processo;
    private Double distancia;
    private Integer NumPassageiros;
    private float CustoPortagens;
    private String Origem;
    private String Destino;
    private String Data;
    private String HoraDeInicio;
    private List<LatLng> Trajeto;

    public float getCustoPortagens() {
        return CustoPortagens;
    }

    public String getData() {
        return Data;
    }

    public String getDestino() {
        return Destino;
    }

    public Double getDistancia() {
        return distancia;
    }

    public String getHoraDeInicio() {
        return HoraDeInicio;
    }

    public int getId() {
        return id;
    }

    public int getidCliente() {
        return idCliente;
    }

    public Integer getNumPassageiros() {
        return NumPassageiros;
    }

    public String getOrigem() {
        return Origem;
    }

    public List<LatLng> getTrajeto() {
        return Trajeto;
    }

    public void setCustoPortagens(float custoPortagens) {
        CustoPortagens = custoPortagens;
    }

    public void setData(String data) {
        Data = data;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public void setDistancia(Double distancia) {
        distancia = distancia;
    }

    public void setHoraDeInicio(String horaDeInicio) {
        HoraDeInicio = horaDeInicio;
    }

    public void setId(int id) {
        id = id;
    }

    public void setidCliente(int idCliente) {
        idCliente = idCliente;
    }

    public void setNumPassageiros(int numPassageiros) {
        NumPassageiros = numPassageiros;
    }

    public void setOrigem(String origem) {
        Origem = origem;
    }

    public void setTrajeto(List<LatLng> trajeto) {
        Trajeto = trajeto;
    }
}
