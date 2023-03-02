package com.mkp.jce.chap2;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.MessageFormat;

/**
 * <B>Section x.x.x</B>
 * <P>
 * Generates cryptographically secure psuedo-random data
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class CsprngExample
{
	public static void main(String[] args)
	{
			try
			{
				//Locate an opaque SHA1PRNG provider
				SecureRandom csprng = SecureRandom.getInstance("SHA1PRNG");
							
				//generate some random booleans!
				String out = MessageFormat.format("3 random boolean values: {0}, {1}, and {2}",
										 new Object[] {	Boolean.toString(csprng.nextBoolean()),
										 				Boolean.toString(csprng.nextBoolean()),
														Boolean.toString(csprng.nextBoolean())});
				
				System.out.println(out);
				
				//Now, let's generate some random ints
				out = MessageFormat.format("3 random int values: {0}, {1}, and {2}",
										 new Object[] {	Integer.toString(csprng.nextInt()),
														Integer.toString(csprng.nextInt()),
														Integer.toString(csprng.nextInt())});
				
				System.out.println(out);
				
				//And finally, the venerable random bytes
				byte[] randomBytes = new byte[3];
				csprng.nextBytes(randomBytes);
				
				out = MessageFormat.format("3 random byte values: {0}, {1}, and {2}",
										 new Object[] {	Byte.toString(randomBytes[0]),
														Byte.toString(randomBytes[1]),
														Byte.toString(randomBytes[2])});
				
				System.out.println(out);
				
				
				
				
			} catch (NoSuchAlgorithmException nsae)
			{
				//Handle this!
				nsae.printStackTrace();
			}
	}
}
