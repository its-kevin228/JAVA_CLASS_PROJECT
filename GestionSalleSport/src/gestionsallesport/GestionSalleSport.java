/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package gestionsallesport;

import dao.DBException;
import java.sql.SQLException;
import java.util.Date;
import model.Abonne;
import service.Abonneservice;

/**
 *
 * @author ike
 */
public class GestionSalleSport {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws DBException, SQLException {
        // TODO code application logic here
        Abonneservice service = new Abonneservice();
        Abonne a1 = new Abonne(1, "loma", "ike", new Date(), "90239721", true);
        service.ajouterAbonne(a1);  // L'ID est maintenant correctement assigné
        a1.setNom("Ameble");
        service.modifierAbonne(a1);  // La modification devrait maintenant fonctionner
        //service.supprimerAbonne(6);
        //System.out.println(service.getAllAbonnes());
        System.out.println(service.getNombreAbonnesActifs());
    }
    
}
