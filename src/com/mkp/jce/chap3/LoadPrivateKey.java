package com.mkp.jce.chap3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * <B>Section 3.1.6</B>
 * <P>
 * Reads a PKCS#8 encoded private key from disk
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class LoadPrivateKey
{

	public static void main(String[] args)
	{
		ByteArrayOutputStream privKeyBaos = new ByteArrayOutputStream();
		
		try
		{
			//Be sure to copy the jcebook.privkey to your working directory
			//or change to an absolute path to ensure the files found			
			FileInputStream privKeyFis = new FileInputStream(new File("/jcebook.privkey"));
	
			int curByte=0;
			while( (curByte = privKeyFis.read()) != -1 )
			{
				privKeyBaos.write(curByte);
			}
			
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privKeyBaos.toByteArray());
			privKeyBaos.close();
			
			//Grab an RSA Key Factory
			KeyFactory keyFactoryEngine = KeyFactory.getInstance("RSA");
			
			//Let the key factory decode the X.509 encoded public key
			PrivateKey privateKey = keyFactoryEngine.generatePrivate(keySpec);

			System.out.println("Private Key Successfully Loaded!");			
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
