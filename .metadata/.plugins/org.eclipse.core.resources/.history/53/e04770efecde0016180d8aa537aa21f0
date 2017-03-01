/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Iterator;
import java.util.Vector;

/**
 *
 * @author alexis
 */
public class ServeurImplem extends UnicastRemoteObject implements Serveur {
    
    /**
	 *	Generated serialization ID 
	 */
	private static final long serialVersionUID = 8751288996597991517L;
	
	
	private Annonce derniereAnnonce;
    // Deviendra un Vector ou autre pour la gestion multi-parties.
    private Partie partie;
    
    public ServeurImplem() throws RemoteException{
        super();
        // derniereAnnonce = new Annonce("", "", );
    }    
    
    public synchronized boolean rejoindrePartie(String pseudo, String partie) throws java.rmi.RemoteException{
        
        // D'abord on vérifie que la partie est bien en attente de joueurs
        // TODO : vérifier le nom de la partie lors des prochaines versions
        if(this.partie == null){
        	System.out.println("[*] Attempt to join non-existent party by " + pseudo);
        	return false;
        }
        
        if (this.partie.getStatus() != "WAIT"){
            System.out.println("[-] Cannot connect to a non-waiting party...");
            return false;
        }
        
        this.partie.ajouterJoueur(pseudo);
        
        return true;
    }
    
    public synchronized void quitterPartie(String pseudo, String partie) throws java.rmi.RemoteException{
        // TODO: gérer le nom des parties
        this.partie.enleverJoueur(pseudo);
    }
    
    public Annonce getDerniereAnnonce() throws java.rmi.RemoteException{
        return derniereAnnonce;
    }
    
    /**
     * Returns a vector of the connected clients connected to the 
     * given party name
     * @param partie
     * @return Vector of client
     * @throws RemoteException
     */
    public Vector<Client> getJoueursConnectes(String partie) throws java.rmi.RemoteException{
        return this.partie.getJoueurs();
    }
    
    // Retourne le nombre de dés de la valeur donnée.
    public int compterDes(int valeur, Vector<Integer> des){
    	int cpt = 0;
    	for(int i = 0; i < des.size(); i++){
    		if(des.get(i) == valeur){
    			cpt++;
    		}
    	}
    	
    	return cpt;
    }
    
    void traiterMenteur(Annonce a){
        
    }
    
    void traiterPile(Annonce a){
        
    }
    
    void traiterAnnonce(Annonce a) throws RemoteException{
    	Client c;
    	Vector<Client> liste = new Vector<Client>();
        liste = this.partie.getJoueurs();

        // On doit d'abord diffuser la dernière annonce
        for(Iterator<Client> i = liste.iterator(); i.hasNext();){
            c = (Client)i.next();
            c.AfficheAnnonce(a);
        }
        
        if(a.getType() == "menteur"){
        	// On doit compter le nombre de dés pour vérifier l'annonce
        	for(Iterator<Client> i = liste.iterator(); i.hasNext();){
        		c = (Client)i.next();
        		this.compterDes(a.getValeur(), c.getDes());
        	}
        }
    }
    
    public static void main(String[] args) throws RemoteException {
        
        try{
            
            ServeurImplem srv = new ServeurImplem();

            // initialise la registry              
            LocateRegistry.createRegistry(1099);

            Naming.rebind("rmi://10.0.0.1/Serveur", srv);
            System.out.println("[+] Serveur déclaré");

            srv.partie = new Partie("Perudo");
            System.out.println("[+] Partie initialisée");
            
            try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            Annonce a = new Annonce("menteur", "Tu es un sale menteur", "Sirbu");
            
            for(Iterator<Client> i = srv.partie.getJoueurs().iterator(); i.hasNext();){
            	Client c = (Client)i.next();
            	System.out.println(c.getJoueur().getPseudo());
            	c.AfficheAnnonce(a);
            }         
                
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        
    }    

    public boolean rejoindrePartie(Joueur j, String partie) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
