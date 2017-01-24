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
	/**
	 *  identifiant serialization généré
	 */
	private static final long serialVersionUID = -8485613108316528072L;
	
	
	private String pseudo;
	private String couleur;
	private Vector<Integer> des;
	private Serveur serveurImplem;
	private String partie; 

	 protected Joueur(Serveur serveurimplem) throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	
		des = new Vector<Integer>();
		for(int i=0;i<des.size();i++){
			des.add(i, 0);
		}
		this.serveurImplem=serveurimplem;
		
	}



	public void AfficheAnnonce(Annonce a) throws RemoteException {
					
		if(a.getType().contentEquals("menteur")){
			System.out.println(a.getPseudo() +" accuse " +serveurImplem.getDerniereAnnonce("perudo").getPseudo() + " de menteur !!");
			
		}
		
		//tout pile
		if(a.getType().contentEquals("toutpile")){
			System.out.println(a.getPseudo() +" à decalré un tout pile !! ");

		} 
		//sur enchere
		if(a.getType().contentEquals("surencherir")){
			System.out.println(a.getPseudo()+"à Annoncer " + a.getNombre()+" Dés de "+a.getValeur());
		}
		if(a.getType().contentEquals("info")){
			System.out.println("infoooooooo");
		}
	}

	public Annonce FaireAnnonce() throws RemoteException {
		Vector<Client> player =this.serveurImplem.getJoueursConnectes(this.partie);
		for(int i=1;i<=player.size();i++){
			System.out.println("le joueur "+ player.elementAt(i).getPseudo() + " a "
			+player.elementAt(i).getDes().size() +" Dés");
		}
		
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
			    if((serveurImplem.getDerniereAnnonce("perudo").getNombre()==nb && serveurImplem.getDerniereAnnonce("perudo").getValeur()< val)|| 
			    		serveurImplem.getDerniereAnnonce("perudo").getNombre()< nb){
			    		mauvaiseSaisie=false;
			    }
			    a = new Annonce("encherir",nb,val,getPseudo(), "perudo");
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
	
	public String getPartie() {
		return partie;
	}



	public void setPartie(String partie) {
		this.partie = partie;
	}
	
	public static void main (String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException{
		
		LocateRegistry.createRegistry(1099);
		
		Serveur serveurimplem=(Serveur)Naming.lookup("rmi://10.0.0.1/Serveur");
		Joueur clientimplem=new Joueur(serveurimplem);
		
		//Naming.rebind("rmi://10.0.0.2/nadjim",clientimplem);
		clientimplem.setPseudo("nadjim");
		Thread.sleep(5000);
		Boolean rep=serveurimplem.rejoindrePartie(clientimplem, "Perudo");
		System.out.println("l'appel a renvoyé "+ rep);
	
		//clientimplem.FaireAnnonce("voila la chaine passé en param");
		
	}

}
