/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexis
 */
public class Partie {
    
    private String nom;
    private int nbrJoueursMax = 6;              // Pourra être modifié à l'occaz
    private String status;                      // WAIT || STARTED || FINISHED
    private Annonce derniereAnnonce;            // la derniereAnnonce émise par un joueur
    private HashMap<String, Client> joueurs;    // Contient les joueurs
    
    
    public Partie(String nom){
        this.nom = nom;
        this.status = "WAIT";
    }
      
    public void enleverJoueur(String pseudo){
        this.joueurs.remove(pseudo);
    }
    
    // TODO : change Joueurs with rmi Clients
    public void ajouterJoueur(String pseudo){
        
        try {
            Client c = (Client)Naming.lookup(pseudo);
         
            this.joueurs.put(pseudo, c);
                    
        } catch (NotBoundException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(Partie.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // getters & setters
    public String getStatus(){
        return this.status;
    }
    
    public Vector<Joueur> getJoueurs(){
        Vector<Joueur> liste = new Vector<Joueur>();
        
        for(Iterator i = liste.iterator(); i.hasNext();){
            liste.add((Joueur)i.next());
        }
        
        return liste;
    }
}
