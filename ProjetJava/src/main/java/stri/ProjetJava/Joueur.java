package stri.ProjetJava;

import java.util.Vector;

public class Joueur {
    
	public Joueur(String pseudo) {
		
		this.pseudo = pseudo;
		des = new Vector<Integer>(6);
		for(int i=0;i<des.size();i++){
			des.add(i, 0);
		}
		
	}
	private String pseudo;
	private String couleur;
	private Vector<Integer> des;
	
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
}
