package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 5.4.4</B>
 * <P>
 * Demonstrates decrypting ciphertext generated from the public key 
 * of a digital certificate using a private key.  For example, Bob 
 * receives a brief instruction from his boss Alice.  Alice used Bob's
 * digital certificate to encrypt the instruction, and now Bob needs to
 * decipher what Alice has instructed him to do.
 * 
 * @see com.mkp.jce.chap5.CertificateEncryptionDemo
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class PrivateKeyDecryptionDemo
{

	public static void main(String[] args)
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
		
		try
		{
			KeyStore store = KeyStore.getInstance(KeyStore.getDefaultType());
			FileInputStream fis = new FileInputStream(new File("/jcebook.keystore"));
			
			char[] secretcode = new char[] {'s','e','c','r','e','t','c','o','d','e'};
			store.load(fis, secretcode);
				
			//we used the keystore password to protect the key as well
			RSAPrivateKey privKey = (RSAPrivateKey) store.getKey("jceRSAcert", secretcode);
			Arrays.fill(secretcode, '\u0000');
			System.out.println("Loaded RSA Private Key!!");
		
			//Locate an RSA cipher engine and initialize it using the public key for encryption
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS#1");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
		
			byte[] cipherText = CryptoUtil.readCipherTextFile(new File("/ciphertext.cert.dat"));
			
			
			//Generate the cipher text
			byte[] plainTextBytes = cipher.doFinal(cipherText);
		
			String plainText = new String(plainTextBytes);
			System.out.println("Decrypted Plain Text:\n" + plainText);
						
		} catch (KeyStoreException kse)
		{
			kse.printStackTrace();
		} catch (FileNotFoundException fnfe)
		{
			fnfe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		} catch (CertificateException ce)
		{
			ce.printStackTrace();
		} catch (IOException ioe)
		{
			ioe.printStackTrace();
		} catch (UnrecoverableKeyException uke)
		{
			uke.printStackTrace();
		} catch (NoSuchPaddingException nspe)
		{
			nspe.printStackTrace();
		} catch (InvalidKeyException ike)
		{
			ike.printStackTrace();
		} catch (IllegalStateException ise)
		{
			ise.printStackTrace();
		} catch (IllegalBlockSizeException ibse)
		{
			ibse.printStackTrace();
		} catch (BadPaddingException bpe)
		{
			bpe.printStackTrace();
		}				
	}
}
