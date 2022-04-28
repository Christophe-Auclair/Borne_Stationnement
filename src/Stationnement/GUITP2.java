package Stationnement;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.ParseException;

public class GUITP2 {
    private JPanel panel1;
    private JLabel labelLogo;
    private JPanel panelNumeros;
    private JPanel panelDroite;
    private JPanel panelComptant;
    private JLabel champMessage;
    private JButton bouton25;
    private JButton bouton100;
    private JButton bouton200;
    private JPanel panelCredit;

    private JButton bouton25Credit;
    private JButton boutonMaxCredit;
    private JButton boutonOk;
    private JTextArea zoneRecu;
    private JButton boutonRapport;
    private JFormattedTextField champNumeroCarte;
    private JFormattedTextField champDateExp;

    private EcouteurNumero ecouteurNumero;
    private EcouteurCarteCredit ecouteurCarteCredit;
    private EcouteurMonnaie ecouteurMonnaie;
    private EcouteurControles ecouteurControles;
    private EcouteurEntree ecouteurEntree;

    private String place ="";                   // place de stationnement choisie
    private String transaction;                 // transaction à afficher
    private Borne borne;                        // borne à créer
    private CarteCredit carte;                  // carte à remplir
    private Transaction transactionCourante;    // transaction courante
    private DecimalFormat dfArgent = new DecimalFormat("0.00");      // format pour argent
    private DecimalFormat dfMinutes = new DecimalFormat("##0");      // format pour minutes

    public GUITP2() throws ParseException
    {
        labelLogo.setIcon(new ImageIcon("logo.png"));
        //Création des écouteurs
        ecouteurNumero = new EcouteurNumero();
        ecouteurCarteCredit = new EcouteurCarteCredit();
        ecouteurMonnaie = new EcouteurMonnaie();
        ecouteurControles = new EcouteurControles();
        ecouteurEntree = new EcouteurEntree();

        // panelNumeros avec la grille
        GridBagLayout gl = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        panelNumeros.setLayout(gl);
        c.fill = GridBagConstraints.BOTH;
        c.weightx =1;
        c.weighty=1;
        for ( int i = 0; i <15 ; i++)
        {
            JButton temp = new JButton();
            temp.setFont(new Font("Tw Cen MT", Font.BOLD, 16));
            temp.setForeground(new Color(0,174,239));
            temp.addActionListener(ecouteurNumero);
            if  ( i ==0 )
                temp.setText("A");

            else if ( i ==1 )
                temp.setText("B");
            else if ( i==2 )
               temp.setText("C");
            else if ( i == 3 ) {
                c.gridwidth = GridBagConstraints.REMAINDER; //end row
                temp.setText("D");
            }
            else if ( i <=6)
            {
                c.weightx=1;
                c.gridwidth = 1;
                temp.setText(String.valueOf(i-4));
            }
            else if ( i==7)
            {
                c.gridwidth = GridBagConstraints.REMAINDER; //end row
                temp.setText(String.valueOf(i-4));
            }
            else if ( i <=10)
            {
                c.weightx=1;
                c.gridwidth = 1;
                temp.setText(String.valueOf(i-4));
            }
            else if ( i==11)
            {
                c.gridwidth = GridBagConstraints.REMAINDER; //end row
                temp.setText(String.valueOf(i-4));
            }
            else if ( i <=13)
            {
                c.weightx=1;
                c.gridwidth = 1;
                temp.setText(String.valueOf(i-4));
            }
            else if ( i==14)
            {
                c.gridwidth = GridBagConstraints.REMAINDER; //end row
                temp.setText("entrée");
                temp.removeActionListener(ecouteurNumero);
                temp.addActionListener(ecouteurEntree);
            }
            gl.setConstraints(temp, c );
            panelNumeros.add( temp);
        }

        // inscrire les sources à l'écouteur
        bouton25.addActionListener(ecouteurMonnaie);
        bouton100.addActionListener(ecouteurMonnaie);
        bouton200.addActionListener(ecouteurMonnaie);

        bouton25Credit.addActionListener(ecouteurCarteCredit);
        boutonMaxCredit.addActionListener(ecouteurCarteCredit);
        boutonOk.addActionListener(ecouteurControles);
        boutonRapport.addActionListener(ecouteurControles);

        borne = new Borne();

       if (!borne.borneDisponible())                        // vérifier si la borne est en fonction
           champMessage.setText("Stationnement gratuit");
       else
           champMessage.setText("Stationnement Montréal --- Veuillez entrer le numéro de la place");
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        try {
            champNumeroCarte = new JFormattedTextField(new MaskFormatter("#### #### #### ####"));
            champDateExp = new JFormattedTextField(new MaskFormatter("##/##"));
        }
        catch ( ParseException pe)
        {
            pe.printStackTrace();
        }
    }

    private void $$$setupUI$$$() {
        createUIComponents();
    }
    private class EcouteurNumero implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            //lettre ou chiffre du bouton qu'on a cliqué dessus
           String lettreChiffre = ((JButton)e.getSource()).getText();
           boutonNumeroLettre_actionPerformed( lettreChiffre);
        }
    }

    private class EcouteurEntree implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            boutonEntree_actionPerformed();
        }
    }

    private class EcouteurMonnaie implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if ( e.getSource() == bouton25)
                bouton25_actionPerformed();
            else if ( e.getSource()==bouton100)
                bouton100_actionPerformed();
            else if ( e.getSource() == bouton200)
                bouton200_actionPerformed();
        }
    }

    private class EcouteurCarteCredit implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {

             if ( e.getSource() == bouton25Credit)
                bouton25Credit_actionPerformed();
            else if (e.getSource() == boutonMaxCredit)
                boutonMaxCredit_actionPerformed();
        }
    }

    private class EcouteurControles implements ActionListener
    {
        @Override
        public void actionPerformed(ActionEvent e) {
            if ( e.getSource() == boutonOk)
                boutonOK_actionPerformed();
            else if ( e.getSource() == boutonRapport)
                boutonRapport_actionPerformed();
        }
    }

    private void afficherTransaction()                  // affiche la transaction dans la zone recu
    {
        transactionCourante = borne.getTransactionCourante();
        String comptantCredit;

        if (!transactionCourante.getComptantOuCredit())
            comptantCredit = "Comptant";
        else
            comptantCredit = "Crédit";

        transaction = "      --- Transaction ---\nNuméro de la place : " + transactionCourante.getNumeroPlace();
        transaction += "\nComptant ou crédit : " + comptantCredit;
        transaction += "\nDate : " + transactionCourante.getDate();
        transaction += "\nHeure début : " + transactionCourante.getHeureDebut();
        transaction += "\nHeure fin : " + transactionCourante.getHeureFin();
        transaction += "\nDurée : " + dfMinutes.format(transactionCourante.getDuree()) + " minutes";
        transaction += "\nMontant : " + dfArgent.format(transactionCourante.getMontant()) + " $";

        zoneRecu.setText(transaction);
        champMessage.setText("Durée maximum : " + borne.getDureeMax() + " minutes --- Heure de fermeture de la borne : " + borne.getHeureFermeture() + "h");
    }

    private void bouton25_actionPerformed()
    {
        if (borne.borneDisponible()) {                              // pour empêcher l'interaction si la borne est fermée
            if (borne.getTransactionCourante() == null)             // check pour être sur d'avoir une transaction courante avant de payer
                champMessage.setText("Veuillez choisir une place avant de payer");
            else if (borne.getComptantOuCredit() == 2)              // check pour être sur que l'usager de change pas de mode de paiement mi-transaction
                champMessage.setText("Veuillez continuer de payer crédit");
            else {
                Piece quarter = new Piece(0.25);              // création de la pièce de monnaie
                borne.paiementComptant(quarter);                    // paiement avec cette dernière
                afficherTransaction();                              // affichage de la transaction en cours
            }
        }
    }

    private void bouton100_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null)
                champMessage.setText("Veuillez choisir une place avant de payer");
            else if (borne.getComptantOuCredit() == 2)
                champMessage.setText("Veuillez continuer de payer crédit");
            else {
                Piece dollar = new Piece(1);
                borne.paiementComptant(dollar);
                afficherTransaction();
            }
        }
    }

    private void bouton200_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null)
                champMessage.setText("Veuillez choisir une place avant de payer");
            else if (borne.getComptantOuCredit() == 2)
                champMessage.setText("Veuillez continuer de payer crédit");
            else {
                Piece toonie = new Piece(2);
                borne.paiementComptant(toonie);
                afficherTransaction();
            }
        }
    }

    private void bouton25Credit_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null)
                champMessage.setText("Veuillez choisir une place avant de payer");
            else if (borne.getComptantOuCredit() == 1)                  // check pour être sur que l'usager de change pas de mode de paiement mi-transaction
                champMessage.setText("Veuillez continuer de payer comptant");
            else if (champNumeroCarte.getText().equals("                   ") || champDateExp.getText().equals("  /  "))
                champMessage.setText("Entrée de carte invalide");       // check pour voir si l'entrée de la carte est valide
            else {
                int choix = 1;
                if (carte == null) {
                    carte = new CarteCredit(champNumeroCarte.getText(), champDateExp.getText());    // création de la carte de crédit
                }
                if (borne.validerCarteCredit(carte)) {                                              // validation de la carte
                    champMessage.setText("Carte valide");
                    borne.paiementCredit(choix);                                                    // paiement avec cette dernière
                    afficherTransaction();                                                          // affichage de la transaction en cours
                } else {
                    champMessage.setText("Carte invalide");
                    carte = null;                                                                   // destruction de la carte invalide
                }
            }
        }
    }

    private void boutonMaxCredit_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null)
                champMessage.setText("Veuillez choisir une place avant de payer");
            else if (borne.getComptantOuCredit() == 1)
                champMessage.setText("Veuillez continuer de payer comptant");
            else if (champNumeroCarte.getText().equals("                   ") || champDateExp.getText().equals("  /  "))
                champMessage.setText("Entrée de carte invalide");
            else {
                int choix = 2;
                if (carte == null) {
                    carte = new CarteCredit(champNumeroCarte.getText(), champDateExp.getText());
                }
                if (borne.validerCarteCredit(carte)) {
                    champMessage.setText("Carte valide");
                    borne.paiementCredit(choix);
                    afficherTransaction();
                } else {
                    champMessage.setText("Carte invalide");
                    carte = null;
                }
            }
        }
    }

    public void boutonNumeroLettre_actionPerformed(String lettreChiffre)
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null) {
                place += lettreChiffre;
                champMessage.setText(place);
                zoneRecu.setText("");
            } else
                champMessage.setText("Veuillez compléter votre transaction courante");  // pour empêcher l'usage du bouton si une transaction est en cours
        }
    }

    private void boutonEntree_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null) {
                if (borne.validerPlace(place))
                    champMessage.setText(place + " : Numéro de place valide, paiement comptant ou crédit?");
                else {
                    champMessage.setText("Numéro de place invalide");
                    place = "";
                }
            } else
                champMessage.setText("Veuillez compléter votre transaction courante");  // pour empêcher l'usage du bouton si une transaction est en cours
        }
    }

    private void boutonOK_actionPerformed()
    {
        if (borne.borneDisponible()) {
            if (borne.getTransactionCourante() == null)
                champMessage.setText("Veuillez choisir une place avant de payer");  // pour empêcher l'usage du bouton si une transaction n'est pas en cours
            else if (borne.getTransactionCourante().getMontant() == 0) {            // pour empêcher de compléter une transaction sans payer
                champMessage.setText("Aucun montant déposé");
            } else {
                afficherTransaction();
                borne.validation();                                                 // pour vider les compteurs
                champMessage.setText("Transcation complétée --- Veuillez entrer le numéro de la place pour une autre transaction");
                place = "";                                                         // pour vider les champs textes
                champNumeroCarte.setText("");
                champDateExp.setText("");
                borne.nullTransactionCourante();                                    // pour vider la transaction
            }
        }
    }

    private void boutonRapport_actionPerformed()
    {
        double total = borne.controle();
        champMessage.setText("Total : " + dfArgent.format(total) + " $");
    }

    public static void main(String[] args) {
        try {
            JFrame frame = new JFrame("GUITP2");
            frame.setContentPane(new GUITP2().panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 950);
            //frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }
        catch ( Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
