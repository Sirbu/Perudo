package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Annuaire extends UnicastRemoteObject implements Serveur {

	/**	
	 *  Generated serial ID
	 */
	private static final long serialVersionUID = 3056871158025906817L;

	// Contient le nom de l'objet RMI avec en clé le nom de la partie.
	private HashMap<String, String> parties;
	
	protected Annuaire() throws RemoteException {
		super();

		this.parties = new HashMap<String, String>();
	}

	public static void main(String[] args) throws RemoteException {
        try{
            Annuaire srv = new Annuaire();

            // initialise la registry
            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://127.0.0.1/Annuaire", srv);
            System.out.println("[+] Serveur déclaré");

        }catch(MalformedURLException e){
            e.printStackTrace();
        }
	}
	

	@Override
	public void quitterPartie(String pseudo, String partie) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Annonce getDerniereAnnonce(String partie) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Client> getJoueursConnectes(String partie) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<String> getListePartie() throws RemoteException {
		Vector<String> liste = new Vector<String>();
		Iterator<String> i = this.parties.keySet().iterator();
		while(i.hasNext()){
			liste.add(i.next());
		}
		return liste;
	}

	@Override
	public boolean rejoindrePartie(Client c, String partie) throws RemoteException {
		String partieDemande = this.parties.get(partie);
		System.out.println("[D] Partie Demandée : " + partie);
		
		if(partieDemande != null){
			// la partie existe
			return true;
		}else{
			// la partie n'existe pas
			return false;
		}
	}
	

	@Override
	public void ajouterPartie(String partie) throws RemoteException {
		this.parties.put(partie, partie);
	}
}
