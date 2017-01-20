package stri.ProjetJava;

public class Annonce {
	private String joueur;
	private String type;
	private int nombre;
	private int valeur;
	private String message;
	
    Annonce(String type, int nb,int val,String joueur){
    	
    	setType(type);
    	setNombre(nb);
    	setValeur(val);
    	setMessage(message);
    	setJoueur(joueur);
    	
    }

     Annonce(String type,String message,String joueur){
    	setType(type);
    	setMessage(message);
    	setJoueur(joueur);
    }
    
    public void setJoueur(String j) {
        this.joueur=j;
    }
    
    public String getJoueur() {
        return joueur;
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
}
