/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Timer;

/**
 *
 * @author alexis
 */
public class Partie implements ActionListener {

    private String nom;
    private int nbrJoueursMax = 2;              // Pourra être modifié à l'occaz
    private String status;                      // WAIT || STARTED || FINISHED
    private Annonce derniereAnnonce;           // la derniereAnnonce émise par un joueur
    private Vector<Client> joueurs;    			// Contient les joueurs
    private Timer timer;


    public Partie(String nom){
        this.nom = nom;
        this.status = "WAIT";
        this.joueurs = new Vector<Client>();
    }

    public void enleverJoueurByPseudo(String pseudo){
    	int joueurCourant = 0;
    	try {

			for(joueurCourant = 0;
					(joueurCourant < this.joueurs.size())
					&& (this.joueurs.get(joueurCourant).getPseudo() != pseudo);
					joueurCourant++);
			this.joueurs.remove(joueurCourant);

    	} catch (RemoteException e) {
			System.out.println("Le client "+ joueurCourant + " est déconnecté !");
			this.joueurs.remove(joueurCourant);
          
		}
    }

    public void enleverJoueurByIndex(int i){
    	this.joueurs.remove(i);
    }

    public boolean ajouterJoueur(Client c){

        if(this.joueurs.size() == nbrJoueursMax){
			return false;
		}
		this.joueurs.add(c);

		if(this.joueurs.size() == 1){
		    this.timer = new Timer(5000, this);
		    this.timer.setInitialDelay(5000);
		    this.timer.start();
		}else if(this.joueurs.size() == nbrJoueursMax){
			this.timer.stop();
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

    // Envoie l'annonce donnée à tous les joueurs sur la partie donnée
    public void broadcastAnnonce(Annonce a) throws RemoteException{
        for(int i = 0; i < this.getListeJoueurs().size(); i++){
        	this.getListeJoueurs().get(i).AfficheAnnonce(a);
        }
    }

    // traiterAnnonce exécute le traitement associé au type de l'annonce
    void traiterAnnonce(Annonce annonceCourante) throws RemoteException{
		// On doit d'abord diffuser l'annonce courante
	    broadcastAnnonce(annonceCourante);

	    // Dans le cas d'une surenchère, on ne fait rien d'autre
	    // que de diffuser l'annonce. Dans le cas contraire on
	    // peut commencer à compter les dés de la dernière annonce
	    if(!annonceCourante.getType().contentEquals("surencherir")){
	       	Annonce derniereAnnonce = this.getDerniereAnnonce();
	        Annonce a = null;

	        int valeur = derniereAnnonce.getValeur();
	        int nbrDesAnnonce = derniereAnnonce.getNombre();
	        int nbrDesReel = 0;

	        // TODO: Vérifier le nom de la partie
	        nbrDesReel = this.compterDes(valeur);

	        if(annonceCourante.getType().contentEquals("menteur")){
		        if(nbrDesReel < nbrDesAnnonce){
		        	a = new Annonce("info", "Oups... "+annonceCourante.getPseudo()+" a perdu un dé !", "Serveur");
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        }
		    }else if(annonceCourante.getType().contentEquals("toutpile")){
		        if(nbrDesReel == nbrDesAnnonce){
		        	a = new Annonce("info", "Yeah ! "+annonceCourante.getPseudo()+" a gagné un dé !", "Serveur");
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).ajouterDes();
		        }else{
		        	a = new Annonce("info", "Oups...", "Serveur");
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        }
		    }

	        broadcastAnnonce(a);
	        this.derniereAnnonce = a;
	    }
	}

    private void lancerTousLesDes(){
    	for(int i = 0; i < this.joueurs.size(); i++){
    		try {
    			System.out.println("[+] Lancer de dés pour "+ this.joueurs.get(i).getPseudo());
				this.joueurs.get(i).lancerDes();
			} catch (RemoteException e) {
				this.enleverJoueurByIndex(i);
				continue;
			}
    	}
    }
    
    // Ne pas incrémenter le compteur de joueur lors de l'élimination d'un joueur
    public void lancerPartie(){
    	int joueurCourant = 0;
    	Annonce annonceCourante;
    	// DEBUG ANNONCE
    	this.derniereAnnonce = new Annonce("surenchrir", 5, 5, "nadjim", "Perudo");
    	
//    	this.lancerTousLesDes();
		try {
			this.joueurs.get(0).lancerDes();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    	while(this.joueurs.size() != 0){
    		try {
				annonceCourante = this.joueurs.get(joueurCourant).FaireAnnonce();
				System.out.println(annonceCourante.getType());
				System.out.println(annonceCourante.getMessage());
				traiterAnnonce(annonceCourante);
				// décrementer joueurCourant si il perd ou gagne un dé !

			} catch (RemoteException e) {
				// TODO Améliorer la gestion des déconnexions brutales
				System.out.println("[!] Le client "+ joueurCourant +" est déconnecté !");
				this.enleverJoueurByIndex(joueurCourant);
				continue;
			}
    		
    		if(joueurCourant == (this.joueurs.size()-1)){
    			joueurCourant = 0;
    		}else{
    			joueurCourant++;
    		}
    	}
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
    	for(i = 0; (i < this.joueurs.size()) && (!this.joueurs.get(i).getPseudo().contentEquals(pseudo)); i++);
    	System.out.println(" Index joueur => "+ i);
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

	public void actionPerformed(ActionEvent arg0) {
		 System.out.println("[+] Time's up !");
		 System.out.println("[+] La partie va commencer !");
		 this.timer.stop();
		 this.lancerPartie();
	}

	public int getNbrJoueursMax() {
		return nbrJoueursMax;
	}

	public void setNbrJoueursMax(int nbrJoueursMax) {
		this.nbrJoueursMax = nbrJoueursMax;
	}

	public Vector<Client> getJoueurs() {
		return joueurs;
	}

	public void setJoueurs(Vector<Client> joueurs) {
		this.joueurs = joueurs;
	}

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setDerniereAnnonce(Annonce derniereAnnonce) {
		this.derniereAnnonce = derniereAnnonce;
	}
}
