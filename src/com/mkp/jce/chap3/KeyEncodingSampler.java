package com.mkp.jce.chap3;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * <B>Section 3.1.4</B>
 * <P>
 * Demonstrates the different encoding mechanisms used by several sample keys
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class KeyEncodingSampler
{

	public static void main(String[] args)
	{
		byte[] salt = new byte[] { (byte) 0x3a, (byte)0x44, (byte)0x7f, (byte)0xf1, (byte)0xa2, (byte)0xe5, (byte)0x87, (byte)0x31 };
		int iterations = 25;
		char[] buf = new char[20];
		int bufPos = 0;
		
		try
		{
			char[] password = new char[] { 's', 'e', 'c', 'r', 'e', 't' };			
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterations);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey pbeKey = factory.generateSecret(pbeKeySpec);
			System.out.println("Key Algorithm: " + pbeKey.getAlgorithm());
			System.out.println("\tKey Encoding: " + pbeKey.getFormat());

			
			KeyGenerator kg = KeyGenerator.getInstance("DES");
			SecretKey desKey = kg.generateKey();	
			System.out.println("Key Algorithm: " + desKey.getAlgorithm());
			System.out.println("\tKey Encoding: " + desKey.getFormat());
			
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair keyPair = kpg.generateKeyPair();
			
			PublicKey pubKey = keyPair.getPublic();
			
			System.out.println("Public Key Algorithm: " + pubKey.getAlgorithm());
			System.out.println("\tKey Encoding: " + pubKey.getFormat());
			
			PrivateKey privKey = keyPair.getPrivate();
			System.out.println("Private Key Algorithm: " + privKey.getAlgorithm());
			System.out.println("\tKey Encoding: " + privKey.getFormat());
			
		} catch (NoSuchAlgorithmException nsae)
		{
			//Handle This!
			nsae.printStackTrace();
		} catch (InvalidKeySpecException ike)
		{
			//Handle This!
			ike.printStackTrace();
		}	
	}
}
