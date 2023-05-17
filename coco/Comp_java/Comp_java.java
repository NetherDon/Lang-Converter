package Comp_java;

import java.util.*;
public class Comp_java{
	public static void main(String[] args) {
		String nf = (args.length > 0)? args[0]:"Test0.java";
		Scanner scan1 = new Scanner(nf); 	
		Parser  pars1 = new Parser(scan1);	
		pars1.Parse(); // запустить анализ
		//System.out.println("k="+k);
		//Scanner in = new Scanner(System.in);
	/*
		  Console.Write("\n errors="+pars1.errors.count);
	*/
	}	
} 
