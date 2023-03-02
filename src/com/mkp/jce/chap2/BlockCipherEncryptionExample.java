package com.mkp.jce.chap2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <B>Section 2.6.3</B>
 * <P>
 * Demonstrates the use of a symmetric block cipher
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class BlockCipherEncryptionExample
{

	public static void main(String[] args)
	{
		//Get an instance of a KeyGenerator engine for DES encryption
		try
		{
			KeyGenerator kg = KeyGenerator.getInstance("DES");

			//Here we could optionally pass a key size and a CSPRNG source
			//In this code example, we�ll use whatever defaults the provider uses

			SecretKey key = kg.generateKey();

			//Translate our key into its encoded byte array
			byte[] desKey = key.getEncoded();

			//Use Option 2 to turn the key into a key specification
			//This approach does not require a try�catch block
			SecretKeySpec keySpec = new SecretKeySpec(desKey, "DES");

			try
			{
				//Find a provider for a DES cipher algorithm
				//The provider must use ECB as the feedback mode
				//and apply a PKCS #5 padding scheme
				Cipher cipher =
					Cipher.getInstance("DES/ECB/PKCS5Padding");

				//Initialize the cipher for encryption
				//and give it the key it should use
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				
				//Encrypt our command line sentence
				byte[] cipherText = cipher.doFinal(args[0].getBytes());
				
				//Display the cipher text
				System.out.println("Plain Text: " + args[0] + "\n");
				System.out.println("Resulting Cipher Text:");
				for(int i=0;i<cipherText.length;i++)
				{
					System.out.print(cipherText[i] + " ");
				}
				System.out.println("");
				
			} catch (NoSuchAlgorithmException nsae)
			{
				//A DES cipher using an ECB feedback mode with PKCS #5 padding isn�t available
				//Handle this!
				nsae.printStackTrace();
			} catch (InvalidKeyException ike)
			{
				//Highly unlikely that a SecretKey from a KeyGenerator engine would get here
				//However, we never assume!
				//Handle this!
				ike.printStackTrace();
			} catch (NoSuchPaddingException nspe)
			{
				//Handle this!				
			} catch (BadPaddingException bpe)
			{
				//Handle this!
				bpe.printStackTrace();
			} catch (IllegalBlockSizeException ibse)
			{
				//Handle this!
				ibse.printStackTrace();
			}

		} catch (NoSuchAlgorithmException nsae)
		{
			//DES isn�t available; must not have SunJCE in your java.security list!
			//Handle this!
			nsae.printStackTrace();
		}
	}
}