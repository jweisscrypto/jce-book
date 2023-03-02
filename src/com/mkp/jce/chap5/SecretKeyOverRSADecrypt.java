package com.mkp.jce.chap5;

import java.io.File;
import java.io.FileInputStream;
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
public class SecretKeyOverRSADecrypt
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
			RSAPrivateKey privKey = (RSAPrivateKey) store.getKey("jceRSAcert", secretcode);
			Arrays.fill(secretcode, '\u0000');
			System.out.println("Loaded RSA Private Key!!");
			
			byte[] secretKey = CryptoUtil.readCipherTextFile(new File("/secretkey.dat"));
			
			Cipher keyCipher = Cipher.getInstance("RSA/ECB/PKCS#1");
			keyCipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] aesKey = keyCipher.doFinal(secretKey);
			
			SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
			System.out.println("Extracted the secret AES key using my private key");
			
			byte[] cipherText = CryptoUtil.readCipherTextFile(new File("/message.dat"));
			
			Cipher msgCipher = Cipher.getInstance("AES");
			msgCipher.init(Cipher.DECRYPT_MODE, keySpec);
			
			byte[] plainTextBytes = msgCipher.doFinal(cipherText);
			String plainText = new String(plainTextBytes);
			System.out.println("Decrypted Message:\n" + plainText);
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
		} catch (KeyStoreException e)
		{
			e.printStackTrace();
		} catch (CertificateException e)
		{
			e.printStackTrace();
		} catch (UnrecoverableKeyException e)
		{
			e.printStackTrace();
		}		
	}
}
