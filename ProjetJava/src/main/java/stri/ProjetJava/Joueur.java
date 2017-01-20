package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class Joueur extends UnicastRemoteObject implements Client {
	private String pseudo;
	private String couleur;
	private Vector<Integer> des;
	 Serveur serveurImplem;

	 protected Joueur(Serveur serveurimplem) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	
		des = new Vector<Integer>();
		for(int i=0;i<des.size();i++){
			des.add(i, 0);
		}
		this.serveurImplem=serveurimplem;
		
	}

	/**
	 * 
	 **/
	private static final long serialVersionUID = 1L;

	public void AfficheAnnonce(Annonce a) throws RemoteException {
		// TODO Auto-generated method stub
		
	
		
		if(a.getType() == "menteur"){
			System.out.println(a.getPseudo() +" accuse " +serveurImplem.getDerniereAnnonce().getPseudo() + " de menteur !!");
			
		}
		
		//tout pile
		if(a.getType() == "toutpile"){
			System.out.println(a.getPseudo() +" à decalré un tout pile !! ");

		} 
		//sur enchere
		if(a.getType() == "encherir"){
			System.out.println(a.getPseudo()+"à Annoncer " + a.getNombre()+" Dés de "+a.getValeur());
		}
	}

	public Annonce FaireAnnonce(String nbreDesJoueurs) throws RemoteException {
		System.out.println(nbreDesJoueurs);
		Scanner sc=new Scanner(System.in);
		System.out.println("Merci de rentrer 1 pour sur encherir ");
		System.out.println("Merci de rentrer 2 pour menteur ");
		System.out.println("Merci de rentrer 3 pour tout pile ");
		String nombre =sc.nextLine();
	    
		Annonce a = null;
		if (nombre=="1"){
			int nb=(Integer) null;
			int val=(Integer) null;
			Boolean mauvaiseSaisie=true;
			while(mauvaiseSaisie){
				System.out.println("merci de rentrer le nombre de Dés puis la valeur:");
			    nombre =sc.nextLine();
			    nb=Integer.parseInt(nombre);
			    nombre =sc.nextLine();
			    val=Integer.parseInt(nombre);
			    //verification
			    if((serveurImplem.getDerniereAnnonce().getNombre()==nb && serveurImplem.getDerniereAnnonce().getValeur()< val)|| 
			    		serveurImplem.getDerniereAnnonce().getNombre()< nb){
			    		mauvaiseSaisie=false;
			    }
			    a = new Annonce("encherir",nb,val,getPseudo());
			    return a;
		   }
		}else if (nombre == "2"){
			 a = new Annonce("menteur"," ",getPseudo());
			return a;
		}
		else{
			 a = new Annonce("toutpile"," ",getPseudo());
			return a;
		}
		return a;
	}

	public void lancerDes() throws RemoteException {
		
		for(int i=0; i< this.getDes().size();i++ ){
			
			Random rand = new Random();
			this.getDes().add(i, rand.nextInt(6));
		
		}
		
	}
	public String getPseudo() {
		return pseudo;
	}
	
        public void setPseudo(String pseudo) {
        	this.pseudo = pseudo;
	}
	
    public String getCouleur() {
		return couleur;
	}
	public void setCouleur(String couleur) {
		this.couleur = couleur;
	}
	public Vector<Integer> getDes() {
		return des;
	}
	public void setDes(Vector<Integer> des) {
		this.des = des;
	}

	

	public void retirerDes() throws RemoteException {
		this.getDes().removeElementAt(0);
		
	}

	public void ajouterDes() throws RemoteException {
		
		getDes().addElement(0);
	}


	

	public Serveur getServeurImplem() {
		return serveurImplem;
	}

	public void setServeurImplem(Serveur serveurImplem) {
		this.serveurImplem = serveurImplem;
	}
	
	public static void main (String[] args) throws RemoteException, MalformedURLException, NotBoundException{
		
		LocateRegistry.createRegistry(1099);
		
		Serveur serveurimplem=(Serveur)Naming.lookup("rmi://10.0.0.1/Serveur");
		Client clientimplem=new Joueur(serveurimplem);
		
		Naming.rebind("rmi://10.0.0.2/nadjim",clientimplem);
		
		Boolean rep=serveurimplem.rejoindrePartie("nadjim", "Perudo");
		System.out.println("l'appel a renvoyé "+ rep);
	
		//clientimplem.FaireAnnonce("voila la chaine passé en param");
		
	}

	

}
