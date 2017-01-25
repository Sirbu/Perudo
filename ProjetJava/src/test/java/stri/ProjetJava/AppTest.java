package stri.ProjetJava;

import java.rmi.RemoteException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName ) throws RemoteException{
        super( testName );
        
        ServeurImplem serveur1= new ServeurImplem();    //on créer le serveur
        Joueur joueur1 = new Joueur(serveur1);          //on créer le joueur
        joueur1.setPseudo("Saidharan");                 //on affecte un pseudo au joueur
        Partie partie1 = new Partie("partie1");         //on créer la partie
        Annonce annonce1 = new Annonce("encherir",2,4,"joueur1","partie1");
        partie1.setDerniereAnnonce(annonce1);           //on affecte l'objet annonce au serveur
        serveur1.setPartie(partie1);                    //on affecte l'objet partie au serveur 
        joueur1.FaireAnnonce();                         //on simule le faire annonce
       
        //à terminer
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}


