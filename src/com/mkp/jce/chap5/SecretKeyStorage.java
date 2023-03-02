package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * <B>Section 5.3.2</B>
 * <P>
 * Demonstrates saving a raw symmetric key to the BKS formatted key store
 * As emphasized in the text, despite its documentation the Sun provider's
 * JKS formatted key store has problems retrieving symmetric keys and is
 * unusable in its present form for symmetric key storage.  Be sure to 
 * review the Bouncy Castle documentation on the BKS format to ensure it
 * is suitable for your needs; they offer several formats.  The Cryptix JCE
 * provider may also offer key store formats suitable for this purpose; see
 * their documentation.
 * 
 * @see com.mkp.jce.chap5.SecretKeyRetrieval
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class SecretKeyStorage
{

	public static void main(String[] args)
	{
		try
		{
			File file = new File("/jcebook.BKSkeystore");
			
			//Dynamically register our Bouncy Castle provider 
			//without requiring java.security modification
			//Place the provider in the fifth position
			Provider bcProv = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			Security.insertProviderAt(bcProv, 5);						
			
			//For demo purposes- in prod use real [] and null out after
			//prompting user for the password
			char[] secretcode = new char[] {'s','e','c','r','e','t','c','o','d','e'};
			
			//The JKS format has problems (despite its stated abilities)
			//handling symmetric keys.  Using the BKS which works properly
			KeyStore store = KeyStore.getInstance("BKS");
			
			//Create the new BKS keystore
			store.load(null, new char[0]);			

			KeyGenerator kg = KeyGenerator.getInstance("DES");
			SecretKey key = kg.generateKey();
			byte[] desKey = key.getEncoded();

			//Convert the bytes to a SecretKeySpec
			//continue with encryption operation from here
			
			//now, save the DES key for future use in the keystore
			//we will use the same password as the keystore, though
			//not required and may often be a different password
			store.setKeyEntry("myDESKey", key, secretcode, null);
			
			//Remember, key store manipulation is done in-memory and only
			//persisted to disk when the store() method is invoked!
			FileOutputStream fos = new FileOutputStream(file);
			store.store(fos, secretcode);
			Arrays.fill(secretcode, '\u0000');
			System.out.println("Key 'myDESKey' stored and written to disk.");			
						
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
