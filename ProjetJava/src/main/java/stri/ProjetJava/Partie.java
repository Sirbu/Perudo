/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Timer;

/**
 *
 * @author alexis
 */
public class Partie implements ActionListener {
    
    private String nom;
    private int nbrJoueursMax = 6;              // Pourra être modifié à l'occaz
    private String status;                      // WAIT || STARTED || FINISHED
    private Annonce derniereAnnonce;           // la derniereAnnonce émise par un joueur
    private Vector<Client> joueurs;    			// Contient les joueurs
    private Timer timer;
    
    
    public Partie(String nom){
        this.nom = nom;
        this.status = "WAIT";
        this.joueurs = new Vector<Client>();
    }
      
    public void enleverJoueur(String pseudo){
        this.joueurs.remove(pseudo);
    }
    
    // TODO : change Joueurs with rmi Clients
    public boolean ajouterJoueur(Client c){
        
        if(this.joueurs.size() == nbrJoueursMax){
			return false;
		}
		this.joueurs.add(c);
		
		if(this.joueurs.size() == 1){
		    this.timer = new Timer(120000, this);
		    this.timer.setInitialDelay(120000);
		    this.timer.start();
		}
        
        return true;
    }
    
    // Retourne le nombre de dés totaux de la valeur donnée.
    public int compterDes(int valeur){
    	int cpt = 0;
    	Vector<Integer> des = new Vector<Integer>();
    	
    	for(Iterator<Client> i = this.getListeJoueurs().iterator(); i.hasNext();){
    		// Pour chaque client on récupére ses dés...
    		try {
				des = i.next().getDes();
			} catch (RemoteException e) {
				// TODO: Il faudra gérer les dconnexion brutales
				e.printStackTrace();
			}
    		
    		// ... et on compte le nombre de fois qu'apparait la valeur
    		// mais aussi le chiffre 1 (valeur du perudo)
    		for(int j = 0; j < des.size(); j++){
    			if((des.get(j) == valeur) || des.get(j) == 1){
    				cpt++;
    			}
    		}
    	}
    	return cpt;
    }
    
    public void lancerPartie(){
    	// TODO do it !
    }
    
    // getters & setters
    public String getStatus(){
        return this.status;
    }
    
    public String getNom(){
    	return this.nom;
    }
    
    public Client getJoueurByPseudo(String pseudo) throws RemoteException{
    	int i = 0;
    	for(i = 0; i < this.joueurs.size(); i++){
    		if(this.joueurs.get(i).getPseudo() == pseudo){
    			break;
    		}
    	}
    	return this.joueurs.get(i);
    }
    
    // Retourne la liste des joueurs connectés à la partie
    // sous la forme d'un vector
    public Vector<Client> getListeJoueurs(){
        return this.joueurs;
    }
    
    public Annonce getDerniereAnnonce(){
    	return this.derniereAnnonce;
    }
    
    public void setDerniereAnnonce(Annonce a){
    	this.derniereAnnonce = a;
    }
    
    

	public void actionPerformed(ActionEvent arg0) {
		 System.out.println("[+] Time's up !");
		 System.out.println("La partie va commencer !");
	}
}
