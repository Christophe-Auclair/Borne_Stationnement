package Stationnement;

public class CarteCredit {

    private String numero;
    private String expiration;

    public CarteCredit(String numero, String expiration)
    {
        this.numero = numero;
        this.expiration = expiration;
    }

    public String getNumero() {
        return numero;
    }

    public String getExpiration() {
        return expiration;
    }

}
