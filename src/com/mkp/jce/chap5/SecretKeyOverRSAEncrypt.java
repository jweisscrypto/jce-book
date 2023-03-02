package com.mkp.jce.chap5;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.cert.Certificate;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.mkp.jce.misc.CryptoUtil;

/**
 * <B>Section 5.4.6</B>
 * <P>
 * Demonstrates the combination of symmetric and asymmetric ciphers
 * to share a large file on demand between Alice and Bob, where the
 * symmetric key is encrypted using Bob's public key.  Bob receives the
 * large encrypted file and the encrypted secret key.  He uses his private
 * key to extract the raw bytes of the secret key, and then using the secret
 * key he decrypts the large file.
 *   
 * @see com.mkp.jce.chap5.SecretKeyOverRSADecrypt
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class SecretKeyOverRSAEncrypt
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
			Certificate cert = CryptoUtil.readDerEncodedX509Certificate(new File("/jceRSAcert.der"));			
			System.out.println("Certificate Loaded.");
		
			KeyGenerator kg = KeyGenerator.getInstance("AES");
			SecretKey key = kg.generateKey();
			byte[] aesKey = key.getEncoded();
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");

			Cipher keyCipher = Cipher.getInstance("RSA/ECB/PKCS#1");
			keyCipher.init(Cipher.ENCRYPT_MODE, cert);
			byte[] keyCipherText = keyCipher.doFinal(aesKey);
			
			CryptoUtil.writeCipherTextFile(new File("/secretkey.dat"), keyCipherText);
			System.out.println("Secret key protected behind public key in /secretkey.dat");
			
			Cipher msgCipher = Cipher.getInstance("AES");
			msgCipher.init(Cipher.ENCRYPT_MODE, keySpec);
			
			String plainText = CryptoUtil.readPlainTextFile(new File("/velocity.log"));
			
			byte[] msgCipherText = msgCipher.doFinal(plainText.getBytes());
			
			CryptoUtil.writeCipherTextFile(new File("/message.dat"), msgCipherText);
			System.out.println("Message encrypted and written to /message.dat");
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
