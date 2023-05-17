package ConvF;

import java.util.*;
public class ConvF
{
	public static void main(String[] args) 
	{
		String nf = args.length > 0 ? args[0] : "Test0.txt";
		Scanner scan1 = new Scanner(nf); 	
		Parser  pars1 = new Parser(scan1);	
		pars1.Parse();
	}	
} 
