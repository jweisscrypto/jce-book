package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * <B>Section 5.3</B>
 * <P>
 * Creates a new keystore based on the default key store format
 * as defined by the user in their JRE's java.security file.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class CreateNewKeystore
{
	public static void main(String[] args)
	{
		try
		{
			//Using the factory method, load a keystore based 
			//on the user's java.security preferences
			String defaultKeyStoreFormat = KeyStore.getDefaultType();
			KeyStore store = KeyStore.getInstance(defaultKeyStoreFormat);
			
			//Before the KeyStore can be used, it has to be loaded.
			//In this example, we pass null to indicate we want to
			//create a new KeyStore for use
			store.load(null, new char[0]);
			
			//Open up an output stream to the new keystore's location
			FileOutputStream fos = new FileOutputStream(new File("/jcebook.keystore"));
			
			//Probably would prompt the user for this value
			char[] password = new char[] { 's', 'e', 'c', 'r', 'e', 't', 'c', 'o', 'd', 'e' };
			
			//Write the keystore out to the file system.
			store.store(fos, password);

			//Clear the memory of the array's values			
			Arrays.fill(password, '\u0000');	
			
			System.out.println("Empty keystore created successfully!");		
			
		} catch(KeyStoreException kse)
		{
			kse.printStackTrace();
		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException nse)
		{
			nse.printStackTrace();
		}		
	}
}