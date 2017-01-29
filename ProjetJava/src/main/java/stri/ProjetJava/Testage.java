package stri.ProjetJava;

import java.util.Random;
import java.util.Vector;

public class Testage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<Integer> a=new Vector<Integer>(6);
		a.addElement(1);
		a.addElement(2);
		a.addElement(3);
		a.addElement(4);
		a.addElement(5);
		a.addElement(6);
		System.out.println("la taille du vecteur est de"+a.size()); 
		Random rand = new Random();
		for(int i=0;i < a.size();i++){
			a.setElementAt(rand.nextInt(5)+1,i);	
		}
		//System.out.println("la taille du vecteur est "+ a.size());
		
		System.out.println("la taille du vecteur est"+a.size());
		a.add(12);
		System.out.println("la taille du vecteur est"+a.size());

		for(int i=0;i < a.size();i++){
			System.out.println(a.elementAt(i));
			
		}
	}

}
