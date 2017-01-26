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
import java.util.HashMap;
import java.util.Iterator;
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
    private Partie partie;
    
    public ServeurImplem() throws RemoteException{
        super();
        // derniereAnnonce = new Annonce("", "", );
    }    
    
    public synchronized boolean rejoindrePartie(Client c, String partie) throws java.rmi.RemoteException{
        // D'abord on vérifie que la partie est bien en attente de joueurs
        if(this.partie == null){
        	System.out.println("[*] Tentative de connexion sur une partie non existante..." + ((Joueur)c).getPseudo());
        	return false;
        }
        
        if(this.getPartie(partie).getStatus() != "WAIT"){
            System.out.println("[-] Connexion impossible : La partie n'est pas prête...");
            return false;
        }
        
        this.getPartie(partie).ajouterJoueur(c);
        Annonce a = new Annonce("info", "Le joueur "+c.getPseudo()+" à rejoint la partie !", "Serveur");
        System.out.println(a.getMessage());
        broadcastAnnonce(a, partie);
        
        System.out.println("[+] Tout s'est bien passé !");
        
        return true;
    }
    
    public synchronized void quitterPartie(String pseudo, String partie) throws java.rmi.RemoteException{  	
    	if(this.getPartie(partie) != null){
    		// On doit prévenir tous les joueurs du départ de ce joueur.
        	Annonce a = new Annonce("info", "Joueur" + pseudo + "s'est déconnecté !", "Serveur");

    		this.getPartie(partie).enleverJoueur(pseudo);
    		// c'est ici qu'on les prévient
        	broadcastAnnonce(a, partie);
    	}else{
    		System.out.println("[!] Tentative de déconnexion d'une partie non existante...");
    	}
    }

    // traiterAnnonce exécute le traitement associé au type de l'annonce
    void traiterAnnonce(Annonce annonceCourante) throws RemoteException{
		Client c;
		Vector<Client> liste = new Vector<Client>();	    
		String partie = annonceCourante.getPartie();
		
		liste = this.getPartie(partie).getListeJoueurs();
       
	    // On doit d'abord diffuser l'annonce courante
	    broadcastAnnonce(annonceCourante, this.getPartie(partie).getNom());
	    
	    // Dans le cas d'une surenchère, on ne fait rien d'autre
	    // que de diffuser l'annonce. Dans le cas contraire on
	    // peut commencer à compter les dés de la dernière annonce
	    if(annonceCourante.getType() != "encherir"){
	       	Annonce derniereAnnonce = this.partie.getDerniereAnnonce();
	        
	        int valeur = derniereAnnonce.getValeur();
	        int nbrDesAnnonce = derniereAnnonce.getNombre();
	        int nbrDesReel = 0;
	        
	        // TODO: Vérifier le nom de la partie
	        nbrDesReel = this.getPartie(partie).compterDes(valeur);  
	    	
	        if(annonceCourante.getType() == "menteur"){
		        if(nbrDesReel < nbrDesAnnonce){
		        	Annonce a = new Annonce("info", "Oups... "+annonceCourante.getPseudo()+" a perdu un dé !", "Serveur");
		        	this.getPartie(partie).getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        	broadcastAnnonce(a, this.getPartie(partie).getNom());
		        }
		    }else if(annonceCourante.getType() == "pile"){
		        if(nbrDesReel == nbrDesAnnonce){
		        	Annonce a = new Annonce("info", "Yeah ! "+annonceCourante.getPseudo()+" a gagné un dé !", "Serveur");
		        	this.getPartie(partie).getJoueurByPseudo(annonceCourante.getPseudo()).ajouterDes();
		        	broadcastAnnonce(a, this.getPartie(partie).getNom());
		        }else{
		        	Annonce a = new Annonce("info", "Oups...", "Serveur");
		        	broadcastAnnonce(a, this.getPartie(partie).getNom());
		        	this.getPartie(partie).getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        }
		    }
	    }
	}

    
    public static void main(String[] args) throws RemoteException {
        try{
            ServeurImplem srv = new ServeurImplem();

            // initialise la registry              
            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://10.0.0.1/Serveur", srv);
            System.out.println("[+] Serveur déclaré");

            srv.partie = new Partie("Perudo");
            System.out.println("[+] Partie initialisée");
            
//            try {
//				Thread.sleep(5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            
//            Annonce a = new Annonce("menteur", "Tu es un sale menteur", "Sirbu");
//            
//            for(Iterator<Client> i = srv.getPartie("Perudo").getListeJoueurs().iterator(); i.hasNext();){
//            	Client c = (Client)i.next();
////            	System.out.println(c.getJoueur().getPseudo());
//            	c.AfficheAnnonce(a);
//            }         
                
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        
    }    
    
    // Envoie l'annonce donnée à tous les joueurs sur la partie donnée
    private void broadcastAnnonce(Annonce a, String partie) throws RemoteException{
        for(int i = 0; i < this.getPartie(partie).getListeJoueurs().size(); i++){
        	this.getPartie(partie).getListeJoueurs().get(i).AfficheAnnonce(a);
        }   	
    }
    
    // getters & setters
    public Annonce getDerniereAnnonce(String partie) throws java.rmi.RemoteException{
        return this.getPartie(partie).getDerniereAnnonce();
    }
    
    public Vector<Client> getJoueursConnectes(String partie) throws java.rmi.RemoteException{
        return this.getPartie(partie).getListeJoueurs();
    } 
    
    // Retourne la partie désignée par le nom
    // ou null si elle n'exsite pas
    public Partie getPartie(String nom){
    	return this.partie;
    }

	public Partie getPartie() {
		return partie;
	}

	public void setPartie(Partie partie) {
		this.partie = partie;
	}
}
