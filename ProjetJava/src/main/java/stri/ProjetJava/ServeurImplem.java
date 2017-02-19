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
import java.util.Vector;

/**
 *
 * @author alexis
 */
public class ServeurImplem extends UnicastRemoteObject implements Serveur {
    /**
	 *	Generated serialization ID 
	 */
	private static final long serialVersionUID = 8751288996597991517L;
		

    // Deviendra un Vector ou autre pour la gestion multi-parties.
    private Vector<Partie> parties;
    
    public ServeurImplem() throws RemoteException{
        super();
        this.parties = new Vector<Partie>();
    }    
    
    public synchronized boolean rejoindrePartie(Client c, String partie) throws java.rmi.RemoteException{
        // D'abord on vérifie que la partie est bien en attente de joueurs
        if(this.getPartie(partie) == null){
        	System.out.println("[*] Tentative de connexion sur une partie non existante..." + c.getPseudo());
        	
        	System.out.println("[+] Je vais donc créer la partie !");
        	Partie p = new Partie(partie);
        	
        	this.parties.add(p);
        	
        }else if(this.getPartie(partie).getStatus() != "WAIT"){
            System.out.println("[-] Connexion impossible : La partie n'est pas prête...");
            return false;
        }
        
        this.getPartie(partie).ajouterJoueur(c);
        Annonce a = new Annonce("info", "Le joueur "+c.getPseudo()+" à rejoint la partie "+partie+" !", "Serveur");
        System.out.println(a.getMessage());
        this.getPartie(partie).broadcastAnnonce(a);
        
        System.out.println("[+] Tout s'est bien passé !");

        return true;
    }
    
    public synchronized void quitterPartie(String pseudo, String partie) throws java.rmi.RemoteException{  	
    	if(this.getPartie(partie) != null){
    		// On doit prévenir tous les joueurs du départ de ce joueur.
        	Annonce a = new Annonce("info", "Joueur" + pseudo + "s'est déconnecté !", "Serveur");

    		this.getPartie(partie).enleverJoueurByPseudo(pseudo);
    		// c'est ici qu'on les prévient
        	this.getPartie(partie).broadcastAnnonce(a);
    	}else{
    		System.out.println("[!] Tentative de déconnexion d'une partie non existante...");
    	}
    }
    
    public static void main(String[] args) throws RemoteException {
        try{
            ServeurImplem srv = new ServeurImplem();

            // initialise la registry              
            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://127.0.0.1/Serveur", srv);
            System.out.println("[+] Serveur déclaré");
            
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
    }    
    
    // getters & setters
    public synchronized Annonce getDerniereAnnonce(String partie) throws java.rmi.RemoteException{
        System.out.println("DEBUG : "+ partie);
    	return this.getPartie(partie).getDerniereAnnonce();
    }
    
    public synchronized Vector<Client> getJoueursConnectes(String partie) throws java.rmi.RemoteException{
        return this.getPartie(partie).getListeJoueurs();
    } 
    
    // Retourne la partie désignée par le nom
    // ou null si elle n'exsite pas
    public synchronized Partie getPartie(String nom){
    	int i = 0;
    	while(i < this.parties.size()){
    		if((this.parties.get(i).getNom().equals(nom))){
    	    	return this.parties.get(i);
    		}
    		i++;
    	}
    	// ici on est arrivé au botu du tableau sans rien trouver...
		return null;	
    }

    // Returns the lists of waiting parties
	public synchronized Vector<Partie> getListePartie() throws RemoteException {
		Vector<Partie> p = new Vector<Partie>();
		int i = 0;
		while(i < this.parties.size()){
			System.out.println("Partie "+ this.parties.get(i).getNom()+" -> statut : "+ this.parties.get(i).getStatus());
			if(this.parties.get(i).getStatus().contentEquals("WAIT")){
				p.add(this.parties.get(i));
			}
			i++;
		}
		return p;
	}
}
