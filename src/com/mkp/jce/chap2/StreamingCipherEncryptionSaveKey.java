package com.mkp.jce.chap2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * <B>Section 2.6.4</B>
 * <P>
 * Demonstrates the use of a symmetric streaming cipher
 * through the CipherInputStream class.  Saves the key
 * <b>for demo purposes, not production</b> to the hard
 * drive for use in the decryption example.  See Chapter 5's
 * examples on KeyStore usage for proper symmetric key storage!
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class StreamingCipherEncryptionSaveKey
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
				//The provider must use CFB as the feedback mode, 
				//processing 8 bits at a time through the stream
				//and apply a PKCS #5 padding scheme
				Cipher cipher =
					Cipher.getInstance("DES/CFB8/PKCS5Padding");

				//Initialize the cipher for encryption
				//and give it the key it should use
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				
				try
				{
					System.out.println("Key Bytes:");
					for(int i=0;i<desKey.length;i++)
					{
						System.out.print(desKey[i] + " ");
					}
					System.out.println("");
					
					System.out.println("Initialization Vector:");
					for(int i=0;i<cipher.getIV().length;i++)
					{
						System.out.print(cipher.getIV()[i] + " ");
					}
					System.out.println("");
					
					FileOutputStream keyFos = new FileOutputStream(new File("/mykey.txt"));
					keyFos.write(keySpec.getEncoded());
					keyFos.close();
												
					FileOutputStream ivFos = new FileOutputStream(new File("/myIV.txt"));
					ivFos.write(cipher.getIV());
					ivFos.close();					
					
					CipherInputStream cis = new CipherInputStream(
												new FileInputStream(
													new File("/plaintext.txt")), cipher);
													
					FileOutputStream fos = new FileOutputStream(
												new File("/ciphertext.txt"));
												
					//Let's read 64-bits of data per pass.  We call this cipherText
					//since we never actually see the plaintext-- it will be encrypted
					//by the time we get it in our variable! 
					byte[] cipherTextBytes = new byte[8];
					int i=0;

					while( (i = cis.read(cipherTextBytes )) !=-1)
					{
						fos.write(cipherTextBytes,0, i);						
					}
					fos.close();
					
					System.out.println("All done!  Check out /ciphertext.txt for the ciphertext representation of /plaintext.txt");
					
					
				} catch (IOException ioe)
				{
					//Handle this!
					ioe.printStackTrace();
				}
				
				
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
			} 

		} catch (NoSuchAlgorithmException nsae)
		{
			//DES isn�t available; must not have SunJCE in your java.security list!
			//Handle this!
			nsae.printStackTrace();
		}
	}
}
