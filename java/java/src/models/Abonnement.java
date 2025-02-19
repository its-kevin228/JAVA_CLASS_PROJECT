// Abonnement.java
package models;

public class Abonnement {
    private int id;
    private String libelle;
    private int dureeMois;
    private double prixMensuel;

    public Abonnement(String libelle, int dureeMois, double prixMensuel) {
        this.libelle = libelle;
        this.dureeMois = dureeMois;
        this.prixMensuel = prixMensuel;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }
    public int getDureeMois() { return dureeMois; }
    public void setDureeMois(int dureeMois) { this.dureeMois = dureeMois; }
    public double getPrixMensuel() { return prixMensuel; }
    public void setPrixMensuel(double prixMensuel) { this.prixMensuel = prixMensuel; }

    @Override
    public String toString() {
        return libelle + " (" + dureeMois + " mois)";
    }
}
