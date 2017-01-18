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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alexis
 */
public class ServeurImplem extends UnicastRemoteObject implements Serveur {
    
    private Annonce derniereAnnonce;
    // Deviendra un Vector ou autre pour la gestion multi-parties.
    private Partie partie;
    
    public ServeurImplem() throws RemoteException{
        super();
        // derniereAnnonce = new Annonce("", "", );
    }    
    
    public synchronized boolean rejoindrePartie(String pseudo) throws java.rmi.RemoteException{
        try {
            //TODO: Implement !
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServeurImplem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
    
    public void quitterPartie(String pseudo) throws java.rmi.RemoteException{
        // TODO : Implement !
        
    }
    
    public Annonce getDerniereAnnonce() throws java.rmi.RemoteException{
        return derniereAnnonce;
    }
    
    void traiterAnnonce(Annonce a){
        // On doit d'abord diffuser la derniere annonce
        
    }
    
    public static void main(String[] args) throws RemoteException {
        
        try{
            
            ServeurImplem srv = new ServeurImplem();

            // initialise la registry              
            LocateRegistry.createRegistry(1099);

            Naming.rebind("Serveur", srv);
            System.out.println("[+] Serveur déclaré");    
                
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        
    }    
}
