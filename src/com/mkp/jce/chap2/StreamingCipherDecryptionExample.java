package com.mkp.jce.chap2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * <B>Section 2.6.4</B>
 * <P>
 * Demonstrates the use of a symmetric streaming cipher
 * through the CipherInputStream class
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class StreamingCipherDecryptionExample
{

	public static void main(String[] args)
	{
		try
		{
			FileInputStream fisKey = new FileInputStream(new File("/mykey.txt"));
			
			byte[] desKey = new byte[fisKey.available()];
			fisKey.read(desKey);
			fisKey.close();
			
			System.out.println("Key Bytes Retrieved:");
			for(int i=0;i<desKey.length;i++)
			{
				System.out.print(desKey[i] + " ");
			}
			System.out.println("");
			
			FileInputStream fisIV = new FileInputStream(new File("/myIV.txt"));
			
			byte[] IV = new byte[fisIV.available()];
			fisIV.read(IV);
			fisIV.close();
			
			System.out.println("Initialization Vector Retrieved:");
			for(int i=0;i<IV.length;i++)
			{
				System.out.print(IV[i] + " ");
			}
			System.out.println("");
						
			try
			{
				SecretKeySpec keySpec = new SecretKeySpec(desKey, "DES");
				IvParameterSpec iv = new IvParameterSpec(IV);
				
				//Find a provider for a DES cipher algorithm
				//The provider must use CFB as the feedback mode, 
				//processing 8 bits at a time through the stream
				//and apply a PKCS #5 padding scheme
				Cipher cipher =
					Cipher.getInstance("DES/CFB8/PKCS5Padding");
	
				//Initialize the cipher for decryption
				//and give it the key from our file
				cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
				
				try
				{
					CipherInputStream cis = new CipherInputStream(
												new FileInputStream(
													new File("/ciphertext.txt")), cipher);
													
					FileOutputStream fos = new FileOutputStream(
												new File("/decrypted_ciphertext.txt"));
												
					//Let's read 64-bits of data per pass.  We call this plainText
					//since we never actually see the ciphertext-- it will be decrypted
					//by the time we get it in our variable! 
					byte[] plainTextBytes = new byte[8];
					int i=0;

					while( (i = cis.read(plainTextBytes )) !=-1)
					{
						fos.write(plainTextBytes,0, i);						
					}
					fos.close();
					
					System.out.println("All done!  Check out /decrypted_ciphertext.txt for the decrypted representation of /ciphertext.txt");
					
				} catch (IOException cipherIoe)
				{
					//Handle this!
					cipherIoe.printStackTrace();
				}
			} catch (NoSuchAlgorithmException nsae)
			{
				//Handle this
				nsae.printStackTrace();
			} catch (NoSuchPaddingException nspe)
			{
				nspe.printStackTrace();
			} catch (InvalidKeyException ike)
			{
				ike.printStackTrace();
			} catch (InvalidAlgorithmParameterException e)
			{
				//Handle this
				e.printStackTrace();
			}
				
			
		} catch (IOException keyIoe)
		{
			//Can't read in the key?
			System.err.println("Can't read in mykey.txt...did you generate it by running StreamingSipherEncryptionSaveKey?");
		}
	}
}