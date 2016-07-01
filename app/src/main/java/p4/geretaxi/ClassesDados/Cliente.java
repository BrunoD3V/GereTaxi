package p4.geretaxi.ClassesDados;

import java.io.Serializable;

import p4.geretaxi.Constantes.Constants;

public class Cliente implements Serializable{

    private int id;
    private String nome;
    private String morada;
    private String codigoPostal;
    private String email;
    private int contacto;
    private int nif;
    private int idMotorista;
    private String tipo;

    public Cliente(String nome) {
        this.nome = nome;
    }

    public Cliente() {
        this.nome="";
        this.morada="";
        this.codigoPostal="";
        this.email="";
        this.contacto=0;
        this.nif = 0;
        this.idMotorista = 0;
        this.tipo = "";
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

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public int getContacto() {
        return contacto;
    }

    public void setContacto(int contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    public int getNif() {
        return nif;
    }

    public void setNif(int nif) {
        this.nif = nif;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return Constants.CLIENTE_TOSTRING + nome + "\n" + Constants.MORADA_TOSTRING + morada + "\n" + Constants.CODIGO_POSTAL_TOSTRING +
               codigoPostal + "\n" + Constants.NIF_TOSTRING +  nif + "\n"  + Constants.CONTACTO_TOSTRING + contacto + "\n" +
                Constants.EMAIL_TOSTRING + email + "\n" + Constants.TIPO_CLIENTE_TOSTRING + tipo;
    }
}
