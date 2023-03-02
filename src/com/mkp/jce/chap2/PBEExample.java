package com.mkp.jce.chap2;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * <B>Section 2.7.1</B>
 * <P>
 * Demonstrates Password Based Encryption (PBE)
 *
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class PBEExample
{

	public static void main(String[] args)
	{
		byte[] salt = new byte[] { (byte) 0x3a, (byte)0x44, (byte)0x7f, (byte)0xf1, (byte)0xa2, (byte)0xe5, (byte)0x87, (byte)0x31 };
		int iterations = 1000;
		char[] buf = new char[20];
		int bufPos = 0;

		try
		{

			//Prompt the user for a short password
			System.out.println("Note:  Window's users must modify this code to work properly!");

			System.out.print("Enter a password please (no more than 20 characters please)> ");






			//
			// I thought about using the line.separator in the JVM details, but wasn't sure if
			// each JVM contains that property, so I decided to be old-fashioned and safe.
			//

			//
			//
			// Windows Version
			//
			//

			//char ch = (char) System.in.read();
			//while((ch != '\n') && (ch != \r'))
			//{
			//	buf[bufPos++] = ch;
			//}


			//
			//
			// *nix & Mac Version
			//
			//

			//Read in a character at a time, shoving it into our buffer
			while( (buf[bufPos++] = (char) System.in.read()) != '\n') {}







			//
			// Continue on platform neutral...
			//

			//Conver the buffer into a password char array, chop off the trailing \n character
			char[] password = new char[--bufPos];
			System.arraycopy(buf, 0, password, 0, bufPos);

			//Create a PBE Key Specification, providing the password, salt, and iteration count
			PBEKeySpec pbeKeySpec = new PBEKeySpec(password, salt, iterations);

			//Locate a PBE secret key factory
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");

			//Generate the secret key from the PBEKeySpec
			SecretKey key = factory.generateSecret(pbeKeySpec);

			//Locat a PBE cipher
			Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");

			//Initialize the cipher for encryption
			//and give it the key it should use
			cipher.init(Cipher.ENCRYPT_MODE, key);

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

			//Clear out all password references from memory
			for(int i=0;i<password.length;i++) buf[i] = password[i] = 0;

		} catch (IOException ioe)
		{
			//Handle This!
			ioe.printStackTrace();
		} catch (NoSuchAlgorithmException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (InvalidKeySpecException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (InvalidKeyException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (IllegalStateException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (IllegalBlockSizeException e)
		{
			//Handle This!
			e.printStackTrace();
		} catch (BadPaddingException e)
		{
			//Handle This!
			e.printStackTrace();
		}
	}
}
