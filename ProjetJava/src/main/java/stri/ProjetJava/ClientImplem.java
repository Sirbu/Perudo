package stri.ProjetJava;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

public class ClientImplem extends UnicastRemoteObject implements Client {
	Joueur joueur;
	 Serveur serveurImplem;

	protected ClientImplem() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void AfficheAnnonce(Annonce a) throws RemoteException {
		// TODO Auto-generated method stub
		 
		//menteur 
		if(a.getType() == "menteur"){
			System.out.println(a.getJoueur() +" accuse " +serveurImplem.getDerniereAnnonce().getJoueur() + " de menteur !!");
			
		}
		
		//tout pile
		if(a.getType() == "toutpile"){
			System.out.println(a.getJoueur() +" à decalré un tout pile !! ");

		} 
		//sur enchere
		if(a.getType() == "encherir"){
			System.out.println(a.getJoueur()+"à Annoncer " + a.getNombre()+" Dés de "+a.getValeur());
		}
	}

	public Annonce FaireAnnonce(String nbreDesJoueurs) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println(nbreDesJoueurs);
		Scanner sc=new Scanner(System.in);
		System.out.println("Merci de rentrer 1 pour sur encherir ");
		System.out.println("Merci de rentrer 2 pour menteur ");
		System.out.println("Merci de rentrer 3 pour tout pile ");
		String nombre =sc.nextLine();
	    
		if (nombre=="1"){
			System.out.println("merci de rentrer le nombre de Dés puis la valeur:");
		    nombre =sc.nextLine();
		    int nb=Integer.parseInt(nombre);
		    nombre =sc.nextLine();
		    int val=Integer.parseInt(nombre);
		    Annonce a = new Annonce("encherir",nb,val," ",joueur.getPseudo());
		    return a;
		}else if(nombre=="2"){
			Annonce a = new Annonce("menteur"," ",joueur.getPseudo());
			return a;
		}
		else{
			Annonce a = new Annonce("toutpile"," ",joueur.getPseudo());
			return a;
		}
		
	}

	public void lancerDes() throws RemoteException {
		
		for(int i=1;i<=this.getJoueur().getDes().size();i++ ){
			
			Random rand = new Random();
			this.getJoueur().getDes().add(rand.nextInt(6));
		
		}
		
	}

	public Vector<Integer> getDes() throws RemoteException {
		
		return this.getJoueur().getDes() ;
	}

	public Vector<Integer> retirerDes() throws RemoteException {
		this.getDes().remove(this.getDes().size());
		return null;
	}

	public Vector<Integer> ajouterDes() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	public Joueur getJoueur() {
		return joueur;
	}

	public void setJoueur(Joueur joueur) {
		this.joueur = joueur;
	}

	public Serveur getServeurImplem() {
		return serveurImplem;
	}

	public void setServeurImplem(Serveur serveurImplem) {
		this.serveurImplem = serveurImplem;
	}

}
