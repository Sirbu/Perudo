/*

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Vector;
import javax.swing.Timer;
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.Timer;

/**
 *
 * @author alexis
 */
public class Partie implements Runnable, ActionListener, Serializable{

    /**
	 * Generated UID
	 */
	private static final long serialVersionUID = 7389133810306179749L;
	
	private String nom;
    private String status;              // WAIT || RUNNING || OVER
    private int nbrJoueursMax = 6;      // Pourra être modifié à l'occaz
    private Annonce derniereAnnonce;    // La derniereAnnonce émise par un joueur
    private Vector<Client> joueurs;    	// Contient les joueurs

    private Timer timer;    // Permet de lancer la partie automatiquement

    public Partie(String nom){
        this.nom = nom;
        this.status = "WAIT";
        this.joueurs = new Vector<Client>();
    }

    public boolean checkFinPartie(){

    	if(this.joueurs.size() == 1){
    		System.out.println("[+] Plus qu'un seul joueur dans la partie.\n[+] " + this.joueurs.get(0) + " est le vainqueur !");
    		return true;
    	}
    	return false;
    }

    public synchronized void enleverJoueurByPseudo(String pseudo){
    	int joueurCourant = 0;
    	try {
			while((joueurCourant < this.joueurs.size()) && (this.joueurs.get(joueurCourant).getPseudo() != pseudo)){
				joueurCourant++;
			}
			System.out.println("Il va partir : "+this.joueurs.get(joueurCourant).getPseudo());

			// -1 car la boucle while s'exécute uen fois de trop.
			this.joueurs.remove(joueurCourant);

    	} catch (RemoteException e) {
			System.out.println("Le client "+ joueurCourant + " est déconnecté !");
			this.joueurs.remove(joueurCourant);
		}
    }

    public synchronized void enleverJoueurByIndex(int i){
    	this.joueurs.remove(i);
    }

    public synchronized boolean ajouterJoueur(Client c){

        if(this.joueurs.size() == nbrJoueursMax){
			return false;
		}
		this.joueurs.add(c);

		if(this.joueurs.size() == 1){
		    this.timer = new Timer(1000, this);
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
				System.out.println("[!] Client " + i + " brutalement déconnecté !");
				this.enleverJoueurByIndex(i);
			}
        }
    }

    // traiterAnnonce exécute le traitement associé au type de l'annonce.
    // Si l'annonce termine une manche, traiterAnnonce retourne le pseudo
    // du joueur à reprendre la main. Sinon elle retourne une chaine vide
    String traiterAnnonce(Annonce annonceCourante) throws RemoteException{
		String prochainJoueur = "none";

        Annonce derniereAnnonce = null;
        Annonce annonceNotif = null;

        int valeurAnnonce = 0;
        int nbrDesAnnonce = 0;
        int nbrDesReel = 0;

    	// On doit d'abord diffuser l'annonce courante
	    broadcastAnnonce(annonceCourante);

	    // Dans le cas d'une surenchère, on ne fait rien d'autre
	    // que de diffuser l'annonce. Dans le cas contraire on
	    // peut commencer à compter les dés de la dernière annonce
	    if(annonceCourante.getType().contentEquals("surencherir")){
	    	// Si l'annonce est une simple surenchère, on l'enregistre comme dernière annonce
		    this.derniereAnnonce = annonceCourante;
	    }else{
	       	derniereAnnonce = this.getDerniereAnnonce();
	        annonceNotif = new Annonce("info", "", "Serveur");

	        valeurAnnonce = derniereAnnonce.getValeur();
	        nbrDesAnnonce = derniereAnnonce.getNombre();
	        nbrDesReel = 0;

	        // TODO: Vérifier le nom de la partie
	        nbrDesReel = this.compterDes(valeurAnnonce);

	        System.out.println("[+] Nombre réel de dés : " + nbrDesReel + " de "+ valeurAnnonce);

	        if(annonceCourante.getType().contentEquals("menteur")){
		        if(nbrDesReel < nbrDesAnnonce){
		        	// Là celui qui vien de dénoncer un mensonge a eu raison !
		        	annonceNotif.setMessage("Hé oui ! "+derniereAnnonce.getPseudo()
                        + " était un sale menteur !\nJ'aime pas les menteurs et les fils de pute.");
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

    // Bon alors attention cette méthode est un peu poilue...
    public void run(){
    	int joueurCourant = 0;
    	int indexJoueurReprise = 0;
    	String joueurReprise = "none";
    	Annonce annonceCourante;

    	// boucle des manches
    	// tant qu'il reste plus d'un joueur, il reste des manches à jouer
    	while((this.joueurs.size() > 1)){

    	 	System.out.println("[+] Début d'une manche !");
        	this.derniereAnnonce = null;

        	joueurReprise = "none";

        	this.broadcastAnnonce(new Annonce("info", "Une nouvelle manche commence !", "Serveur"));
        	this.lancerTousLesDes();

        	while(joueurReprise.contentEquals("none")){
        		try {
    				annonceCourante = this.joueurs.get(joueurCourant).FaireAnnonce();

    				joueurReprise = traiterAnnonce(annonceCourante);
    			} catch (RemoteException e) {
    				// TODO Améliorer la gestion des déconnexions brutales
    				System.out.println("[!] Le client "+ joueurCourant +" est déconnecté !");

    				if(this.joueurs.size() >= joueurCourant){
    					System.out.println("[+] Il est déjà parti...");
    				}else{
    					this.enleverJoueurByIndex(joueurCourant);
    				}
    				continue;
    			}

        		joueurCourant++;
        		if(joueurCourant >= (this.joueurs.size())){
        			joueurCourant = 0;
        		}
        	}

        	// ici, le joueurReprise != "none"
        	// Permet de retrouver l'indice du joueur dont le pseudo est joueurReprise
			// TODO : a mettre dans une fonction
        	if(!joueurReprise.contentEquals("none")){
        		try{
					indexJoueurReprise = getIndexByPseudo(joueurReprise);

					// offset to test
					System.out.println("Joueur de merde : "+ this.joueurs.get(indexJoueurReprise).getPseudo()
                        + "index : " + indexJoueurReprise);

					System.out.println("Manche terminée : " + this.joueurs.get(indexJoueurReprise).getPseudo()
                        + " doit recommencer !");

					// déterminer si le joueur doit quitter la partie.
					if(this.getJoueurByPseudo(joueurReprise).getDes().size() == 0){
						// on prévient tout le monde de la défaite du joueur
						System.out.println("[+] Le joueur "+ joueurReprise +" n'a plus de dés !");
						Annonce a = new Annonce("info", "Le joueur " + joueurReprise + " a perdu !", "Serveur");
						broadcastAnnonce(a);

						// on prévient le joueur avec une annonce particulière
						a.setType("defaite");
						this.getJoueurByPseudo(joueurReprise).AfficheAnnonce(a);

						this.enleverJoueurByIndex(indexJoueurReprise);
						System.out.println("AFTER => Taille joueurs = " + this.joueurs.size());

						// Comme le joueur qui devait reprendre a perdu, on rend
						// la main au joueur suivant.
						// joueurCourant = indexJoueurReprise+1;
					}

					joueurCourant = getIndexByPseudo(joueurReprise);

				}catch(RemoteException e){
					System.out.println("RemoteException");
					e.printStackTrace();
					System.out.println("Taille de merde : " + this.joueurs.size());
				}
			}else{
				joueurCourant++;
			}

    		if(joueurCourant >= (this.joueurs.size())){
    			joueurCourant = 0;
    		}
    	}

		try {
			System.out.println("[+] Il ne reste plus qu'un joueur.");
			System.out.println("[+] C'est la victoire pour "+ this.joueurs.get(0).getPseudo()+" !");

			Annonce a = new Annonce("info", "Vous avez gagné !", "Serveur");
			this.joueurs.get(0).AfficheAnnonce(a);

			// on informe le joueur que la partie est terminée
			a = new Annonce("gameover", "gameover", "Serveur");
			this.joueurs.get(0).AfficheAnnonce(a);
			this.enleverJoueurByIndex(0);

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Méthode déclenchée lors de la fin du timer !
    // En vérité elle n'est plus utilisée depuis que le mode multi-parties
    // mais on sait pas jamais, ça pourrait servir ;)
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
            this.run();
        }
    }

    // getters & setters
    public int getIndexByPseudo(String pseudo) throws RemoteException{
    	int i = 0;
		while( (i < this.joueurs.size()-1) && !this.joueurs.get(i).getPseudo().contentEquals(pseudo) ){
			i++;
		}
		return i;
    }

    public String getStatus(){
        return this.status;
    }

    public String getNom(){
    	return this.nom;
    }

    public Client getJoueurByPseudo(String pseudo) throws RemoteException{
    	int i = 0;
    	for(i = 0; (i < this.joueurs.size()) && (!this.joueurs.get(i).getPseudo().contentEquals(pseudo)); i++);
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
