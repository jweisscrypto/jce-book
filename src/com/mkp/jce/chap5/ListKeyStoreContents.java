package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * <B>Section 5.3.1</B>
 * <P>
 * Connects to an existing key store and dumps the 
 * alias names out to the console
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class ListKeyStoreContents
{

	public static void main(String[] args)
	{
		try
		{
			//Using the factory method, load a keystore based 
			//on the user's java.security preferences
			String defaultKeyStoreFormat = Security.getProperty("keystore.type");
			KeyStore store = KeyStore.getInstance(defaultKeyStoreFormat);

			//By default, the initial keystore is named .keystore and is found 
			//in the user's home directory.  You could use the System.getProperty()
			//and dynamically look here first if applicable
			//String userHome = System.getProperty("user.home");
			//FileInputStream fis = new FileInputStream(new File(userHome + "/.keystore"));
			
			FileInputStream fis = new FileInputStream(new File("/jcebook.keystore"));
			
			//Before the KeyStore can be used, it has to be loaded.
			char[] password = new char[] { 's', 'e', 'c', 'r', 'e', 't', 'c', 'o', 'd', 'e' };			
			store.load(fis, "secretcode".toCharArray());
			Arrays.fill(password, '\u0000');
			
			for(Enumeration e = store.aliases();e.hasMoreElements();)
			{
				String name = (String) e.nextElement();
				System.out.print("Located alias " + name);
				System.out.println(" of type " + 
					((store.isKeyEntry(name))? " key/certificate" : " trusted certificate"));
			}
						
		} catch (KeyStoreException kse)
		{
			kse.printStackTrace();
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (CertificateException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}		
	}
}
