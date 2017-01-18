/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

/**
 *
 * @author alexis
 * 
 */
public interface Serveur extends java.rmi.Remote {
    public boolean rejoindrePartie(String pseudo) throws java.rmi.RemoteException;
    public void quitterPartie(String pseudo) throws java.rmi.RemoteException;
    public Annonce getDerniereAnnonce() throws java.rmi.RemoteException;
}
