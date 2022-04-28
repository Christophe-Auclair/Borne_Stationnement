package Stationnement;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Borne {

    private long dureeMax;
    private int heureFermeture;
    private double total;
    private int duree;
    private int compteur;
    private double montant;
    private int comptantOuCredit;
    private Transaction transactionCourante;
    private GregorianCalendar present;

    public Borne() {
        dureeMax = 120;         // durée maximum par default en minutes ( 2h )
        total = 0;              // total contenu dans la borne
        compteur = 0;           // tracker des minutes payées de la transaction courante
        montant = 0;            // tracker de l'argent déposé lors de la transaction courante
        comptantOuCredit = 0;   // boolean a 3 value (0,1,2), pour empecher de payer avec autre mode de paiement une fois paiement commencé
    }

    public Transaction getTransactionCourante() {
        return transactionCourante;
    }

    public void nullTransactionCourante() {
        this.transactionCourante = null;
    }

    public int getComptantOuCredit() {
        return comptantOuCredit;
    }

    public long getDureeMax() {
        return dureeMax;
    }

    public int getHeureFermeture() {
        return heureFermeture;
    }

    public boolean borneDisponible()
    {
        present = new GregorianCalendar();
        if (present.get(Calendar.DAY_OF_WEEK) == 1) {                                               // Dimanche
            if (present.get(Calendar.HOUR_OF_DAY) < 13 || present.get(Calendar.HOUR_OF_DAY) >= 18)  // check heures d'ouverture
                return false;
            heureFermeture = 18;       // set heure fermeture pour pas payer plus que 2h si la borne ferme dans moins de 2h
            return true;
        }
        else if (present.get(Calendar.DAY_OF_WEEK) > 1 && present.get(Calendar.DAY_OF_WEEK) < 7) {  // Semaine
            if (present.get(Calendar.HOUR_OF_DAY) < 9 || present.get(Calendar.HOUR_OF_DAY) >= 21)
                return false;
            heureFermeture = 21;
            return true;
        }
        else if (present.get(Calendar.DAY_OF_WEEK) == 7) {                                          // Samedi
            if (present.get(Calendar.HOUR_OF_DAY) < 9 || present.get(Calendar.HOUR_OF_DAY) >= 18)
                return false;
            heureFermeture = 18;
            return true;
        }
        return false;
    }

    public boolean validerPlace(String place)
    {
        if (place.matches("[A-Z]\\d{3}")) {             // Maj + 3 numbers
            transactionCourante = new Transaction();
            transactionCourante.setNumeroPlace(place);
            return true;
        }
        return false;
    }

    public boolean validerCarteCredit(CarteCredit carteCredit)
    {
        present = new GregorianCalendar();
        String num = carteCredit.getNumero();
        String exp = carteCredit.getExpiration();
        int expMois = Integer.valueOf(exp.substring(0, 2));               // extracts first 2 digits and convert to int
        int expAnnee = Integer.valueOf(exp.substring(3, 5)) + 2000;       // add 2000 because user inputs only decades
        int presMois = present.get(Calendar.MONTH) + 1;                   // add 1 because months starts at 0
        int presAnnee = present.get(Calendar.YEAR);

        if (num.matches("(\\d{4} ){3}\\d{4}"))          // check 16 digits with spaces in between
            if (exp.matches("[0-1]\\d/\\d{2}")) {       // check expiration date format
                if (exp.matches("1[3-9].*"))            // check if expiration month is between 13 and 19
                    return false;
                if (presAnnee < expAnnee)
                    return true;
                if (presMois <= expMois && presAnnee == expAnnee)
                    return true;
            }
        return false;
    }

    public void paiement(int minutes)
    {
        compteur += minutes;
        transactionCourante.ajoutMinutes(minutes);
        if (transactionCourante.getCal().get(Calendar.HOUR_OF_DAY) == this.heureFermeture)      // si l'heure de fin depasse heure de fermeture
            transactionCourante.setHeureFin(this.heureFermeture);                               // si heure de fin dépasse, on met le max
        duree += minutes;
        if (duree > dureeMax)                                               // pour emêcher de payer plus que 2h
            duree = (int) dureeMax;                                         // si duree dépasse, on met le max
        montant += (double)minutes / 20;                                    // division des minutes par 20 pour avoir le montant en argent
        transactionCourante.setDuree(duree);
        transactionCourante.setMontant(montant);
        total += (double)minutes / 20;
    }

    public void testHeureFermeture()                    // test pour savoir si 2h dépasse l'heure de fermeture de la borne
    {
        present = new GregorianCalendar();
        GregorianCalendar heureFermeture = (GregorianCalendar)present.clone();
        heureFermeture.set(Calendar.HOUR_OF_DAY, Math.toIntExact(this.heureFermeture));
        heureFermeture.set(Calendar.MINUTE, 0);
        heureFermeture.set(Calendar.SECOND, 0);
        GregorianCalendar testHeureFermeture = (GregorianCalendar)present.clone();
        testHeureFermeture.add(Calendar.HOUR_OF_DAY, 2);

        if (testHeureFermeture.after(heureFermeture)) {
            dureeMax = (heureFermeture.getTimeInMillis() - present.getTimeInMillis()) / 60000; // divison pour convertir millisecondes en minutes
        }
    }

    public void paiementComptant(Piece piece)
    {
        final int QUARTER = 5;      // valeur de la pièce en minutes
        final int DOLLAR = 20;      // valeur de la pièce en minutes
        final int TOONIE = 40;      // valeur de la pièce en minutes
        comptantOuCredit = 1;       // bool pour dire que le paiement est comptant
        String valeur = String.valueOf(piece.getValeur());      // convertir valeur de la pièce en string pour le switch
        testHeureFermeture();

        switch (valeur) {
            case "0.25":
                if (compteur < dureeMax)
                    paiement(QUARTER);
                break;
            case "1.0":
                if (compteur < dureeMax)
                    paiement(DOLLAR);
                break;
            case "2.0":
                if (compteur < dureeMax)
                    paiement(TOONIE);
                break;
        }
    }

    public void paiementCredit(int choix)
    {
        final int QUARTER = 5;      // valeur en minutes de 25c
        comptantOuCredit = 2;      // bool pour dire que le paiement est comptant
        testHeureFermeture();

        switch (choix) {
            case 1:
                if (compteur < dureeMax) {
                    paiement(QUARTER);
                    transactionCourante.setComptantOuCredit(true);
                }
                    break;
            case 2:
                if (compteur == dureeMax)
                    break;
                else {
                    int max = (int) dureeMax - compteur;                // maximum de minutes possibles
                    compteur += max;
                    transactionCourante.ajoutMinutes(max);
                    if (transactionCourante.getCal().get(Calendar.HOUR_OF_DAY) == this.heureFermeture)
                        transactionCourante.setHeureFin(this.heureFermeture);
                    transactionCourante.setComptantOuCredit(true);
                    duree += max;
                    montant += (double) max / 20;
                    transactionCourante.setDuree(duree);
                    transactionCourante.setMontant(montant);
                    total += (double) max / 20;
                }
                break;
        }
    }

    public void validation()            // réinitialise les compteurs
    {
        duree = 0;
        compteur = 0;
        montant = 0;
        comptantOuCredit = 0;
        dureeMax = 120;
    }

    public double controle(){           // méthode de contrôle pour les employés
        return total;
    }

}
