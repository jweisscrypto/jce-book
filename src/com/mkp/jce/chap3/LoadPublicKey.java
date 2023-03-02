package com.mkp.jce.chap3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

/**
 * <B>Section 3.1.5</B>
 * <P>
 * Reans an X509 encoded public key from disk
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class LoadPublicKey
{
	public static void main(String[] args)
	{
		ByteArrayOutputStream pubKeyBaos = new ByteArrayOutputStream();
		
		try
		{
			//Be sure to copy the jcebook.pubkey to your working directory
			//or change to an absolute path to ensure the files found			
			FileInputStream pubKeyFis = new FileInputStream(new File("/jcebook.pubkey"));
	
			int curByte=0;
			while( (curByte = pubKeyFis.read()) != -1 )
			{
				pubKeyBaos.write(curByte);
			}
			
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKeyBaos.toByteArray());
			pubKeyBaos.close();
			
			//Grab an RSA Key Factory
			KeyFactory keyFactoryEngine = KeyFactory.getInstance("RSA");
			
			//Let the key factory decode the X.509 encoded public key
			PublicKey publicKey = keyFactoryEngine.generatePublic(keySpec);

			System.out.println("Public Key Successfully Loaded!");			
		} catch (IOException ioe)
		{
			//Handle This!
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae)
		{
			//Handle This!
			nsae.printStackTrace();
		} catch (InvalidKeySpecException ikse)
		{
			//Handle This!
			ikse.printStackTrace();
		}		
	}
}