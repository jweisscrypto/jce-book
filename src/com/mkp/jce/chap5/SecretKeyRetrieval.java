package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

/**
 * <B>Section 5.3.3</B>
 * <P>
 * Retrieves a symmetric key from a BKS formatted key store
 * 
 * @see com.mkp.jce.chap5.SecretKeyStorage
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class SecretKeyRetrieval
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
			
			char[] secretcode = new char[] {'s','e','c','r','e','t','c','o','d','e'};
			KeyStore store = KeyStore.getInstance("BKS");
			FileInputStream fis = new FileInputStream(file);
			store.load(fis, secretcode);
			
			Key key = store.getKey("myDESKey", secretcode);			
			byte[] desKey = key.getEncoded();
			System.out.println("Key Loaded- " + desKey.length + " bytes:\n");
			for(int i=0;i<desKey.length;i++)
			{
				System.out.print(desKey[i] + " ");
			}
			System.out.println("");

			Arrays.fill(secretcode, '\u0000');
			
			//Convert the bytes to a SecretKeySpec
			//continue with decryption operation from here
			
			
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
		} catch (UnrecoverableKeyException e)
		{
			e.printStackTrace();
		}		
	}
}
