package stri.ProjetJava;

import java.rmi.RemoteException;
import jdk.nashorn.internal.runtime.RewriteException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import stri.ProjetJava.*;
/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase{
       
    //Cette méthode test si verifAnnonce fonctionne correctement
    public void testVerifAnnonce() throws RemoteException{
    	ServeurImplem serveur1= new ServeurImplem();    //on créer le serveur
        Joueur joueur1 = new Joueur(serveur1);          //on créer le joueur
        joueur1.setPseudo("Saidharan");                 //on affecte un pseudo au joueur
        Partie partie1 = new Partie("partie1");         //on créer la partie
        Annonce annonce1 = new Annonce("surencherir", 5, 5, "joueur2", "partie1");
        Annonce annonce2 = new Annonce("surencherir",2,4,"joueur1","partie1");
        partie1.setDerniereAnnonce(annonce1);           //on affecte l'objet annonce1 au serveur
        serveur1.setPartie(partie1);                    //on affecte l'objet partie1 au serveur
        
        
        assertEquals(false, annonce2.verifAnnonce(serveur1));
        
       // deuxieme test
        Annonce annonce3 = new Annonce("surencherir",5,5,"joueur1","partie1"); 
       
        assertEquals(false, annonce3.verifAnnonce(serveur1));
        
        // troisieme test
        Annonce annonce4 = new Annonce("surencherir",345356,23,"joueur1","partie1"); 
       
        assertEquals(false, annonce4.verifAnnonce(serveur1));
        
        // quatrieme test
        Annonce annonce5 = new Annonce("surencherir",8,23,"joueur1","partie1"); 
       
        assertEquals(false, annonce5.verifAnnonce(serveur1));
    }
    
    //Cette méthode test si la méthode retirerDes enlève bien un dé
    public void testRetirerDe() throws RemoteException{
                
        ServeurImplem serveur1= new ServeurImplem();    //on créer le serveur
        Joueur joueur1 = new Joueur(serveur1);          //on créer le joueur
        joueur1.setPseudo("Saidharan");                 //on affecte un pseudo au joueur
        
        
        joueur1.retirerDes();
        
        assertEquals(5, joueur1.getDes().size());
    }
    
    //Cette méthode test si la méthode ajouterDes ajoute bien un dé
    public void testAjouterDe() throws RemoteException{
                
        ServeurImplem serveur1= new ServeurImplem();    //on créer le serveur
        Joueur joueur1 = new Joueur(serveur1);          //on créer le joueur
        joueur1.setPseudo("Saidharan");                 //on affecte un pseudo au joueur
        
        
        joueur1.ajouterDes();
        
        assertEquals(7, joueur1.getDes().size());
    }
    
    //Cette méthode test si l'etat de la partie est initialisé à WAIT avant le début de la partie
    public void testEtatPartie() throws RemoteException{
        ServeurImplem serveur1= new ServeurImplem();    //on créer le serveur
        Joueur joueur1 = new Joueur(serveur1);          //on créer le joueur
        Partie partie1 = new Partie("partie1");         //on créer la partie
        serveur1.setPartie(partie1);                    //on affecte l'objet partie1 au serveur
        joueur1.setPartie("partie1");                   //on affecte le joueur à la partie
        joueur1.setPseudo("Saidharan");                 //on affecte un pseudo au joueur
        
        assertEquals("WAIT", serveur1.getPartie().getStatus());
    }      
}


