/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ike
 */
public class Abonnement {

    private int id;
    private String libelle;
    private int dureeMois;
    private double prixMensuel;

    public Abonnement(int id, String libelle, int dureeMois, double prixMensuel) {
        this.id = id;
        this.libelle = libelle;
        this.dureeMois = dureeMois;
        this.prixMensuel = prixMensuel;
    }

    @Override
    public String toString() {
        return "Abonnement{" + "id=" + id + ", libelle=" + libelle + ", dureeMois=" + dureeMois + ", prixMensuel=" + prixMensuel + '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public int getDureeMois() {
        return dureeMois;
    }

    public void setDureeMois(int dureeMois) {
        this.dureeMois = dureeMois;
    }

    public double getPrixMensuel() {
        return prixMensuel;
    }

    public void setPrixMensuel(double prixMensuel) {
        this.prixMensuel = prixMensuel;
    }
}
