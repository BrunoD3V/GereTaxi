package p4.geretaxi;
import com.google.maps.model.LatLng;

import java.util.List;

public class Servico {

    private int Id;
    private int IdCliente;
    private Double Distancia;
    private int NumPassageiros;
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
        return Distancia;
    }

    public String getHoraDeInicio() {
        return HoraDeInicio;
    }

    public int getId() {
        return Id;
    }

    public int getIdCliente() {
        return IdCliente;
    }

    public int getNumPassageiros() {
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
        Distancia = distancia;
    }

    public void setHoraDeInicio(String horaDeInicio) {
        HoraDeInicio = horaDeInicio;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setIdCliente(int idCliente) {
        IdCliente = idCliente;
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
