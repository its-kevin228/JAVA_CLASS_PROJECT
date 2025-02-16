/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Date;

/**
 *
 * @author ike
 */
public class Abonne {
     private int id;
    private String nom;
    private String prenom;
    private Date dateinscription;
    private String telephone;
    private boolean statut;

    public Abonne(int id, String nom, String prenom, Date dateInscription, String numeroTelephone, boolean statutSouscription) {
        this.id = id ; 
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = numeroTelephone;
        this.statut = statutSouscription;
        this.dateinscription = new Date();
    }
    
    @Override
public String toString() {
    return "Abonne{" +
            "id=" + id +
            ", nom='" + nom + '\'' +
            ", prenom='" + prenom + '\'' +
            ", date_inscription=" + dateinscription +
            ", numero_telephone='" + telephone + '\'' +
            ", statut_souscription=" + statut +
            '}';
}


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Date getDateinscription() {
        return dateinscription;
    }

    public String getTelephone() {
        return telephone;
    }

    public boolean isStatut() {
        return statut;
    }



    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateinscription(Date dateinscription) {
        this.dateinscription = dateinscription;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setStatut(boolean statut) {
        this.statut = statut;
    }


}
