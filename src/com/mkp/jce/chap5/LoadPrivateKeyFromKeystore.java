package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;

/**
 * <B>(not formally cited in book)</B>
 * <P>
 * Demonstrates loading an RSA private key from a key store
 *   
 * @see com.mkp.jce.chap5.SecretKeyOverRSAEncrypt
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class LoadPrivateKeyFromKeystore
{
	public static void main(String[] args)
	{
			try
			{
				KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
			
				FileInputStream fis = new FileInputStream(new File("/jcebook.keystore"));
			
				store.load(fis, "secretcode".toCharArray());
				
				//we used the keystore password to protect the key as well
				RSAPrivateKey key = (RSAPrivateKey) store.getKey("jceRSAcert", "secretcode".toCharArray());
				System.out.println("Loaded RSA Private Key!!");
				
			} catch (KeyStoreException e)
			{
				e.printStackTrace();
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