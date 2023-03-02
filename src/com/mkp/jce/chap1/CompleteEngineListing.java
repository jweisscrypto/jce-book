package com.mkp.jce.chap1;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mkp.jce.misc.EngineIterator;

/**
 * <B>Section 1.6</B>
 * <P>
 * Dynamically registers the Cryptix and BC providers.  
 * 2 command line arguments are required to run this example, the first
 * is the formal name of the Provider to print, the second is the
 * engine name to display.  Engine names can include:
 * <ul>
 *   <li>Cipher</li>
 *   <li>Signature</li>
 *   <li>MessageDigest</li>
 * </ul>
 * Consult with the JCA/JCE documentation for a complete list of all engines. 
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class CompleteEngineListing
{
	
	private static SortedSet _sortedListing = new TreeSet();
	private static String _description = "";

	public static void main(String[] args)
	{
		try
		{
			//Dynamically register our Bouncy Castle provider without requiring java.security modification
			//Place the provider in the fifth position
			Provider bcProv = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			Security.insertProviderAt(bcProv, 5);

			//Dynamically register our Cryptix provider without requiring java.security modification
			//Place the provider in the sixth position
			Provider prov = new cryptix.jce.provider.CryptixCrypto();
			Security.insertProviderAt(prov, 6);
			
			Provider provider = Security.getProvider(args[0]);
			System.out.println(provider.getName() + " formally supports the following implementations for the " + args[1] + " engine :\n");
			
			EngineIterator iter = new EngineIterator(provider, args[1]);
			while(iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				if(!iter.isAlias())
				{
					_description = entry.getKey().toString().substring(1 + args[1].length()) + " as implemented in class " + entry.getValue().toString();					
				} else
				{
					_description = entry.getValue().toString() + " is also aliased to the name " + entry.getKey().toString().substring(EngineIterator.ALGORITHM_ALIAS.length() + args[1].length() + 1);
				}

				//Add our description to our sorted list				
				_sortedListing.add(_description);				
			}
			
			Iterator printIter = _sortedListing.iterator();
			while(printIter.hasNext())
			{
				System.out.println(printIter.next().toString());
			}
			
		} catch (ArrayIndexOutOfBoundsException aioobe)
		{
			System.err.println("Usage: java EngineListing providerName engineType");
			System.err.println("  If the provider name isn't know, run the ProviderDetail class");
			System.err.println("  Engine names are case-sensitive as documented in the JCA/JCE");
		}		
	}
}
