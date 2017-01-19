/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author alexis
 */
public class ServeurImplem extends UnicastRemoteObject implements Serveur {
    
    private Annonce derniereAnnonce;
    // Deviendra un Vector ou autre pour la gestion multi-parties.
    private Partie partie;
    
    public ServeurImplem() throws RemoteException{
        super();
        // derniereAnnonce = new Annonce("", "", );
    }    
    
    public synchronized boolean rejoindrePartie(String pseudo, String partie) throws java.rmi.RemoteException{
        
        // D'abord on vérifie que la partie est bien en attente de joueurs
        // TODO : vérifier le nom de la partie lors des prochaines versions
        if (this.partie.getStatus() != "WAIT"){
            System.out.println("[-] Cannot connect to a non-waiting party...");
            return false;
        }
        
        this.partie.ajouterJoueur(pseudo);
        
        return true;
    }
    
    public synchronized void quitterPartie(String pseudo, String partie) throws java.rmi.RemoteException{
        // TODO: gérer le nom des parties
        this.partie.enleverJoueur(pseudo);
    }
    
    public Annonce getDerniereAnnonce() throws java.rmi.RemoteException{
        return derniereAnnonce;
    }
    
    public Vector<Joueur> getJoueursConnectes(String partie) throws java.rmi.RemoteException{
        return this.partie.getJoueurs();
    }
    
    void traiterMenteur(Annonce a){
        
    }
    
    void traiterPile(Annonce a){
        
    }
    
    void traiterAnnonce(Annonce a) throws RemoteException{
        Vector<Joueur> liste = new Vector<Joueur>();
        Client c;
        liste = this.partie.getJoueurs();

        // On doit d'abord diffuser la derniere annonce
        for(Iterator i = liste.iterator(); i.hasNext();){
            c = (Client)i.next();
            c.AfficheAnnonce(a);
        }
    }
    
    public static void main(String[] args) throws RemoteException {
        
        try{
            
            ServeurImplem srv = new ServeurImplem();

            // initialise la registry              
            LocateRegistry.createRegistry(1099);

            Naming.rebind("Serveur", srv);
            System.out.println("[+] Serveur déclaré");    
                
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        
    }    

    public boolean rejoindrePartie(Joueur j, String partie) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
