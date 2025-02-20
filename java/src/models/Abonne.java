package models;

import java.util.Date;

public class Abonne {
    private int id;
    private String nom;
    private String prenom;
    private Date dateInscription;
    private String numeroTelephone;
    private boolean statutSouscription;

    // Constructeur
    public Abonne(String nom, String prenom, Date dateInscription, String numeroTelephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateInscription = dateInscription;
        this.numeroTelephone = numeroTelephone;
        this.statutSouscription = false;
    }

    public String toString() {
        return " " + nom  +
               " " + prenom
               ;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public Date getDateInscription() { return dateInscription; }
    public void setDateInscription(Date dateInscription) { this.dateInscription = dateInscription; }
    public String getNumeroTelephone() { return numeroTelephone; }
    public void setNumeroTelephone(String numeroTelephone) { this.numeroTelephone = numeroTelephone; }
    public boolean isStatutSouscription() { return statutSouscription; }
    public void setStatutSouscription(boolean statutSouscription) { this.statutSouscription = statutSouscription; }
}
