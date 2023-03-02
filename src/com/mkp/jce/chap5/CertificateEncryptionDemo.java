package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 5.4.3</B>
 * <P>
 * Demonstrates encrypting a short text message using the public key
 * associated with a digital certificate.  For example, Alice uses
 * Bob's digital certificate to send him a brief instruction.  
 * 
 * <b>Note:</b>Be sure to review the limitations
 * of RSA algorithms as discussed in section 5.4.5 in the book! 
 * 
 * @see com.mkp.jce.chap5.PrivateKeyDecryptionDemo
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class CertificateEncryptionDemo
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
			CertificateFactory factory = CertificateFactory.getInstance("X.509");
			
			FileInputStream fis = new FileInputStream(new File("/jceRSAcert.der"));
			
			Certificate cert = factory.generateCertificate(fis);
			
			System.out.println("Certificate Loaded.");
			fis.close();

			//Locate an RSA cipher engine and initialize it using the public key for encryption
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS#1");
			cipher.init(Cipher.ENCRYPT_MODE, cert);
			
			String plainText = "This is my secret message";
			
			//Generate the cipher text
			byte[] cipherText = cipher.doFinal(plainText.getBytes("UTF-8"));
			
			//In the interest of brevity, use CryptoUtil to write the plaintext file
			CryptoUtil.writeCipherTextFile(new File("/ciphertext.cert.dat"), cipherText);
			
			System.out.println("See /ciphertext.cert.dat for the encrypted text-- " + cipherText.length +" total bytes written");			
			
		} catch (IOException ioe)
		{
			//Handle This
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException nsae)
		{
			//Handle This
			nsae.printStackTrace();
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
		} catch (CertificateException ce)
		{
			//Handle This
			ce.printStackTrace();
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