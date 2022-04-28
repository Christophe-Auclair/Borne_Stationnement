package Stationnement;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

public class Transaction {

    private GregorianCalendar heureDebut;
    private GregorianCalendar heureFin;
    private String numeroPlace;
    private double duree;
    private double montant;
    private boolean comptantOuCredit;
    private SimpleDateFormat sdfHeure = new SimpleDateFormat("HH:mm:ss");
    private SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yy");

    public Transaction() {
        heureDebut = new GregorianCalendar();
        heureFin = new GregorianCalendar();
    }

    public void ajoutMinutes(int mins) {
        heureFin.add(Calendar.MINUTE, mins);            // ajoute des minutes a l'heure de fin
    }

    public String getHeureDebut() {
        return sdfHeure.format(heureDebut.getTime());
    }

    public String getHeureFin() {
        return sdfHeure.format(heureFin.getTime());
    }

    public void setHeureFin(int heure) {                // set l'heure de fin à l'heure de fermeture de la borne
        heureFin.set(Calendar.HOUR_OF_DAY, heure);
        heureFin.set(Calendar.MINUTE, 0);
        heureFin.set(Calendar.SECOND, 0);
    }

    public GregorianCalendar getCal() {
        return heureFin;
    }

    public String getDate() {
        return sdfDate.format(heureDebut.getTime());
    }

    public String getNumeroPlace() {
        return numeroPlace;
    }

    public double getDuree() { return duree; }

    public double getMontant() { return montant; }

    public boolean getComptantOuCredit() {
        return comptantOuCredit;
    }

    public void setHeureDebut(GregorianCalendar  heureDebut) { this.heureDebut = heureDebut; }

    public void setNumeroPlace(String numeroPlace) { this.numeroPlace = numeroPlace; }

    public void setDuree(double duree) { this.duree = duree; }

    public void setMontant(double montant) { this.montant = montant; }

    public void setComptantOuCredit(boolean comptantOuCredit) { this.comptantOuCredit = comptantOuCredit; }
}
