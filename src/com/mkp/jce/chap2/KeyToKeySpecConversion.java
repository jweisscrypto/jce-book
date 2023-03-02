package com.mkp.jce.chap2;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * <B>Section 2.4.1</B>
 * <P>
 * Demonstrates the use of a DesKeySpec passed to a 
 * SecretKeyFactory.  Play with the myKey variable to see
 * what a "weak-key" for DES
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class KeyToKeySpecConversion
{

	public static void main(String[] args)
	{
		//Punch in some test bytes and see if the key is weak!
		//
		//For the motivated, check out http://www.chipcenter.com/eexpert/jleiseboer/jleiseboer026.html 
		//which contains a great article on DES weak and semi-weak keys.  This one below is weak
		byte[] myKey =
			new byte[] {
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe,
				(byte) 0xfe };

		try
		{
			//Use the Key Specification to see if the key is weak; not possible with SecretKey
			//Since we used a KeyGenerator, it is 99.999% guaranteed NOT to be weak
			System.out.println(
				"Key is weak check: " + DESKeySpec.isWeak(myKey, 0));

			//Turn the raw byte array into a key specification
			DESKeySpec desKeySpec = new DESKeySpec(myKey);

			//Feedback Modes and Padding Schemes are for the Cipher, and have no effect
			//on the key.  As such, we only specify the cipher algorithm name here
			try
			{
				SecretKeyFactory factory = SecretKeyFactory.getInstance("DES");

				//Using our secret key factory, create a secret key without 
				//losing provider-specific data
				try
				{
					SecretKey key = factory.generateSecret(desKeySpec);

					//The SecretKey instance is now ready to be passed to a cipher 
					//instance as the key for an encryption or decryption operation

				} catch (InvalidKeySpecException ike)
				{
					//The key provided doesn�t pass the SecretKeyFactor rules!
					//Handle this!
					ike.printStackTrace();
				}

			} catch (NoSuchAlgorithmException nsae)
			{
				//There is no provider that can fulfill our request for a DES
				//SecretKeyFactory engine.  Handle this!
			}

		} catch (InvalidKeyException ike)
		{
			//The key provided doesn�t pass the key spec rules!
			//Handle this!
		}

	}
}
