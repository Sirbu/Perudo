package stri.ProjetJava;

import java.io.Serializable;
import java.rmi.RemoteException;

public class Annonce implements Serializable {
	/**
	 * 
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
    	
    	int nbserv = serveurImplem.getDerniereAnnonce(this.getPartie()).getNombre();
    	int valserv=serveurImplem.getDerniereAnnonce(this.getPartie()).getValeur();
    	return(( nbserv == this.getNombre() && valserv< this.getValeur() ) || nbserv < this.getNombre() ) &&
    			(this.getValeur()>1 && this.getValeur()<=6);
    	
    }
    
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
