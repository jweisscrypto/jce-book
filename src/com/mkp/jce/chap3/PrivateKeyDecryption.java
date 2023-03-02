package com.mkp.jce.chap3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 3.3.2</B>
 * <P>
 * Decrypts the ciphertext generated from an encryption operation that used
 * the corresponding public key.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class PrivateKeyDecryption
{

	public static void main(String[] args)
	{
		try
		{			
			//Dynamically register our Bouncy Castle provider without requiring java.security modification
			//Place the provider in the fifth position
			Provider bcProv = new org.bouncycastle.jce.provider.BouncyCastleProvider();
			Security.insertProviderAt(bcProv, 5);
			System.out.println("Registered BC provider successfully");

			//Dynamically register our Cryptix provider without requiring java.security modification
			//Place the provider in the sixth position
			Provider cryptixProv = new cryptix.jce.provider.CryptixCrypto();
			Security.insertProviderAt(cryptixProv, 6);
			System.out.println("Registered Cryptix provider successfully");


			//
			// NOTE:  Chapter 6 Code examples demonstrate the use of a KeyStore for holding private keys! 
			//	

			
			//Be sure to copy the jcebook.privkey to your working directory
			//or change to an absolute path to ensure the files found			
			FileInputStream privKeyFis = new FileInputStream(new File("/jcebook.privkey"));
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
			int curByte=0;
			while( (curByte = privKeyFis.read()) != -1 )
			{
				baos.write(curByte);
			}
			
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(baos.toByteArray());
			baos.close();
			
			//Grab an RSA Key Factory
			KeyFactory keyFactoryEngine = KeyFactory.getInstance("RSA");
			
			//Let the key factory decode the X.509 encoded public key
			PrivateKey privateKey = keyFactoryEngine.generatePrivate(keySpec);

			
			//Locate an RSA cipher engine and initialize it using the public key for encryption
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");			
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			
			//In the interest of brevity, use CryptoUtil to load plaintext file
			byte[] cipherText = CryptoUtil.readCipherTextFile(new File("/ciphertext.dat"));
			
			//Generate the cipher text
			byte[] plainTextBytes = cipher.doFinal(cipherText);
			
			String plainText = new String(plainTextBytes);
			
			System.out.println("Decrypted:\n" + plainText);			
			
		} catch (IOException ioe)
		{
			//Handle This
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae)
		{
			//Handle This
			nsae.printStackTrace();
		} catch (InvalidKeySpecException ikse)
		{
			//Handle This
			ikse.printStackTrace();
		} catch (NoSuchPaddingException nspe)
		{
			//Handle This
			nspe.printStackTrace();
		} catch (InvalidKeyException ike)
		{
			//Handle This
			ike.printStackTrace();
		} catch (IllegalStateException ise)
		{
			//Handle This
			ise.printStackTrace();
		} catch (IllegalBlockSizeException ibse)
		{
			//Handle This
			ibse.printStackTrace();
		} catch (BadPaddingException bpe)
		{
			//Handle This
			bpe.printStackTrace();
		}		
	}
}
