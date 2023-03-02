package com.mkp.jce.chap1;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.Map;

/**
 * <B>Section 1.5.6</B>
 * <P>
 * A single command line argument is required to run this example,
 * the formal name of the Provider to print.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class DisorganizedListing
{

	public static void main(String[] args)
	{
		try
		{
			//Lookup the named provider using its formal name
			Provider provider = Security.getProvider(args[0]);

			System.out.print(provider.getName());
			System.out.println(" formally supports the following algorithms: ");

			//Step over the list of supported algorithms
			Iterator iter = provider.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry entry = (Map.Entry) iter.next();
				System.out.println(	"\t" + entry.getKey() + " = " + entry.getValue());
			}
		} catch (NullPointerException nspe)
		{
			//NPE means Provider wasn't found!
			System.err.println(
				"The provider you requested is not installed in the JRE");
		} catch (ArrayIndexOutOfBoundsException aioobe)
		{
			System.err.println("Usage: java ProviderDetail providerName");
		}
	}
}
