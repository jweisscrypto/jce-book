package com.mkp.jce.chap3;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 3.3.1</B>
 * <P>
 * Encrypts a short message using a public key.  <b>Note:</b> the RSA asymmetric algorithms have
 * a mathematical restriction on how much data they can encrypt, so the operation may fail if too
 * much data is passed into this example.  For large encryption processes, consider using a symmetric
 * cipher and using the public key encryption to only encrypt the raw secret key.
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class PublicKeyEncryption
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
			
			//In the interest of brevity, use the CryptoUtil to load the 
			//public key saved from the GenerateKeyPair code example
			PublicKey pubKey = CryptoUtil.loadPublicKey(new File("/jcebook.pubkey"), "RSA");
			System.out.println("Loaded the public key successfully");
			
			//Locate an RSA cipher engine and initialize it using the public key for encryption
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");			
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			
			//In the interest of brevity, use CryptoUtil to load plaintext file
			String plainText = CryptoUtil.readPlainTextFile(new File("/message.txt"));
			System.out.println("Plaintext Bytes: " + plainText.getBytes().length);
			
			//Generate the cipher text
			byte[] cipherText = cipher.doFinal(plainText.getBytes());
			System.out.println("Total Ciphertext Bytes: " + cipherText.length);
			
			//In the interest of brevity, use CryptoUtil to write the plaintext file
			CryptoUtil.writeCipherTextFile(new File("/ciphertext.dat"), cipherText);
			
			System.out.println("See /ciphertext.dat for the encrypted text");			
			
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
