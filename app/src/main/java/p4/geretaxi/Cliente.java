package p4.geretaxi;

import java.io.Serializable;

public class Cliente implements Serializable{

    SharedPreference sharedPreference;

    private int id;
    private String nome;
    private String morada;
    private String codigoPostal;
    private String email;
    private int contacto;
    private int nif;
    private String tipo;
    private int idMotorista;

    public Cliente(String nome) {
        this.nome = nome;
        this.idMotorista = sharedPreference.getValueInt(MyApplication.getAppContext(),Constants.ID_MOTORISTA);
    }

    public Cliente() {
        this.idMotorista = sharedPreference.getValueInt(MyApplication.getAppContext(),Constants.ID_MOTORISTA);
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
}
