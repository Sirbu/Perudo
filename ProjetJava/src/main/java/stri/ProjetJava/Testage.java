package stri.ProjetJava;

import java.util.Vector;

public class Testage {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Vector<Integer> a=new Vector<Integer>(15);
		a.addElement(2);
		a.addElement(23);
		a.addElement(2);
		a.addElement(23);
		a.addElement(2);
		a.addElement(23);
		for(int i=0;i < a.size();i++){
			System.out.println(a.get(i));
			
		}
		a.remove(0);
		System.out.println("eeeeeeeeeee");
		for(int i=0;i < a.size();i++){
			System.out.println(a.get(i));
			
		}
	}

}
