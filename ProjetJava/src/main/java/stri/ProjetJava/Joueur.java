package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
	private String statut;

	 protected Joueur(Serveur serveurimplem) throws RemoteException {
		super();
		
		this.serveurImplem=serveurimplem;
		des = new Vector<Integer>();
		//au debut de la partie tout le monde a 6 des initilaisé à 0
		for(int i=0;i<1;i++){
			des.add(i, 0);
		}
		
		
	}



	public void AfficheAnnonce(Annonce a) throws RemoteException {
		// menteur		
		if(a.getType().contentEquals("menteur")){
			System.out.println(a.getPseudo() +" accuse " +serveurImplem.getDerniereAnnonce(this.partie).getPseudo() + " de menteur !!");
		}
		
		//tout pile
		if(a.getType().contentEquals("toutpile")){
			System.out.println(a.getPseudo() +" à decalré un tout pile !! ");

		}
		
		//sur enchere
		if(a.getType().contentEquals("surencherir")){
			System.out.println(a.getPseudo()+" à Annoncer " + a.getNombre()+" Dés de "+a.getValeur());
		}
		
		// annonce de type info
		if(a.getType().contentEquals("info")){
			System.out.println(a.getMessage());
		}
	}

	public Annonce FaireAnnonce() throws RemoteException {	
		int tot=0;
		// recupére le nombre de des de chaque joueur pour l'aider a ajuster son annonce
		Vector<Client> player =this.serveurImplem.getJoueursConnectes(this.partie);
		for(int i=0;i< player.size();i++){
			System.out.println("le joueur "+ player.elementAt(i).getPseudo() + " a "
			+player.elementAt(i).getDes().size() +" Dés");
			tot+=player.elementAt(i).getDes().size();
		}
		
		System.out.println("le nombre total de dés dans le jeu est de : "+tot);
		//on affiche la derniere annonce
		if(this.serveurImplem.getDerniereAnnonce(this.partie) != null){
			System.out.println("la denière mise : "+this.serveurImplem.getDerniereAnnonce(this.getPartie()).getPseudo()+" a dit "+this.serveurImplem.getDerniereAnnonce(this.getPartie()).getNombre()
					+" Dés de "+this.serveurImplem.getDerniereAnnonce(this.getPartie()).getValeur());
		}
		//on affiche le jeu du joueur
		System.out.println("votre jeu est le suivant");
		
		for(int i=0;i <this.getDes().size();i++){
			if(this.getDes().elementAt(i).intValue()==1){
				System.out.print(" |Perudo");
			}else{
				System.out.print(" |"+this.getDes().elementAt(i));
			}
		}
		System.out.println(" ");

		Scanner sc=new Scanner(System.in);
	    
		Annonce a;
		String nombre = "";
		
		do{
			a = null;
			System.out.println("Merci de rentrer 1 pour sur encherir ");
			System.out.println("Merci de rentrer 2 pour menteur ");
			System.out.println("Merci de rentrer 3 pour tout pile ");
			
			nombre =sc.nextLine();

			if (nombre.contentEquals("1")){
				int nb=0;
				int val=0;
				Boolean bonneSaisie=false;
		
				while(!bonneSaisie){
					System.out.println("merci de rentrer le nombre de Dés puis la valeur:");
				    try{
				    	nb = Integer.parseInt(sc.nextLine());
				    	val = Integer.parseInt(sc.nextLine());
				    }catch(NumberFormatException e){
				    	continue;
				    }
				    //verification de l validité de la saisie
				    //3 des de 4 derniere annonce
				    // 2 des de 4 moi
				    a = new Annonce("surencherir",nb,val,this.getPseudo(), "perudo");
				    bonneSaisie=a.verifAnnonce(serveurImplem);    
				 // si le joueur est le premier a jouer dans cette manche il ne peut dire toutpile ni menteur
				}
			}else if(nombre.contentEquals("2")){
				if(this.serveurImplem.getDerniereAnnonce(this.partie) == null){
					System.out.println("Impossible, vous êtes le premier joueur !");
				}else{
					a = new Annonce("menteur"," ",this.getPseudo());
				}
			}else if(nombre.contentEquals("3")){
				if(this.serveurImplem.getDerniereAnnonce(this.partie) == null){
					System.out.println("Impossible, vous êtes le premier joueur !");
				}else{
					a = new Annonce("toutpile"," ",this.getPseudo());	
				}
			}else {
					System.out.println("Vous êtes stupide, ce n'est pas un chiffre valide...");
			}	
			
		}while((!nombre.contentEquals("1") && !nombre.contentEquals("2") && !nombre.contentEquals("3")) || a == null);
		
		return a;
	}

	public void lancerDes() throws RemoteException {
		Random rand = new Random();
		for(int i=0; i< this.getDes().size();i++ ){
			this.getDes().setElementAt(rand.nextInt(5)+1,i);	
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
		if(this.getDes().size() > 0){
			this.getDes().removeElementAt(0);
		}
		
		if(this.des.size() == 0){
			// ici on a plus de dés donc on doit quitter la partie
			this.serveurImplem.quitterPartie(this.pseudo, this.partie);
		}
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
	public String getStatut() {
		return statut;
	}



	public void setStatut(String statut) {
		this.statut = statut;
	}


	public void setPartie(String partie) {
		this.partie = partie;
	}
	
	public static void main (String[] args) throws RemoteException, MalformedURLException, NotBoundException, InterruptedException{
		
		System.out.println("*****************************PERUDO**************************************");
		System.out.println("1: quitter");
		System.out.println("2: Rejoindre une Partie");
		System.out.println("merci d'indiquer votre choix : ");
		Scanner sc=new Scanner(System.in);
		String nombre =sc.nextLine();
	    if(nombre.contentEquals("1")){
	    	
	    	System.out.println("************************MERCI DE VOTRE VISITE***********************");
	    	System.exit(0);
	    }else{
	    	//LocateRegistry.createRegistry(1099);
			Serveur serveurimplem=(Serveur)Naming.lookup("rmi://10.0.0.1/Serveur");
			Joueur clientimplem=new Joueur(serveurimplem);

			clientimplem.setPseudo("Nadjim");

			clientimplem.setPartie("Perudo");
			
			Boolean rep=serveurimplem.rejoindrePartie(clientimplem, clientimplem.getPartie());
			System.out.println("l'appel a renvoyé "+ rep);
	    }
	
		//clientimplem.FaireAnnonce("voila la chaine passé en param");
		
	}

}
