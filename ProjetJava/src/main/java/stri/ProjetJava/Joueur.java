package stri.ProjetJava;

import java.net.MalformedURLException;
import java.nio.channels.ClosedByInterruptException;
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
    private String partie;
    private String statut;
	private Vector<Integer> des;
	private Serveur serveurImplem;

    protected Joueur(Serveur serveurimplem) throws RemoteException, MalformedURLException, NotBoundException {
        super();

        this.serveurImplem = serveurimplem;
        des = new Vector<Integer>();

        //au debut de la partie tout le monde a 6 des initilaisé à 0
        for(int i=0;i < 6;i++){
        	des.add(i, 0);
        }
	}

    private void menuSaisieAnnonce(){
        System.out.println("-=[ Votre tour ]=-");
        System.out.println("  1) Miser");
        System.out.println("  2) Dire menteur");
        System.out.println("  3) Dire tout pile");
    }

	public void AfficheAnnonce(Annonce a) throws RemoteException {

		if(a.getType().contentEquals("menteur")){
            // menteur
    		System.out.println("[+] " + a.getPseudo() +" accuse " +serveurImplem.getDerniereAnnonce(this.partie).getPseudo() + " de menteur !!");
		}else if(a.getType().contentEquals("toutpile")){
            //tout pile
            System.out.println("[+] " + a.getPseudo() +" à decalré un tout pile !! ");
		}else if(a.getType().contentEquals("surencherir")){
    		//sur enchere
            System.out.println("[+] " + a.getPseudo()+" a annoncé " + a.getNombre()+" Dés de "+a.getValeur());
		}else if(a.getType().contentEquals("info")){
            // annonce de type info
            System.out.println("[INFO] " + a.getMessage());
		}else {
			System.out.println("[*] You lost THE GAME");
		}
	}

	public Annonce FaireAnnonce() throws RemoteException {
        int nbrDesTotal = 0;

		Annonce a;
		String choix = "";
        Scanner sc = new Scanner(System.in);

        int nbrDes = 0;
        int valDes = 0;
        Boolean bonneSaisie = false;

		// recupére le nombre de des de chaque joueur pour l'aider a ajuster son annonce
		Vector<Client> player =this.serveurImplem.getJoueursConnectes(this.partie);

		for(int i=0;i< player.size();i++){
			System.out.println("[+] Le joueur "+ player.elementAt(i).getPseudo()
                + " a " + player.elementAt(i).getDes().size() +" dés");

            nbrDesTotal += player.elementAt(i).getDes().size();
		}

		System.out.println("[+] Le nombre total de dés est " + nbrDesTotal);

        //on affiche la derniere annonce
		if(this.serveurImplem.getDerniereAnnonce(this.partie) != null){
			System.out.println("[+] " + this.serveurImplem.getDerniereAnnonce(this.getPartie()).getPseudo()
                + " a dit " + this.serveurImplem.getDerniereAnnonce(this.getPartie()).getNombre()
					+ " dés de "+this.serveurImplem.getDerniereAnnonce(this.getPartie()).getValeur());
		}

		//on affiche le jeu du joueur
		System.out.println("[+] Votre jeu est le suivant : ");

		for(int i = 0; i < this.getDes().size(); i++){
			if(this.getDes().elementAt(i).intValue() == 1){
				System.out.print("~(°▿°)~");
			}else{
				System.out.print(this.getDes().elementAt(i));
			}

            System.out.print(" - ");
		}

        // Boucle permettant de vérifier le choix de l'utilisateur
		do{
            a = null;
            System.out.println("");
            // Affichage du menu donnant le choix à l'utilisateur
            menuSaisieAnnonce();

			choix = sc.nextLine();

            // miser
			if (choix.contentEquals("1")){
				while(!bonneSaisie){
					System.out.println("[+] Merci de rentrer le nombre de dés puis la valeur :");
				    try{
                        System.out.print("(Nombre dés) -> ");
				    	nbrDes = Integer.parseInt(sc.nextLine());
                        System.out.print("(Valeur dés) -> ");
				    	valDes = Integer.parseInt(sc.nextLine());
				    }catch(NumberFormatException e){
				    	continue;
				    }

				    // vérification de la validité de la saisie
				    a = new Annonce("surencherir", nbrDes, valDes, this.getPseudo(), this.partie);
				    bonneSaisie = a.verifAnnonce(serveurImplem);
				}

			}else if(choix.contentEquals("2")){  // dire menteur
                // On ne peut dire menteur que si il existe une mise précédente
				if(this.serveurImplem.getDerniereAnnonce(this.partie) == null){
					System.out.println("[!] Impossible, vous êtes le premier joueur !");
				}else{
					a = new Annonce("menteur","",this.getPseudo());
				}

			}else if(choix.contentEquals("3")){  // Dire tout pile
                // on ne peut dire tout pile que si il existe une mise précédente
				if(this.serveurImplem.getDerniereAnnonce(this.partie) == null){
					System.out.println("[!] Impossible, vous êtes le premier joueur !");
				}else{
					a = new Annonce("toutpile","",this.getPseudo());
				}
			}else {
					System.out.println("[!] Vous êtes stupide, ce n'est pas un chiffre valide...");
			}

		}while((!choix.contentEquals("1") && !choix.contentEquals("2") && !choix.contentEquals("3")) || a == null);

        return a;
    }

	public void lancerDes() throws RemoteException {
		Random rand = new Random();

		for(int i = 0; i< this.getDes().size(); i++){
			this.getDes().setElementAt(rand.nextInt(5) + 1, i);
		}
	}


	public void retirerDes() throws RemoteException {
		if(this.getDes().size() > 0){
			this.getDes().removeElementAt(0);
		}
	}

	public void ajouterDes() throws RemoteException {

		getDes().addElement(0);
	}

    // Getters & Setters
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
        Scanner sc = new Scanner(System.in);
        String choix = "";

        Serveur serveurimplem = null;
        Joueur clientimplem = null;
        Vector<String> listPartie = new Vector<String>();

        String nomJ = "";
        String partie = "";

        Boolean rep = true;

        System.out.println("***************************** PERUDO *****************************");
        System.out.println("Merci d'indiquer votre choix : ");
        System.out.println("   1) quitter");
        System.out.println("   2) Rejoindre ou Créer une Partie");
        System.out.print("-> ");

        choix = sc.nextLine();

        if(choix.contentEquals("1")){
            System.out.println("************************* Merci de votre visite *************************");
            System.exit(0);
        }else{
            serveurimplem = (Serveur)Naming.lookup("rmi://127.0.0.1/Annuaire");

            clientimplem = new Joueur(serveurimplem);

            listPartie = serveurimplem.getListePartie();

            System.out.println("[+] Merci de rentrer un pseudo :");
            System.out.print("-> ");

            nomJ = sc.nextLine();
            clientimplem.setPseudo(nomJ);

            System.out.println("Voici la liste des Parties, Rejoignez-en une ou saisissez un nom pour en créer une !!");
            if(listPartie.size() == 0){
            	System.out.println(" Aucune partie disponible !");
            }else{
                for(int i = 0; i < listPartie.size(); i++){
                    System.out.println(" - " + listPartie.get(i));
                }
            }

            System.out.print("-> ");
            partie = sc.nextLine();
            clientimplem.setPartie(partie);

            rep = serveurimplem.rejoindrePartie(clientimplem, clientimplem.getPartie());
            
            if (!rep){
            	// La partie n'existe pas, donc on doit la créer et la déclarer à la registry
                System.out.println("[*] La partie n'existe pas !");
                System.out.println("[+] Création et hébergement de la partie... ");
                clientimplem.serveurImplem.ajouterPartie(partie);
                // SI la partie n'existe pas on doit créer un serveur pour l'héberger
                clientimplem.serveurImplem = new ServeurImplem();
                Naming.rebind("rmi://127.0.0.1/"+partie, clientimplem.serveurImplem);
            }else{
            	// la partie existe, on doit la rejoindre
            	clientimplem.serveurImplem = (Serveur) Naming.lookup("rmi://127.0.0.1/"+partie);
            }

            // Ici on rejoint la partie !
            clientimplem.serveurImplem.rejoindrePartie(clientimplem, partie);
        }
	}
}
