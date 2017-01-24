package stri.ProjetJava;

import java.util.Vector;

public interface Client extends java.rmi.Remote {
	
	void AfficheAnnonce(Annonce a) throws java.rmi.RemoteException;
	Annonce FaireAnnonce(String nbreDesJoueurs) throws java.rmi.RemoteException; 
	void lancerDes() throws java.rmi.RemoteException;
	Vector<Integer> getDes() throws java.rmi.RemoteException;
	void retirerDes() throws java.rmi.RemoteException;
	void ajouterDes() throws java.rmi.RemoteException;
	String getPseudo() throws java.rmi.RemoteException;
}
