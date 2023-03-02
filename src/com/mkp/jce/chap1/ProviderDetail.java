package com.mkp.jce.chap1;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;

/**
 * <B>Section 1.5.5</B>
 * <P>
 * Dynamically registers the Cryptix and BC providers.  
 * A single command line argument is required to run this example, 
 * either the formal name of the Provider to print, or "all" for all
 * providers known to the system.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ProviderDetail
{
	public static final void main(String[] args)
	{
		try
		{		
			//Dynamically register our Bouncy Castle provider without requiring java.security modification
			//Place the provider in the fifth position
			Provider bcProv = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			Security.insertProviderAt(bcProv, 5);

			//Dynamically register our Cryptix provider without requiring java.security modification
			//Place the provider in the sixth position
			Provider cryptixProv = new cryptix.jce.provider.CryptixCrypto();
			Security.insertProviderAt(cryptixProv, 6);
			
			if("all".equalsIgnoreCase(args[0]))
			{						
				Provider[] providers = Security.getProviders();
				for(int i=0;i<providers.length;i++)
				{
					System.out.println("********************");
					System.out.println("** Provider:   " + providers[i].getName());
					System.out.println("********************");
					System.out.print(providers[i].toString());
					System.out.print(" is formally referred to as the '" + providers[i].getName() + "'");
					System.out.println(" provider in a getInstance() factory method");
					System.out.println("");
				}
				System.out.println("Total Providers: " + providers.length);

			} else
			{
				Provider provider = Security.getProvider(args[0]);
				System.out.println(provider.getName() + " formally supports the following algorithms: ");
	
				Iterator iter = provider.entrySet().iterator();
				while(iter.hasNext())
				{
					Map.Entry entry = (Map.Entry) iter.next();
					System.out.println("\t" + entry.getKey() + " = " + entry.getValue());
				}
			}
		} catch (NullPointerException nspe)
		{
			//NPE means Provider wasn't found!
			System.err.println("The provider you requested is not installed in the JRE");
		} catch (ArrayIndexOutOfBoundsException aioobe)
		{
			System.err.println("Usage: java ProviderDetail providerName");
			System.err.println("  Set providerName to 'all' to list all known providers");
		}
	}
}