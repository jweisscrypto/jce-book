package com.mkp.jce.chap1;

import java.security.Security;
import java.util.Iterator;
import java.util.Set;

/**
 * <B>Section 1.6</B>
 * <P>
 * Play with the sample by running it with and without the dynamically 
 * registered provider.  A single command line argument is required to 
 * run this example, the formal name of the Provider to print
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class SimpleEngineListing
{
	public static void main(String[] args)
	{		
		//Dynamically register our Cryptix provider without requiring java.security modification
		//Place the provider in the fifth position
		//Provider prov = new cryptix.jce.provider.CryptixCrypto();
		//Security.insertProviderAt(prov, 5);
		
		/*
		 * Run first with the above two lines of code commented out for the Signature engine.
		 * Then uncomment these lines and re-run the code example
		 * You should see a substatianlly larger list of supported Signature algorithms. 
		 * 
		 */
		
		Set set = Security.getAlgorithms(args[0]);
		Iterator iter = set.iterator();
		while(iter.hasNext())
		{
			System.out.println(iter.next().toString());
		}
	}
}