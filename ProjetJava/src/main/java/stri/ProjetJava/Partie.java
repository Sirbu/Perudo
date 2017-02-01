/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.Timer;

/**
 *
 * @author alexis
 */
public class Partie implements ActionListener {

    private String nom;
    private int nbrJoueursMax = 6;              // Pourra être modifié à l'occaz
    private String status;                      // WAIT || RUNNING || OVER
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
			while((joueurCourant < this.joueurs.size()-1) && (this.joueurs.get(joueurCourant).getPseudo() != pseudo)){
				joueurCourant++;
			}
			System.out.println(this.joueurs.get(joueurCourant).getPseudo());

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
		    this.timer = new Timer(10000, this);
		    this.timer.setInitialDelay(10000);
		    this.timer.start();
		}else if(this.joueurs.size() == nbrJoueursMax){
			this.actionPerformed(null);
		}

        return true;
    }

    // Retourne le nombre de dés totaux de la valeur donnée.
    public int compterDes(int valeur){
    	int cpt = 0;
    	Vector<Integer> des = new Vector<Integer>();
    	
    	for(int i = 0; i< this.joueurs.size(); i++){
    		// Pour chaque client on récupére ses dés...
    		try {
				des = this.joueurs.get(i).getDes();
			} catch (RemoteException e) {
				// TODO Il faudra gérer les déconnexion brutales
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
    public void broadcastAnnonce(Annonce a){
        for(int i = 0; i < this.getListeJoueurs().size(); i++){
        	try {
				this.getListeJoueurs().get(i).AfficheAnnonce(a);
			} catch (RemoteException e) {
				System.out.println("Client "+ i +" déconnecté !");
				this.enleverJoueurByIndex(i);
			}
        }
    }

    // traiterAnnonce exécute le traitement associé au type de l'annonce.
    // Si l'annonce termine une manche, traiterAnnonce retourne le pseudo
    // du joueur à reprendre la main. Sinon elle retourne une chaine vide
    String traiterAnnonce(Annonce annonceCourante) throws RemoteException{
		String prochainJoueur = "";
    	
    	// On doit d'abord diffuser l'annonce courante
	    broadcastAnnonce(annonceCourante);

	    // Dans le cas d'une surenchère, on ne fait rien d'autre
	    // que de diffuser l'annonce. Dans le cas contraire on
	    // peut commencer à compter les dés de la dernière annonce
	    if(annonceCourante.getType().contentEquals("surencherir")){ 
	    	// Si l'annonce est une simple surenchère, on l'enregistre comme dernière annonce
		    this.derniereAnnonce = annonceCourante;
	    }else{
	       	Annonce derniereAnnonce = this.getDerniereAnnonce();
	        Annonce annonceNotif = new Annonce("info", "", "Serveur");

	        int valeurAnnonce = derniereAnnonce.getValeur();
	        int nbrDesAnnonce = derniereAnnonce.getNombre();
	        int nbrDesReel = 0;

	        // TODO: Vérifier le nom de la partie
	        nbrDesReel = this.compterDes(valeurAnnonce);
	        
	        System.out.println("Il y a en tout "+ nbrDesReel + " de "+ valeurAnnonce);
	        
	        if(annonceCourante.getType().contentEquals("menteur")){
		        if(nbrDesReel < nbrDesAnnonce){
		        	// Là celui qui vien de dénoncer un mensonge a eu raison !
		        	annonceNotif.setMessage("Hé oui ! "+derniereAnnonce.getPseudo()+" était un sale menteur !\nJ'aime pas les menteurs et les fils de pute.");
		        	this.getJoueurByPseudo(derniereAnnonce.getPseudo()).retirerDes();
		        	prochainJoueur = derniereAnnonce.getPseudo();
		        }else{
		        	// Ici celui qui dénnonce le mensonge a tort
		        	annonceNotif.setMessage("Loupé... "+ derniereAnnonce.getPseudo()+" avait raison !");
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        	prochainJoueur = annonceCourante.getPseudo();
		        }
		    }else if(annonceCourante.getType().contentEquals("toutpile")){
		        if(nbrDesReel == nbrDesAnnonce){
		        	annonceNotif.setMessage(("Yeah ! "+annonceCourante.getPseudo()+" a gagné un dé !"));
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).ajouterDes();
		        }else{
		        	annonceNotif.setMessage("Ouch..." + annonceCourante.getPseudo() + " a perdu un dé !");
		        	this.getJoueurByPseudo(annonceCourante.getPseudo()).retirerDes();
		        }
		        prochainJoueur = annonceCourante.getPseudo();
		    }
	        broadcastAnnonce(annonceNotif);
	    }	  
	    return prochainJoueur;
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
    public void lancerPartie(int joueur){
    	String joueurReprise = "";  // Le nom du joueur qui reprend la main après la fin d'une manche  
    	int joueurCourant = joueur;
    	Annonce annonceCourante;
    	
    	System.out.println("[+] Début d'une manche !");
    	this.derniereAnnonce = null;
    	this.broadcastAnnonce(new Annonce("info", "Une nouvelle manche commence !", "Serveur"));
    	
    	this.lancerTousLesDes();
    	
		try {
			this.joueurs.get(0).lancerDes();
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}

    	while((this.joueurs.size() != 0) && !(joueurReprise.contentEquals(""))){
    		try {
				annonceCourante = this.joueurs.get(joueurCourant).FaireAnnonce();

				joueurReprise = traiterAnnonce(annonceCourante);
				if(!joueurReprise.contentEquals("")){
					if(this.joueurs.size() == 1){
						System.out.println("[+] Il ne reste plus qu'un joueur.");
						System.out.println("[+] C'est la victoire pour "+ this.joueurs.get(0).getPseudo()+" !");
					}
					
					// Permet de retrouver l'indice du joueur dont le pseudo est joueurReprise
					int i = 0;
					while( (i < this.joueurs.size()) && !this.joueurs.get(i).getPseudo().contentEquals(joueurReprise) ){
						i++;
					}
					this.lancerPartie(i);
				}
				
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
    
    public void setDerniereAnnonce(Annonce a){
    	this.derniereAnnonce = a;
    }
    
    public void actionPerformed(ActionEvent arg0) {
             System.out.println("[+] Time's up !");
             if(this.joueurs.size() == 1){
            	 System.out.println("[+] Mais personne n'est là pour jouer...");
            	 System.out.println("[+] Relance du timer !");
            	 this.timer.start();
             }else{
            	 System.out.println("[+] La partie va commencer !");
                 this.timer.stop();
                 this.status = "RUNNNING";
                 this.lancerPartie(0); 
             }
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
}
