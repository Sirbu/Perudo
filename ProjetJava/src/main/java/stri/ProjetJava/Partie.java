/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package stri.ProjetJava;

import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author alexis
 */
public class Partie {
    private String nom;
    private int nbrJoueursMax = 6;      // Pourra être modifié à l'occas'
    private String statut;                      // INIT || WAIT || STARTED || FINISHED
    private Annonce derniereAnnonce;
    private HashMap<String, Joueur> joueurs;
    
    
    public Partie(String nom){
        this.nom = nom;
        this.statut = "INIT";
    }
    
    public void enleverJoueur(String pseudo){
        this.joueurs.remove(pseudo);
    }
}
