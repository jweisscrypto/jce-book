package com.mkp.jce.chap4;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;

/**
 * <B>Section 4.6.1</B>
 * <P>
 * Generates a message authentication code using a secret key
 * 
 * @author Jason R. Weiss
 * @version 1.0
 *
 */
public class MacExample
{

	public static void main(String[] args)
	{
		StringBuffer buf = new StringBuffer();

		try
		{
			//Be sure to copy the velocity.log to your root directory
			//or change to an absolute path to ensure the files found			
			FileInputStream fis = new FileInputStream(new File("/velocity.log"));
		
			int curChar=0;
			while( (curChar = fis.read()) != -1 )
			{
				buf.append((char) curChar);
			}				
		} catch (IOException ioe)
		{
			//Handle this!
			System.out.println("Couldn't find the file.  Did you adjust the path accordingly?");
			System.exit(0);
		} 
		
		try
		{
			//Get an HMac SHA-1 Key Generator
			KeyGenerator kg = KeyGenerator.getInstance("hmacmd5");
			
			//Generate the SecretKey
			SecretKey key = kg.generateKey();
			
			////alternative approach
			//SecureRandom csprng = SecureRandom.getInstance("SHA1PRNG");
			//byte[] randomBytes = new byte[16];
			//csprng.nextBytes(randomBytes);
			//SecretKeySpec key = new SecretKeySpec(randomBytes, "HMACMD5");
			
			
			//Get an HMac SHA-1 instance
			Mac mac = Mac.getInstance("hmacmd5");
			
			//Initialize the Mac with the secret key
			//Some providers may require a special
			//secret key (check provider docs), in
			//which case an inadequate key throws
			//an InvalidKeyException
			mac.init(key);
			
			byte[] digest = mac.doFinal(buf.toString().getBytes("UTF-8"));
			
			System.out.println("HMac MD5 Digest Length: " + digest.length);
			
			for(int i=0;i<digest.length;i++)
			{
				System.out.print(digest[i] + " ");
			}
			System.out.println("");		

			
		} catch (NoSuchAlgorithmException nsae)
		{
			nsae.printStackTrace();
		} catch (InvalidKeyException ike)
		{
			ike.printStackTrace();
		} catch (UnsupportedEncodingException uee)
		{
			uee.printStackTrace();
		}
	}
}
