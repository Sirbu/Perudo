/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.util.Vector;

/**
 *
 * @author alexis
 * 
 */
public interface Serveur extends java.rmi.Remote {
    public boolean rejoindrePartie(Client c, String partie) throws java.rmi.RemoteException;
    public void quitterPartie(String pseudo, String partie) throws java.rmi.RemoteException;
    public Annonce getDerniereAnnonce(String partie) throws java.rmi.RemoteException;
    public Vector<Client> getJoueursConnectes(String partie) throws java.rmi.RemoteException;
    public Vector<String> getListePartie() throws java.rmi.RemoteException;
    public void ajouterPartie(String partie) throws java.rmi.RemoteException; 
}
