package fr.ng;

import java.io.Serializable;

public class Singleton implements Serializable
{	
	private Singleton() {
		
	}
 
	private static Singleton INSTANCE = new Singleton();
 
	public static Singleton getInstance() {	
		return INSTANCE;
	}
 
	private Object readResolve() {
		return INSTANCE;
	}
}