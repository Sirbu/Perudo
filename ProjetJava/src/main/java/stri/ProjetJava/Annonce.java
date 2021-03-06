package stri.ProjetJava;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Annonce implements Serializable {
	/**
	 *     Generated serialization ID
	 */
	private static final long serialVersionUID = 9099397892249113227L;


	private String pseudo;
	private String type;
	private int nombre;
	private int valeur;
	private String message;
	private String partie;

    Annonce(String type, int nb,int val,String joueur, String partie){

    	setType(type);
    	setNombre(nb);
    	setValeur(val);
    	setMessage(message);
    	setPseudo(joueur);
    	setPartie(partie);

    }

     Annonce(String type,String message,String joueur){
    	setType(type);
    	setMessage(message);
    	setPseudo(joueur);
    }

    public boolean verifAnnonce(Serveur serveurImplem) throws RemoteException{
    	// on verifie bien que la mise respecte les condition du jeu et que les valeurs annoncées soit raisonnable
    	//càd ne pas annocer 2 des de 45 ...par exemple
    	if(serveurImplem.getDerniereAnnonce(this.getPartie()) != null){
            // nbServ est le nombre de dés de la dernière annonce.
            int nbserv = serveurImplem.getDerniereAnnonce(this.getPartie()).getNombre();
            // valserv est la valeur de dés de la dernière annonce.
        	int valserv=serveurImplem.getDerniereAnnonce(this.getPartie()).getValeur();

            // La condition vérifie que l'annonce que l'on vérifie (this) est bien conforme.
            // Elle doit avoir un nomrbe de dés au supérieur ou égal à la dernière annonce,
            // sauf si la valeur augmente.
        	return(( nbserv == this.getNombre() && valserv< this.getValeur() ) || nbserv < this.getNombre() ) &&
        			(this.getValeur()>1 && this.getValeur()<=6);

    	}else{
            // ici la vérification ne regarde que si les valeurs sont dans les valeurs possibles
            // d'un jeu de dés.
    		return (this.getValeur()<=6 && this.getValeur()>1);
    	}

    }

    // Getters & Setters
    public void setPseudo(String pseudo) {
        this.pseudo=pseudo;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	public int getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public void setPartie(String p){
		this.partie = p;
	}
	public String getPartie(){
		return this.partie;
	}
}
