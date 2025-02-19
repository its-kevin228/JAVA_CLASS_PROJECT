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
public class Souscription {
    private int id;
    private int idAbonne;
    private int idAbonnement;
    private Date dateDebut;

    public Souscription(int id, int idAbonne, int idAbonnement, Date dateDebut) {
        this.id = id;
        this.idAbonne = idAbonne;
        this.idAbonnement = idAbonnement;
        this.dateDebut = dateDebut;
    }

    @Override
    public String toString() {
        return "Souscription{" + "id=" + id + ", idAbonne=" + idAbonne + ", idAbonnement=" + idAbonnement + ", dateDebut=" + dateDebut + '}';
    }

    public int getId() {
        return id;
    }

    public int getIdAbonne() {
        return idAbonne;
    }

    public int getIdAbonnement() {
        return idAbonnement;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdAbonne(int idAbonne) {
        this.idAbonne = idAbonne;
    }

    public void setIdAbonnement(int idAbonnement) {
        this.idAbonnement = idAbonnement;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }
}
