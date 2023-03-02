package com.mkp.jce.chap3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * <B>Section 3.1.1</B>
 * <P>
 * Generates an asymmetric key pair
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class GenerateKeyPair
{

	public static void main(String[] args)
	{
		try
		{
			File pubKeyFile = new File("/" + args[0] + ".pubkey");
			File privKeyFile = new File("/" + args[0] + ".privkey");
			
			System.out.println("Searching for an RSA KeyPairGenerator...");
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			
			System.out.println("Initializing to 1024 bits");
			kpg.initialize(1024);
			
			System.out.println("Generating the key pairs");
			KeyPair keyPair = kpg.genKeyPair();
			
			System.out.println("Writing the public key to " + pubKeyFile.getAbsolutePath());			
			FileOutputStream publicFos = new FileOutputStream(pubKeyFile);
			publicFos.write(keyPair.getPublic().getEncoded());
			publicFos.close();
			
			System.out.println("Writing the private key to " + privKeyFile.getAbsolutePath());
			FileOutputStream privFos = new FileOutputStream(privKeyFile);
			privFos.write(keyPair.getPrivate().getEncoded());
			privFos.close();
			
			System.out.println("***********");
			System.out.println("** NOTE: **  See Chapter 5 for an implementation that properly stores the keys in a KeyStore!");
			System.out.println("***********");
			
		} catch (NoSuchAlgorithmException kpgNsae)
		{
			//Handle This!
			kpgNsae.printStackTrace();
		} catch (ArrayIndexOutOfBoundsException aioobe)
		{
			System.err.println("Usage: java GenerateKeyPair pairName");
			System.err.println("  Keys are written to the root directory with .pubkey and .privkey extensions");
		} catch (FileNotFoundException fnfe)
		{
			//Handle This!
			fnfe.printStackTrace();
		} catch (IOException ioe)
		{
			//Handle This!
			ioe.printStackTrace();
		}
	}
}
