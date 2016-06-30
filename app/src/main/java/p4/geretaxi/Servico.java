package p4.geretaxi;

import java.io.Serializable;

public class Servico implements Serializable{


    private int id;
    private String nomeCliente;
    private String processo;
    private Double distancia;
    private Integer numPassageiros;
    private Double custoPortagens;
    private String origem;
    private String destino;
    private String data;
    private String horaDeInicio;
    private Double horasDeEspera;
    private String tipo;
    private String trajeto;
    private int idMotorista;


    public Servico(Double custoPortagens, String data, String destino, Double distancia, String horaDeInicio, Double horasDeEspera, int id, String nomeCliente, Integer numPassageiros, String origem, String processo, String tipo) {


        this.custoPortagens = custoPortagens;
        this.data = data;
        this.destino = destino;
        this.distancia = distancia;
        this.horaDeInicio = horaDeInicio;
        this.horasDeEspera = horasDeEspera;
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.numPassageiros = numPassageiros;
        this.origem = origem;
        this.processo = processo;
        this.tipo = tipo;
        this.trajeto = null;

    }
    public Servico(String processo) {

        this.processo = processo;
        this.horasDeEspera = 0.0;
        this.custoPortagens = 0.0;

    }

    public Servico(){

        this.horasDeEspera = 0.0;
        this.custoPortagens = 0.0;

    }

    public int getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(int idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getCustoPortagens() {
        return custoPortagens;
    }

    public void setCustoPortagens(Double custoPortagens) {
        this.custoPortagens = custoPortagens;
    }

    public Double getHorasDeEspera() {
        return horasDeEspera;
    }

    public void setHorasDeEspera(Double horasDeEspera) {
        this.horasDeEspera = horasDeEspera;
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


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getTrajeto() {
        return trajeto;
    }

    public void setTrajeto(String trajeto) {
        this.trajeto = trajeto;
    }

    @Override
    public String toString() {
        return Constants.PROCESSO_TOSTRING + processo + "\n" + Constants.CLIENTE_TOSTRING + nomeCliente + "\n"
                + Constants.DATA_TOSTRING + data + "\n" + Constants.HORA_DE_INICIO_TOSTRING + horaDeInicio + "\n" +
                Constants.ORIGEM_TOSTRING + origem + "\n" + Constants.DESTINO_TOSTRING + destino + "\n" +
                Constants.NUM_PASSAGEIROS_TOSTRING + numPassageiros + "\n" + Constants.HORAS_DE_ESPERA_TOSTRING + horasDeEspera + "\n" +
                Constants.CUSTO_PORTAGENS_TOSTRING + custoPortagens + " €\n" + Constants.DISTANCIA_TOSTRING + distancia +
                Constants.KMS_TOSTRING + "\n" +Constants.TIPO_SERVICO_TOSTRING + tipo;
    }
}
